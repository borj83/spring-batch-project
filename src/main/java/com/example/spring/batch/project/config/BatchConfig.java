package com.example.spring.batch.project.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.spring.batch.project.domain.Contact;
import com.example.spring.batch.project.processor.ContactItemProcessor;

/**
 * Spring Batch beans configuration.
 * 
 * @author Borja López Altarriba
 */
@Configuration
@EnableConfigurationProperties
@EnableBatchProcessing
public class BatchConfig {
	
    @Bean
    @Primary
    public DataSource jobRepoDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .build();
    }
    
    @Bean
    public JdbcTemplate jobRepoJdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    DefaultBatchConfigurer batchConfigurer(final DataSource dataSource) {
        return new DefaultBatchConfigurer(dataSource);
    }
    
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1) {
    	return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Contact> reader,
            ItemWriter<Contact> writer, ItemProcessor<Contact, Contact> processor) {
    	return stepBuilderFactory.get("step1")
                .<Contact, Contact> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    
    @Bean
    public ItemReader<Contact> reader(@Value("${reader.resource}") final String resourceName) {
    	System.out.println("Execute ItemReader");
    	
    	FlatFileItemReader<Contact> reader = new FlatFileItemReader<Contact>();
    	reader.setResource(new ClassPathResource(resourceName));
        reader.setLineMapper(new DefaultLineMapper<Contact>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "name", "email", "status" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Contact>() {{
                setTargetType(Contact.class);
            }});
        }});
        return reader;
    }
    
    @Bean
    public ItemProcessor<Contact, Contact> processor() {
    	System.out.println("Execute ItemProcessor");
        
    	return new ContactItemProcessor();
    }
    
    @Bean
    public ItemWriter<Contact> writer(@Value("${writer.resource}") final String resourceName) {
    	System.out.println("Execute ItemWriter");
		
		final FlatFileItemWriter<Contact> writer = new FlatFileItemWriter<Contact>();
        writer.setEncoding("UTF-8");
        writer.setResource(new ClassPathResource(resourceName));
        writer.setLineAggregator(new DelimitedLineAggregator<Contact>() {
            {
                setDelimiter("|");

                setFieldExtractor(new BeanWrapperFieldExtractor<Contact>() {
                    {
                        setNames(new String[] {"email", "name", "status"});
                    }
                });
            }
        });
        return writer;
    }
}
