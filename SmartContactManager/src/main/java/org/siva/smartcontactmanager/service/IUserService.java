package org.siva.smartcontactmanager.service;

import org.siva.smartcontactmanager.models.User;

public interface IUserService {
	
	User saveUser(User user);
	
	Boolean existsByEmail(String email);
	
	User getUserByEmail(String email);
	
	User getUserById(long id);
	
	Boolean removeUser(User user);
	
}