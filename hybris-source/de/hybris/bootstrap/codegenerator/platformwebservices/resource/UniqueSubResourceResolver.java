package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Deprecated(since = "1818", forRemoval = true)
public class UniqueSubResourceResolver implements SubResourceResolver
{
    private Map<YComposedType, Set<YComposedType>> uniqueInfoMap;


    public void setUniqueInfoMap(Map<YComposedType, Set<YComposedType>> uniqueInfoMap)
    {
        this.uniqueInfoMap = uniqueInfoMap;
    }


    public Map<? extends YType, String> getAllSubResources(YComposedType startType, WebservicesConfig provider)
    {
        Map<YType, String> uniqueSubTypes = new HashMap<>();
        Set<YComposedType> values;
        if((values = this.uniqueInfoMap.get(startType)) != null)
        {
            for(YType yType : values)
            {
                uniqueSubTypes.put(yType, null);
            }
        }
        return uniqueSubTypes;
    }
}
