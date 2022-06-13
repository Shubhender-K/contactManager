package com.kaushik.contactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kaushik.contactManager.entities.User;
import com.kaushik.contactManager.repository.DbRepo;

public class CustomUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private DbRepo dbrepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user= dbrepo.getUserByEmail(username);
		
		if(user==null)
			throw new UsernameNotFoundException("Could not found the user with this username !!");
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user); 
		
		return customUserDetails;
	}

}
