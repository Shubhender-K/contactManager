package com.kaushik.contactManager.myvalidation;

//import java.sql.SQLException;
//import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;

//import javax.persistence.EntityManager;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

//import com.kaushik.contactManager.entities.User;

@Repository
public class MyValidation {
	
	@Autowired
	private JdbcTemplate template;
	
	public boolean finUserByEmail(String email) {
		
		Map<String, Object> loginMap =null;
		try{
		    loginMap = template.queryForMap("select * from user where email= ?", new Object[] {email});
		}
		catch(EmptyResultDataAccessException ex){
		    System.out.println("Exception.......");
		    loginMap = null;
		}
		if(loginMap==null || loginMap.isEmpty()){
		    return true;
		}
		else{
		    return false;
		}
	}
}
