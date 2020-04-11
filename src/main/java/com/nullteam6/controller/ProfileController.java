package com.nullteam6.controller;

import com.nullteam6.models.Profile;
import com.nullteam6.service.ProfileDAO;
import com.nullteam6.utility.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private ProfileDAO dao;

    @Autowired
    public void setDao(ProfileDAO dao) {
        this.dao = dao;
    }

    @GetMapping
    public PaginatedList<Profile> getAllProfile(
            @RequestParam(name = "offset", required = false) Integer offset
    ) {
        if (offset != null)
            return dao.getAllOffset(offset);
        else
            return dao.getAll();
    }

    @GetMapping("/{id}")
    public Profile getByUID(@PathVariable String id) {
        return dao.getProfileByUID(id);
    }

    @PutMapping
    public boolean updateProfile(@RequestBody Profile profile) {
        return dao.updateProfile(profile);
    }
}
