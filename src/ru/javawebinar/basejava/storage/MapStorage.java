package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage {
    protected final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsValue((Resume) searchKey);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid);
        }
        return new Resume(uuid, "");

    }

    @Override
    protected Resume doGet(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        Resume searchKeyResume = (Resume) searchKey;
        storage.put(searchKeyResume.getUuid(), resume);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        Resume searchKeyarchKeyResume = (Resume) searchKey;
        storage.put(searchKeyarchKeyResume.getUuid(), resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        Resume searchKeyResume = (Resume) searchKey;
        storage.remove(searchKeyResume.getUuid());
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public List<Resume> getAllResume() {
        return new ArrayList<>(storage.values());
    }
}
