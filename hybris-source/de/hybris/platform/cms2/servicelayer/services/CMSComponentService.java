package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public interface CMSComponentService
{
    <T extends AbstractCMSComponentModel> T getAbstractCMSComponent(String paramString) throws CMSItemNotFoundException;


    <T extends AbstractCMSComponentModel> SearchPageData<T> getAbstractCMSComponents(Collection<String> paramCollection, SearchPageData paramSearchPageData);


    <T extends AbstractCMSComponentModel> SearchPageData<T> getAllAbstractCMSComponents(SearchPageData paramSearchPageData);


    <T extends AbstractCMSComponentModel> T getAbstractCMSComponent(String paramString, Collection<CatalogVersionModel> paramCollection) throws CMSItemNotFoundException;


    <T extends AbstractCMSComponentModel> T getAbstractCMSComponent(String paramString1, String paramString2, Collection<CatalogVersionModel> paramCollection) throws CMSItemNotFoundException;


    Collection<String> getEditorProperties(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    Collection<String> getReadableEditorProperties(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    <T extends de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel> T getSimpleCMSComponent(String paramString) throws CMSItemNotFoundException;


    Collection<String> getSystemProperties(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    boolean isComponentRestricted(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    boolean isComponentContainer(String paramString);


    boolean isComponentUsedOutsidePage(AbstractCMSComponentModel paramAbstractCMSComponentModel, AbstractPageModel paramAbstractPageModel);


    default Set<AbstractCMSComponentModel> getAllParents(AbstractCMSComponentModel component)
    {
        return Collections.emptySet();
    }


    default Set<AbstractCMSComponentModel> getAllChildren(AbstractCMSComponentModel component)
    {
        return Collections.emptySet();
    }


    default boolean inSharedSlots(AbstractCMSComponentModel component)
    {
        return false;
    }
}
