package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.Item;
import com.ZharikovaES.PersonalListsApp.models.List;
import com.ZharikovaES.PersonalListsApp.models.Tag;
import com.ZharikovaES.PersonalListsApp.models.User;
import com.ZharikovaES.PersonalListsApp.repos.ListRepo;
import com.ZharikovaES.PersonalListsApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class MessageController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ListRepo listRepo;

    @RequestMapping(method = RequestMethod.POST, value = "/push_list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<List> saveList(@RequestBody List list, @AuthenticationPrincipal User user) {
        if (list == null) {
            return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
        }
        java.util.List<Item> itemList = new ArrayList<>(list.getItems());
        for (Item item : itemList){
            item.setList(list);
        }

        list.setUser(user);
        listRepo.save(list);

        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

}
