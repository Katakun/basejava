package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        String uuid = r.getUuid();
        if (isResumeInStorage(uuid)) {
            storage[getIndex(uuid)] = r;
        } else {
            System.out.printf("ERROR: Resume with uuid '%s' not exist%n", uuid);
        }
    }

    public void save(Resume r) {
        if (!isResumeInStorage(r.getUuid()) && size < storage.length) {
            storage[size] = r;
            size++;
        } else {
            if (isResumeInStorage(r.getUuid())) {
                System.out.printf("ERROR: Resume with uuid '%s' already exist%n", r.getUuid());
            }
            if (size >= storage.length) {
                System.out.println("ERROR: Resume database is full%n");
            }
        }
    }

    public Resume get(String uuid) {
        if (isResumeInStorage(uuid)) {
            return storage[getIndex(uuid)];
        } else {
            System.out.printf("ERROR: Resume with uuid '%s' not exist%n", uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        if (isResumeInStorage(uuid)) {
            int index = getIndex(uuid);
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            storage[size] = null;
            size--;
        } else {
            System.out.printf("ERROR: Resume with uuid '%s' not exist%n", uuid);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    private boolean isResumeInStorage(String uuid) {
        return getIndex(uuid) >= 0;
    }
}
