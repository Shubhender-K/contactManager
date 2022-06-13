package com.kaushik.contactManager.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kaushik.contactManager.entities.User;
import com.kaushik.contactManager.helper.Message;
import com.kaushik.contactManager.myvalidation.MyValidation;
import com.kaushik.contactManager.repository.DbRepo;

@Controller
public class HomeController {

	@Autowired
	private DbRepo userrepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String home() {
		return "home";
	}

	@RequestMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@GetMapping("/success")
	public String success(Model model) {
//		model.addAttribute("user", new User());
		return "success";
	}
	
	// handler for registeration

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult res,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		try {
			if (!agreement) {
				throw new Exception("You have not agreed to terms and conditions..!!");
			}
			User user1 = userrepo.getUserByEmail(user.getEmail());
			if(user1!=null) {
				System.out.println("email used");
				throw new Exception("Email already used !!");
			}
			if (res.hasErrors()) {
				model.addAttribute(user);
				System.out.println(res.toString());
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword( passwordEncoder.encode(user.getPassword()));
			User result = userrepo.save(user);
			model.addAttribute(result);
			//session.setAttribute("message", new Message("Registeration successfull..!!", "alert-success"));
			return "redirect:/success";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(user);
			session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	//login controller
	
	@GetMapping("/signin")
	public String login(Model model) {
		return "login";
	}


}
