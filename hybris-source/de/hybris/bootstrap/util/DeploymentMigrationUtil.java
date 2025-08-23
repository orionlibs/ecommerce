package de.hybris.bootstrap.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DeploymentMigrationUtil
{
    public static Map<String, List<String>> prepareMigratedCoreTypesInfoMap(String property)
    {
        Map<String, List<String>> map = new HashMap<>();
        String rawString = property.replace(" ", "").trim();
        if(rawString == null || rawString.isEmpty())
        {
            return map;
        }
        if(!rawString.contains(";"))
        {
            System.err
                            .println("value of the 'migrated_core_type.info' property is invalid. At least one ';' character should oocur within: " + rawString);
        }
        List<String> rawTypes = Arrays.asList(rawString.split(";"));
        for(String rawType : rawTypes)
        {
            if(!rawType.contains(","))
            {
                map.put(rawType, null);
                continue;
            }
            List<String> rawElements = new ArrayList<>(Arrays.asList(rawType.split(",")));
            String type = rawElements.get(0);
            rawElements.remove(0);
            map.put(type, rawElements);
        }
        return map;
    }
}
