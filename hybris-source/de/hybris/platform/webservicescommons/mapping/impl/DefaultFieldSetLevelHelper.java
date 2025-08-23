package de.hybris.platform.webservicescommons.mapping.impl;

import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.mapping.config.FieldSetLevelMapping;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultFieldSetLevelHelper implements FieldSetLevelHelper, ApplicationContextAware
{
    private Map<Class, Map<String, String>> levelMap;


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        buildLevelMap(applicationContext);
    }


    protected void buildLevelMap(ApplicationContext applicationContext)
    {
        this.levelMap = (Map)new HashMap<>();
        Map<String, FieldSetLevelMapping> mappings = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)applicationContext, FieldSetLevelMapping.class);
        for(FieldSetLevelMapping mapping : mappings.values())
        {
            addToLevelMap(mapping);
        }
    }


    protected void addToLevelMap(FieldSetLevelMapping mapping)
    {
        if(this.levelMap.containsKey(mapping.getDtoClass()))
        {
            Map<String, String> existingMap = this.levelMap.get(mapping.getDtoClass());
            for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)mapping.getLevelMapping().entrySet())
            {
                if(existingMap.containsKey(entry.getKey()))
                {
                    String levelDefinition = (String)existingMap.get(entry.getKey()) + "," + (String)existingMap.get(entry.getKey());
                    existingMap.put(entry.getKey(), levelDefinition);
                    continue;
                }
                existingMap.put(entry.getKey(), entry.getValue());
            }
        }
        else
        {
            this.levelMap.put(mapping.getDtoClass(), mapping.getLevelMapping());
        }
    }


    public boolean isLevelName(String levelName, Class objectClass)
    {
        if("BASIC".equals(levelName) || "DEFAULT".equals(levelName) || "FULL".equals(levelName))
        {
            return true;
        }
        Map<String, String> map = getLevelMapForClass(objectClass);
        return (map != null && map.containsKey(levelName));
    }


    public String createBasicLevelDefinition(Class objectClass)
    {
        return createLevelDefinition(objectClass, "BASIC");
    }


    public String createDefaultLevelDefinition(Class objectClass)
    {
        return createLevelDefinition(objectClass, "DEFAULT");
    }


    protected String createLevelDefinition(Class objectClass, String levelName)
    {
        Map<String, String> map = getOrCreateLevelMapForClass(objectClass);
        StringBuilder levelDefinition = new StringBuilder();
        addFieldsFromClass(objectClass, levelDefinition);
        if(levelDefinition.length() > 0)
        {
            levelDefinition.deleteCharAt(levelDefinition.length() - 1);
        }
        map.put(levelName, levelDefinition.toString());
        return map.get(levelName);
    }


    protected static void addFieldsFromClass(Class objectClass, StringBuilder levelDefinition)
    {
        Field[] fieldList = objectClass.getDeclaredFields();
        for(Field field : fieldList)
        {
            if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
            {
                levelDefinition.append(field.getName()).append(",");
            }
        }
    }


    protected Map<String, String> getOrCreateLevelMapForClass(Class clazz)
    {
        Map<String, String> map = getLevelMapForClass(clazz);
        if(map == null)
        {
            map = new HashMap<>();
            getLevelMap().put(clazz, map);
        }
        return map;
    }


    public String createFullLevelDefinition(Class<Object> objectClass)
    {
        Map<String, String> map = getOrCreateLevelMapForClass(objectClass);
        StringBuilder levelDefinition = new StringBuilder();
        Class<Object> processedObjectClass = objectClass;
        while(processedObjectClass != null && processedObjectClass != Object.class)
        {
            addFieldsFromClass(processedObjectClass, levelDefinition);
            processedObjectClass = (Class)processedObjectClass.getSuperclass();
        }
        if(levelDefinition.length() > 0)
        {
            levelDefinition.deleteCharAt(levelDefinition.length() - 1);
        }
        map.put("FULL", levelDefinition.toString());
        return map.get("FULL");
    }


    public String getLevelDefinitionForClass(Class objectClass, String levelName)
    {
        Map<String, String> map = getLevelMapForClass(objectClass);
        if(map != null)
        {
            return map.get(levelName);
        }
        return null;
    }


    public Map<Class, Map<String, String>> getLevelMap()
    {
        return this.levelMap;
    }


    public void setLevelMap(Map<Class<?>, Map<String, String>> levelMap)
    {
        this.levelMap = levelMap;
    }


    public Map<String, String> getLevelMapForClass(Class clazz)
    {
        return getLevelMap().get(clazz);
    }
}
