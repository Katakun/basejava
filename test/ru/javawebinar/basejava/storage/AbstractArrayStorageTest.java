package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    private Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";


    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() throws Exception {
        Assertions.assertEquals(3, storage.size());
    }


    @Test
    public void clear() throws Exception {
        storage.clear();
        Assertions.assertEquals(0, storage.size());
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.get("uuid1");
        });
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
        Resume[] allResume = {new Resume("uuid1"), new Resume("uuid2"), new Resume("uuid3")};
        Assertions.assertArrayEquals(allResume, storage.getAll());
    }

    @Test
    public void save() throws Exception {
        Resume resume = new Resume("save_test");
        storage.save(resume);
        Assertions.assertEquals(storage.get("save_test"), resume);
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
            for (int i = 4; i <= 10_000; i++) {
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
        storage.delete("uuid1");
        Assertions.assertEquals(2, storage.size());
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.get("uuid1");
        });
    }

    @Test
    public void deleteNotExistResume() throws Exception {
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.delete("dummy");
        });
    }


    @Test
    public void get() throws Exception {
        Resume test_resume = new Resume("test");
        storage.save(test_resume);
        Assertions.assertSame(storage.get("test"), test_resume);
    }

    @Test
    public void getNotExistResume() throws Exception {
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.get("dummy");
        });
    }
}