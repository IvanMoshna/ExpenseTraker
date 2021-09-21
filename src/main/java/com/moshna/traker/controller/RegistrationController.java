package com.moshna.traker.controller;

import com.moshna.traker.dto.UserDto;
import com.moshna.traker.mapper.UserMapping;
import com.moshna.traker.model.Role;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(UserDto userDto, Model model) {
        User userFromDb = userRepo.findByUsername(userDto.getUsername());

        if(userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        User user = UserMapping.toUser(userDto);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepo.save(user);

        return "redirect:/login";
    }
}
