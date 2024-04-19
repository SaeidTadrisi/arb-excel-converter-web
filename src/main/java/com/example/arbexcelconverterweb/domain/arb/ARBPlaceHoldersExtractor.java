package com.example.arbexcelconverterweb.domain.arb;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
public class ARBPlaceHoldersExtractor {

    private final String stringFile;

    public ARBPlaceHoldersExtractor(String stringFile) {
        this.stringFile = stringFile;
    }

    Map<String, String> placeHoldersExtractor() {

        Map<String, String> orderedMap = new LinkedHashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(stringFile);
            placeHolderPattern(jsonObject, orderedMap, "");

        } catch (JSONException e) {
            log.error("Your ARB file is not standard.");
        }
        orderedMap.entrySet().removeIf(entry -> !entry.getKey().contains("@"));

        return orderedMap;
    }

    private void placeHolderPattern(JSONObject jsonObject, Map<String, String> orderedMap, String prefix) {
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject jasonObjectValue) {
                placeHolderPattern(jasonObjectValue, orderedMap, prefix + key + "$#");
            } else {
                orderedMap.put(prefix + key, value.toString());
            }
        }
    }
}
