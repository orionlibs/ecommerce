package de.hybris.platform.core.initialization.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupAuditDAO;
import de.hybris.platform.core.initialization.SystemSetupCollector;
import de.hybris.platform.core.initialization.SystemSetupCollectorResult;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.util.Config;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

public class SystemSetupCollectorImpl implements SystemSetupCollector, ApplicationContextAware
{
    private static final String SYSTEM_SETUP_SORT_LEGACY_MODE = "system.setup.sort.legacy.mode";
    private static final Logger log = Logger.getLogger(SystemSetupCollector.class);
    private volatile Map<String, List<SystemSetupCollectorResult>> _dataPool = null;
    private Map<String, List<SystemSetupParameter>> _parameterPool = null;
    private ApplicationContext applicationContext;
    private SystemSetupAuditDAO systemSetupAuditDAO;


    private void loadSystemSetupBeans()
    {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(SystemSetup.class);
        Map<String, List<SystemSetupCollectorResult>> dataPool = new HashMap<>();
        Map<String, List<SystemSetupParameter>> parameterPool = new HashMap<>();
        for(Map.Entry<String, Object> bean : beans.entrySet())
        {
            String beanId = bean.getKey();
            Object importObject = bean.getValue();
            Class<?> importClass = importObject.getClass();
            Method[] methods = importClass.getMethods();
            SystemSetup classAnnotation = (SystemSetup)AnnotationUtils.findAnnotation(importClass, SystemSetup.class);
            Method[] arrayOfMethod1 = methods;
            int i = arrayOfMethod1.length;
            byte b = 0;
            while(true)
            {
                if(b < i)
                {
                    Method method = arrayOfMethod1[b];
                    SystemSetupParameterMethod systemSetupParameterMethodAnnotation = (SystemSetupParameterMethod)AnnotationUtils.findAnnotation(method, SystemSetupParameterMethod.class);
                    if(systemSetupParameterMethodAnnotation != null)
                    {
                        String extensionName;
                        if(!systemSetupParameterMethodAnnotation.extension().isEmpty())
                        {
                            extensionName = systemSetupParameterMethodAnnotation.extension();
                        }
                        else if(!classAnnotation.extension().isEmpty())
                        {
                            extensionName = classAnnotation.extension();
                        }
                        else
                        {
                            log.warn("No extension given for " + importClass.getSimpleName() + "::" + method.getName());
                            b++;
                        }
                        if(parameterPool.get(extensionName) == null)
                        {
                            parameterPool.put(extensionName, new ArrayList<>());
                        }
                        Object returnObject = ReflectionUtils.invokeMethod(method, importObject);
                        if(returnObject instanceof List)
                        {
                            ((List)parameterPool.get(extensionName)).addAll((List)returnObject);
                        }
                    }
                }
                else
                {
                    break;
                }
                b++;
            }
            arrayOfMethod1 = methods;
            i = arrayOfMethod1.length;
            b = 0;
            while(true)
            {
                if(b < i)
                {
                    Method method = arrayOfMethod1[b];
                    SystemSetup methodAnnotation = (SystemSetup)AnnotationUtils.findAnnotation(method, SystemSetup.class);
                    if(methodAnnotation != null)
                    {
                        String extensionName;
                        if(!methodAnnotation.extension().isEmpty())
                        {
                            extensionName = methodAnnotation.extension();
                        }
                        else if(!classAnnotation.extension().isEmpty())
                        {
                            extensionName = classAnnotation.extension();
                        }
                        else
                        {
                            log.warn("No extension given for " + importClass.getSimpleName() + "::" + method.getName());
                            b++;
                        }
                        String name = StringUtils.isNotEmpty(methodAnnotation.name()) ? methodAnnotation.name() : classAnnotation.name();
                        String description = StringUtils.isNotEmpty(methodAnnotation.description()) ? methodAnnotation.description() : classAnnotation.description();
                        boolean patch = false;
                        if(methodAnnotation.patch())
                        {
                            patch = true;
                        }
                        else if(!methodAnnotation.patch() && classAnnotation.patch())
                        {
                            patch = true;
                        }
                        if(dataPool.get(extensionName) == null)
                        {
                            dataPool.put(extensionName, new ArrayList<>());
                        }
                        ((List<SystemSetupCollectorResult>)dataPool.get(extensionName)).add(new SystemSetupCollectorResult(classAnnotation, methodAnnotation, importObject, extensionName, beanId, method, name, description, methodAnnotation
                                        .required(), patch));
                    }
                }
                else
                {
                    break;
                }
                b++;
            }
        }
        this._parameterPool = parameterPool;
        this._dataPool = dataPool;
    }


    private void assertBeansLoaded()
    {
        synchronized(this)
        {
            loadSystemSetupBeans();
        }
    }


    public boolean hasProjectData(String extensionName)
    {
        List<SystemSetupCollectorResult> systemSetupObjects = getDataPoolForExtension(extensionName);
        if(systemSetupObjects != null)
        {
            for(SystemSetupCollectorResult systemSetupObject : systemSetupObjects)
            {
                if(systemSetupObject.getType().isProject())
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean hasEssentialData(String extensionName)
    {
        List<SystemSetupCollectorResult> systemSetupObjects = getDataPoolForExtension(extensionName);
        if(systemSetupObjects != null)
        {
            for(SystemSetupCollectorResult systemSetupObject : systemSetupObjects)
            {
                if(systemSetupObject.getType().isEssential())
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean hasParameter(String extensionName)
    {
        assertBeansLoaded();
        return (this._parameterPool.get(extensionName) != null && !((List)this._parameterPool.get(extensionName)).isEmpty());
    }


    public List<SystemSetupParameter> getParameterMap(String extensionName)
    {
        return this._parameterPool.get(extensionName);
    }


    public Map<String, String[]> getDefaultParameterMap(String extensionName)
    {
        assertBeansLoaded();
        Map<String, String[]> params = (Map)new HashMap<>();
        if(this._parameterPool.get(extensionName) != null)
        {
            for(SystemSetupParameter parameter : this._parameterPool.get(extensionName))
            {
                params.put(parameter.getKey(), parameter.getDefaults());
            }
        }
        return params;
    }


    public void executeMethods(SystemSetupContext ctx)
    {
        List<SystemSetupCollectorResult> systemSetupObjects = getDataPoolForExtension(ctx.getExtensionName());
        if(systemSetupObjects == null)
        {
            return;
        }
        for(SystemSetupCollectorResult systemSetupObject : systemSetupObjects)
        {
            if(systemSetupObject.getType().isAll() || systemSetupObject.getType().equals(ctx.getType()))
            {
                if(systemSetupObject.getProcess().isAll() || systemSetupObject.getProcess().equals(ctx.getProcess()))
                {
                    if(systemSetupObject.isPatch())
                    {
                        if(shouldApplyPatch(ctx, systemSetupObject))
                        {
                            invokeSystemSetupMethod(ctx, systemSetupObject);
                            this.systemSetupAuditDAO.storeSystemPatchAction(systemSetupObject);
                        }
                        continue;
                    }
                    invokeSystemSetupMethod(ctx, systemSetupObject);
                }
            }
        }
    }


    private boolean shouldApplyPatch(SystemSetupContext ctx, SystemSetupCollectorResult systemSetupObject)
    {
        boolean applyPatch;
        if(ctx.isFilteredPatches())
        {
            List<String> patchHashesToApply = ctx.getPatchHashesToApply(ctx.getExtensionName());
            applyPatch = (patchHashesToApply.contains(systemSetupObject.getHash()) && isPatchNotApplied(systemSetupObject));
        }
        else
        {
            applyPatch = isPatchNotApplied(systemSetupObject);
        }
        return applyPatch;
    }


    public List<SystemSetupCollectorResult> getApplicablePatches(String extensionName)
    {
        Objects.requireNonNull(extensionName, "extensionName is required");
        List<SystemSetupCollectorResult> dataPool = getDataPoolForExtension(extensionName);
        return (List<SystemSetupCollectorResult>)dataPool.stream().filter(SystemSetupCollectorResult::isPatch).filter(this::isPatchNotApplied).collect(Collectors.toList());
    }


    private boolean isPatchNotApplied(SystemSetupCollectorResult collectorResult)
    {
        return !this.systemSetupAuditDAO.isPatchApplied(collectorResult.getHash());
    }


    private void invokeSystemSetupMethod(SystemSetupContext systemSetupContext, SystemSetupCollectorResult systemSetupObject)
    {
        Class[] parameterTypes = systemSetupObject.getMethod().getParameterTypes();
        if(parameterTypes.length == 0)
        {
            ReflectionUtils.invokeMethod(systemSetupObject.getMethod(), systemSetupObject.getObject());
        }
        else
        {
            ReflectionUtils.invokeMethod(systemSetupObject.getMethod(), systemSetupObject.getObject(),
                            getParameterTypes(systemSetupObject, systemSetupContext));
        }
    }


    private Object[] getParameterTypes(SystemSetupCollectorResult systemSetupObject, SystemSetupContext systemSetupContext)
    {
        List<Object> objects = new ArrayList();
        for(Class<?> clazz : systemSetupObject.getMethod().getParameterTypes())
        {
            objects.add(getParameterType(clazz, systemSetupContext));
        }
        return objects.toArray();
    }


    private Object getParameterType(Class clazz, SystemSetupContext systemSetupContext)
    {
        if(clazz.equals(SystemSetupContext.class))
        {
            if(systemSetupContext.getParameterMap() == null)
            {
                systemSetupContext.setParameterMap(getDefaultParameterMap(systemSetupContext.getExtensionName()));
            }
            return systemSetupContext;
        }
        log.warn("No suitable parameter found for class type " + clazz.getSimpleName());
        return null;
    }


    private List<SystemSetupCollectorResult> getDataPoolForExtension(String extensionName)
    {
        assertBeansLoaded();
        List<SystemSetupCollectorResult> objects = new ArrayList<>();
        if(this._dataPool.containsKey(extensionName))
        {
            objects.addAll(this._dataPool.get(extensionName));
        }
        if(this._dataPool.containsKey("ALL_EXTENSIONS"))
        {
            objects.addAll(this._dataPool.get("ALL_EXTENSIONS"));
        }
        if(isNotUsingLegacySort())
        {
            objects.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
        return objects;
    }


    private boolean isNotUsingLegacySort()
    {
        return !Config.getBoolean("system.setup.sort.legacy.mode", false);
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    @Required
    public void setSystemSetupAuditDAO(SystemSetupAuditDAO systemSetupAuditDAO)
    {
        this.systemSetupAuditDAO = systemSetupAuditDAO;
    }
}
