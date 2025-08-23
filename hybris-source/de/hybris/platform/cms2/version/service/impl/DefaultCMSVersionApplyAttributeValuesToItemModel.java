package de.hybris.platform.cms2.version.service.impl;

import de.hybris.platform.cms2.version.service.CMSVersionApplyAttributeValuesToModel;
import de.hybris.platform.cms2.version.service.CMSVersionHelper;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionApplyAttributeValuesToItemModel implements CMSVersionApplyAttributeValuesToModel
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCMSVersionApplyAttributeValuesToItemModel.class);
    private Predicate<ItemModel> modelPredicate;
    private ModelService modelService;
    private CMSVersionHelper cmsVersionHelper;


    public void apply(ItemModel itemModel, Map<String, Object> values)
    {
        values.forEach((qualifier, value) -> applyValueToQualifier(itemModel, qualifier, value));
    }


    public Predicate<ItemModel> getConstrainedBy()
    {
        return this.modelPredicate;
    }


    protected void applyValueToQualifier(ItemModel itemModel, String qualifier, Object value)
    {
        try
        {
            getModelService().setAttributeValue(itemModel, qualifier, value);
        }
        catch(AttributeNotSupportedException e)
        {
            LOGGER.error("Attribute [" + qualifier + "] is not supported.");
        }
        catch(IllegalArgumentException e)
        {
            LOGGER.error("The operation setAttributeValue for [" + qualifier + "] is not supported.");
        }
    }


    @Required
    public void setModelPredicate(Predicate<ItemModel> modelPredicate)
    {
        this.modelPredicate = modelPredicate;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public CMSVersionHelper getCmsVersionHelper()
    {
        return this.cmsVersionHelper;
    }


    @Required
    public void setCmsVersionHelper(CMSVersionHelper cmsVersionHelper)
    {
        this.cmsVersionHelper = cmsVersionHelper;
    }
}
