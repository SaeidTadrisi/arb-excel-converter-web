package com.example.arbexcelconverterweb.domain.excel;

import com.example.arbexcelconverterweb.domain.excel.CustomSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.gson.FieldNamingPolicy.IDENTITY;

public class ExcelToARBPattern {

    Map<String, Map<String, String>> excelFile;

    public ExcelToARBPattern(Map<String, Map<String, String>> excelFile) {
        this.excelFile = excelFile;
    }

    public Map<String, String> multiFileStringCreator() {
        return excelFile.entrySet().stream()
                .collect(LinkedHashMap::new,
                        (stringMap, languagesMapEntry) -> {
                            Map<String, String> eachLanguagesMap = new LinkedHashMap<>(languagesMapEntry.getValue());
                            Map<String, String> otherElementsMap = otherElementsExtractor(eachLanguagesMap);
                            Map<String, String> placeHolderMap = placeHoldersExtractor(eachLanguagesMap);
                            Map<String, String> combinedMap = mapsCombiner(otherElementsMap, placeHolderMap);
                            Map<String, Object> arbPatternedMap = arbPatternCreator(combinedMap);
                            String jsonString = jasonPatternCreator(arbPatternedMap);
                            stringMap.put(languagesMapEntry.getKey(), jsonString);
                        },
                        LinkedHashMap::putAll);
    }

    private Map<String, String> otherElementsExtractor(Map<String, String> languageMap){
        Map<String, String> otherElementsMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : languageMap.entrySet()){
            if (!entry.getKey().startsWith("@")){
                otherElementsMap.put(entry.getKey(), entry.getValue());
            }
        }
        return otherElementsMap;
    }

    private Map<String, String> placeHoldersExtractor(Map<String, String> languageMap){
        Map<String, String> placeHoldersMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : languageMap.entrySet()){
            if (entry.getKey().startsWith("@")){
                placeHoldersMap.put(entry.getKey(), entry.getValue());
            }
        }
        return placeHoldersMap;
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

    private Map<String, Object> arbPatternCreator(Map<String, String> combinedMap) {
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

    private String jasonPatternCreator(Map<String, Object> arbPatternedMap) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(IDENTITY)
                .disableHtmlEscaping()
                .registerTypeAdapter(String.class, new CustomSerializer())
                .create();
        return gson.toJson(arbPatternedMap);
    }
}
