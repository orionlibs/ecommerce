package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import com.google.common.base.Strings;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.CMSItemData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSItemDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminItemService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSAdminItemService extends AbstractCMSAdminService implements CMSAdminItemService
{
    private PersistentKeyGenerator componentUidGenerator;
    private CMSItemDao cmsItemDao;
    private CatalogVersionService catalogVersionService;
    private static final String DEFAULT_UID_PREFIX = "cmsitem_";


    public CMSItemModel findByUid(String uid, CatalogVersionModel catalogVersion) throws CMSItemNotFoundException
    {
        CMSItemModel cmsItemModel = getCmsItemDao().findByUid(uid, catalogVersion);
        if(cmsItemModel == null)
        {
            throw new CMSItemNotFoundException(String.format("Could not find an instance of %s with uid %s for the catalogversion %s", new Object[] {CMSItemModel.class
                            .getSimpleName(), uid, catalogVersion.getVersion()}));
        }
        return cmsItemModel;
    }


    public CMSItemModel findByUid(String uid) throws CMSItemNotFoundException
    {
        return findByUid(uid, getActiveCatalogVersion());
    }


    public Optional<CMSItemModel> findByItemData(CMSItemData itemData)
    {
        CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(itemData.getCatalogId(), itemData
                        .getCatalogVersion());
        return Optional.ofNullable(getCmsItemDao().findByUid(itemData.getUid(), catalogVersion));
    }


    public <T extends CMSItemModel> T createItem(Class<T> modelClass)
    {
        CMSItemModel cMSItemModel = (CMSItemModel)getModelService().create(modelClass);
        if(Strings.isNullOrEmpty(cMSItemModel.getUid()))
        {
            cMSItemModel.setUid(generateCmsComponentUid());
        }
        return (T)cMSItemModel;
    }


    public SearchResult<CMSItemModel> findByTypeCodeAndName(CatalogVersionModel catalogVersion, String typeCode, String name)
    {
        return getCmsItemDao().findByTypeCodeAndName(catalogVersion, typeCode, name);
    }


    protected String generateCmsComponentUid()
    {
        return "cmsitem_" + getComponentUidGenerator().generate();
    }


    @Required
    public void setCmsItemDao(CMSItemDao cmsItemDao)
    {
        this.cmsItemDao = cmsItemDao;
    }


    protected CMSItemDao getCmsItemDao()
    {
        return this.cmsItemDao;
    }


    @Required
    public void setComponentUidGenerator(PersistentKeyGenerator componentUidGenerator)
    {
        this.componentUidGenerator = componentUidGenerator;
    }


    protected PersistentKeyGenerator getComponentUidGenerator()
    {
        return this.componentUidGenerator;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
