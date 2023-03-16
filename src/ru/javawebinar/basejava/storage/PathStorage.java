package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final ObjectSaveStrategy objectSaveStrategy;

    protected void doWrite(Resume r, OutputStream os) throws IOException {
        objectSaveStrategy.doWrite(r, os);
    }

    protected Resume doRead(InputStream is) throws IOException {
        return objectSaveStrategy.doRead(is);
    }

    protected PathStorage(String dir, ObjectSaveStrategy oss) {
        Objects.requireNonNull(dir, "directory must not be null");
        if (!Files.isDirectory(Paths.get(dir)) || !Files.isWritable(Paths.get(dir))) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
        directory = Paths.get(dir);
        this.objectSaveStrategy = oss;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        try (Stream<Path> list = Files.list(directory)) {
            return (int) list.count();
        } catch (IOException e) {
            throw new StorageException("Directory read error", null);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            path.toFile();
        } catch (Exception e) {
            throw new StorageException("Couldn't create Path " +
                    path.getFileName().toString(), path.toFile().toString(), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.getFileName().toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        try {
            Stream<Path> pathStream = Files.list(directory);
            pathStream.forEach(path -> resumes.add(doGet(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resumes;
    }
}
