package de.hybris.platform.cms2.jalo.site;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.store.BaseStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CMSSite extends GeneratedCMSSite
{
    @Deprecated(since = "4.3")
    public List<Catalog> getProductCatalogs(SessionContext ctx)
    {
        List<Catalog> result = new ArrayList<>();
        Collection<BaseStore> stores = getStores(ctx);
        if(stores != null)
        {
            for(BaseStore baseStore : stores)
            {
                if(baseStore != null)
                {
                    Collection<Catalog> catalogs = baseStore.getCatalogs();
                    if(catalogs != null)
                    {
                        for(Catalog catalog : catalogs)
                        {
                            if(catalog != null && !(catalog instanceof de.hybris.platform.cms2.jalo.contents.ContentCatalog) && !(catalog instanceof de.hybris.platform.catalog.jalo.classification.ClassificationSystem) &&
                                            !result.contains(catalog))
                            {
                                result.add(catalog);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    @Deprecated(since = "4.3")
    public List<Catalog> getClassificationCatalogs(SessionContext ctx)
    {
        List<Catalog> ret = new ArrayList<>();
        Collection<BaseStore> stores = getStores(ctx);
        if(stores != null)
        {
            for(BaseStore baseStore : stores)
            {
                if(baseStore != null)
                {
                    Collection<Catalog> catalogs = baseStore.getCatalogs();
                    if(catalogs != null)
                    {
                        for(Catalog catalog : catalogs)
                        {
                            if(catalog != null && catalog instanceof de.hybris.platform.catalog.jalo.classification.ClassificationSystem && !ret.contains(catalog))
                            {
                                ret.add(catalog);
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }


    @Deprecated(since = "4.3")
    public String getStartPageLabel(SessionContext ctx)
    {
        ContentPage page = getStartingPage();
        if(page != null)
        {
            return page.getLabelOrId();
        }
        return null;
    }
}
