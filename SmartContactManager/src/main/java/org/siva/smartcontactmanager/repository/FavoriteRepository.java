package org.siva.smartcontactmanager.repository;

import java.util.List;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.Favorite;
import org.siva.smartcontactmanager.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	
	Favorite findByContactAndUser(Contact contact, User user);
	
	List<Favorite> findByUser(User user); 
	
	Page<Favorite> findByUser(Pageable pageable, User user);
	
}
