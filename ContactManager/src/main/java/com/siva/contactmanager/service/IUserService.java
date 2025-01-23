package com.siva.contactmanager.service;

import com.siva.contactmanager.model.User;

public interface IUserService {
	
	User addUser(User user);
	User findUserByEmailAndPassword(String email, String password);

}
