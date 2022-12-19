package com.example.admin.controller;

import com.example.library.Dto.AdminDto;
import com.example.library.model.Admin;
import com.example.library.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping("/index")
    public String home(Model model){
        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @GetMapping("/register")
    public String addNewAdmin(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new AdminDto());
        return "login";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto")AdminDto adminDto,
                              BindingResult result,
                              Model model,
                              HttpSession session){

        try {
            session.removeAttribute("message");
            if(result.hasErrors()){
                model.addAttribute("adminDto", adminDto);
                result.toString();
                return "login";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if(admin != null){
                model.addAttribute("adminDto", adminDto);
                session.setAttribute("message", "Your email has been registered!");
                return "login";
            }
            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminService.save(adminDto);
                System.out.println("success");
                session.setAttribute("message", "Register successfully!");
                model.addAttribute("adminDto", adminDto);
            }else{
                model.addAttribute("adminDto", adminDto);
                session.setAttribute("message", "Password is not same!");
                System.out.println("password not same");
                return "/login";
            }
        }catch (Exception e){
            session.setAttribute("message", "Sever is error, please try again later");
        }
        return "login";

    }
}
