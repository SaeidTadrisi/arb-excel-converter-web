package com.example.arbexcelconverterweb.domain.excel;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CustomSerializer implements JsonSerializer<String> {

    @Override
    public JsonElement serialize(String message, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(message.replace("\\n", "\n"));
    }
}
