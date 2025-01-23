package com.siva.contactmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.siva.contactmanager.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByEmailAndPassword(String email, String password);

}
