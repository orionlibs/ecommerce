package de.hybris.platform.scripting.engine.internal.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.scripting.engine.internal.ScriptEngineType;
import de.hybris.platform.scripting.engine.internal.ScriptEnginesRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultScriptEnginesRegistry implements ScriptEnginesRegistry, InitializingBean
{
    @Autowired
    private Set<ScriptEngineType> engineTypes;
    private Map<String, ScriptEngineType> type2engineMap;


    public ScriptEngineType getScriptEngineType(String scriptType)
    {
        if(scriptType == null)
        {
            throw new IllegalArgumentException("scriptType is required for checking registered engine types");
        }
        ScriptEngineType type = this.type2engineMap.get(scriptType);
        if(type == null)
        {
            throw new IllegalArgumentException("No script engine registered: " + scriptType);
        }
        return type;
    }


    public Set<ScriptEngineType> getRegisteredEngineTypes()
    {
        return (Set<ScriptEngineType>)ImmutableSet.copyOf(this.engineTypes);
    }


    public void afterPropertiesSet() throws Exception
    {
        this.type2engineMap = new HashMap<>(this.engineTypes.size());
        for(ScriptEngineType type : this.engineTypes)
        {
            this.type2engineMap.put(type.getName(), type);
            this.type2engineMap.put(type.getFileExtension(), type);
            this.type2engineMap.put(type.getMime(), type);
        }
    }
}
