package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "active", columnDefinition = "BOOLEAN")
    private boolean active;

    @Column(name = "date_registration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistration;

    @Column(name = "date_last_activity")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLastActivity;

    @Column(name = "timezone")
    private String timezoneID;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<List> lists = new HashSet<>();;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;
    public User() {
    }

    public Set<List> getLists() {
        return lists;
    }

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
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateRegistration() {
        return dateRegistration;
    }

    public Date getDateLastActivity() {
        return dateLastActivity;
    }

    public void setLists(Set<List> lists) {
        this.lists = lists;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public Set<Role> getRoles() {
        return role;
    }

    public void setRoles(Set<Role> role) {
        this.role = role;
    }

    public void setDateRegistration(Date dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public void setDateLastActivity(Date dateLastActivity) {
        this.dateLastActivity = dateLastActivity;
    }

    public String getTimezoneID() {
        return timezoneID;
    }

    public void setTimezoneID(String timezone) {
        this.timezoneID = timezone;
    }

}
