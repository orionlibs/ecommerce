package de.hybris.platform.ruleengineservices.impex.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ImportProcessor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.util.Config;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

public class ConfigPropertyImportProcessor implements ImportProcessor
{
    private static final Logger LOG = Logger.getLogger(ConfigPropertyImportProcessor.class);


    public void init(ImpExImportReader reader)
    {
        Method addDefinitionMethod = findAddDefinitionMethod();
        Config.getAllParameters();
        for(Map.Entry<String, String> paramEntry : (Iterable<Map.Entry<String, String>>)Config.getAllParameters().entrySet())
        {
            addDefinition((ImpExReader)reader, addDefinitionMethod, "config-" + (String)paramEntry.getKey(), paramEntry.getValue());
        }
    }


    public Item processItemData(ValueLine valueLine) throws ImpExException
    {
        throw new IllegalStateException("ConfigPropertyImportProcessor cannot be used to process any lines");
    }


    protected Method findAddDefinitionMethod()
    {
        Method addDefinitionMethod = BeanUtils.findMethod(ImpExReader.class, "addDefinition", new Class[] {String.class});
        addDefinitionMethod.setAccessible(true);
        return addDefinitionMethod;
    }


    protected void addDefinition(ImpExReader reader, Method addDefinitionMethod, String key, String value)
    {
        String definition = "$" + key + "=" + value;
        try
        {
            addDefinitionMethod.invoke(reader, new Object[] {definition});
        }
        catch(IllegalAccessException e)
        {
            LOG.error("Failed to call ImpExReader.addDefinition", e);
        }
        catch(InvocationTargetException e)
        {
            LOG.error("Failed to call ImpExReader.addDefinition", e);
        }
    }
}
