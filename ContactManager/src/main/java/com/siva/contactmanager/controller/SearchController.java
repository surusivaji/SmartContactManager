package com.siva.contactmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.siva.contactmanager.model.Contact;
import com.siva.contactmanager.model.User;
import com.siva.contactmanager.service.IContactService;

import jakarta.servlet.http.HttpSession;

@RestController
public class SearchController {
	
	@Autowired
	private IContactService contactService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, HttpSession session) {
		System.out.println(query);
		User user = (User) session.getAttribute("user");
		List<Contact> contacts = contactService.getContactsByNameAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}

}
