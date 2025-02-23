package com.example.lab2weblayer.controllers;

import com.example.lab2weblayer.model.Manga;
import com.example.lab2weblayer.model.enums.Demographic;
import com.example.lab2weblayer.repos.MangaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/mangas")
public class MangaController {

    @Autowired
    private MangaRepo mangaRepo;

    @PostMapping("/createManga")
    public String createManga(@RequestBody Manga manga) {
        mangaRepo.save(manga);
        return "Manga created successfully.";
    }

    @GetMapping("/allMangas")
    public Iterable<Manga> getAllMangas() {
        return mangaRepo.findAll();
    }

    @GetMapping("/manga/{id}")
    public EntityModel<Manga> getMangaById(@PathVariable Integer id) {
        Manga manga = mangaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Manga not found with ID " + id));

        return EntityModel.of(manga,
                linkTo(methodOn(MangaController.class).getMangaById(id)).withSelfRel(),
                linkTo(methodOn(MangaController.class).getAllMangas()).withRel("allMangas"));
    }

    @PutMapping("/updateLatestManga")
    public EntityModel<Manga> updateLatestManga(@RequestBody Manga updatedManga) {
        Manga latestManga = mangaRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No mangas found"));

        latestManga.setTitle(updatedManga.getTitle());
        latestManga.setAuthor(updatedManga.getAuthor());
        latestManga.setOwner(updatedManga.getOwner());
        latestManga.setClient(updatedManga.getClient());
        latestManga.setRecords(updatedManga.getRecords());
        latestManga.setPublicationStatus(updatedManga.getPublicationStatus());
        latestManga.setIllustrator(updatedManga.getIllustrator());
        latestManga.setOriginalLanguage(updatedManga.getOriginalLanguage());
        latestManga.setVolumeNumber(updatedManga.getVolumeNumber());
        latestManga.setDemographic(updatedManga.getDemographic());
        latestManga.setColor(updatedManga.isColor());

        mangaRepo.save(latestManga);

        return EntityModel.of(latestManga,
                linkTo(methodOn(MangaController.class).getMangaById(latestManga.getId())).withSelfRel(),
                linkTo(methodOn(MangaController.class).getAllMangas()).withRel("allMangas"));
    }

    @DeleteMapping("/deleteManga/{id}")
    public String deleteManga(@PathVariable Integer id) {
        Optional<Manga> manga = mangaRepo.findById(id);
        if (manga.isPresent()) {
            mangaRepo.deleteById(id);
            return "Manga deleted successfully.";
        } else {
            return "Manga not found.";
        }
    }

    @DeleteMapping("/deleteLatestManga")
    public String deleteLatestManga() {
        Optional<Manga> latestManga = mangaRepo.findTopByOrderByIdDesc();

        if (latestManga.isPresent()) {
            mangaRepo.delete(latestManga.get());
            return "Latest manga deleted successfully.";
        } else {
            return "No mangas found.";
        }
    }

    @GetMapping("/findByDemographic/{demographic}")
    public List<Manga> findMangasByDemographic(@PathVariable Demographic demographic) {
        return mangaRepo.findByDemographic(demographic);
    }
}
