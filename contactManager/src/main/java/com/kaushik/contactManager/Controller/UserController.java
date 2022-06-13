package com.kaushik.contactManager.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kaushik.contactManager.entities.Contact;
import com.kaushik.contactManager.entities.User;
import com.kaushik.contactManager.helper.Message;
import com.kaushik.contactManager.repository.ContactRepository;
import com.kaushik.contactManager.repository.DbRepo;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private DbRepo dbrepo;

	@Autowired
	private ContactRepository contactRepository;

	// @model attribute will allow to add this common data to all the methods of
	// this handler.
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		User user = dbrepo.getUserByEmail(userName);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String userDashboard(Model model) {
		model.addAttribute("title", "User Dashboard");
		return "normal_user/userdashboard";
	}

	@GetMapping("/add-contact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal_user/add-contact";
	}

	@RequestMapping(value = "/add-contact", method = RequestMethod.POST)
	public String processContact(@ModelAttribute @Valid Contact contact, BindingResult res,
			@RequestParam("profilePhoto") MultipartFile file, Model model, Principal principal, HttpSession session) {

		try {
			if (res.hasErrors()) {
				model.addAttribute("contact", contact);
				System.out.println("Error happened");
				return "normal_user/add-contact";
			}

			String userName = principal.getName();
			User user = dbrepo.getUserByEmail(userName);

			if (file.isEmpty()) {
				contact.setcImageUrl("contact.png");
				model.addAttribute("contact", contact);
			} else {
				contact.setcImageUrl(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}

			contact.setUser(user);

			user.getContacts().add(contact);

			dbrepo.save(user);

			System.out.println("Contact added");

			session.setAttribute("message", new Message("Contact saved successfully", "alert-success"));

			return "redirect:/user/add-contact";

		} catch (Exception e) {

			session.setAttribute("message", new Message("Something went wrong !!", "alert-danger"));
			return "normal_user/add-contact";
		}

	}

	// show contacts handler

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") int page, Model model, Principal principal, HttpSession session) {
		model.addAttribute("title", "Your Contacts");

		String userName = principal.getName();

		User user = dbrepo.getUserByEmail(userName);

		Pageable pageable = PageRequest.of(page, 5);

		Page<Contact> contacts = contactRepository.findContactByUser(user.getId(), pageable);

		if (contacts.isEmpty()) {
			session.setAttribute("empty", "No contacts");
			session.setAttribute("nav", "nav");
		} else {
			session.removeAttribute("nav");
		}
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal_user/show-contacts";
	}
	// showing specific contact details

	@RequestMapping("/contact/{cId}")
	public String showContactDetail(@PathVariable("cId") int cId, Model model, Principal principal) {

		Contact contact;
		try {
			contact = contactRepository.getById(cId);

			String userName = principal.getName();

			User user = dbrepo.getUserByEmail(userName);

			if (user.getId() == contact.getUser().getId()) {
				model.addAttribute("contact", contact);
				model.addAttribute("title", contact.getcName());
			}

			return "normal_user/contact-detail";
		} catch (Exception e) {
			contact = null;
			return "normal_user/contact-detail";
		}

	}

	// to delete a contact

	@RequestMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") int cId, Model model, Principal principal, HttpSession session) {
		Contact contact;
		try {
			contact = contactRepository.getById(cId);

			String userName = principal.getName();
			User user = dbrepo.getUserByEmail(userName);

			if (user.getId() == contact.getUser().getId()) {
				contact.setUser(null);
				contactRepository.delete(contact);
			} else {
				session.setAttribute("null", "null");
			}

			return "redirect:/user/show-contacts/0";
		} catch (Exception e) {
			session.setAttribute("null", "null");
			return "redirect:/user/show-contacts/0";
		}

	}

	// update contact

	@RequestMapping("/update-contact/{cId}")
	public String updateContact(@PathVariable("cId") int cId, Model model, HttpSession session, Principal principal) {

		try {
			Contact contact = contactRepository.getById(cId);

			String userName = principal.getName();
			User user = dbrepo.getUserByEmail(userName);

			if (user.getId() == contact.getUser().getId()) {
				model.addAttribute("contact", contact);
			} else {
				session.setAttribute("null", "null");
				return "redirect:/user/show-contacts/0";
			}
			return "normal_user/update-contact";

		} catch (Exception e) {
			session.setAttribute("null", "null");
			return "redirect:/user/show-contacts/0";
		}

	}

	// process-update
	@RequestMapping("/process-update")
	public String processContact(@ModelAttribute @Valid Contact contact, BindingResult res,
			@RequestParam("profilePhoto") MultipartFile file, Model model, HttpSession session, Principal principal) {
		try {
			
			Contact oldContactDetail = contactRepository.getById(contact.getcId());
			
			if (res.hasErrors()) {
				contact.setcImageUrl(oldContactDetail.getcImageUrl());
				model.addAttribute("contact", contact);
				return "normal_user/update-contact";
			}

			

			if (!file.isEmpty()) {
				
				//to delete old image
				
				File deleteFile = new ClassPathResource("static/img").getFile();
				File f1= new File(deleteFile,oldContactDetail.getcImageUrl());
				f1.delete();
				
				//to add new image
				contact.setcImageUrl(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			} else {
				contact.setcImageUrl(oldContactDetail.getcImageUrl());
			}

			String userName = principal.getName();
			User user = dbrepo.getUserByEmail(userName);

			contact.setUser(user);

			System.out.println("Contact ID: " + contact.getcId());

			this.contactRepository.save(contact);

			return "redirect:/user/contact/"+contact.getcId();

		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong !!", "alert-danger"));
			return "normal_user/update-contact";
		}

	}
	
	@RequestMapping("/profile")
	public String profile(Model model,Principal principal) {
		try {
			String userName = principal.getName();
			User user = dbrepo.getUserByEmail(userName);
			
			model.addAttribute("user",user);
			model.addAttribute("title","Profile page");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "normal_user/profile";
	}

}
