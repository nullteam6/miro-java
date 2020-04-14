package com.nullteam6.controller;

import com.nullteam6.models.Profile;
import com.nullteam6.models.ProfileDTO;
import com.nullteam6.service.ProfileDAO;
import com.nullteam6.utility.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/profile")
public class ProfileController {

    private ProfileDAO dao;

    @Autowired
    public void setDao(ProfileDAO dao) {
        this.dao = dao;
    }

    @GetMapping
    public PaginatedList<Profile> getAllProfile(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "search", required = false) String search
    ) {
        if (offset != null) {
            if (search != null) {
                return dao.searchOffset(search, offset);
            } else {
                return dao.getAllOffset(offset);
            }
        } else {
            if (search != null) {
                return dao.search(search);
            }
            return dao.getAll();
        }
    }

    @GetMapping("/{id}")
    public ProfileDTO getByUID(@PathVariable String id) {
        return dao.getProfileByUID(id);
    }

    @PutMapping
    public boolean updateProfile(@RequestBody ProfileDTO profile) {
        Profile p = new Profile(profile);
        return dao.updateProfile(p);
    }
}
