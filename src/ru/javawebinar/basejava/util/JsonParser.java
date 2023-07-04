package ru.javawebinar.basejava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.javawebinar.basejava.model.Section;

import java.io.Reader;
import java.io.Writer;

public class JsonParser {
    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Section.class, new JsonSectionAdapter())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }


    public static <T> T read(String content, Class<T> claszz) {
        return GSON.fromJson(content, claszz);
    }

    public static <T> String write(T objct) {
        return GSON.toJson(objct);
    }

    public static <T> String write(T objct, Class<T> clazz) {
        return GSON.toJson(objct, clazz);
    }
}
