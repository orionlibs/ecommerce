package de.hybris.platform.hac.facade;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetupCollector;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockHandler;
import de.hybris.platform.core.system.InitializationLockInfo;
import de.hybris.platform.core.system.impl.DefaultInitLockDao;
import de.hybris.platform.hac.data.dto.BeautifulInitializationData;
import de.hybris.platform.hac.data.dto.PatchData;
import de.hybris.platform.hac.util.InitializationUtil;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacInitUpdateFacade
{
    private static final Logger LOG = Logger.getLogger(HacInitUpdateFacade.class);
    private volatile StringWriter logWriter = null;
    private SystemTrayNotifier notifier;


    public synchronized Map<String, Object> executeInitUpdate(BeautifulInitializationData data)
    {
        this.notifier.notify("Platform Init/Update", "Init/Update has started", SystemTrayNotifier.NotificationLevel.NOTICE);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Init/Update? " + data.getInitMethod());
            LOG.debug("Drop Tables? " + data.getDropTables());
        }
        this.logWriter = new StringWriter();
        JspContext mockJspContext = InitializationUtil.createMockJspContext(data, this.logWriter);
        Map<Object, Object> result = new HashMap<>();
        try
        {
            Registry.setCurrentTenant(Registry.getCurrentTenant());
            Initialization.doInitialize(mockJspContext);
            result.put("success", Boolean.TRUE);
            mockJspContext.getJspWriter().flush();
            result.put("log", this.logWriter.toString());
            result.put("initUpdateData", data);
            this.notifier.notify("Platform Init/Update", "Init/Update has finished successfully", SystemTrayNotifier.NotificationLevel.NOTICE);
        }
        catch(Exception e)
        {
            LOG.error("Failed to initialize", e);
            result.put("success", Boolean.FALSE);
            result.put("log", this.logWriter.toString());
            this.notifier.notify("Platform Init/Update", "Init/Update has failed (" + e.getMessage() + ")", SystemTrayNotifier.NotificationLevel.ERROR);
        }
        finally
        {
            this.logWriter = null;
        }
        return (Map)result;
    }


    public Map<String, Object> getProjectDataSettings()
    {
        Map<Object, Object> result = new HashMap<>();
        Boolean isInitializing = Boolean.valueOf(Initialization.isAnyTenantInitializingGlobally());
        result.put("isInitializing", isInitializing);
        if(isInitializing.booleanValue())
        {
            InitializationLockHandler handler = new InitializationLockHandler((InitializationLockDao)new DefaultInitLockDao());
            InitializationLockInfo lockInfo = handler.getLockInfo();
            result.put("initInfo", lockInfo.getProcessName() + " started at: " + lockInfo.getProcessName() + " on tenant &quot;" + lockInfo.getDate() + "&quot; with cluster id: " + lockInfo
                            .getTenantId());
        }
        List<Map<Object, Object>> projectDataList = new ArrayList();
        result.put("projectDatas", projectDataList);
        for(Extension ext : Initialization.getCreators())
        {
            Map<Object, Object> extMap = new HashMap<>();
            List parameterList = new ArrayList();
            extMap.put("name", ext.getCreatorName());
            extMap.put("description", ext.getCreatorDescription());
            extMap.put("parameter", parameterList);
            setProjectDataParamsOldWay(ext, parameterList);
            setProjectDataParamsNewWay(ext, parameterList);
            projectDataList.add(extMap);
        }
        return (Map)result;
    }


    public Map<String, List<PatchData>> getSystemPatches()
    {
        ImmutableMap.Builder<String, List<PatchData>> patchesBuilder = ImmutableMap.builder();
        Initialization.getAllPatchesForExtensions().forEach((extName, patches) -> patchesBuilder.put(extName, patches.stream().map(PatchData::from).collect(Collectors.toList())));
        return (Map<String, List<PatchData>>)patchesBuilder.build();
    }


    private void setProjectDataParamsNewWay(Extension ext, List<Map<Object, Object>> parameterList)
    {
        SystemSetupCollector systemSetupCollector = Initialization.getSystemSetupCollector();
        if(systemSetupCollector.hasParameter(ext.getCreatorName()))
        {
            for(SystemSetupParameter systemSetupParameter : Initialization.getSystemSetupCollector()
                            .getParameterMap(ext.getCreatorName()))
            {
                Map<Object, Object> parameterMap = new HashMap<>();
                parameterMap.put("legacy", Boolean.FALSE);
                parameterMap.put("label", systemSetupParameter.getLabel());
                parameterMap.put("name", systemSetupParameter.getKey());
                parameterMap.put("multiSelect", Boolean.valueOf(systemSetupParameter.isMultiSelect()));
                if(systemSetupParameter.getValues().size() > 0)
                {
                    parameterMap.put("values", systemSetupParameter.getValues());
                }
                parameterList.add(parameterMap);
            }
        }
    }


    private void setProjectDataParamsOldWay(Extension ext, List<Map<Object, Object>> parameterList)
    {
        for(String parameter : ext.getCreatorParameterNames())
        {
            Map<Object, Object> parameterMap = new HashMap<>();
            parameterMap.put("legacy", Boolean.TRUE);
            parameterMap.put("name", parameter);
            parameterMap.put("label", parameter);
            String defaultValue = ext.getCreatorParameterDefault(parameter);
            parameterMap.put("default", defaultValue);
            List<String> possibleValues = ext.getCreatorParameterPossibleValues(parameter);
            LinkedHashMap<String, Boolean> values = new LinkedHashMap<>();
            if(!possibleValues.isEmpty())
            {
                for(String possibleValue : possibleValues)
                {
                    values.put(possibleValue, Boolean.valueOf(possibleValue.equals(defaultValue)));
                }
                parameterMap.put("values", values);
            }
            parameterList.add(parameterMap);
        }
    }


    public boolean isLocked()
    {
        return SystemEJB.getInstance().isLocked();
    }


    public boolean isUnlockable()
    {
        return !Config.getBoolean("system.unlocking.disabled", false);
    }


    public boolean isInitialized()
    {
        return JaloConnection.getInstance().isSystemInitialized();
    }


    public StringWriter getLogWriter()
    {
        return this.logWriter;
    }


    @Required
    public void setNotifier(SystemTrayNotifier notifier)
    {
        this.notifier = notifier;
    }
}
