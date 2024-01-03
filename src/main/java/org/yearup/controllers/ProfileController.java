package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class ProfileController {
    @Autowired
    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<Profile> getProfile(Principal principal) {
        try {
            Profile profile = profileDao.getProfile(principal.getName());

            if (profile == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
            }

            return new ResponseEntity<>(profile, HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            throw rse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", e);
        }
    }

    @RequestMapping(path = "/profile", method = RequestMethod.PUT)
    public ResponseEntity<String> updateProfile(Principal principal, @RequestBody Profile profile) {
        try {
            Profile updatedProfile = profileDao.update(principal.getName(), profile);
            if (updatedProfile != null) {
                return new ResponseEntity<>("Profile updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Profile not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating profile: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}