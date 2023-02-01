package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/*
 * ArrayList based storage for resumes
 */
public class ListStorage extends AbstractStorage {

    @Override
    protected void insertElement(Resume r, int index) {
        storage.add(index, r);
    }

    @Override
    protected int getIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected Resume getResume(int index) {
        return storage.get(index);
    }
}
