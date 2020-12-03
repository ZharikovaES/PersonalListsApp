package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.*;
import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.repos.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class MessageController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ListRepo listRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private TagRepo tagRepo;

    @RequestMapping(method = RequestMethod.POST, value = "/push-list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public Map<String, Object> saveList(@RequestBody List list, @AuthenticationPrincipal User user) throws JsonProcessingException {
        java.util.List<Item> itemList = new ArrayList<>(list.getItems());
        for (Item item : itemList){
            item.setList(list);
        }
        list.setUser(user);
        list.setDateUpdateTZByUser();
        java.util.List<Tag> tagList = new ArrayList<>(list.getTags());
        list.getTags().clear();
        for (Tag tag : tagList){
            Tag tag1 = tagRepo.findByName(tag.getName());
            if (tag1 == null) {
                tag1 = tag;
                tag1.setUser(user);
            }
            tag1.getLists().add(list);
            list.getTags().add(tag1);
            tagRepo.save(tag1);
        }
        listRepo.save(list);
        userRepo.save(user);
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("tags", tagList1);
        return map;
    }
    @RequestMapping(method = RequestMethod.POST, value = "/push-note", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public Map<String, Object> saveNote(@RequestBody Note note, @AuthenticationPrincipal User user) {
        java.util.List<Tag> tagList = new ArrayList<>(note.getTags());
        note.getTags().clear();
        note.setUser(user);
        note.setDateUpdateTZByUser();
        for (Tag tag : tagList){
            Tag tag1 = tagRepo.findByName(tag.getName());
            if (tag1 == null) {
                tag1 = tag;
                tag1.setUser(user);
            }
            tag1.getNotes().add(note);
            note.getTags().add(tag1);
            tagRepo.save(tag1);
        }
        noteRepo.save(note);
        userRepo.save(user);
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("note", note);
        map.put("tags", tagList1);
        return map;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/lists", produces = "application/json")
    public ResponseEntity<java.util.List<List>> getAllLists(@AuthenticationPrincipal User user) {
        java.util.List<List> lists =  listRepo.findAllByUserId(user.getId());
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/notes", produces = "application/json")
    public ResponseEntity<java.util.List<Note>> getAllNotes(@AuthenticationPrincipal User user) {
        java.util.List<Note> notes = noteRepo.findAllByUserId(user.getId());
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/tags", produces = "application/json")
    public ResponseEntity<java.util.List<Tag>> getAllTags(@AuthenticationPrincipal User user) {
        java.util.List<Tag> tags = tagRepo.findAll();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-list", produces = "application/json")
    public Map<String, Object> updateList(@RequestBody List list, @AuthenticationPrincipal User user) {
        list.setUser(user);
        list.setDateUpdateByUser();
        Long id = list.getId();
        java.util.List<Tag> tagList = new ArrayList<>(list.getTags());

        for (Tag tag : tagList){
            tag.setId(tagRepo.findIdByName(tag.getName()));
            tag.setUser(user);
        }

        listRepo.save(list);
        List l = listRepo.findById(id).get();
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("list", l);
        map.put("tags", tagList1);

        return map;
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/update-note", produces = "application/json")
    public Map<String, Object> updateNote(@RequestBody Note note, @AuthenticationPrincipal User user) {
        note.setUser(user);
        note.setDateUpdateByUser();
        Long id = note.getId();
        java.util.List<Tag> tagList = new ArrayList<>(note.getTags());
        for (Tag tag : tagList){
            tag.setId(tagRepo.findIdByName(tag.getName()));
            tag.setUser(user);
        }

        noteRepo.save(note);
        Note n = noteRepo.findById(id).get();
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("note", n);
        map.put("tags", tagList1);

        return map;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-list/{id}")
    public void deleteList(@PathVariable("id") Long id) {
        listRepo.deleteById(id);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-note/{id}")
    public void deleteNote(@PathVariable("id") Note note) {
        noteRepo.delete(note);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-tag/{id}")
    public void deleteTag(@PathVariable("id") Tag tag) {
        tagRepo.delete(tag);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/sort-tag")
    public Map<String, Object> sortByTag(@RequestBody Map<String,Object> req) {
        System.out.println(req.entrySet());
        System.out.println(req.get("date") + " " + req.get("id"));
        java.util.List<List> lists = new ArrayList<>();
        if (((Integer) req.get("date")) == 0) {
            lists = listRepo.findAllByTagIdByDate(Integer.toUnsignedLong((Integer)req.get("id")));
        } else lists = listRepo.findAllByTagIdByDateDesc(Integer.toUnsignedLong((Integer)req.get("id")));
        java.util.List<Note> notes = new ArrayList<>();
        if (((Integer) req.get("date")) == 0) {
            notes = noteRepo.findAllByTagIdByDate(Integer.toUnsignedLong((Integer)req.get("id")));
        } else notes = noteRepo.findAllByTagIdByDateDesc(Integer.toUnsignedLong((Integer)req.get("id")));
        Map<String, Object> map = new HashMap<>();
        map.put("lists", lists);
        map.put("notes", notes);
        return map;
    }
}
