package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage {
    protected final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    protected boolean isExist(Object searchKey) {
        int searchKeyInt = (int) searchKey;
        return searchKeyInt >= 0;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuidToIndex(uuid);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKeyToUuid(searchKey));
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        String key = searchKeyToUuid(searchKey);
        storage.put(key, resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        String key = searchKeyToUuid(searchKey);
        storage.remove(key);
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
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>(storage.values());
        resumes.sort(Comparator.comparing(Resume::getFullName)
                .thenComparing(Resume::getUuid));
        return resumes;
    }

    private int uuidToIndex(String uuid) {
        Set<String> keys = storage.keySet();
        String[] keysArr = keys.toArray(new String[0]);
        for (int i = 0; i < keysArr.length; i++) {
            if (uuid.equals(keysArr[i])) {
                return i;
            }
        }
        return -1;
    }

    private String searchKeyToUuid(Object searchKey) {
        int searchKeyInt = (int) searchKey;
        Set<String> keys = storage.keySet();
        String[] keysArr = keys.toArray(new String[0]);
        return keysArr[searchKeyInt];
    }
}
