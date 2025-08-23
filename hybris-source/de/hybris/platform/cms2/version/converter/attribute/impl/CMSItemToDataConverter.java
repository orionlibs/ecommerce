package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.data.CMSItemData;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class CMSItemToDataConverter implements Converter<CMSItemModel, PK>
{
    private CMSVersionService cmsVersionService;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private CatalogVersionService catalogVersionService;
    private TypeService typeService;
    private Predicate<CMSItemModel> abstractPageTypePredicate;


    public PK convert(CMSItemModel source)
    {
        PK pk;
        if(Objects.isNull(source))
        {
            pk = null;
        }
        else if(!getCmsVersionService().isVersionable(source) || getAbstractPageTypePredicate().test(source))
        {
            pk = source.getPk();
        }
        else
        {
            Optional<CMSItemData> cmsItemFromSession = findItemInSessionContext(source);
            if(cmsItemFromSession.isPresent())
            {
                pk = ((CMSItemData)cmsItemFromSession.get()).getPk();
            }
            else
            {
                pk = getCmsVersionService().createRevisionForItem(source).getPk();
            }
        }
        return pk;
    }


    protected Optional<CMSItemData> findItemInSessionContext(CMSItemModel cmsItem)
    {
        Set<CMSItemData> sessionItems = getCmsVersionSessionContextProvider().getAllUnsavedVersionedItemsFromCached();
        return sessionItems.stream()
                        .filter(sessionItem -> getTypeService().isAssignableFrom("CMSItem", sessionItem.getTypeCode()))
                        .filter(sessionItem -> {
                            CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(sessionItem.getCatalogId(), sessionItem.getCatalogVersion());
                            return (sessionItem.getUid().equals(cmsItem.getUid()) && catalogVersion.equals(cmsItem.getCatalogVersion()));
                        }).findFirst();
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


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
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


    protected Predicate<CMSItemModel> getAbstractPageTypePredicate()
    {
        return this.abstractPageTypePredicate;
    }


    @Required
    public void setAbstractPageTypePredicate(Predicate<CMSItemModel> abstractPageTypePredicate)
    {
        this.abstractPageTypePredicate = abstractPageTypePredicate;
    }
}
