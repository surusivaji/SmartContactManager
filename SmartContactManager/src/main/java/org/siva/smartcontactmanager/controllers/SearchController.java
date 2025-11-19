package org.siva.smartcontactmanager.controllers;

import java.security.Principal;
import java.util.List;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.User;
import org.siva.smartcontactmanager.service.IContactService;
import org.siva.smartcontactmanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class SearchController {
	
	@Autowired
	private IContactService contactService;
	
	@Autowired
	private IUserService userService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable String query, HttpSession session, Principal principal) {
		User user = userService.getUserByEmail(principal.getName());
		List<Contact> contacts = contactService.getContactsByNameAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}

}