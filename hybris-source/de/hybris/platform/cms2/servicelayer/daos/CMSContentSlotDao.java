package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.List;

public interface CMSContentSlotDao extends Dao
{
    List<ContentSlotForTemplateModel> findAllContentSlotRelationsByPageTemplate(PageTemplateModel paramPageTemplateModel, CatalogVersionModel paramCatalogVersionModel);


    List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndContentSlot(PageTemplateModel paramPageTemplateModel, ContentSlotModel paramContentSlotModel, CatalogVersionModel paramCatalogVersionModel);


    List<ContentSlotForTemplateModel> findAllContentSlotRelationsByPageTemplate(PageTemplateModel paramPageTemplateModel);


    List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndPosition(PageTemplateModel paramPageTemplateModel, String paramString, Collection<CatalogVersionModel> paramCollection);


    default List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndCatalogVersions(PageTemplateModel template, Collection<CatalogVersionModel> catalogVersions)
    {
        throw new UnsupportedOperationException("The method findContentSlotRelationsByPageTemplateAndCatalogVersions(template, catalogVersions) must be implemented");
    }


    default List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndCatalogVersionsAndContentSlot(PageTemplateModel template, ContentSlotModel contentSlot, Collection<CatalogVersionModel> catalogVersions)
    {
        throw new UnsupportedOperationException("The method findContentSlotRelationsByPageTemplateAndCatalogVersionsAndContentSlot(template, contentSlot, catalogVersions) must be implemented");
    }


    List<ContentSlotForPageModel> findAllContentSlotRelationsByPage(AbstractPageModel paramAbstractPageModel, CatalogVersionModel paramCatalogVersionModel);


    List<ContentSlotForPageModel> findAllContentSlotRelationsByPageUid(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<ContentSlotForPageModel> findAllContentSlotRelationsByPage(AbstractPageModel paramAbstractPageModel);


    List<ContentSlotForPageModel> findAllContentSlotRelationsByContentSlot(ContentSlotModel paramContentSlotModel, CatalogVersionModel paramCatalogVersionModel);


    Collection<CMSRelationModel> findAllContentSlotRelationsByContentSlot(ContentSlotModel paramContentSlotModel);


    Collection<CMSRelationModel> findOnlyContentSlotRelationsByContentSlot(ContentSlotModel paramContentSlotModel);


    List<ContentSlotModel> findContentSlotsByIdAndCatalogVersions(String paramString, Collection<CatalogVersionModel> paramCollection);


    default List<ContentSlotModel> findContentSlotsByIdAndCatalogVersions(List<String> slotIds, Collection<CatalogVersionModel> catalogVersions)
    {
        throw new UnsupportedOperationException("CMSContentSlotDao.findContentSlotsByIdAndCatalogVersions method is not implemented.");
    }


    List<ContentSlotModel> findContentSlotsById(String paramString);


    List<ContentSlotForPageModel> findContentSlotRelationsByPageAndContentSlot(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel, CatalogVersionModel paramCatalogVersionModel);


    List<ContentSlotForPageModel> findContentSlotRelationsByPageAndContentSlot(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel, Collection<CatalogVersionModel> paramCollection);


    List<ContentSlotForPageModel> findContentSlotRelationsByPageAndPosition(AbstractPageModel paramAbstractPageModel, String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<ContentSlotForPageModel> findContentSlotRelationsByPageAndPosition(AbstractPageModel paramAbstractPageModel, String paramString, Collection<CatalogVersionModel> paramCollection);


    Collection<AbstractPageModel> findPagesByContentSlot(ContentSlotModel paramContentSlotModel);


    Collection<ContentSlotModel> findContentSlotsForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    List<ContentSlotForTemplateModel> findAllContentSlotForTemplateByContentSlot(ContentSlotModel paramContentSlotModel, CatalogVersionModel paramCatalogVersionModel);


    @Deprecated(since = "2105", forRemoval = true)
    List<ContentSlotModel> findAllMultiCountryContentSlotsByOriginalSlots(List<ContentSlotModel> paramList, List<CatalogVersionModel> paramList1);


    @Deprecated(since = "2202", forRemoval = true)
    default List<ContentSlotModel> findAllMultiCountryContentSlotsByOriginalSlots(List<ContentSlotModel> contentSlots, List<CatalogVersionModel> catalogVersions, AbstractPageModel page)
    {
        throw new UnsupportedOperationException("The method findAllMultiCountryContentSlotsByOriginalSlots(contentSlots, catalogVersions, page) must be implemented");
    }


    default List<CMSRelationModel> getAllDeletedRelationsForPage(CatalogVersionModel targetCatalogVersion, AbstractPageModel sourcePage)
    {
        throw new UnsupportedOperationException("The method getRelations(catalogVersion, page) must be implemented");
    }
}
