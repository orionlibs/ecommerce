package de.hybris.platform.hac.data.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeautifulInitializationData
{
    private Boolean dropTables = Boolean.FALSE;
    private Boolean clearHMC = Boolean.FALSE;
    private Boolean createEssentialData = Boolean.FALSE;
    private Boolean createProjectData = Boolean.FALSE;
    private Boolean localizeTypes = Boolean.FALSE;
    private InitMethod initMethod = InitMethod.INIT;
    private Map<String, Object> allParameters = new HashMap<>();
    private Map<String, List<String>> patches = new HashMap<>();


    public Map<String, String[]> getParametersAsStringMap()
    {
        Map<String, String[]> map = (Map)new HashMap<>();
        for(Map.Entry<String, Object> entry : getAllParameters().entrySet())
        {
            if(entry.getValue() instanceof List)
            {
                List values = (List)entry.getValue();
                String[] stringValues = new String[values.size()];
                int pos = 0;
                for(Object value : values)
                {
                    stringValues[pos] = (String)value;
                    pos++;
                }
                map.put(entry.getKey(), stringValues);
                continue;
            }
            if(entry.getValue() instanceof String)
            {
                map.put(entry.getKey(), new String[] {(String)entry
                                .getValue()});
            }
        }
        map.put("initmethod", new String[] {(getInitMethod() == InitMethod.INIT) ? "init" : "update"});
        return map;
    }


    public Boolean getDropTables()
    {
        return this.dropTables;
    }


    public void setDropTables(Boolean dropTables)
    {
        this.dropTables = dropTables;
    }


    public Boolean getClearHMC()
    {
        return this.clearHMC;
    }


    public void setClearHMC(Boolean clearHMC)
    {
        this.clearHMC = clearHMC;
    }


    public Boolean getCreateEssentialData()
    {
        return this.createEssentialData;
    }


    public void setCreateEssentialData(Boolean createEssentialData)
    {
        this.createEssentialData = createEssentialData;
    }


    public Boolean getCreateProjectData()
    {
        return this.createProjectData;
    }


    public void setCreateProjectData(Boolean createProjectData)
    {
        this.createProjectData = createProjectData;
    }


    public Boolean getLocalizeTypes()
    {
        return this.localizeTypes;
    }


    public void setLocalizeTypes(Boolean localizeTypes)
    {
        this.localizeTypes = localizeTypes;
    }


    public InitMethod getInitMethod()
    {
        return this.initMethod;
    }


    public void setInitMethod(InitMethod initMethod)
    {
        this.initMethod = initMethod;
    }


    public Map<String, Object> getAllParameters()
    {
        return this.allParameters;
    }


    public void setAllParameters(Map<String, Object> allParameters)
    {
        this.allParameters = allParameters;
    }


    public void setPatches(Map<String, List<String>> patches)
    {
        this.patches = patches;
    }


    public void traverse(InitUpdateConfigWalker walker)
    {
        walker.entryDetected("init", "Go");
        if(InitMethod.INIT.equals(getInitMethod()))
        {
            walker.entryDetected("initmethod", "init");
        }
        if(InitMethod.UPDATE.equals(getInitMethod()))
        {
            walker.entryDetected("initmethod", "update");
        }
        if(getClearHMC().booleanValue())
        {
            walker.entryDetected("clearhmc", "true");
        }
        if(getCreateEssentialData().booleanValue())
        {
            walker.entryDetected("essential", "true");
        }
        if(getLocalizeTypes().booleanValue())
        {
            walker.entryDetected("localizetypes", "true");
        }
        if(getDropTables().booleanValue())
        {
            walker.entryDetected("droptables", "droptables");
        }
        if(getCreateProjectData().booleanValue())
        {
            walker.entryDetected("createProjectData", "true");
        }
        processProjectData(walker);
        processPatchData(walker);
    }


    private void processProjectData(InitUpdateConfigWalker walker)
    {
        for(Map.Entry<String, Object> entry : getAllParameters().entrySet())
        {
            if(entry.getValue() instanceof String)
            {
                walker.entryDetected(entry.getKey(), (String)entry.getValue());
                continue;
            }
            if(entry.getValue() instanceof List)
            {
                List<String> values = (List<String>)entry.getValue();
                walker.entryDetected(entry.getKey(), values);
            }
        }
    }


    private void processPatchData(InitUpdateConfigWalker walker)
    {
        this.patches.entrySet().forEach(e -> walker.entryDetected("patch_" + (String)e.getKey(), (List)e.getValue()));
        walker.entryDetected("filteredPatches", "true");
    }
}
