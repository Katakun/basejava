package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        String key = (String) searchKey;
        storage.put(key, resume);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        String key = (String) searchKey;
        return storage.containsKey(key);
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        String key = (String) searchKey;
        storage.put(key, resume);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doDelete(Object searchKey) {
        String key = (String) searchKey;
        storage.remove(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> doGetAllSorted() {
        List<Resume> resumes = new ArrayList<>(storage.values());
        resumes.sort(Comparator.comparing(Resume::getFullName)
                .thenComparing(Resume::getUuid));
        return resumes;

    }

    @Override
    public int size() {
        return storage.size();
    }
}
