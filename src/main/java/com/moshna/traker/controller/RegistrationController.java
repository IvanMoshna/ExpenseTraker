package com.moshna.traker.controller;

import com.moshna.traker.dto.UserDto;
import com.moshna.traker.mapper.UserMapping;
import com.moshna.traker.model.Role;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.UserRepo;
import com.moshna.traker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepo userRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(UserDto userDto, @RequestParam Map<String, String> form, Model model) {
        User userFromDb = userRepo.findByUsername(userDto.getUsername());

        if(userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        User user = UserMapping.toUser(userDto);
        Set<Role> roleSet = new HashSet<>();
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        for (Map.Entry<String, String> entry : form.entrySet()) {
            String key = entry.getKey();
            if (roles.contains(key)) {
                roleSet.add(Role.valueOf(form.get(key)));
            }
        }

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(roleSet.isEmpty()) {
            user.setRoles(Collections.singleton(Role.USER));
            userRepo.save(user);
            return "redirect:/login";
        } else {
            user.setRoles(roleSet);
            userRepo.save(user);
            model.addAttribute("users", userService.getUserDtoList(userService.getUserList()));
            model.addAttribute("userId", userService.getCurrentlyUser().getId());
            model.addAttribute("allRoles", roles);
            return "userList";
        }
    }
}
