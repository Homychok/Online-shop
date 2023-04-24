package com.example.diplomproject.dto;

import com.example.diplomproject.annotations.MyAnnotation;
import com.example.diplomproject.config.GrantedAuthorityDeserializer;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class RegisterReq  implements UserDetails {
    @MyAnnotation(name = "логин")
    private String username;
    @MyAnnotation(name = "пароль")
    private String password;
    @MyAnnotation(name = "имя пользователя")
    private String firstName;
    @MyAnnotation(name = "фамилия пользователя")
    private String lastName;
    @MyAnnotation(name = "телефон пользователя")
    private String phone;
    @MyAnnotation(name = "роль пользователя")
    private Role role;
    @JsonIgnore
    @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
    private List<GrantedAuthority> authorities;

    public static RegisterReq fromRegisterReq(User user) {
        RegisterReq registerReq = new RegisterReq();

        registerReq.setUsername(user.getUsername());
        registerReq.setPassword(user.getPassword());
        registerReq.setRole(user.getRole());
        return registerReq;
    }

    public User toUser() {
        User user = new User();

        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        user.setRole(Role.USER);

        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setPhone(this.getPhone());
        user.setEnabled(true);
        return user;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
