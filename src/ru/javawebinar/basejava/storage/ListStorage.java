package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/*
 * ArrayList based storage for resumes
 */
public class ListStorage extends AbstractStorage {

    @Override
    protected boolean isExist(Object searchKey) {
        int index = (int) searchKey;
        return index >= 0;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return storage.indexOf(new Resume(uuid));
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


}
