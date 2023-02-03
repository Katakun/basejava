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
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        } else {
            doDelete(searchKey);
        }
    }

    public Resume get(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            return doGet(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    public void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(r);
    }

    @Override
    public void update(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(searchKey, r);
    }

    public int size() {
        return storage.size();
    }

    // Exceptions
    private Object getExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            return searchKey;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        } else {
            return searchKey;
        }
    }

    // abstracts
    protected abstract boolean isExist(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Resume resume);

    protected abstract void doUpdate(Object searhKey, Resume resume);

    protected abstract void doDelete(Object searchKey);
}
