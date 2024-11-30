package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.*;
import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.repos.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Value("${upload.path}")
    private String uploadPath;

    @RequestMapping(method = RequestMethod.POST, value = "/update-check-list")
    public void saveCheckItem(@RequestBody Map<String,Object> itemData, @AuthenticationPrincipal User user) throws JsonProcessingException {
        System.out.println(itemData.get("id") + " " + itemData.get("is_marked"));
        itemRepo.updateCheck(Integer.toUnsignedLong((Integer)itemData.get("id")), (Boolean) itemData.get("is_marked"));
    }
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
        for (int i = 0; i < tagList.size(); i++){
            if (tagList.get(i).getName().isEmpty()) {
                tagList.remove(i);
                i--;
            } else {
                Tag tag1 = tagRepo.findByName(tagList.get(i).getName());
                if (tag1 == null) {
                    tag1 = tagList.get(i);
                    tag1.setUser(user);
                }
                tag1.getLists().add(list);
                list.getTags().add(tag1);
                tagRepo.save(tag1);
            }
        }
        listRepo.save(list);
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("tags", tagList1);
        return map;
    }
    @RequestMapping(method = RequestMethod.POST, value = "/push-note-image", consumes = "multipart/form-data", produces = "application/json")
    public Map<String, Object> saveNoteImage(@RequestPart("file") MultipartFile multipartFile, @RequestPart("note") Note note, @AuthenticationPrincipal User user) throws IOException {
        java.util.List<Tag> tagList = new ArrayList<>(note.getTags());
        note.getTags().clear();
        note.setUser(user);
        note.setDateUpdateTZByUser();
        for (int i = 0; i < tagList.size(); i++){
            if (tagList.get(i).getName().isEmpty()) {
                tagList.remove(i);
                i--;
            } else {
                Tag tag1 = tagRepo.findByName(tagList.get(i).getName());
                if (tag1 == null) {
                    tag1 = tagList.get(i);
                    tag1.setUser(user);
                }
                tag1.getNotes().add(note);
                note.getTags().add(tag1);
                tagRepo.save(tag1);
            }
        }
        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(uploadPath + "/" + resultFileName));
            System.out.println("путь " + resultFileName);
            note.setFilename(resultFileName);
        }
        noteRepo.save(note);
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("note", note);
        map.put("tags", tagList1);
        return map;
    }
    @RequestMapping(method = RequestMethod.POST, value = "/push-note", produces = "application/json")
    public Map<String, Object> saveNote(@RequestBody Note note, @AuthenticationPrincipal User user) throws IOException {
        java.util.List<Tag> tagList = new ArrayList<>(note.getTags());
        note.getTags().clear();
        note.setUser(user);
        note.setDateUpdateTZByUser();
        note.setFilename("");
        for (int i = 0; i < tagList.size(); i++){
            if (tagList.get(i).getName().isEmpty()) {
                tagList.remove(i);
                i--;
            } else {
                Tag tag1 = tagRepo.findByName(tagList.get(i).getName());
                if (tag1 == null) {
                    tag1 = tagList.get(i);
                    tag1.setUser(user);
                }
                tag1.getNotes().add(note);
                note.getTags().add(tag1);
                tagRepo.save(tag1);
            }
        }
        noteRepo.save(note);
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("note", note);
        map.put("tags", tagList1);
        return map;
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
            Long idDB = tagRepo.findIdByName(tag.getName());
            if (idDB != null) {
                tag.setId(idDB);
            }
            tag.setUser(user);
        }
        Set<Tag> tagSet = new LinkedHashSet<>(tagList);
    list.setTags(tagSet);
        listRepo.save(list);
        List l = listRepo.findById(id).get();
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("list", l);
        map.put("tags", tagList1);

        return map;
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/update-note-not-image", produces = "application/json")
    public Map<String, Object> updateNoteNotImage(@RequestBody Note note, @AuthenticationPrincipal User user) throws IOException {
        note.setUser(user);
        note.setDateUpdateByUser();
        Long id = note.getId();
        java.util.List<Tag> tagNote = new ArrayList<>(note.getTags());
        for (Tag tag : tagNote){
            Long idDB = tagRepo.findIdByName(tag.getName());
            if (idDB != null) {
                tag.setId(idDB);
            }
            tag.setUser(user);
        }
        Set<Tag> tagSet = new LinkedHashSet<>(tagNote);
        note.setTags(tagSet);

        noteRepo.save(note);
        Note n = noteRepo.findById(id).get();
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("note", n);
        map.put("tags", tagList1);

        return map;
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/update-note", produces = "application/json")
    public Map<String, Object> updateNote(@RequestBody Note note, @AuthenticationPrincipal User user) throws IOException {
        File file = new File(uploadPath + "/" + note.getFilename());
        file.delete();

        note.setFilename("");

        note.setUser(user);
        note.setDateUpdateByUser();
        Long id = note.getId();
        java.util.List<Tag> tagNote = new ArrayList<>(note.getTags());
        for (Tag tag : tagNote){
            Long idDB = tagRepo.findIdByName(tag.getName());
            if (idDB != null) {
                tag.setId(idDB);
            }
            tag.setUser(user);
        }
        Set<Tag> tagSet = new LinkedHashSet<>(tagNote);
        note.setTags(tagSet);

        noteRepo.save(note);
        Note n = noteRepo.findById(id).get();
        java.util.List<Tag> tagList1 = tagRepo.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("note", n);
        map.put("tags", tagList1);

        return map;
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/update-note-image", consumes = "multipart/form-data", produces = "application/json")
    public Map<String, Object> updateNoteImage(@RequestPart("file") MultipartFile multipartFile, @RequestPart Note note, @AuthenticationPrincipal User user) throws IOException {

        File file = new File(uploadPath + "/" + noteRepo.findFileNameById(note.getId()));
        file.delete();

        note.setUser(user);
        note.setDateUpdateByUser();
        Long id = note.getId();
        java.util.List<Tag> tagNote = new ArrayList<>(note.getTags());
        for (Tag tag : tagNote){
            Long idDB = tagRepo.findIdByName(tag.getName());
            if (idDB != null) {
                tag.setId(idDB);
            }
            tag.setUser(user);
        }
        Set<Tag> tagSet = new LinkedHashSet<>(tagNote);
        note.setTags(tagSet);

        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(uploadPath + "/" + resultFileName));
            System.out.println("путь " + resultFileName);
            note.setFilename(resultFileName);
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
    public void deleteList(@PathVariable("id") List list) {
        listRepo.delete(list);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-note/{id}")
    public void deleteNote(@PathVariable("id") Note note) {
        File file = new File(uploadPath + "/" + note.getFilename());
        file.delete();
        noteRepo.delete(note);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-tag/{id}")
    public void deleteTag(@PathVariable("id") Long id) {
        tagRepo.deleteById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete-note-image/{filename}")
    public void deleteImage(@PathVariable("filename") String filename) {
        File file = new File(uploadPath + "/" + filename);
        file.delete();
        noteRepo.updateImage(filename, "");
    }
    @RequestMapping(method = RequestMethod.POST, value = "/lists")
    public java.util.List<List> getAllLists(@RequestBody Map<String,Object> req, @AuthenticationPrincipal User user) {
        System.out.println(req.entrySet());
        System.out.println(req.get("date") + " " + req.get("id"));
        java.util.List<List> lists = new ArrayList<>();
        if ((Integer)req.get("id") != -1) {
            if ((Integer)req.get("date") == 0) {
                lists = listRepo.findAllByUserIdByTagIdByDateDesc(user.getId(), Integer.toUnsignedLong((Integer)req.get("id")));
            } else lists = listRepo.findAllByUserIdByTagIdByDateAsc(user.getId(), Integer.toUnsignedLong((Integer)req.get("id")));
        } else {
            if ((Integer)req.get("date") == 0) {
                lists = listRepo.findAllByUserIdByDateDesc(user.getId());
            } else lists = listRepo.findAllByUserIdByDateAsc(user.getId());
        }
        return lists;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/notes")
    public java.util.List<Note> getAllNotes(@RequestBody Map<String,Object> req, @AuthenticationPrincipal User user) {
        System.out.println(req.entrySet());
        System.out.println(req.get("date") + " " + req.get("id"));
        java.util.List<Note> notes = new ArrayList<>();
        if ((Integer)req.get("id") != -1) {
            if ((Integer)req.get("date") == 0) {
                notes = noteRepo.findAllByUserIdByTagIdByDateDesc(user.getId(), Integer.toUnsignedLong((Integer)req.get("id")));
            } else notes = noteRepo.findAllByUserIdByTagIdByDateAsc(user.getId(), Integer.toUnsignedLong((Integer)req.get("id")));
        } else {
            if ((Integer)req.get("date") == 0) {
                notes = noteRepo.findAllByUserIdByDateDesc(user.getId());
            } else notes = noteRepo.findAllByUserIdByDateAsc(user.getId());
        }
        return notes;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search-name")
    public Map<String,Object> getByName(@RequestBody Map<String,Object> req, @AuthenticationPrincipal User user) {
        java.util.List<List> lists = new ArrayList<>();
        if ((Integer)req.get("id") != -1) {
            if ((Integer)req.get("date") == 0) {
                lists = listRepo.findAllByUserIdByTitleByTagIdByDateDesc(user.getId(), (String)req.get("title"), Integer.toUnsignedLong((Integer)req.get("id")));
            } else lists = listRepo.findAllByUserIdByTitleByTagIdByDateAsc(user.getId(), (String)req.get("title"), Integer.toUnsignedLong((Integer)req.get("id")));
        } else {
            if ((Integer)req.get("date") == 0) {
                lists = listRepo.findAllByUserIdByTitleByDateDesc(user.getId(), (String)req.get("title"));
            } else lists = listRepo.findAllByUserIdByTitleByDateAsc(user.getId(), (String)req.get("title"));
        }
        java.util.List<Note> notes = new ArrayList<>();
        if ((Integer)req.get("id") != -1) {
            if ((Integer)req.get("date") == 0) {
                notes = noteRepo.findAllByUserIdByTitleByTagIdByDateDesc(user.getId(), (String)req.get("title"), Integer.toUnsignedLong((Integer)req.get("id")));
            } else notes = noteRepo.findAllByUserIdByTitleByTagIdByDateAsc(user.getId(), (String)req.get("title"), Integer.toUnsignedLong((Integer)req.get("id")));
        } else {
            if ((Integer)req.get("date") == 0) {
                notes = noteRepo.findAllByUserIdByTitleByDateDesc(user.getId(), (String)req.get("title"));
            } else notes = noteRepo.findAllByUserIdByTitleByDateAsc(user.getId(), (String)req.get("title"));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("lists", lists);
        map.put("notes", notes);
        return map;
    }
}
