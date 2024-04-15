package com.example.arbexcelconverterweb.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ARBToExcelPattern {
    private final List<String> arbFiles;

    public ARBToExcelPattern(List<String> arbFiles) {
        this.arbFiles = arbFiles;
    }

    public List<Map<String, String>> multiLanguageExporter() {
        List<Map<String, String>> mapList = new LinkedList<>();

        for (String arbFile : arbFiles) {
            Map<String, String> otherElementsMap = otherElementsExtractor(arbFile);
            LinkedHashMap<String, Object> placeHoldersMap = placeHoldersExtractor(arbFile);
            Map<String, String> combinedMap = mapsCombiner(otherElementsMap, placeHoldersMap);
            Map<String, String> formattedMap = placeHolderTypeReplacer(combinedMap);
            mapList.add(formattedMap);
        }
        return mapList;
    }

    private Map<String, String> otherElementsExtractor(String stringFile) {
        Map<String, String> stringMap = new LinkedHashMap<>();

        String[] lines = stringFile.split("\n");

        for (String line : lines) {
            if (line.contains("\": \"")) {
                String[] parts = line.split("\": \"");
                if (parts[0].startsWith("  \"")) {
                    if (parts[0].contains("@@")) {
                        languageKeyFinder(stringMap, parts);
                    } else {
                        keyValuesElementsFinder(stringMap, parts);
                    }
                }
            }
        }
        return stringMap;
    }

    private void languageKeyFinder(Map<String, String> stringMap, String[] parts) {
        String key = "Key";
        String value = parts[1].trim().replace("\",", "").replace("\"", "");
        stringMap.put(key, value);
    }

    private void keyValuesElementsFinder(Map<String, String> stringMap, String[] parts) {
        String key = parts[0].trim().replace("\"", "");
        String value = parts[1].trim().replace("\",", "").replace("\"", "");
        stringMap.put(key, value);
    }

    private LinkedHashMap<String, Object> placeHoldersExtractor(String stringFile) {

        LinkedHashMap<String, Object> orderedMap = new LinkedHashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(stringFile);

            placeHolderPattern(jsonObject, orderedMap, "");

        } catch (JSONException e) {
            throw new IllegalArgumentException("Your ARB file is not standard.");
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

    private Map<String, String> mapsCombiner(Map<String, String> exceptPlaceHoldersMap
            , Map<String, Object> placeHoldersMap) {
        LinkedHashMap<String, String> mapIncludePlaceHolders = new LinkedHashMap<>();

        exceptPlaceHoldersMap.entrySet().stream()
                .map(simpleEntries -> mapPutterAndKeyFinder(mapIncludePlaceHolders, simpleEntries))
                .forEach(key -> placeHoldersMap.entrySet().stream()
                        .filter(placeHolderEntries -> placeHolderEntries.getKey().startsWith(key))
                        .forEach(matchingEntry -> mapIncludePlaceHolders
                                .put(matchingEntry.getKey(), matchingEntry.getValue().toString())));
        return mapIncludePlaceHolders;
    }

    private static String mapPutterAndKeyFinder(LinkedHashMap<String, String> mapIncludePlaceHolders,
                                                Map.Entry<String, String> exceptPlaceHoldersMap) {
        mapIncludePlaceHolders.put(exceptPlaceHoldersMap.getKey(), exceptPlaceHoldersMap.getValue());
        String starterChar = "@";
        return starterChar + exceptPlaceHoldersMap.getKey();
    }

    private static Map<String, String> placeHolderTypeReplacer(Map<String, String> combinedMap) {
        Map<String, String> finalMap = new LinkedHashMap<>();

        Iterator<Map.Entry<String, String>> entryIterator = combinedMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> entry = entryIterator.next();
            String type = "$#type";
            if (entry.getKey().contains(type)) {
                Map.Entry<String, String> nextEntry = entryIterator.next();
                finalMap.put(entry.getKey() + "$#" + entry.getValue() + "$#example", nextEntry.getValue());
            } else {
                finalMap.put(entry.getKey(), entry.getValue());
            }
        }
        return finalMap;
    }

}
