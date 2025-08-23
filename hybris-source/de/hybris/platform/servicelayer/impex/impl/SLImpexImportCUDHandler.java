package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.DefaultImpExImportCUDHandler;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class SLImpexImportCUDHandler extends DefaultImpExImportCUDHandler
{
    private static final Logger LOG = Logger.getLogger(SLImpexImportCUDHandler.class);
    private final ModelService modelService;
    private final UserService userService;


    public SLImpexImportCUDHandler(ImpExImportReader reader)
    {
        super(reader);
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        this.modelService = (ModelService)ctx.getBean("modelService", ModelService.class);
        this.userService = (UserService)ctx.getBean("userService", UserService.class);
    }


    protected void doRemove(Item toRemove) throws ConsistencyCheckException
    {
        try
        {
            this.modelService.remove(asModel(toRemove));
        }
        catch(ModelRemovalException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Cannot remove model with PK: " + toRemove.getPK() + " (reason: " + e.getMessage() + ")", (Throwable)e);
            }
            throw new ConsistencyCheckException(e, 0);
        }
    }


    protected void doUpdate(Item toUpdate, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        ComposedType jaloType = toUpdate.getComposedType();
        try
        {
            ItemModel model = asModel(toUpdate);
            Map<String, Object> unsupportedValues = setModelValues(jaloType, model, values);
            this.modelService.save(model);
            setNonSLValuesIfNecessary(toUpdate, unsupportedValues, valueLine);
        }
        catch(Exception e)
        {
            throw new ImpExException(e, "line " + valueLine
                            .getLineNumber() + ": cannot update " + toUpdate.getPK() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
    }


    protected Item doCreate(ComposedType targetType, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        try
        {
            String targetTypeCode = targetType.getCode();
            ItemModel model = (ItemModel)this.modelService.create(targetTypeCode);
            Map<String, Object> unsupportedValues = setModelValues(targetType, model, values);
            this.modelService.save(model);
            Item newItem = (Item)this.modelService.getSource(model);
            setNonSLValuesIfNecessary(newItem, unsupportedValues, valueLine);
            return newItem;
        }
        catch(Exception e)
        {
            throw new ImpExException(e, "line " + valueLine.getLineNumber() + ": cannot create " + targetType.getCode() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
    }


    private Map<String, Object> setModelValues(ComposedType jaloType, ItemModel model, Map<String, Object> values)
    {
        Map<String, Object> unsupportedValues = null;
        Map<String, Object> orderedValues = putValuesInProperOrderForUserModel(model, values);
        for(Map.Entry<String, Object> entry : orderedValues.entrySet())
        {
            String attribute = entry.getKey();
            Object value = entry.getValue();
            try
            {
                if(jaloType.getAttributeDescriptorIncludingPrivate(attribute).isLocalized())
                {
                    setModelLocalizedValue(model, attribute, (Map<Language, Object>)value);
                    continue;
                }
                setModelValue(model, attribute, value);
            }
            catch(AttributeNotSupportedException e)
            {
                if(unsupportedValues == null)
                {
                    unsupportedValues = new HashMap<>();
                }
                unsupportedValues.put(attribute, value);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Can't set " + jaloType.getCode() + "." + attribute + "=" + value + " for " + model + " , " + e
                                    .getMessage() + ". Will try Jalo layer instead.");
                }
            }
        }
        return unsupportedValues;
    }


    private Map<String, Object> putValuesInProperOrderForUserModel(ItemModel uModel, Map<String, Object> map)
    {
        if(uModel instanceof UserModel)
        {
            Map<String, Object> sortedMap = new LinkedHashMap<>();
            if(map.containsKey("uid"))
            {
                sortedMap.put("uid", map.get("uid"));
            }
            sortedMap.putAll(map);
            return sortedMap;
        }
        return map;
    }


    private void setNonSLValuesIfNecessary(Item toUpdate, Map<String, Object> nonServiceLayerValues, ValueLine valueLine) throws JaloSecurityException, JaloBusinessException
    {
        if(MapUtils.isNotEmpty(nonServiceLayerValues))
        {
            super.doUpdate(toUpdate, nonServiceLayerValues, valueLine);
        }
    }


    private void setModelValue(ItemModel model, String attribute, Object value) throws AttributeNotSupportedException
    {
        if(model instanceof de.hybris.platform.core.model.link.LinkModel && value instanceof PK)
        {
            this.modelService.setAttributeValue(model, attribute, this.modelService.get((PK)value));
        }
        else if(model instanceof UserModel && "password".equalsIgnoreCase(attribute))
        {
            this.userService.setPassword((UserModel)model, (String)value, "*");
        }
        else
        {
            this.modelService.setAttributeValue(model, attribute, this.modelService.toModelLayer(value));
        }
    }


    private void setModelLocalizedValue(ItemModel model, String attribute, Map<Language, Object> value) throws AttributeNotSupportedException
    {
        Map<LanguageModel, Object> combinedSLValues = (Map<LanguageModel, Object>)this.modelService.toModelLayer(value);
        this.modelService.setAttributeValue(model, attribute, combinedSLValues);
    }


    private ItemModel asModel(Item item)
    {
        if(item instanceof de.hybris.platform.jalo.enumeration.EnumerationValue)
        {
            return (ItemModel)this.modelService.get(item, "EnumerationValue");
        }
        return (ItemModel)this.modelService.get(item);
    }
}
