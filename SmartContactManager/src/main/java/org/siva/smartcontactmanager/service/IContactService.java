package org.siva.smartcontactmanager.service;

import java.util.List;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.User;
import org.springframework.data.domain.Page;

public interface IContactService {
	
	Contact saveContact(Contact contact);
	
	Page<Contact> getContactsByUser(User user, int pageNo);
	
	Contact getContactById(long id);
	
	Boolean deleteContact(Contact contact, User user);

	List<Contact> getContactsByNameAndUser(String query, User user);


}
