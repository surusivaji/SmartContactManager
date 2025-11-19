package org.siva.smartcontactmanager.service;

import java.util.List;
import java.util.Optional;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.Favorite;
import org.siva.smartcontactmanager.models.User;
import org.siva.smartcontactmanager.repository.ContactRepository;
import org.siva.smartcontactmanager.repository.FavoriteRepository;
import org.siva.smartcontactmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class IUserServiceImpl implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private FavoriteRepository favoriteRepository;
	
	@Override
	public User saveUser(User user) {
		try {	
			User save = userRepository.save(user);
			return save;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Boolean existsByEmail(String email) {
		try {
			Boolean isExists = userRepository.existsByEmail(email);
			return isExists;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public User getUserByEmail(String email) {
		try {
			User user = userRepository.findByEmail(email);
			return user;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public User getUserById(long id) {
		try {
			Optional<User> optional = userRepository.findById(id);
			return optional.get();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Boolean removeUser(User user) {
		try {
			/*List<Favorite> favorites = favoriteRepository.findByUser(user);
			List<Contact> contacts = contactRepository.findByUser(user);
			favoriteRepository.deleteAllInBatch(favorites);
			contactRepository.deleteAllInBatch(contacts); */
			userRepository.delete(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 

}
