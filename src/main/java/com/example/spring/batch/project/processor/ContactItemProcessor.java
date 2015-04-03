package com.example.spring.batch.project.processor;

import org.springframework.batch.item.ItemProcessor;
import com.example.spring.batch.project.domain.Contact;

/**
 * Process each input Contact to upper case all data to output Contact.
 * 
 * @author Borja López Altarriba
 */
public class ContactItemProcessor implements ItemProcessor<Contact, Contact> {

    public Contact process(final Contact contact) throws Exception {
		final String name = contact.getName().toUpperCase();
        final String email = contact.getEmail().toUpperCase();
        final String status = contact.getStatus().toUpperCase();

        final Contact transformedContact = new Contact(name, email, status);

        return transformedContact;
	}

}
