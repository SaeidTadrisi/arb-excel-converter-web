package com.example.arbexcelconverterweb.domain.arb;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ARBElementsCombiner {

    private final Map<String, String> simpleElementsMap;
    private final Map<String, String> placeHoldersMap;

    public ARBElementsCombiner(Map<String, String> simpleElementsMap, Map<String, String> placeHoldersMap) {
        this.simpleElementsMap = simpleElementsMap;
        this.placeHoldersMap = placeHoldersMap;
    }

    public Map<String, String> placeHolderTypeReplacer() {

        Map<String, String> combinedMap = mapsCombiner(simpleElementsMap, placeHoldersMap);

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

    private Map<String, String> mapsCombiner(Map<String, String> simpleElementsMap
            , Map<String, String> placeHoldersMap) {
        LinkedHashMap<String, String> mapIncludePlaceHolders = new LinkedHashMap<>();

        simpleElementsMap.entrySet().stream()
                .map(simpleEntries -> mapPutterAndKeyFinder(mapIncludePlaceHolders, simpleEntries))
                .forEach(key -> placeHoldersMap.entrySet().stream()
                        .filter(placeHolderEntries -> placeHolderEntries.getKey().startsWith(key))
                        .forEach(matchingEntry -> mapIncludePlaceHolders
                                .put(matchingEntry.getKey(), matchingEntry.getValue())));
        return mapIncludePlaceHolders;
    }

    private static String mapPutterAndKeyFinder(LinkedHashMap<String, String> mapIncludePlaceHolders,
                                                Map.Entry<String, String> simpleElementsMap) {
        mapIncludePlaceHolders.put(simpleElementsMap.getKey(), simpleElementsMap.getValue());
        String starterChar = "@";
        return starterChar + simpleElementsMap.getKey();
    }
}
