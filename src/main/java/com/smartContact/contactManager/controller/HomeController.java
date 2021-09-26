package com.smartContact.contactManager.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContact.contactManager.Dao.UserRepository;
import com.smartContact.contactManager.Entity.User;
import com.smartContact.contactManager.Helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	BCryptPasswordEncoder bpe;

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model ) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	
	@RequestMapping("/about")
	public String about(Model model ) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signUp(Model model ) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signUp";
	}
	
	//Handler for register form
	
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value = "agreement",defaultValue = "false")boolean agreement ,Model model,HttpSession session) {
		
		try {
			if(!agreement) {
				System.out.println("User did not agreed terms and Condition");
				throw new Exception("you did not agreed terms and Condition");
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImgUrl("default.png");
			user.setPassword(bpe.encode(user.getPassword()));
			
			System.out.println("Agreement  : "+ agreement);
			System.out.println("USER : "+ user);
			User result=userRepository.save(user);
			
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully registerd!! ", "alert-success"));
			return "signUp";
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
			model.addAttribute(user);
			session.setAttribute("message", new Message("Something went wrong!! "+e.getMessage(), "alert-danger"));
			return "signUp";
		}
		
	}
}
 