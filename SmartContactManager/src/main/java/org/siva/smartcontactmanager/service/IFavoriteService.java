package org.siva.smartcontactmanager.service;

import java.util.List;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.Favorite;
import org.siva.smartcontactmanager.models.User;
import org.springframework.data.domain.Page;

public interface IFavoriteService {
	
	Favorite saveToFavorites(Favorite favorite);
	
	Favorite getByContactAndUser(Contact contact, User user);
	
	List<Favorite> getFavoritesByUser(User user);
	
	Page<Favorite> getFavoritesByUser(User user, int pageNo);
	
	Favorite getFavoriteById(Long id);
	
	Boolean removeFavorite(Favorite favorite);


}
