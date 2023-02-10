package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

/*
 * ArrayList based storage for resumes
 */
public class ListStorage extends AbstractStorage {

    protected final List<Resume> storage = new ArrayList<>();

    @Override
    protected boolean isExist(Object searchKey) {
        int index = (int) searchKey;
        return index >= 0;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (uuid.equals(storage.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        int index = (int) searchKey;
        return storage.get(index);
    }

    @Override
    protected void doSave(Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        int index = (int) searchKey;
        storage.add(index, resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (int) searchKey;
        storage.remove(index);
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
        return storage.toArray(new Resume[0]);
    }
}
