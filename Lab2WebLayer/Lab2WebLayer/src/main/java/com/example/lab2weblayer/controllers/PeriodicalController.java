package com.example.lab2weblayer.controllers;

import com.example.lab2weblayer.model.Periodical;
import com.example.lab2weblayer.model.enums.Frequency;
import com.example.lab2weblayer.repos.PeriodicalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/periodicals")
public class PeriodicalController {

    @Autowired
    private PeriodicalRepo periodicalRepo;

    @PostMapping("/createPeriodical")
    public String createPeriodical(@RequestBody Periodical periodical) {
        periodicalRepo.save(periodical);
        return "Periodical created successfully.";
    }

    @GetMapping("/allPeriodicals")
    public Iterable<Periodical> getAllPeriodicals() {
        return periodicalRepo.findAll();
    }

    @GetMapping("/periodical/{id}")
    public EntityModel<Periodical> getPeriodicalById(@PathVariable Integer id) {
        Periodical periodical = periodicalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Periodical not found with ID " + id));

        return EntityModel.of(periodical,
                linkTo(methodOn(PeriodicalController.class).getPeriodicalById(id)).withSelfRel(),
                linkTo(methodOn(PeriodicalController.class).getAllPeriodicals()).withRel("allPeriodicals"));
    }

    @PutMapping("/updateLatestPeriodical")
    public String updateLatestPeriodical(@RequestBody Periodical updatedPeriodical) {
        Optional<Periodical> latestPeriodical = periodicalRepo.findTopByOrderByIdDesc();

        if (latestPeriodical.isPresent()) {
            Periodical existingPeriodical = latestPeriodical.get();

            existingPeriodical.setTitle(updatedPeriodical.getTitle());
            existingPeriodical.setPublisher(updatedPeriodical.getPublisher());
            existingPeriodical.setFrequency(updatedPeriodical.getFrequency());
            existingPeriodical.setPublicationDate(updatedPeriodical.getPublicationDate());

            periodicalRepo.save(existingPeriodical);
            return "Latest periodical updated successfully.";
        } else {
            return "No periodicals found to update.";
        }
    }

    @DeleteMapping("/deletePeriodical/{id}")
    public String deletePeriodical(@PathVariable Integer id) {
        Optional<Periodical> periodical = periodicalRepo.findById(id);
        if (periodical.isPresent()) {
            periodicalRepo.deleteById(id);
            return "Periodical deleted successfully.";
        } else {
            return "Periodical not found.";
        }
    }

    @DeleteMapping("/deleteLatestPeriodical")
    public String deleteLatestPeriodical() {
        Optional<Periodical> latestPeriodical = periodicalRepo.findTopByOrderByIdDesc();

        if (latestPeriodical.isPresent()) {
            periodicalRepo.delete(latestPeriodical.get());
            return "Latest periodical deleted successfully.";
        } else {
            return "No periodicals found.";
        }
    }

    @GetMapping("/findByFrequency/{frequency}")
    public List<Periodical> findPeriodicalsByFrequency(@PathVariable Frequency frequency) {
        return periodicalRepo.findByFrequency(frequency);
    }

    @GetMapping("/findByPublicationDate/{publicationDate}")
    public List<Periodical> findPeriodicalsByPublicationDate(@PathVariable LocalDate publicationDate) {
        return periodicalRepo.findByPublicationDate(publicationDate);
    }
}
