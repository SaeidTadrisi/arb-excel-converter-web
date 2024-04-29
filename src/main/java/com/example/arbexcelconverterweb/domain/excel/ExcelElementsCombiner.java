package com.example.arbexcelconverterweb.domain.excel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.gson.FieldNamingPolicy.IDENTITY;

public class ExcelElementsCombiner {

    private final Map<String, String> otherElementsMap;
    private final Map<String, String> placeHoldersMap;

    public ExcelElementsCombiner(Map<String, String> otherElementsMap, Map<String, String> placeHoldersMap) {
        this.otherElementsMap = otherElementsMap;
        this.placeHoldersMap = placeHoldersMap;
    }

    String arbPatternedMap() {
        Map<String, Object> stringObjectMap = arbPatternCreator();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(IDENTITY)
                .disableHtmlEscaping()
                .registerTypeAdapter(String.class, new CustomSerializer())
                .create();
        return gson.toJson(stringObjectMap);
    }

    private Map<String, Object> arbPatternCreator() {
        Map<String, String> combinedMap = mapsCombiner(otherElementsMap, placeHoldersMap);
        Map<String, Object> finalMap = new LinkedHashMap<>();
        Map<String, Object> valuesMap = new LinkedHashMap<>();
        Map<String, Object> titleMap = new LinkedHashMap<>();
        Map<String, Object> placeHolderMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> stringEntry : combinedMap.entrySet()) {
            String key = stringEntry.getKey();
            String value = stringEntry.getValue();
            if (key.equals("Key")){
                finalMap.put("@@locale", value);
            }else if (key.startsWith("@")) {
                String[] keys = key.split("\\$#");
                String[] values = value.split("\\$#");
                int valueIndex = 0;
                String subKey = "";
                for (int keyIndex = 0; keyIndex < keys.length; keyIndex++) {
                    if (keys[keyIndex].equals("type")) {
                        valuesMap.put(keys[keyIndex], keys[keyIndex + 1]);
                        subKey = keys[keyIndex - 1];
                    }
                    if (keys[keyIndex].equals("example")) {
                        valuesMap.put(keys[keyIndex], values[valueIndex]);
                        titleMap.put(subKey, valuesMap);
                        valuesMap = new LinkedHashMap<>();
                        valueIndex++;
                    }
                    placeHolderMap.put(keys[1], titleMap);
                    finalMap.put(keys[0], placeHolderMap);
                }
                placeHolderMap = new LinkedHashMap<>();

            }else {
                titleMap = new LinkedHashMap<>();
                finalMap.put(key, value);
            }
        }
        return finalMap;
    }

    private Map<String, String> mapsCombiner(Map<String, String> otherElementsMap, Map<String, String> placeHolderMap){

        LinkedHashMap<String, String> mapIncludePlaceHolders = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : otherElementsMap.entrySet()) {
            for (Map.Entry<String, String> entry2 : placeHolderMap.entrySet()){
                mapIncludePlaceHolders.put(entry.getKey(), entry.getValue());
                String key = entry.getKey();
                key = "@" + key;
                if (entry2.getKey().startsWith(key)){
                    mapIncludePlaceHolders.put(entry2.getKey(), entry2.getValue());
                }
            }
        }
        return mapIncludePlaceHolders;
    }
}
