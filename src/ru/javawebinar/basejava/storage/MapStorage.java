package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    protected final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    protected boolean isExist(Object searchKey) {
        String key = (String) searchKey;
        return storage.containsKey(key);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doSave(Resume resume) {
        String key = resume.getUuid();
        storage.put(key, resume);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        String key = (String) searchKey;
        storage.put(key, resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        String key = (String) searchKey;
        storage.remove(key);
    }

    @Override
    protected void doClear() {
        storage.clear();
    }

    @Override
    protected int getSize() {
        return storage.size();
    }

    @Override
    protected Resume[] doGetAll() {
        return storage.values().toArray(new Resume[0]);
    }
}
