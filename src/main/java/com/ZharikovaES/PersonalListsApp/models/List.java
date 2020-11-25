package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "lists")
public class List implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "list", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Item> items;

    @ElementCollection(targetClass = Tag.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "record_tags", joinColumns = @JoinColumn(name = "record_id"))
    @Enumerated(EnumType.STRING)
    private Set<Tag> tags;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private User user;

    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    public List() {
        this.dateUpdate = new Date();
    }

    public List(String title, Set<Item> items, Set<Tag> tags, User user) {
        this.title = title;
        if (items != null) this.items = items;
        if (tags != null) this.tags = tags;
        this.dateUpdate = new Date();
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @Override
    public String toString() {
        return "List{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", items=" + items +
                ", tags=" + tags +
                ", user=" + user +
                ", dateUpdate=" + dateUpdate +
                '}';
    }
}
