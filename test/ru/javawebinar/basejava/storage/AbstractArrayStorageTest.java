package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String UUID_NOT_EXIST = "dummy";

    private final Storage storage;
    private final Resume RESUME_1 = new Resume(UUID_1);
    private final Resume RESUME_2 = new Resume(UUID_2);
    private final Resume RESUME_3 = new Resume(UUID_3);
    private final Resume RESUME_4 = new Resume(UUID_4);

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() throws Exception {
        Assertions.assertTrue(assertSize(3));
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assertions.assertTrue(assertSize(0));
        Resume[] empty = new Resume[0];
        Assertions.assertArrayEquals(empty, storage.getAll());
    }

    @Test
    public void update() throws Exception {
        Resume updateResume = new Resume("uuid1");
        storage.update(updateResume);
        Assertions.assertSame(storage.get("uuid1"), updateResume);
    }

    @Test
    public void updateNotExistResume() throws Exception {
        Resume dummy = new Resume("dummy");
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.update(dummy);
        });
    }

    @Test
    public void getAll() throws Exception {
        Resume[] allResume = {RESUME_1, RESUME_2, RESUME_3};
        Assertions.assertArrayEquals(allResume, storage.getAll());
        Assertions.assertTrue(assertSize(3));
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        Assertions.assertTrue(assertGet(RESUME_4));
        Assertions.assertTrue(assertSize(4));
    }

    @Test
    public void saveExistResume() throws Exception {
        Resume existResume = new Resume("uuid1");
        Assertions.assertThrows(ExistStorageException.class, () -> {
            storage.save(existResume);
        });
    }

    @Test
    public void saveStorageOverflow() throws Exception {
        try {
            storage.clear();
            for (int i = 1; i <= 10_000; i++) {
                String uuid = "uuid" + i;
                storage.save(new Resume(uuid));
            }
        } catch (StorageException e) {
            Assertions.fail("Overflow occurred earlier");
        }
        Assertions.assertThrows(StorageException.class, () -> {
            storage.save(new Resume("uuid10_001"));
        });
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        Assertions.assertTrue(assertSize(2));
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.get(UUID_1);
        });
    }

    @Test
    public void deleteNotExistResume() throws Exception {
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.delete(UUID_NOT_EXIST);
        });
    }

    @Test
    public void get() throws Exception {
        Assertions.assertTrue(assertGet(RESUME_1));
        Assertions.assertTrue(assertGet(RESUME_2));
        Assertions.assertTrue(assertGet(RESUME_3));
    }

    @Test
    public void getNotExistResume() throws Exception {
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.get(UUID_NOT_EXIST);
        });
    }

    private boolean assertGet(Resume resume) {
        String uuid = resume.getUuid();
        return resume.equals(storage.get(uuid));
    }

    private boolean assertSize(int expectedSize) {
        return expectedSize == storage.size();
    }
}