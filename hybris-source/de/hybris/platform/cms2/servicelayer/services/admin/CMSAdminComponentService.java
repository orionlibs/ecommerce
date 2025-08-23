package de.hybris.platform.cms2.servicelayer.services.admin;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.List;

public interface CMSAdminComponentService
{
    AbstractCMSComponentModel createCmsComponent(ContentSlotModel paramContentSlotModel, String paramString1, String paramString2, String paramString3);


    String generateCmsComponentUid();


    Collection<ComposedTypeModel> getAllowedCMSComponentContainers();


    Collection<ComposedTypeModel> getAllowedCMSComponents();


    AbstractCMSComponentContainerModel getCMSComponentContainerForId(String paramString) throws AmbiguousIdentifierException, UnknownIdentifierException;


    AbstractCMSComponentModel getCMSComponentForId(String paramString) throws AmbiguousIdentifierException, UnknownIdentifierException;


    AbstractCMSComponentModel getCMSComponentForIdAndCatalogVersions(String paramString, Collection<CatalogVersionModel> paramCollection) throws AmbiguousIdentifierException, UnknownIdentifierException;


    Collection<String> getEditorProperties(ItemModel paramItemModel);


    Collection<String> getSystemProperties(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    void removeCMSComponentFromContentSlot(AbstractCMSComponentModel paramAbstractCMSComponentModel, ContentSlotModel paramContentSlotModel);


    List<AbstractCMSComponentModel> getAllCMSComponentsForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    Collection<AbstractCMSComponentModel> getCMSComponentsForContainer(AbstractCMSComponentContainerModel paramAbstractCMSComponentContainerModel);


    Collection<AbstractCMSComponentModel> getDisplayedComponentsForContentSlot(ContentSlotModel paramContentSlotModel);


    Collection<AbstractCMSComponentContainerModel> getContainersForContentSlot(ContentSlotModel paramContentSlotModel);


    SearchResult<AbstractCMSComponentModel> findByCatalogVersionAndMask(CatalogVersionModel paramCatalogVersionModel, String paramString, PageableData paramPageableData);
}
