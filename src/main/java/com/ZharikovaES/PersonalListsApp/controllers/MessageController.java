package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.*;
import com.ZharikovaES.PersonalListsApp.repos.ItemRepo;
import com.ZharikovaES.PersonalListsApp.repos.ListRepo;
import com.ZharikovaES.PersonalListsApp.repos.NoteRepo;
import com.ZharikovaES.PersonalListsApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

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

    @RequestMapping(method = RequestMethod.POST, value = "/push-list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<List> saveList(@RequestBody List list, @AuthenticationPrincipal User user) {
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        java.util.List<Item> itemList = new ArrayList<>(list.getItems());
        for (Item item : itemList){
            item.setList(list);
        }

        list.setUser(user);
        list.setDateUpdateTZByUser();
        listRepo.save(list);
        userRepo.save(user);

        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/push-note", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<Note> saveNote(@RequestBody Note note, @AuthenticationPrincipal User user) {
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        note.setUser(user);
        note.setDateUpdateTZByUser();
        noteRepo.save(note);
        userRepo.save(user);

        return new ResponseEntity<>(note, HttpStatus.CREATED);
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

    @RequestMapping(method = RequestMethod.PUT, value = "/update-list", produces = "application/json")
    public ResponseEntity<List> updateList(@RequestBody List list, @AuthenticationPrincipal User user) {
        list.setUser(user);
        list.setDateUpdateByUser();
        Long id = list.getId();

        listRepo.save(list);
        List l = listRepo.findById(id).get();

        return new ResponseEntity<>(l, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/update-note", produces = "application/json")
    public ResponseEntity<Note> updateNote(@RequestBody Note note, @AuthenticationPrincipal User user) {
        note.setUser(user);
        note.setDateUpdateByUser();
        Long id = note.getId();

        noteRepo.save(note);
        Note n = noteRepo.findById(id).get();

        return new ResponseEntity<>(n, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-list/{id}")
    public void deleteList(@PathVariable("id") List list) {
        listRepo.delete(list);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-note/{id}")
    public void deleteNote(@PathVariable("id") Note note) {
        noteRepo.delete(note);
    }
}
