package com.siva.contactmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.siva.contactmanager.model.Contact;
import com.siva.contactmanager.model.User;

public interface IContactService {
	
	Contact saveContact(Contact contact);
	List<Contact> getAllContactsByUser(User user);
	Page<Contact> getAllContactsByUser(User user, int pageNo);
	Contact getContactById(int id);
	boolean deleteContact(Contact contact);
	List<Contact> getContactsByNameAndUser(String name, User user);

}
