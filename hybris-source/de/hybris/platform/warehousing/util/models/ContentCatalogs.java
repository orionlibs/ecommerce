package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.warehousing.util.builder.ContentCatalogModelBuilder;
import de.hybris.platform.warehousing.util.dao.impl.ContentCatalogDaoImpl;
import org.springframework.beans.factory.annotation.Required;

public class ContentCatalogs extends AbstractItems<ContentCatalogModel>
{
    public static final String CONTENTCATALOG_ID = "contentCatalog_online";
    private ContentCatalogDaoImpl contentCatalogDao;
    private CmsSites cmsSites;
    private CatalogVersions catalogVersions;


    public ContentCatalogModel contentCatalog_online()
    {
        return getOrCreateContentCatalog("contentCatalog_online");
    }


    protected ContentCatalogModel getOrCreateContentCatalog(String id)
    {
        return (ContentCatalogModel)getOrSaveAndReturn(() -> (ContentCatalogModel)getContentCatalogDao().getByCode(id), () -> ContentCatalogModelBuilder.aModel().withId(id).withDefaultCatalog(Boolean.valueOf(true)).build());
    }


    public ContentCatalogDaoImpl getContentCatalogDao()
    {
        return this.contentCatalogDao;
    }


    @Required
    public void setContentCatalogDao(ContentCatalogDaoImpl contentCatalogDao)
    {
        this.contentCatalogDao = contentCatalogDao;
    }


    public CmsSites getCmsSites()
    {
        return this.cmsSites;
    }


    @Required
    public void setCmsSites(CmsSites cmsSites)
    {
        this.cmsSites = cmsSites;
    }


    public CatalogVersions getCatalogVersions()
    {
        return this.catalogVersions;
    }


    @Required
    public void setCatalogVersions(CatalogVersions catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }
}
