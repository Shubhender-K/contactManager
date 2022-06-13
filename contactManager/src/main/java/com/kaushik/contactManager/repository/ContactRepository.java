package com.kaushik.contactManager.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaushik.contactManager.entities.Contact;
import com.kaushik.contactManager.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("from Contact as c where c.user.id =:userId ")
	public Page<Contact> findContactByUser(@Param("userId")int userId, Pageable pageable);
	
	@Query("from Contact as c where c.cName like %:name% and c.user.id= :userId")
	public List<Contact> search(@Param("name")String name,@Param("userId") int userId);
	
	
	//Query qry = session.createQuery("From RegistrationBean as rb where rb."+searchCriteria+"  like ?");
	//qry.setString(0, "%"+searchField+"%");
	
	//from RegistrationBean as rb where rb.:searchCriteria like '%:searchField%'"
}
