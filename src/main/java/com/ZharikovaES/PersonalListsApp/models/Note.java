package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "notes")
public class Note extends UnitData implements Serializable {

    @Column(name = "text")
    private String text;

    @Column(name = "image")
    private String filename;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "note_tags",
            joinColumns = { @JoinColumn(name = "note_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )

    @JsonIgnoreProperties("notes")
    private Set<Tag> tags;

    public Note() {
    }
    public Note(String title, String text, User user, Set<Tag> tags) {
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
