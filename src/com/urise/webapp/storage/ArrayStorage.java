package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertResume(Resume r) {
        storage[size] = r;
    }

    @Override
    protected void deleteResume(String uuid) {
        int index = getIndex(uuid);
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
    }
}