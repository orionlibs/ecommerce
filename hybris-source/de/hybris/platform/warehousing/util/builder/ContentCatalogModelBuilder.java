package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Lists;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import java.util.Locale;

public class ContentCatalogModelBuilder
{
    private final ContentCatalogModel model = new ContentCatalogModel();


    private ContentCatalogModel getModel()
    {
        return this.model;
    }


    public static ContentCatalogModelBuilder aModel()
    {
        return new ContentCatalogModelBuilder();
    }


    public ContentCatalogModel build()
    {
        return getModel();
    }


    public ContentCatalogModelBuilder withId(String id)
    {
        getModel().setId(id);
        return this;
    }


    public ContentCatalogModelBuilder withActiveCatalogVersion(CatalogVersionModel version)
    {
        getModel().setActiveCatalogVersion(version);
        return this;
    }


    public ContentCatalogModelBuilder withCmsSites(CMSSiteModel... sites)
    {
        getModel().setCmsSites(Lists.newArrayList((Object[])sites));
        return this;
    }


    public ContentCatalogModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }


    public ContentCatalogModelBuilder withDefaultCatalog(Boolean defaultCatalog)
    {
        getModel().setDefaultCatalog(defaultCatalog);
        return this;
    }
}
