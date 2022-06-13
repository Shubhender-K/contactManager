package com.kaushik.contactManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaushik.contactManager.entities.User;

public interface DbRepo extends JpaRepository<User, Integer >{
	
	@Query("select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email") String email);

}
