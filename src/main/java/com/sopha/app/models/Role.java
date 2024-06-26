package com.sopha.app.models;



import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
//@NotBlank(message = "Le mot de passe  est obligatoire !!!!!")
  private String name;
  
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

 
}