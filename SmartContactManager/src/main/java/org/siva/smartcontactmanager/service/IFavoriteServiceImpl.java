package org.siva.smartcontactmanager.service;

import java.util.List;
import java.util.Optional;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.Favorite;
import org.siva.smartcontactmanager.models.User;
import org.siva.smartcontactmanager.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IFavoriteServiceImpl implements IFavoriteService {
	
	@Autowired
	private FavoriteRepository favoriteRepository;

	@Override
	public Favorite saveToFavorites(Favorite favorite) {
		try {
			Favorite save = favoriteRepository.save(favorite);
			return save;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Favorite getByContactAndUser(Contact contact, User user) {
		try {
			Favorite favorite = favoriteRepository.findByContactAndUser(contact, user);
			return favorite;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Favorite> getFavoritesByUser(User user) {
		try {
			List<Favorite> favorites = favoriteRepository.findByUser(user);
			return favorites;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Page<Favorite> getFavoritesByUser(User user, int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 4);
			Page<Favorite> page = favoriteRepository.findByUser(pageable, user);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	@Override
	public Favorite getFavoriteById(Long id) {
		try {
			Optional<Favorite> optional = favoriteRepository.findById(id);
			return optional.get();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Boolean removeFavorite(Favorite favorite) {
		try {
			favoriteRepository.delete(favorite);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

