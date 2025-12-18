package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

import java.util.List;

@RestController
@RequestMapping("profiles")
public class ProfileController
{
    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao){ this.profileDao = profileDao; }

    @GetMapping("{userId}")
    public List<Profile> getByUserId(@PathVariable int userId)
    {
        try
        {
            var profile = profileDao.getByUserId(userId);

            if(profile.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return profile;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }

    @PutMapping
    public void updateProfile(@RequestBody Profile profile)
    {
        try
        {
            profileDao.update(profile);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }

}
