package de.hybris.platform.cms2.servicelayer.services.admin;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface CMSAdminContentSlotService
{
    ContentSlotModel createContentSlot(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, String paramString3);


    ContentSlotModel createContentSlot(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, String paramString3, boolean paramBoolean);


    ContentSlotModel createContentSlot(AbstractPageModel paramAbstractPageModel, String paramString1, String paramString2, String paramString3, boolean paramBoolean, Date paramDate1, Date paramDate2);


    void deleteContentSlot(String paramString) throws CMSItemNotFoundException;


    void deleteRelation(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel);


    void deleteRelationByPosition(AbstractPageModel paramAbstractPageModel, String paramString);


    Collection<CMSRelationModel> getAllRelationsForSlot(ContentSlotModel paramContentSlotModel);


    ContentSlotModel getContentSlotForId(String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;


    default List<ContentSlotModel> getContentSlots(List<String> contentSlotIds) throws UnknownIdentifierException, AmbiguousIdentifierException
    {
        throw new UnsupportedOperationException("CMSAdminContentSlotService.getContentSlots is not implemented.");
    }


    ContentSlotModel getContentSlotForIdAndCatalogVersions(String paramString, Collection<CatalogVersionModel> paramCollection) throws UnknownIdentifierException, AmbiguousIdentifierException;


    Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel paramAbstractPageModel);


    @Deprecated(since = "2105", forRemoval = true)
    List<ContentSlotForTemplateModel> findAllContentSlotRelationsByPageTemplate(PageTemplateModel paramPageTemplateModel);


    List<ContentSlotForPageModel> findAllContentSlotRelationsByPage(AbstractPageModel paramAbstractPageModel);


    Collection<ContentSlotData> getContentSlotsForPage(AbstractPageModel paramAbstractPageModel, boolean paramBoolean);


    boolean hasOtherRelations(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel);


    boolean hasRelations(ContentSlotModel paramContentSlotModel);


    Collection<CMSRelationModel> getOnlyContentSlotRelationsForSlot(ContentSlotModel paramContentSlotModel);


    void addCMSComponentToContentSlot(AbstractCMSComponentModel paramAbstractCMSComponentModel, ContentSlotModel paramContentSlotModel, Integer paramInteger);


    void updatePositionCMSComponentInContentSlot(AbstractCMSComponentModel paramAbstractCMSComponentModel, ContentSlotModel paramContentSlotModel, Integer paramInteger);


    Collection<ContentSlotModel> getContentSlotsForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    @Deprecated(since = "2105", forRemoval = true)
    List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> paramList, List<CatalogVersionModel> paramList1);


    default List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> contentSlots, List<CatalogVersionModel> catalogVersions, AbstractPageModel page)
    {
        throw new UnsupportedOperationException("The method getSortedMultiCountryContentSlots(contentSlots, catalogVersions, page) must be implemented");
    }


    @Deprecated(since = "2105", forRemoval = true)
    ContentSlotModel getContentSlotOverride(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel);


    List<ContentSlotForPageModel> getContentSlotRelationsByPageId(String paramString, CatalogVersionModel paramCatalogVersionModel);


    String getContentSlotPosition(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel);


    default List<CMSRelationModel> getAllDeletedRelationsForPage(CatalogVersionModel targetCatalogVersion, AbstractPageModel sourcePage)
    {
        throw new UnsupportedOperationException("The method getRelations(catalogVersion, page) must be implemented");
    }
}
