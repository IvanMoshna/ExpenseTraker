package com.moshna.traker.mapper;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.dto.UserDto;
import com.moshna.traker.model.Expense;
import com.moshna.traker.model.User;
import lombok.NoArgsConstructor;

import javax.jws.soap.SOAPBinding;

@NoArgsConstructor
public class UserMapping {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .password(user.getPassword())//???
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .roles(userDto.getRoles())
                .password(userDto.getPassword())
                .build();
    }

    public static User toUser(User user, UserDto dto) {
        user.setUsername(dto.getUsername());
        user.setRoles(dto.getRoles());

        return user;
    }
}
