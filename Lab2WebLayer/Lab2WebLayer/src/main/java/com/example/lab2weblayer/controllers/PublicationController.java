package com.example.lab2weblayer.controllers;

import com.example.lab2weblayer.model.Publication;
import com.example.lab2weblayer.model.enums.PublicationStatus;
import com.example.lab2weblayer.repos.PublicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/publications")
public class PublicationController {

    @Autowired
    private PublicationRepo publicationRepo;

    @GetMapping("/allAvailablePublications")
    public Iterable<Publication> getAllAvailablePublications() {
        return publicationRepo.findAllByPublicationStatusEquals(PublicationStatus.AVAILABLE);
    }
}
