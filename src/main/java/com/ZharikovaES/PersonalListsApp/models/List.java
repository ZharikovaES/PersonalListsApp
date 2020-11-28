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
public class List implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @JsonIgnore
    @Column(name = "list_id", insertable = false, updatable =false)
    private Long listId;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "list", cascade = CascadeType.ALL , orphanRemoval = true)
    private java.util.List<Item> items;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Tag.class)
    @CollectionTable(name = "record_tags", joinColumns = @JoinColumn(name = "record_id"))
    private Set<Tag> tags  = new HashSet<>();;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private User user;

    @JsonIgnore
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @JsonIgnore
    @Column(name = "date_update_tz")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdateTZ;

    @JsonIgnore
    @Transient
    private String dateUpdateTZStr;

    @JsonIgnore
    @Transient
    private SimpleDateFormat formatForDate = new SimpleDateFormat("HH:mm:ss ' ' E dd.MM.yyyy");

    public List() {
        this.dateUpdate = new Date();
    }

    public List(String title, java.util.List<Item> items, Set<Tag> tags, User user) {
        this.title = title;
        this.items = items;
        this.tags = tags;
        dateUpdate = new Date();
        dateUpdateTZ = Calendar.getInstance(TimeZone.getTimeZone(user.getTimezoneID())).getTime();
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

    public Date getDateUpdateTZ() {
        return dateUpdateTZ;
    }

    public void setDateUpdateTZ(Date dateUpdateTZ) {
        this.dateUpdateTZ = dateUpdateTZ;
    }

    public void setDateUpdateTZByUser() {
        this.dateUpdateTZ = Calendar.getInstance(TimeZone.getTimeZone(user.getTimezoneID())).getTime();
    }

    @JsonGetter("date_update")
    public String dateToString() {
        dateUpdateTZStr = formatForDate.format(dateUpdateTZ);
        return dateUpdateTZStr;
    }

    public SimpleDateFormat getFormatForDate() {
        return formatForDate;
    }

    public void setFormatForDate(SimpleDateFormat formatForDate) {
        this.formatForDate = formatForDate;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public String getDateUpdateTZStr() {
        return dateUpdateTZStr;
    }

    public void setDateUpdateTZStr(String dateUpdateTZStr) {
        this.dateUpdateTZStr = dateUpdateTZStr;
    }

}
