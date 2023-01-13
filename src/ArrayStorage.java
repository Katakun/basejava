/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int resumeCount;
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
        return i >= 0 ? storage[i] : null;
    }

    void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            System.arraycopy(storage, index + 1, storage, index, resumeCount - index - 1);
            resumeCount--;
            storage[resumeCount] = null;
        }
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

    private int getIndex(String uuid) {
        for (int i = 0; i < resumeCount; i++) {
            if (uuid.equals(storage[i].uuid)) {
                return i;
            }
        }
        System.out.println("uuid not found");
        return -1;
    }
}
