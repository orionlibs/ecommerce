package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContextAreaActionColumnConfigurationRegistry
{
    protected Map<String, ActionColumnConfiguration> map;


    public Map<String, ActionColumnConfiguration> getMap()
    {
        return this.map;
    }


    public void setMap(Map<String, ActionColumnConfiguration> map)
    {
        this.map = map;
    }


    public ActionColumnConfiguration getActionColumnConfigurationForType(ObjectType type)
    {
        ActionColumnConfiguration ret = null;
        if(this.map != null && type != null)
        {
            if(this.map.containsKey(type.getCode()))
            {
                ret = this.map.get(type.getCode());
            }
            else
            {
                List<ObjectType> parentTypes = TypeTools.getAllSupertypes(type);
                Collections.reverse(parentTypes);
                for(ObjectType parentType : parentTypes)
                {
                    if(this.map.containsKey(parentType.getCode()))
                    {
                        ret = this.map.get(parentType.getCode());
                        break;
                    }
                }
            }
        }
        return ret;
    }
}
