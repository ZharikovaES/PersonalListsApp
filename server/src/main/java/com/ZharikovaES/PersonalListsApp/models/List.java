package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "lists")
public class List extends UnitData implements Serializable {

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "list", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "list_tags",
            joinColumns = { @JoinColumn(name = "list_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    @JsonIgnoreProperties("lists")
    private Set<Tag> tags = new HashSet<>();

    public List() {
    }

    public List(String title, Set<Item> items, Set<Tag> tags, User user) {
        super(user, title);
        this.items = items;
        this.tags = tags;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

}
