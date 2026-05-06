package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;

import java.util.List;

@Service
@Slf4j
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    public MpaService(@Qualifier("MpaDbStorage") MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }


    public Mpa getById(long id) {
        return mpaDbStorage.getById(id);
    }

    public boolean checkMpaValid(long id) {
        Mpa mpa = mpaDbStorage.getById(id);
        if (mpa == null) {
            throw new NotFoundException("Id mpa не существует");
        }
        return true;
    }

}
