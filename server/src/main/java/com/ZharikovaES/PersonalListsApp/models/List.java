package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "lists")
public class List extends UnitData implements Serializable {

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "list", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "list_tags",
            joinColumns = { @JoinColumn(name = "list_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    @JsonIgnoreProperties("lists")
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "view_order", nullable = false)
    @ColumnDefault("0")
    private int order;

    public List() {
    }

    public List(String title, Set<Item> items, Set<Tag> tags, User user, int order) {
        super(user, title);
        this.items = items;
        this.tags = tags;
        this.order = order;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @JsonGetter("items")
    public java.util.List<Item> getItemsList(){
        return items.stream().sorted(Comparator.comparingInt(Item::getOrder)).toList();
    }

    @JsonSetter("items")
    public void setItemsList(java.util.List<Item> items){
        this.items.clear();
        for(int i = 0; i < items.size(); i++){
            items.get(i).setOrder(i);
        }
        this.items.addAll(items);
    }
}
