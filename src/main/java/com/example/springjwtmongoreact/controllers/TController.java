package com.example.springjwtmongoreact.controllers;

import com.example.springjwtmongoreact.models.ERole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RolesAllowed("USER")
public class TController {
    @GetMapping("/public")
    public String forPublic() {
        return "FOR PUBLIC";
    }
}
