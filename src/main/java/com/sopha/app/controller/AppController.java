package com.sopha.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sopha.app.models.Role;
import com.sopha.app.models.User;
import com.sopha.app.repository.RoleRepository;
import com.sopha.app.repository.UserRepository;

@Controller
@RequestMapping("/")
public class AppController {
  
	@Autowired
	private UserRepository userRepo;
	

	@Autowired
	private RoleRepository roleRepository;
	
	
	@GetMapping("/acceuil")
	public String viewHomePage() {
		return "vue/acceuil";
	}

	
	
	@Controller
	public class LoginController {

	    @GetMapping("/connexion")
	    public String showLoginForm() {
	        return "vue/connexion";
	    }

	    @PostMapping("/traiter_connexion")
	    public String performLogin(@RequestParam String email, @RequestParam String mot_de_passe) {
	        // Traitement de l'authentification ici
	        return "redirect:/tableau_de_bord"; // Redirection après une connexion réussie
	    }
	}

	@Controller
	public class DashboardController {

	    @GetMapping("/tableau_de_bord")
	    public String dashboard() {
	        // Logique du tableau de bord ici
	        return "vue/acceuil"; // Le nom correspond à un fichier HTML dans le répertoire "templates"
	    }
	}

	
	
	
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "vue/authentification";
	}
	@PostMapping("/register")
	public String processRegister(@Valid User user, BindingResult result) {
	    if (result.hasErrors()) {
	        return "vue/authentification"; 
	    }
        Role userRole = roleRepository.findByName("ROLE_USER")
        	    .orElseThrow(() -> new RuntimeException("Role not found")); 
        if (userRole != null) {
            user.setRole(userRole); 
        } 
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getMot_de_passe());
		user.setMot_de_passe(encodedPassword);
        userRepo.save(user);
        return "redirect:/connexion"; 
    }
	
	
}