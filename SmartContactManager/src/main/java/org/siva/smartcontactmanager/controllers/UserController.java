package org.siva.smartcontactmanager.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Collections;

import org.siva.smartcontactmanager.models.Contact;
import org.siva.smartcontactmanager.models.Favorite;
import org.siva.smartcontactmanager.models.User;
import org.siva.smartcontactmanager.service.IContactService;
import org.siva.smartcontactmanager.service.IFavoriteService;
import org.siva.smartcontactmanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public IUserService userService;

	@Autowired
	private IContactService contactService;
	
	@Autowired
	private IFavoriteService favoriteService;

	@ModelAttribute
	public void commonData(Model model, Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
	}

	@GetMapping("/home")
	public String homePage() {
		return "/User/Home";
	}

	@GetMapping("/addContact")
	public String addContactPage() {
		return "/User/AddContact";
	}

	@PostMapping("/process-contact")
	public String saveContactInformation(@ModelAttribute Contact contact,
	        @RequestParam("profileImage") MultipartFile multipartFile,
	        HttpSession session, Principal principal) {
	    try {
	        if (multipartFile.isEmpty()) {
	            session.setAttribute("infoMsg", "Please upload the image");
	            return "redirect:/user/addContact";
	        }
	        String uploadDirectory = System.getProperty("user.dir") + "/uploads/contacts/";
	        File directory = new File(uploadDirectory);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }
	        String originalFilename = multipartFile.getOriginalFilename();
	        String name = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
	        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
	        String uniqueFilename;
	        File file;
	        do {
	            int randomNumber = (int) (Math.random() * 9000) + 100;
	            uniqueFilename = name + "_" + randomNumber + extension;
	            file = new File(uploadDirectory + uniqueFilename);
	        } while (file.exists());
	        Path path = Paths.get(uploadDirectory + uniqueFilename);
	        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        contact.setImage(uniqueFilename);
	        String email = principal.getName();
	        User user = userService.getUserByEmail(email);
	        contact.setUser(user);
	        Contact saveContact = contactService.saveContact(contact);
	        if (saveContact != null) {
	            session.setAttribute("successMsg", "Contact information saved successfully");
	        } else {
	            session.setAttribute("failMsg", "Something went wrong");
	        }
	        return "redirect:/user/addContact";
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.setAttribute("warningMsg", "Image location not found");
	        return "redirect:/user/addContact";
	    }
	}


	@GetMapping("/viewContacts")
	public String viewContactsPage(Model model, @RequestParam(defaultValue = "0") int pageNo, Principal principal) {
		User user = userService.getUserByEmail(principal.getName());
		Page<Contact> page = contactService.getContactsByUser(user, pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("contacts", page.getContent());
		return "/User/ViewContacts";
	}

	@GetMapping("/viewContact/{id}")
	public String viewContactPage(@PathVariable long id, Model model, Principal principal) {
		Contact contact = contactService.getContactById(id);
		System.out.println(contact);
		if (contact != null) {
			User user = userService.getUserByEmail(principal.getName());
			System.out.println(user);
			if (contact.getUser().getId() == user.getId()) {
				System.out.println(user.getId());
				model.addAttribute("contact", contact);
				return "/User/ViewContact";
			} 
			else {
				model.addAttribute("contactInfo", "You don't have permission to see this contact");
				return "user/ViewContact";
			}
		} 
		else {
			model.addAttribute("contactInfo", "Contact information not found");
			return "/User/ViewContact";
		}
	}

	@GetMapping("/updateContact/{id}")
	public String updateContactPage(@PathVariable long id, Model model, Principal principal, HttpSession session) {
		Contact contact = contactService.getContactById(id);
		if (contact != null) {
			User user = userService.getUserByEmail(principal.getName());
			if (contact.getUser().getId() == user.getId()) {
				model.addAttribute("contact", contact);
				return "/User/UpdateContact";
			} 
			else {
				session.setAttribute("warningMsg", "Contact information not found");
				return "redirect:/user/viewContacts";
			}
		} 
		else {
			session.setAttribute("warningMsg", "Contact information not found");
			return "redirect:/user/viewContacts";
		}
	}

	@PostMapping("/update-process")
	public String updateContactInformation(@ModelAttribute Contact contact, HttpSession session,
			@RequestParam("profileImage") MultipartFile multipartFile, Principal principal) {
		try {
			Contact oldContact = contactService.getContactById(contact.getId());
			if (multipartFile.isEmpty()) {
				contact.setImage(oldContact.getImage());
			} 
			else {
				String uploadDirectory = System.getProperty("user.dir")+"/uploads/contacts/";
				File deleteFile = new File(uploadDirectory, oldContact.getImage());
				deleteFile.delete();
				File file = new File(uploadDirectory);
		        if (!file.exists()) {
		            file.mkdirs();
		        }
		        String originalFilename = multipartFile.getOriginalFilename();
		        String name = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
		        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
		        int randomNumber = (int) (Math.random() * 9000) + 100;
		        String uniqueFilename = name + "_" + randomNumber + extension;
		        Path path = Paths.get(uploadDirectory + uniqueFilename);
		        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		        contact.setImage(uniqueFilename);
			}
			User user = userService.getUserByEmail(principal.getName());
			contact.setUser(user);
			Contact saveContact = contactService.saveContact(contact);
			if (saveContact != null) {
				session.setAttribute("successMsg", "Contact information saved successfully");
			} 
			else {
				session.setAttribute("failMsg", "Something went wrong");
			}
			return "redirect:/user/viewContact/" + contact.getId();
		} catch (Exception e) {
			session.setAttribute("warningMsg", "Image location not found");
			return "redirect:/user/viewContact/" + contact.getId();
		}
	}

	@GetMapping("/deleteContact/{id}")
	public String deleteContactInformation(@PathVariable long id, HttpSession session,Model model, Principal principal) {
		Contact contact = contactService.getContactById(id);
		if (contact != null) {
			User user = userService.getUserByEmail(principal.getName());
			if (user.getId() == contact.getUser().getId()) {
				String fileDirectory = System.getProperty("user.dir")+"/uploads/contacts/";
				File deleteFile = new File(fileDirectory, contact.getImage());
				deleteFile.delete();
				Boolean isDelete = contactService.deleteContact(contact, user);
				if (isDelete) {
					return "redirect:/user/viewContacts";
				} 
				else {
					session.setAttribute("failMsg", "Something went wrong");
					return "redirect:/user/viewContact/" + contact.getId();
				}
			} 
			else {
				model.addAttribute("contactInfo", "You don't have permission to delete this contact");
				return "/User/ViewContact";
			}
		} else {
			session.setAttribute("warningMsg", "Contact information not found");
			return "redirect:/user/viewContacts";
		}
	}

	@GetMapping("/viewProfile")
	public String viewProfilePage(Model model, Principal principal) {
		User user = userService.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "/User/ViewProfile";
	}

	@GetMapping("/settings")
	public String changePasswordPage() {
		return "/User/ChangePassword";
	}

	@PostMapping("/change-password")
	public String changeUserPassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			Principal principal, HttpSession session) {
		User currentUser = userService.getUserByEmail(principal.getName());
		if (bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
			User saveUser = userService.saveUser(currentUser);
			if (saveUser != null) {
				session.setAttribute("successMsg", "Your password is successfully changed");
				return "redirect:/user/home";
			} 
			else {
				session.setAttribute("failMsg", "Something went wrong");
				return "redirect:/user/home";
			}
		} 
		else {
			session.setAttribute("failMsg", "Please Enter Correct Old Password");
			return "redirect:/user/settings";
		}
	}

	@GetMapping("/updateProfile")
	public String updateProfilePage(Principal principal, Model model) {
		User user = userService.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "/User/UpdateProfile";
	}

	@PostMapping("/updateUserInformation")
	public String updateUserInformation(@ModelAttribute User user,
			@RequestParam("profileImage") MultipartFile multipartFile, HttpSession session) {
		try {
			User oldUser = userService.getUserById(user.getId());
			if (multipartFile.isEmpty()) {
				user.setUserImage(oldUser.getUserImage());
			} 
			else {
				String uploadDirectory = System.getProperty("user.dir")+"/uploads/users/";
				File deleteFile = new File(uploadDirectory, oldUser.getUserImage());
				deleteFile.delete();
				File file = new File(uploadDirectory);
				if (!file.exists()) {
					file.mkdirs();
				}
				Path path = Paths.get(uploadDirectory + multipartFile.getOriginalFilename());
				Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				user.setUserImage(multipartFile.getOriginalFilename());
			}
			user.setEnabled(true);
			user.setPassword(oldUser.getPassword());
			user.setRole(oldUser.getRole());
			User saveUser = userService.saveUser(user);
			if (saveUser != null) {
				UserDetails userDetails = new org.springframework.security.core.userdetails.User(saveUser.getEmail(), saveUser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(saveUser.getRole())));
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, saveUser.getPassword(), userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				session.setAttribute("successMsg", "User information updated successfully");
			}
			else {
				session.setAttribute("failMsg", "Something went wrong");
			}
			return "redirect:/user/viewProfile";
		} catch (Exception e) {
			session.setAttribute("warningMsg", "Something went wrong");
			e.printStackTrace();
			return "redirect:/user/updateProfile";
		}
	}
	
	@GetMapping("/deleteAccount")
	public String deleteAccountPage() {
		return "/User/DeleteAccount";
	}
	
	@PostMapping("/accountDeletion")
	public String accountDeletion(Principal principal, HttpSession session, @RequestParam String email, @RequestParam String password) {
		User user = userService.getUserByEmail(principal.getName());
		if (user.getEmail().equals(email) && bCryptPasswordEncoder.matches(password, user.getPassword())) {
			String profileImage = user.getUserImage();
			Boolean isDelete = userService.removeUser(user);
			if (isDelete) {		
				String uploadDirectory = System.getProperty("user.dir")+"/uploads/users/";
				File deleteFile = new File(uploadDirectory, profileImage);
				deleteFile.delete();
				session.setAttribute("warningMsg", "User account deleted successfully");
				return "redirect:/signin";
			}
			else {
				session.setAttribute("failMsg", "Something went wrong");
				return "redirect:/user/deleteAccount";
			}
		} 
		else {
			session.setAttribute("failMsg", "email and password is incorrect");
			return "redirect:/user/deleteAccount";
		}
	}
	
	@GetMapping("/addToFavorites")
	public String addToFavorites(HttpSession session, @RequestParam Long userId, @RequestParam Long contactId) {
		Contact contact = contactService.getContactById(contactId);
		User user = userService.getUserById(userId);
		Favorite favorite = favoriteService.getByContactAndUser(contact, user);
		if (favorite==null) {
			favorite = new Favorite();
			favorite.setContact(contact);
			favorite.setUser(user);
			Favorite isSave = favoriteService.saveToFavorites(favorite);
			if (isSave!=null) {
				session.setAttribute("successMsg", "Favorite contact is added");
			}
			else {
				session.setAttribute("failMsg", "Something went wrong");
			}
		}
		else {
			session.setAttribute("warningMsg","Already contact is added to favorites");
		}
		return "redirect:/user/viewContacts";
	}
	
	@GetMapping("/favorites")
	public String showAllFavorites(Principal principal, Model model, @RequestParam(defaultValue="0") int pageNo) {
		User user = userService.getUserByEmail(principal.getName());
		Page<Favorite> page = favoriteService.getFavoritesByUser(user, pageNo);
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("favorites", page.getContent());
		return "/User/Favorites";
	}
	
	@GetMapping("/removeFavorite/{id}")
	public String removeFavorites(@PathVariable Long id, Principal principal, Model model, HttpSession session) {
		User user = userService.getUserByEmail(principal.getName());
		Favorite favorite = favoriteService.getFavoriteById(id);
		if (favorite!=null) {			
			if (favorite.getUser().getId()==user.getId()) {
				Boolean isDelete = favoriteService.removeFavorite(favorite);
				if (isDelete) {
					session.setAttribute("successMsg", "Favorite is removed successfully");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong");
				}
				return "redirect:/user/favorites";
			} else {
				model.addAttribute("contactInfo", "You don't have permission to access this contact");
				return "/User/ViewContact";
			}
		}
		else {
			model.addAttribute("contactInfo", "Contact information not found");
			return "/User/ViewContact";	
		}
	}

}
