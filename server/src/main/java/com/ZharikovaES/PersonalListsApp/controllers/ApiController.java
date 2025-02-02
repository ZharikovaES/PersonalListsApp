package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.*;
import com.ZharikovaES.PersonalListsApp.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController()
public class ApiController {

        @Autowired
        private ListRepo listRepo;
        @Autowired
        private NoteRepo noteRepo;

    @RequestMapping(method = RequestMethod.GET, value = "/api/lists")
    public java.util.List<List> getAllLists(@ModelAttribute RequestParams params, @AuthenticationPrincipal User user) {
        java.util.List<List> lists = null;

        if(StringUtils.hasText(params.getSearchText()) && params.getTagId() != null)
            lists = listRepo.findByUserIdAndTagIdAndTitleIsLike(user.getId(), params.getTagId(), params.getSearchText(), buildPager(params));
        else if(StringUtils.hasText(params.getSearchText()))
            lists = listRepo.findByUserIdAndTitleIsLike(user.getId(), params.getSearchText(), buildPager(params));
        else if (params.getTagId() != null)
            lists = listRepo.findByUserIdAndTagId(user.getId(), params.getTagId(), buildPager(params));
        else lists = listRepo.findByUserId(user.getId(), buildPager(params));

        return lists;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/notes")
    public java.util.List<Note> getAllNotes(@ModelAttribute RequestParams params, @AuthenticationPrincipal User user) {
        java.util.List<Note> notes = null;

        if(StringUtils.hasText(params.getSearchText()) && params.getTagId() != null)
            notes = noteRepo.findByUserIdAndTagIdAndTitleIsLike(user.getId(), params.getTagId(), params.getSearchText(), buildPager(params));
        else if(StringUtils.hasText(params.getSearchText()))
            notes = noteRepo.findByUserIdAndTitleIsLike(user.getId(), params.getSearchText(), buildPager(params));
        else if (params.getTagId() != null)
            notes = noteRepo.findByUserIdAndTagId(user.getId(), params.getTagId(), buildPager(params));
        else notes = noteRepo.findByUserId(user.getId(), buildPager(params));

        return notes;
    }

    private PageRequest buildPager(RequestParams params){
        Sort.Order order = null;
        switch (params.getOrderingType()){
            case DateAsc -> order = new Sort.Order(Sort.Direction.ASC, "dateUpdate");
            case DateDesc -> order = new Sort.Order(Sort.Direction.DESC, "dateUpdate");
            case UserAsc -> order = new Sort.Order(Sort.Direction.ASC, "order");
            case UserDesc -> order = new Sort.Order(Sort.Direction.DESC, "order");
        }

        return PageRequest.of(params.getPageNum(),
                params.getPageSize(),
                Sort.by(order));
    }
}
