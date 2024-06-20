package com.sopha.app.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sopha.app.models.User;
import com.sopha.app.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepo.findByEmail(authentication.getName()).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getMot_de_passe(),
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())));
    }
    
    public List<User> getUsersByRole(Long roleId) {
        return userRepo.findByRoleId(roleId);
    }
    @Transactional
    public List<User> getUsersWithCommandesByRole(Long roleId) {
        List<User> users = userRepo.findByRoleId(roleId);
        users.forEach(user -> user.getCommandes().size()); 
        return users;
    }
}
