package de.hybris.platform.core.initialization;

import com.google.common.base.Splitter;
import de.hybris.platform.util.JspContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

public class SystemSetupContext
{
    public static final String FILTERED_PATCHES_FLAG = "filteredPatches";
    private static final Logger log = Logger.getLogger(SystemSetupContext.class);
    private final String extensionName;
    private Map<String, List<String>> patchHashesToApply;
    private Map<String, String[]> parameterMap;
    private final SystemSetup.Type type;
    private final SystemSetup.Process process;
    private JspContext jspContext = null;
    private static final String INITMETHOD = "initmethod";


    @Deprecated(since = "ages", forRemoval = true)
    public SystemSetupContext(Map<String, String[]> parameterMap, SystemSetup.Type type, String extensionName)
    {
        this.parameterMap = parameterMap;
        this.type = type;
        this.extensionName = extensionName;
        if(parameterMap != null && parameterMap.get("initmethod") != null)
        {
            String initmethod = ((String[])parameterMap.get("initmethod"))[0].toString();
            if("update".equals(initmethod))
            {
                this.process = SystemSetup.Process.UPDATE;
            }
            else if("init".equals(initmethod))
            {
                this.process = SystemSetup.Process.INIT;
            }
            else
            {
                log.debug("Please use init|update for parameter initmethod! Parameter set to Process.ALL!");
                this.process = SystemSetup.Process.ALL;
            }
        }
        else
        {
            log.debug("Parameter initmethod wasn't set, default is Process.ALL!");
            this.process = SystemSetup.Process.ALL;
        }
        this.patchHashesToApply = buildPatchHashesToApply(parameterMap);
    }


    private Map<String, List<String>> buildPatchHashesToApply(Map<String, String[]> parameterMap)
    {
        if(parameterMap == null)
        {
            return Collections.emptyMap();
        }
        Map<String, List<String>> result = new HashMap<>();
        List<Map.Entry<String, String[]>> requestedPatches = (List<Map.Entry<String, String[]>>)parameterMap.entrySet().stream().filter(e -> ((String)e.getKey()).startsWith("patch_")).collect(Collectors.toList());
        requestedPatches.forEach(e -> {
            List<String> splitResult = SPLITTER.splitToList((CharSequence)e.getKey());
            String extName = splitResult.get(1);
            result.put(extName, Arrays.asList((String[])e.getValue()));
        });
        return result;
    }


    public SystemSetupContext(Map<String, String[]> parameterMap, SystemSetup.Type type, SystemSetup.Process process, String extensionName)
    {
        this.parameterMap = parameterMap;
        this.type = type;
        this.extensionName = extensionName;
        this.process = process;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public Map<String, String[]> getParameterMap()
    {
        return this.parameterMap;
    }


    public void setParameterMap(Map<String, String[]> parameterMap)
    {
        this.parameterMap = parameterMap;
    }


    public String getParameter(String key)
    {
        if(this.parameterMap.get(key) != null && ((String[])this.parameterMap.get(key)).length > 0)
        {
            return ((String[])this.parameterMap.get(key))[0].toString();
        }
        return null;
    }


    public String[] getParameters(String key)
    {
        if(this.parameterMap.get(key) != null && ((String[])this.parameterMap.get(key)).length > 0)
        {
            return this.parameterMap.get(key);
        }
        return null;
    }


    public boolean isFilteredPatches()
    {
        return this.parameterMap.containsKey("filteredPatches");
    }


    private static final Splitter SPLITTER = Splitter.on('_');


    public List<String> getPatchHashesToApply(String extensionName)
    {
        List<String> hashes = this.patchHashesToApply.get(extensionName);
        return (hashes == null) ? Collections.<String>emptyList() : hashes;
    }


    public SystemSetup.Type getType()
    {
        return this.type;
    }


    public SystemSetup.Process getProcess()
    {
        return this.process;
    }


    public void setJspContext(JspContext jspc)
    {
        this.jspContext = jspc;
    }


    public JspContext getJspContext()
    {
        return this.jspContext;
    }
}
