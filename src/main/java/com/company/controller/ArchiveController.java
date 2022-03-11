package com.company.controller;

import com.company.model.Archive;
import com.company.repository.ArchiveRepository;
import com.company.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ArchiveController {

    @Autowired
    private ServiceLayer serviceLayer;
//    private ArchiveRepository archiveRepository;

    @PostMapping("/archive") // create new archive
    @ResponseStatus(value = HttpStatus.OK)
    public Archive createArchive(@RequestBody Archive archive) {
//        archiveRepository.save(archive);
        serviceLayer.saveArchive(archive);
        return archive;
    }

    @GetMapping("/archive") // find all archives
    public List<Archive> getAllArchives() {
//        return archiveRepository.findAll();
        return serviceLayer.findAllArchives();
    }

    @GetMapping("/archive/{archiveId}") // find archive by id
    public Archive getArchive(@PathVariable int archiveId) {
//        Optional<Archive> archive = archiveRepository.findById(archiveId);
        Archive archive = serviceLayer.findArchive(archiveId).get();

//        if(archive.getArchiveName() == null) {
//            return null;
//        }
        return archive;
    }


    @PutMapping("/archive/{archiveId}") // update archive by id
    public Archive updateArchiveById(@RequestBody Archive archive, @PathVariable int archiveId) {
        return serviceLayer.updateArchive(archive);
    }

    @DeleteMapping("/archive/{archiveId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteArchive(@PathVariable int archiveId) {
        serviceLayer.deleteArchive(archiveId);
    }
}