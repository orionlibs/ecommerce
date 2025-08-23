package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class MediaContainerToDataConverter implements Converter<MediaContainerModel, PK>
{
    private ModelService modelService;
    private Converter<ItemModel, String> cmsVersionToDataConverter;
    private CMSVersionService cmsVersionService;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;


    public PK convert(MediaContainerModel source)
    {
        CMSVersionModel cmsVersionModel = (CMSVersionModel)getModelService().create("CMSVersion");
        cmsVersionModel.setUid(getCmsVersionService().generateVersionUid());
        cmsVersionModel.setTransactionId(getCmsVersionService().getTransactionId());
        cmsVersionModel.setPayload((String)getCmsVersionToDataConverter().convert(source));
        cmsVersionModel.setItemTypeCode("MediaContainer");
        cmsVersionModel.setItemCatalogVersion(source.getCatalogVersion());
        getModelService().save(cmsVersionModel);
        getCmsVersionSessionContextProvider().addUnsavedVersionedItemToCache(cmsVersionModel);
        return cmsVersionModel.getPk();
    }


    protected Converter<ItemModel, String> getCmsVersionToDataConverter()
    {
        return this.cmsVersionToDataConverter;
    }


    @Required
    public void setCmsVersionToDataConverter(Converter<ItemModel, String> cmsVersionToDataConverter)
    {
        this.cmsVersionToDataConverter = cmsVersionToDataConverter;
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


    protected CMSVersionService getCmsVersionService()
    {
        return this.cmsVersionService;
    }


    @Required
    public void setCmsVersionService(CMSVersionService cmsVersionService)
    {
        this.cmsVersionService = cmsVersionService;
    }


    protected CMSVersionSessionContextProvider getCmsVersionSessionContextProvider()
    {
        return this.cmsVersionSessionContextProvider;
    }


    @Required
    public void setCmsVersionSessionContextProvider(CMSVersionSessionContextProvider cmsVersionSessionContextProvider)
    {
        this.cmsVersionSessionContextProvider = cmsVersionSessionContextProvider;
    }
}
