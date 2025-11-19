package org.siva.smartcontactmanager.config;

import org.siva.smartcontactmanager.models.User;
import org.siva.smartcontactmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user==null) {
			throw new UsernameNotFoundException("User information not found");
		}
		UserDetails userDetails = new CustomUserDetails(user);
		return userDetails;
	}

}
