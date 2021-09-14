package com.moshna.traker.dto;

import com.moshna.traker.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

public class UserDto {

    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
}
