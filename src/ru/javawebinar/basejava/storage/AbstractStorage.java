package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

public abstract class AbstractStorage implements Storage {

    protected ArrayList<Resume> storage = new ArrayList<>();


    public void clear() {
        storage.clear();
    }

    @Override
    public void delete(String uuid) {
        if (!storage.remove(new Resume(uuid))) {
            throw new NotExistStorageException(uuid);
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getResume(index);
    }

    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        } else {
            storage.add(r);
        }
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
           insertElement(r, index);
        }
    }

    public int size() {
        return storage.size();
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract int getIndex(String uuid);

    protected abstract Resume getResume(int index);
}


