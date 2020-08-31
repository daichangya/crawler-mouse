package com.daicy.crawler.common;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.common
 * @date:8/26/20
 */
public final class InterfaceAdapter<T>
        implements JsonSerializer<T>, JsonDeserializer<T> {

    private final Class<T> implementationClass;

    private InterfaceAdapter(final Class<T> implementationClass) {
        this.implementationClass = implementationClass;
    }

    public static <T> InterfaceAdapter<T> interfaceSerializer(final Class<T> implementationClass) {
        return new InterfaceAdapter<>(implementationClass);
    }

    @Override
    public JsonElement serialize(final T value, final Type type, final JsonSerializationContext context) {
        final Type targetType = value != null
                ? value.getClass() // `type` can be an interface so Gson would not even try to traverse the fields, just pick the implementation class
                : type;            // if not, then delegate further
        return context.serialize(value, targetType);
    }

    @Override
    public T deserialize(final JsonElement jsonElement, final Type typeOfT, final JsonDeserializationContext context) {
        return context.deserialize(jsonElement, implementationClass);
    }

}