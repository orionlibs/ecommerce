package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.warehousing.util.builder.CatalogModelBuilder;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class Catalogs extends AbstractItems<CatalogModel>
{
    public static final String ID_PRIMARY = "primary";
    private CatalogDao catalogDao;
    private CatalogVersions catalogVersions;


    public CatalogModel Primary()
    {
        CatalogModel catalogModel = (CatalogModel)getOrSaveAndReturn(() -> getCatalogDao().findCatalogById("primary"), () -> CatalogModelBuilder.aModel().withDefaultCatalog(Boolean.TRUE).withId("primary").withName("primary", Locale.ENGLISH).build());
        if(catalogModel.getActiveCatalogVersion() == null)
        {
            catalogModel.setActiveCatalogVersion(getCatalogVersions().Staging(catalogModel));
            getModelService().save(catalogModel);
        }
        return catalogModel;
    }


    public CatalogDao getCatalogDao()
    {
        return this.catalogDao;
    }


    @Required
    public void setCatalogDao(CatalogDao catalogDao)
    {
        this.catalogDao = catalogDao;
    }


    protected CatalogVersions getCatalogVersions()
    {
        return this.catalogVersions;
    }


    @Required
    public void setCatalogVersions(CatalogVersions catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }
}
