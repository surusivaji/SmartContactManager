package com.siva.contactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.siva.contactmanager.model.Contact;
import com.siva.contactmanager.model.User;
import com.siva.contactmanager.service.IContactService;
import com.siva.contactmanager.service.IUserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IContactService contactService;
	
	@PostMapping("/loginInfo")
	public String userLogin(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
		User user = userService.findUserByEmailAndPassword(email, password);
		if (user!=null) {
			session.setAttribute("user", user);
			return "redirect:/user/home";
		}
		else {
			session.setAttribute("failMsg", "Invalid Credientials");
			return "redirect:/login";
		}
	}
	
	@GetMapping("/user/home")
	public String homePage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			return "user/Home";
		}
		else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/user/addcontact")
	public String addContactForm(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			return "user/AddContact";
		}
		else {
			return "redirect:/login";
		}
	}
	
	@PostMapping("/user/process-contact")
	public String storeContactInformation(HttpSession session, 
										@ModelAttribute Contact contact,
										@RequestParam("profileImage") MultipartFile file) {
		try {
			User user = (User) session.getAttribute("user");
			if (user!=null) {
				if (file.isEmpty()) {
					System.out.println("you are not uploading the image");
					contact.setImage("contact.png");
				}
				else {
					File saveFile = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					contact.setImage(file.getOriginalFilename());
				}
				contact.setUser(user);
				Contact saveContact = contactService.saveContact(contact);
				if (saveContact!=null) {
					session.setAttribute("successMsg", "contact saved successfully");
					return "redirect:/user/addcontact";
				}
				else {
					session.setAttribute("failMsg", "invalid information");
					return "redirect:/user/addcontact";
				}
			}
			else {
				return "redirect:/login";
			}
		}
		catch (Exception e) {
			session.setAttribute("failMsg", e.getMessage());
			return "redirect:/user/addcontact";
		}
	}
	
	@GetMapping("/user/viewcontacts/{pageNo}")
	public String showContacts(@PathVariable("pageNo") int pageNo, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			Page<Contact> page = contactService.getAllContactsByUser(user, pageNo);
			if (page!=null) {
				model.addAttribute("contacts", page.getContent());
				model.addAttribute("currentPage", pageNo);
				model.addAttribute("totalPages", page.getTotalPages());
				model.addAttribute("totalElements", page.getTotalElements());
				return "user/ShowContacts";
			}
			else {
				return "redirect:/user/home";
			}
		}
		else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/user/viewcontact/{id}")
	public String displayContactInformation(HttpSession session, Model model, @PathVariable("id") int id) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			Contact contact = contactService.getContactById(id);
			if (contact!=null) {
				if (user.getId()==contact.getUser().getId()) {
					model.addAttribute("contact", contact);
					return "user/ViewContact";
				}
				else {
					model.addAttribute("contactInfo", "You don't have permission to see this contact");
					return "user/ViewContact";
				}
			}
			else {
				model.addAttribute("contactInfo", "contact information not found");
				return "user/ViewContact";
			}
		}
		else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/user/deletecontact/{id}")
	public String deleteContact(HttpSession session, Model model, @PathVariable("id") int id) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			Contact contact = contactService.getContactById(id);
			if (contact!=null) {
				if (contact.getUser().getId()==user.getId()) {
					boolean deleteContact = contactService.deleteContact(contact);
					if (deleteContact) {
						session.setAttribute("successMsg", "contact removed successfully...");
						return "redirect:/user/viewcontacts/0";
					}
					else {
						session.setAttribute("failMsg", "something went wrong on the server...");
						return "redirect:/user/viewcontacts/0";
					}
				}
				else {
					session.setAttribute("failMsg", "you are unautherized...");
					return "redirect:/user/viewcontacts/0";
				}
			}
			else {
				session.setAttribute("failMsg", "contact is not found...");
				return "redirect:/user/viewcontacts/0";
			}
		}
		else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/user/updatecontact/{id}")
	public String updateContact(@PathVariable("id") int id, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			Contact contact = contactService.getContactById(id);
			if (contact!=null) {
				if (contact.getUser().getId()==user.getId()) {
					model.addAttribute("contact", contact);
					return "user/UpdateContact";
				}
				else {
					session.setAttribute("failMsg", "you are unauthorized...");
					return "redirect:/user/viewcontacts/0";
				}
			}
			else {
				session.setAttribute("failMsg", "contact is not found...");
				return "redirect:/user/viewcontacts/0";
			}
		}
		else {
			return "redirect:/login";
		}
	}
	
	@PostMapping("/user/update-process")
	public String updateContactInformation(@ModelAttribute Contact contact, HttpSession session, Model model, @RequestParam("profileImage") MultipartFile multipartFile) {
		try {
			User user = (User) session.getAttribute("user");
			if (user!=null) {
				Contact oldContactDetail = contactService.getContactById(contact.getId());
				if (!multipartFile.isEmpty()) {
//					File deleteFile = new ClassPathResource("static/images").getFile();
//					File file1 = new File(deleteFile, oldContactDetail.getImage());
//					file1.delete();
					
					File file = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					contact.setImage(multipartFile.getOriginalFilename());
				}
				else {
					contact.setImage(oldContactDetail.getImage());
				}
				contact.setUser(user);
				Contact saveContact = contactService.saveContact(contact);
				if (saveContact!=null) {
					session.setAttribute("successMsg", "contact updated successfully");
					return "redirect:/user/viewcontact/"+contact.getId();
				}
				else {
					session.setAttribute("failMsg", "something went wrong on the server");
					return "redirect:/user/viewcontact/"+contact.getId();					
				}
			}
			else {
				return "redirect:/login";
			}
		}
		catch (Exception e) {
			session.setAttribute("failMsg", "something went wrong");
			return "redirect:/user/viewcontact/0";
		}
	}
	
	@GetMapping("/user/viewprofile")
	public String viewUserInformation(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			return "user/ViewProfile";
		}
		else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/user/settings")
	public String changePassword(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			return "user/ChangePassword";
		}
		else {
			return "redirect:/login";
		}
	}
	
	@PostMapping("/user/changepassword") 
	public String passwordUpdation(HttpSession session, Model model, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			model.addAttribute("user", user);
			User user1 = userService.findUserByEmailAndPassword(user.getEmail(), oldPassword);
			if (user1!=null) {
				user1.setPassword(newPassword);
				User updation = userService.addUser(user1);
				if (updation!=null) {
					session.setAttribute("user", updation);
					session.setAttribute("successMsg", "password updated successfully");
					return "redirect:/user/settings";
				}
				else {
					session.setAttribute("failMsg", "something went wrong");
					return "redirect:/user/settings";
				}
			}
			else {
				session.setAttribute("failMsg", "your password is incorrect");
				return "redirect:/user/settings";
			}
		}
		else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/user/logout")
	public String logoutPage(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			session.removeAttribute("user");
			session.setAttribute("logout", "You Been Logged Out");
			return "redirect:/login";
		}
		else {
			return "redirect:/login";
		}
	}
	
}
