package com.inventrevo.movieapi.controllers;

import com.inventrevo.movieapi.entities.Tag;
import com.inventrevo.movieapi.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/all")
    public List<Tag> getAllTags() {
        return tagRepository.findAll(Sort.by(Sort.Direction.ASC, "tagId")).stream().toList();
    }

    @GetMapping("/name/{name}")
    public Tag getTagByName(@PathVariable String name) {
        return tagRepository.findByName(name);
    }

    @GetMapping("/id/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagRepository.findById(id).get();
    }

    @PostMapping("/add")
    public Tag addTag(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagRepository.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public Tag updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

}
