package com.sopha.app.controller;

import com.sopha.app.models.User;
import com.sopha.app.repository.UserRepository;
import com.sopha.app.service.UserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired 
    private UserDetailService userDetailService;
   
    @GetMapping("/monProfile")
    public String showUserProfile(Model model) {
        User authenticatedUser = userDetailService.getCurrentAuthenticatedUser();

        if (authenticatedUser != null) {
            model.addAttribute("user", authenticatedUser);
            return "vue/monProfile"; 
        } else {
            
            return "redirect:/connexion"; 
        }
    }
    
    
    @GetMapping("/editProfile")
    public String showEditProfilePage(Model model) {
        User authenticatedUser = userDetailService.getCurrentAuthenticatedUser();

        if (authenticatedUser != null) {
            model.addAttribute("user", authenticatedUser);
            return "vue/editProfile";
        } else {
            
            return "redirect:/connexion";
        }
    }
    
    @PostMapping("/saveProfile")
    public String saveProfile(@ModelAttribute User updatedUser) {
        User authenticatedUser = userDetailService.getCurrentAuthenticatedUser();

        if (authenticatedUser != null) {
            
            authenticatedUser.setNom(updatedUser.getNom());
            authenticatedUser.setPrenom(updatedUser.getPrenom());
            authenticatedUser.setEmail(updatedUser.getEmail());
            authenticatedUser.setTelephone(updatedUser.getTelephone());
            authenticatedUser.setCin(updatedUser.getCin());
            authenticatedUser.setAdresse(updatedUser.getAdresse());

            
             updateUser(authenticatedUser);

            return "redirect:/monProfile";
        } else {
            
            return "redirect:/connexion"; 
        }
    }
    public void updateUser(User user) {
        
        userRepo.save(user); 
    }
}