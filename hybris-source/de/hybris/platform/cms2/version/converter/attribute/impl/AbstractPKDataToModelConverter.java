package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionPayloadDescriptor;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractPKDataToModelConverter implements Converter<VersionPayloadDescriptor, ItemModel>
{
    private ModelService modelService;
    private TypeService typeService;


    public ItemModel convert(VersionPayloadDescriptor payloadDescriptor)
    {
        String pk = payloadDescriptor.getValue();
        try
        {
            ItemModel dataModel = (ItemModel)getModelService().get(PK.parse(pk));
            if(isAssignableFromVersion(dataModel.getItemtype()))
            {
                return getItemModelByVersion((CMSVersionModel)dataModel);
            }
            return dataModel;
        }
        catch(ModelLoadingException | de.hybris.platform.core.PK.PKException e)
        {
            throw new IllegalArgumentException("The revision PK (" + pk + ") is not found. Version is corrupted.", e);
        }
    }


    protected boolean isAssignableFromVersion(String itemTypeCode)
    {
        return getTypeService().isAssignableFrom("CMSVersion", itemTypeCode);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public abstract ItemModel getItemModelByVersion(CMSVersionModel paramCMSVersionModel);
}
