package com.siva.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.siva.contactmanager.model.User;
import com.siva.contactmanager.service.IUserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private IUserService userService;
	
	@GetMapping("/")
	public String homePage() {
		return "Home";
	}
	
	@GetMapping("/about")
	public String aboutPage() {
		return "About";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return "Login";
	}
	
	@GetMapping("/register")
	public String registrationPage() {
		return "Register";
	}
	
	@PostMapping("/do_register")
	public String saveUserInformation(@ModelAttribute("user") User user, @RequestParam(value="agreement", defaultValue = "false") boolean agreement, HttpSession session) {
		if (agreement) {
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setImageUrl("default.png");
			User save = userService.addUser(user);
			if (save!=null) {
				session.setAttribute("successMsg", "Registration Successful");
				return "redirect:/register"; 
			}
			else {
				session.setAttribute("failMsg", "Something Went Wrong");
				return "redirect:/register";
			}
		}
		else {
			session.setAttribute("failMsg", "you are not agreed the terms and conditions");
			return "redirect:/register";
		}
	}
	

}
