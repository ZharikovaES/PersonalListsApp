package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;


    @ManyToMany(mappedBy = "tags")
    @JsonIgnoreProperties("tags")
    private Set<List> lists = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonIgnoreProperties("tags")
    private Set<Note> notes = new HashSet<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("tags")
    private User user;

    public Tag() {
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Tag)) return false;
        Tag tag = (Tag) obj;
        return this.id.equals(tag.id) && this.name.equals(tag.name);
    }
    @Override
    public int hashCode(){
        return Objects.hash(id, name);
    }

    public Tag(String name) {
        this.name = name;
    }

    @JsonGetter("id_db")
    public Long getId() {
        return id;
    }

    @JsonSetter("id_db")
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Set<List> getLists() {
        return lists;
    }

    public void setLists(Set<List> lists) {
        this.lists = lists;
    }

    @JsonIgnore
    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @PreRemove
    private void removeAssociations() {
        for(List list: lists){
            list.getTags().remove(this);
        }
        for(Note note: notes){
            note.getTags().remove(this);
        }
    }
}
