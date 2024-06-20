package com.sopha.app.controller;

import com.sopha.app.models.User;
import com.sopha.app.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ResponsableController {

    @Autowired
    private UserDetailService userDetailService;

    @GetMapping("/clientsRole2")
    public String getClientsRole2(Model model) {
        List<User> clientsRole2 = userDetailService.getUsersWithCommandesByRole(2L);
        model.addAttribute("clientsRole2", clientsRole2);
        return "vue/clientsRole2";
    }
}
