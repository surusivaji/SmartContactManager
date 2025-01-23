package com.siva.contactmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.siva.contactmanager.model.Contact;
import com.siva.contactmanager.model.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	List<Contact> findByUser(User user);
	Page<Contact> findByUser(User user, Pageable pageable);
	List<Contact> findByNameContainingAndUser(String name, User user);

}
