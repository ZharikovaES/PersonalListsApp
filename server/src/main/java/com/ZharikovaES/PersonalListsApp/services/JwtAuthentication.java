package com.ZharikovaES.PersonalListsApp.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.ZharikovaES.PersonalListsApp.models.Role;

import java.util.Collection;
import java.util.Set;

public class JwtAuthentication implements Authentication {
    private boolean authenticated;
    private String username;
    private String firstName;
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setAuthenticatedFlag(boolean authenticated) {
        this.authenticated = authenticated;
    }
} 
