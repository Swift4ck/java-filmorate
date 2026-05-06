package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.inter.GenreStorage;
import ru.yandex.practicum.filmorate.storage.inter.MpaStorage;

import java.util.List;

@Slf4j
@RestController
public class MpaController {

    private final MpaStorage mpaStorage;
    private final MpaService mpaService;

    @Autowired
    public MpaController(@Qualifier("MpaDbStorage") MpaStorage mpaStorage, MpaService mpaService) {
        this.mpaStorage = mpaStorage;
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa(){
        return mpaService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getById(@PathVariable("id") long id){
        mpaService.checkMpaValid(id);
        return mpaService.getById(id);
    }

}
