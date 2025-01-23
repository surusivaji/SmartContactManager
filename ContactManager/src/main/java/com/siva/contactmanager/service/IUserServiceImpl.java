package com.siva.contactmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.siva.contactmanager.model.User;
import com.siva.contactmanager.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class IUserServiceImpl implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User addUser(User user) {
		try {
			User save = userRepository.save(user);
			if (save!=null) {
				return save;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public User findUserByEmailAndPassword(String email, String password) {
		try {
			User user = userRepository.findByEmailAndPassword(email, password);
			if (user!=null) {
				return user;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public void removeSessionMessage() {
		HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		session.removeAttribute("successMsg");
		session.removeAttribute("failMsg");
		session.removeAttribute("logout");
	}

}
