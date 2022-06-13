package com.kaushik.contactManager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.contactManager.entities.Contact;
import com.kaushik.contactManager.entities.User;
import com.kaushik.contactManager.repository.ContactRepository;
import com.kaushik.contactManager.repository.DbRepo;

@RestController
public class SearchController {
	
	@Autowired
	private DbRepo dbrepo;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
		User user = dbrepo.getUserByEmail(principal.getName());
		List<Contact> contacts = contactRepository.search(query, user.getId());
		return ResponseEntity.ok(contacts);
	}
	
}
