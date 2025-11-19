package org.siva.smartcontactmanager.service;

import java.util.List;
import java.util.Optional;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.Favorite;
import org.siva.smartcontactmanager.models.User;
import org.siva.smartcontactmanager.repository.ContactRepository;
import org.siva.smartcontactmanager.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IContactServiceImpl implements IContactService {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private FavoriteRepository favoriteRepository;

	@Override
	public Contact saveContact(Contact contact) {
		try {
			Contact save = contactRepository.save(contact);
			return save;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Page<Contact> getContactsByUser(User user, int pageNo) {
		try {			
			Pageable pageable = PageRequest.of(pageNo, 5);
			Page<Contact> page = contactRepository.findByUser(user, pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Contact getContactById(long id) {
		try {
			Optional<Contact> optional = contactRepository.findById(id);
			return optional.get();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Boolean deleteContact(Contact contact, User user) {
		try {
			Favorite favorite = favoriteRepository.findByContactAndUser(contact, user);
			favoriteRepository.delete(favorite);
			contactRepository.delete(contact);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public List<Contact> getContactsByNameAndUser(String query, User user) {
		List<Contact> contacts = contactRepository.findByNameContainingAndUser(query, user);
		return contacts;
	}

}

