package org.siva.smartcontactmanager.repository;

import java.util.List;
import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	Page<Contact> findByUser(User user, Pageable pageable);
	
	List<Contact> findByNameContainingAndUser(String name, User user);
	
	List<Contact> findByUser(User user);
	
}

