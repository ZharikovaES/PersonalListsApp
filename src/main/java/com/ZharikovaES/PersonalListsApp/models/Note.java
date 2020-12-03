package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

@Entity
@Table(name = "notes")
public class Note extends UnitData implements Serializable {

    @Column(name = "text")
    private String text;

//    @JsonIgnore
//    @JsonIgnoreProperties({"user", "lists", "notes"})

//    @JsonManagedReference
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "note_tags",
            joinColumns = { @JoinColumn(name = "note_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )

    @JsonIgnoreProperties("notes")
    private List<Tag> tags;

    public Note() {
    }
    public Note(String title, String text, User user, List<Tag> tags) {
        super(user, title);
        this.text = text;
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
