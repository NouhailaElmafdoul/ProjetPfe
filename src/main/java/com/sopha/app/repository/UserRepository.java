package com.sopha.app.repository;



import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sopha.app.models.User;


public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findById(Long id);
	/*public User findByEmail(String email); */
	Optional<User> findByEmail(String email);
	
	 @Query("SELECT u FROM User u WHERE u.role.id = :roleId")
	    List<User> findByRoleId(Long roleId);

}