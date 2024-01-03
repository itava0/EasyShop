package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class ProfileController {
    @Autowired
    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Profile> getProfile(@PathVariable int userId) {
        try {
            Profile profile = profileDao.getProfile(userId);

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

    @RequestMapping(path = "/profile/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateProfile(@PathVariable int userId, @RequestBody Profile profile) {
        try {
            Profile updatedProfile = profileDao.update(userId, profile);
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