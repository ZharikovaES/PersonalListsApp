package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "lists")
public class List extends UnitData implements Serializable {

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "list", cascade = CascadeType.ALL , orphanRemoval = true)
    private java.util.List<Item> items;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "list_tags",
            joinColumns = { @JoinColumn(name = "list_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags = new HashSet<>();

    public List() {
    }

    public List(String title, java.util.List<Item> items, Set<Tag> tags, User user) {
        super(user, title);
        this.items = items;
        this.tags = tags;
    }

    public java.util.List<Item> getItems() {
        return items;
    }

    public void setItems(java.util.List<Item> items) {
        this.items = items;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
