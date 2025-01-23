package com.siva.contactmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.siva.contactmanager.model.Contact;
import com.siva.contactmanager.model.User;
import com.siva.contactmanager.repository.ContactRepository;

@Service
public class IContactServiceImpl implements IContactService {
	
	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Contact saveContact(Contact contact) {
		try {
			Contact save = contactRepository.save(contact);
			return save;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Contact> getAllContactsByUser(User user) {
		try {
			List<Contact> contacts = contactRepository.findByUser(user);
			return contacts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Page<Contact> getAllContactsByUser(User user, int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 5);
			Page<Contact> page = contactRepository.findByUser(user, pageable);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Contact getContactById(int id) {
		try {
			Optional<Contact> optional = contactRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
			else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean deleteContact(Contact contact) {
		try {
			contactRepository.delete(contact);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	@Override
	public List<Contact> getContactsByNameAndUser(String name, User user) {
		try {
			List<Contact> contacts = contactRepository.findByNameContainingAndUser(name, user);
			return contacts;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
