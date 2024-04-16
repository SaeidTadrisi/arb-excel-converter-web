package com.example.arbexcelconverterweb.domain.arb;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
public class PlaceHolders {

    public Map<String, Object> placeHoldersExtractor(String stringFile) {

        Map<String, Object> orderedMap = new LinkedHashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(stringFile);
            placeHolderPattern(jsonObject, orderedMap, "");

        } catch (JSONException e) {
            log.error("Your ARB file is not standard.");
        }
        orderedMap.entrySet().removeIf(entry -> !entry.getKey().contains("@"));

        return orderedMap;
    }

    private void placeHolderPattern(JSONObject jsonObject, Map<String, Object> orderedMap, String prefix) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject jasonObjectValue) {
                placeHolderPattern(jasonObjectValue, orderedMap, prefix + key + "$#");
            } else {
                orderedMap.put(prefix + key, value);
            }
        }
    }
}
