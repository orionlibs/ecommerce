package de.hybris.platform.cms2.servicelayer.services.admin;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collection;
import java.util.Map;

public interface CMSAdminSiteService
{
    Collection<CMSSiteModel> getSites();


    CMSSiteModel getSiteForId(String paramString) throws AmbiguousIdentifierException, UnknownIdentifierException;


    void setActiveSiteForId(String paramString) throws AmbiguousIdentifierException, UnknownIdentifierException;


    void setActiveSite(CMSSiteModel paramCMSSiteModel);


    CMSSiteModel getActiveSite();


    boolean hasActiveSite();


    void setActiveCatalogVersion(String paramString1, String paramString2) throws CMSItemNotFoundException;


    void setActiveCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    CatalogVersionModel getActiveCatalogVersion();


    boolean hasActiveCatalogVersion();


    Collection<ComposedTypeModel> getAllPageTypes();


    boolean hasPreviewURL();


    boolean hasPreviewURL(CMSSiteModel paramCMSSiteModel);


    Collection<CatalogModel> getAllCatalogs(CMSSiteModel paramCMSSiteModel);


    void setCloneContext(Map<String, String> paramMap);


    Map<String, String> getCloneContext();


    void setOriginalItemContext(Map<String, Object> paramMap);


    Map<String, Object> getOriginalItemContext();


    void setRestoreContext(Map<String, Object> paramMap);


    Map<String, Object> getRestoreContext();


    void setTypeContext(Map<String, Object> paramMap);


    Map<String, Object> getTypeContext();
}
