package com.example.roleauth.service.impl;

import com.example.roleauth.dao.RoleRepository;
import com.example.roleauth.entity.Role;
import com.example.roleauth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailsImpl implements UserDetails {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl get(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.id = id;
        userDetails.username = username;
        userDetails.email = email;
        userDetails.password = password;
        userDetails.authorities = authorities;
        return userDetails;
    }

    public UserDetailsImpl build(User user) {
        List<Role> roles = roleRepository.getUserRoles(user.getId());
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        try {
            LOGGER.info("authorities {} ", new ObjectMapper().writeValueAsString(authorities));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return get(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}