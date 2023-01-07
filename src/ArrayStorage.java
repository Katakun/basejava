/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int resumeCount = 0;
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < resumeCount; i++) {
            storage[i] = null;
        }
        resumeCount = 0;
    }

    void save(Resume r) {
        storage[resumeCount] = r;
        resumeCount++;
    }

    Resume get(String uuid) {
        int i = getIndex(uuid);
        if (i >= 0) {
            return storage[i];
        } else {
            return null;
        }
    }

    void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            Resume[] tmpCopy = new Resume[resumeCount];
            // копируем левую часть в новый массив
            System.arraycopy(storage, 0, tmpCopy, 0, index);
            // копируем правую часть со смещением
            System.arraycopy(storage, index + 1, tmpCopy, index, resumeCount - index - 1);
            resumeCount--;
            System.arraycopy(tmpCopy, 0, storage, 0, resumeCount);
            storage[resumeCount] = null;
        }

    }

    int getIndex(String uuid) {
        for (int i = 0; i < resumeCount; i++) {
            if (uuid.equals(storage[i].uuid)) {
                return i;
            }
        }
        System.out.println("uuid not found");
        return -1;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] allResume = new Resume[resumeCount];
        System.arraycopy(storage, 0, allResume, 0, resumeCount);
        return allResume;
    }

    int size() {
        return resumeCount;
    }
}
