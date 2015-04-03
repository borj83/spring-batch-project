package com.example.spring.batch.project.domain;

/**
 * Contact.
 * 
 * @author Borja López Altarriba
 */
public class Contact {
    private String name;
    private String email;
    private String status;

    public Contact() {

    }

    public Contact(String name, String email, String status) {
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    @Override
    public String toString() {
        return "name: " + name + ", email: " + email + ", status: " + status;
    }

}