package org.siva.smartcontactmanager.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

import org.siva.smartcontactmanager.models.User;
import org.siva.smartcontactmanager.service.EmailService;
import org.siva.smartcontactmanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private IUserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmailService emailService;

	@GetMapping("/")
	public String indexPage() {
		return "Index";
	}

	@GetMapping("/register")
	public String registrationPage() {
		return "Registration";
	}

	@GetMapping("/signin")
	public String loginPage() {
		return "Login";
	}

	@GetMapping("/about")
	public String aboutUsPage() {
		return "About";
	}

	@GetMapping("/login-fail")
	public String failurePage() {
		return "Failure";
	}

	@PostMapping("/saveUserInformation")
	public String saveUserInformation(@ModelAttribute User user,
	        						  @RequestParam("profileImage") MultipartFile multipartFile, 
	        						  HttpSession session) {
	    try {
	        if (multipartFile.isEmpty()) {
	            session.setAttribute("warningMsg", "Please upload your image");
	            return "redirect:/register";
	        }
	        String uploadDir = System.getProperty("user.dir")+ "/uploads/users/";
	        File uploadFolder = new File(uploadDir);
	        if (!uploadFolder.exists()) {
	            uploadFolder.mkdirs();
	        }
	        Path path = Paths.get(uploadDir + multipartFile.getOriginalFilename());
	        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        user.setUserImage(multipartFile.getOriginalFilename());
	        user.setRole("ROLE_USER");
	        user.setEnabled(true);
	        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	        if (userService.existsByEmail(user.getEmail())) {
	            session.setAttribute("infoMsg", "Email already exists");
	            return "redirect:/register";
	        }
	        User savedUser = userService.saveUser(user);
	        if (savedUser != null) {
	            session.setAttribute("successMsg", "User registration successful");
	        } else {
	            session.setAttribute("failMsg", "Something went wrong");
	        }
	    } catch (Exception e) {
	        session.setAttribute("warningMsg", "Error saving image");
	    }
	    return "redirect:/register";
	}


	@GetMapping("/forgotPassword")
	public String forgotPasswordPage() {
		return "ForgotPassword";
	}

	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam String email, HttpSession session) {
	    Boolean isPresent = userService.existsByEmail(email);
	    if (isPresent) {
	        Random random = new Random();
	        int otp = 100000 + random.nextInt(900000);
	        System.out.println("OTP: " + otp);

	        String subject = "OTP From Smart Contact Manager";

	        String message = """
	            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px;
	                        border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">
	                <h2 style="color: #0b63a8; text-align: center;">Smart Contact Manager</h2>
	                <p style="font-size: 16px; color: #333;">Hello,</p>
	                <p style="font-size: 16px; color: #333;">Your One-Time Password (OTP) is:</p>
	                <div style="text-align: center; margin: 20px 0;">
	                    <span style="font-size: 28px; font-weight: bold; color: #0b63a8; 
	                                border: 2px dashed #0b63a8; padding: 10px 20px; border-radius: 5px;">
	                        %d
	                    </span>
	                </div>
	                <p style="font-size: 14px; color: #555;">This OTP is valid for <b>5 minutes</b>. 
	                   Please do not share it with anyone.</p>
	                <p style="font-size: 13px; color: #888;">If you didn’t request this, you can ignore this email.</p>
	            </div>
	        """.formatted(otp);

	        boolean flag = emailService.sendEmail(email, subject, message);

	        if (flag) {
	            session.setAttribute("myOtp", otp);
	            session.setAttribute("email", email);
	            return "VerifyOTP";
	        } else {
	            session.setAttribute("failMsg", "Please check your email");
	            return "redirect:/forgotPassword";
	        }
	    } else {
	        session.setAttribute("failMsg", "Please enter correct email");
	        return "redirect:/forgotPassword";
	    }
	}
	
	@PostMapping("/otp-checking")
	public String verifyOTP(@RequestParam("otp") Integer OTP, HttpSession session) {
		Integer myOTP = (Integer) session.getAttribute("myOtp");
		String email = (String) session.getAttribute("email");
		if (myOTP.equals(OTP)) {
			Boolean userPresent = userService.existsByEmail(email);
			if (userPresent) {
				return "ChangePassword";
			}
			else {
				session.setAttribute("failMsg", "User does not exits with this email");
				return "redirect:/forgotPassword";
			}
		}
		else {
			session.setAttribute("failMsg", "You have entered wrong otp");
			return "VerifyOTP";
		}
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword, HttpSession session) {
		String email = (String) session.getAttribute("email");
		User user = userService.getUserByEmail(email);
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		userService.saveUser(user);
		session.setAttribute("successMsg", "Password changed successfully");
		return "redirect:/signin";
	}
	
	

}
