package ru.javawebinar.basejava.storage;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

//    @Test
//    public void saveOverflow() throws Exception {
//        try {
//            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
//                storage.save(new Resume());
//            }
//        } catch (StorageException e) {
//            Assert.fail();
//        }
//        storage.save(new Resume());
//    }

}