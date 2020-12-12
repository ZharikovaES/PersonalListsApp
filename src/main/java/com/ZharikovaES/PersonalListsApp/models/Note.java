package com.ZharikovaES.PersonalListsApp.models;

import com.fasterxml.jackson.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
