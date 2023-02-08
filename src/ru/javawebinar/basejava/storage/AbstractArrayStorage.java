package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int getSize() {
        return size;
    }

    public void doClear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] doGetAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (int) searchKey;
        fillDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        int index = (int) searchKey;
        return index >= 0;
    }

    protected abstract void fillDeletedElement(int index);

    @Override
    protected Resume doGet(Object searchKey) {
        int index = (int) searchKey;
        return storage[index];
    }
}
