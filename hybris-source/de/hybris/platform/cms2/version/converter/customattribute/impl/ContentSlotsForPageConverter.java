package de.hybris.platform.cms2.version.converter.customattribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionPayloadDescriptor;
import de.hybris.platform.cms2.version.converter.attribute.impl.CMSItemToDataConverter;
import de.hybris.platform.cms2.version.converter.customattribute.CustomAttributeContentConverter;
import de.hybris.platform.cms2.version.converter.customattribute.data.ContentSlotForPageRelationData;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public class ContentSlotsForPageConverter implements CustomAttributeContentConverter
{
    private String qualifier;
    private Predicate<ItemModel> constrainedBy;
    private CMSAdminContentSlotService cmsAdminContentSlotService;
    private CMSItemToDataConverter cmsItemToDataConverter;
    private ObjectFactory<ContentSlotForPageRelationData> contentSlotRelationDataFactory;
    private Converter<VersionPayloadDescriptor, ItemModel> pkDataToModelConverter;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private ModelService modelService;
    private CMSContentSlotDao cmsContentSlotDao;


    public List<ContentSlotForPageRelationData> convertModelToData(ItemModel itemModel)
    {
        return (List<ContentSlotForPageRelationData>)getCmsAdminContentSlotService().findAllContentSlotRelationsByPage((AbstractPageModel)itemModel)
                        .stream()
                        .filter(Objects::nonNull)
                        .map(this::convertContentSlotModelToRelationData)
                        .collect(Collectors.toList());
    }


    public void populateItemModel(ItemModel itemModel, String value)
    {
        AbstractPageModel page = (AbstractPageModel)itemModel;
        ContentSlotForPageRelationData contentSlotForPageRelationData = (ContentSlotForPageRelationData)getContentSlotRelationDataFactory().getObject();
        contentSlotForPageRelationData.init(value);
        ContentSlotModel contentSlotModel = getContentSlotByPk(contentSlotForPageRelationData.getPk());
        Optional<ContentSlotForPageModel> contentSlotForPageOpt = getCmsContentSlotDao().findContentSlotRelationsByPageAndPosition(page, contentSlotForPageRelationData.getPosition(), page.getCatalogVersion()).stream().findFirst();
        if(contentSlotForPageOpt.isEmpty())
        {
            ContentSlotForPageModel contentSlotForPage = (ContentSlotForPageModel)getModelService().create(ContentSlotForPageModel.class);
            contentSlotForPage.setContentSlot(contentSlotModel);
            contentSlotForPage.setPosition(contentSlotForPageRelationData.getPosition());
            contentSlotForPage.setCatalogVersion(page.getCatalogVersion());
            contentSlotForPage.setPage(page);
            getCmsVersionSessionContextProvider().addContentSlotForPageToCache(contentSlotForPage);
        }
        else
        {
            ContentSlotForPageModel contentSlotForPage = contentSlotForPageOpt.get();
            contentSlotForPage.setContentSlot(contentSlotModel);
            getCmsVersionSessionContextProvider().addContentSlotForPageToCache(contentSlotForPage);
        }
    }


    protected ContentSlotModel getContentSlotByPk(PK pk)
    {
        return (ContentSlotModel)getPkDataToModelConverter()
                        .convert(new VersionPayloadDescriptor(ContentSlotModel.class.getCanonicalName(), pk.getLongValueAsString()));
    }


    protected ContentSlotForPageRelationData convertContentSlotModelToRelationData(ContentSlotForPageModel contentSlotForPageModel)
    {
        ContentSlotForPageRelationData contentSlotForPageRelationData = (ContentSlotForPageRelationData)getContentSlotRelationDataFactory().getObject();
        contentSlotForPageRelationData.setPosition(contentSlotForPageModel.getPosition());
        contentSlotForPageRelationData.setPk(getCmsItemToDataConverter().convert((CMSItemModel)contentSlotForPageModel.getContentSlot()));
        return contentSlotForPageRelationData;
    }


    public Predicate<ItemModel> getConstrainedBy()
    {
        return this.constrainedBy;
    }


    @Required
    public void setConstrainedBy(Predicate<ItemModel> constrainedBy)
    {
        this.constrainedBy = constrainedBy;
    }


    protected CMSItemToDataConverter getCmsItemToDataConverter()
    {
        return this.cmsItemToDataConverter;
    }


    @Required
    public void setCmsItemToDataConverter(CMSItemToDataConverter cmsItemToDataConverter)
    {
        this.cmsItemToDataConverter = cmsItemToDataConverter;
    }


    protected ObjectFactory<ContentSlotForPageRelationData> getContentSlotRelationDataFactory()
    {
        return this.contentSlotRelationDataFactory;
    }


    @Required
    public void setContentSlotRelationDataFactory(ObjectFactory<ContentSlotForPageRelationData> contentSlotRelationDataFactory)
    {
        this.contentSlotRelationDataFactory = contentSlotRelationDataFactory;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    @Required
    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    protected CMSAdminContentSlotService getCmsAdminContentSlotService()
    {
        return this.cmsAdminContentSlotService;
    }


    @Required
    public void setCmsAdminContentSlotService(CMSAdminContentSlotService cmsAdminContentSlotService)
    {
        this.cmsAdminContentSlotService = cmsAdminContentSlotService;
    }


    protected Converter<VersionPayloadDescriptor, ItemModel> getPkDataToModelConverter()
    {
        return this.pkDataToModelConverter;
    }


    @Required
    public void setPkDataToModelConverter(Converter<VersionPayloadDescriptor, ItemModel> pkDataToModelConverter)
    {
        this.pkDataToModelConverter = pkDataToModelConverter;
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


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public CMSContentSlotDao getCmsContentSlotDao()
    {
        return this.cmsContentSlotDao;
    }


    @Required
    public void setCmsContentSlotDao(CMSContentSlotDao cmsContentSlotDao)
    {
        this.cmsContentSlotDao = cmsContentSlotDao;
    }
}
