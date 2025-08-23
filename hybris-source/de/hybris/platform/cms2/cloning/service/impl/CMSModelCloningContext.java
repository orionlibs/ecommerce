package de.hybris.platform.cms2.cloning.service.impl;

import de.hybris.platform.cms2.cloning.service.preset.AttributePresetHandler;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import org.springframework.beans.factory.annotation.Required;

public class CMSModelCloningContext implements ModelCloningContext
{
    private List<BiPredicate<ItemModel, String>> treatAsPartOfPredicates;
    @Deprecated(since = "2105", forRemoval = true)
    private List<BiPredicate<ItemModel, String>> presetValuePredicates;
    private List<AttributePresetHandler<Object>> presetValueHandlers;


    public boolean treatAsPartOf(Object component, String qualifier)
    {
        ItemModel cmsComponent = (ItemModel)component;
        for(BiPredicate<ItemModel, String> biPredicate : getTreatAsPartOfPredicates())
        {
            boolean treatAsPartOf = biPredicate.test(cmsComponent, qualifier);
            if(treatAsPartOf)
            {
                return true;
            }
        }
        return false;
    }


    public boolean skipAttribute(Object component, String qualifier)
    {
        return false;
    }


    public boolean usePresetValue(Object component, String qualifier)
    {
        ItemModel cmsComponent = (ItemModel)component;
        for(AttributePresetHandler<Object> attributePresetHandler : getPresetValueHandlers())
        {
            if(attributePresetHandler.test(cmsComponent, qualifier))
            {
                return true;
            }
        }
        return false;
    }


    public Object getPresetValue(Object component, String qualifier)
    {
        ItemModel cmsComponent = (ItemModel)component;
        for(AttributePresetHandler<Object> attributePresetHandler : getPresetValueHandlers())
        {
            if(attributePresetHandler.test(cmsComponent, qualifier))
            {
                Object originalValue = cmsComponent.getItemModelContext().getOriginalValue(qualifier);
                if(Objects.nonNull(originalValue))
                {
                    return attributePresetHandler.get(originalValue);
                }
                return attributePresetHandler.get();
            }
        }
        return null;
    }


    public void addPresetValuePredicate(AttributePresetHandler<Object> presetValueHandler)
    {
        getPresetValueHandlers().add(presetValueHandler);
    }


    protected List<BiPredicate<ItemModel, String>> getTreatAsPartOfPredicates()
    {
        return this.treatAsPartOfPredicates;
    }


    @Required
    public void setTreatAsPartOfPredicates(List<BiPredicate<ItemModel, String>> treatAsPartOfPredicates)
    {
        this.treatAsPartOfPredicates = new ArrayList<>(treatAsPartOfPredicates);
    }


    @Deprecated(since = "2105", forRemoval = true)
    protected List<BiPredicate<ItemModel, String>> getPresetValuePredicates()
    {
        return this.presetValuePredicates;
    }


    @Deprecated(since = "2105", forRemoval = true)
    @Required
    public void setPresetValuePredicates(List<BiPredicate<ItemModel, String>> presetValuePredicates)
    {
        this.presetValuePredicates = new ArrayList<>(presetValuePredicates);
    }


    protected List<AttributePresetHandler<Object>> getPresetValueHandlers()
    {
        return this.presetValueHandlers;
    }


    @Required
    public void setPresetValueHandlers(List<AttributePresetHandler<Object>> presetValueHandlers)
    {
        this.presetValueHandlers = new ArrayList<>(presetValueHandlers);
    }
}
