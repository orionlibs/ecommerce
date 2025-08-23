package de.hybris.platform.catalog.data;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.io.Serializable;
import java.util.Map;

public class CatalogVersionOverview implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CatalogVersionModel catalogVersion;
    private Map<ComposedTypeModel, Long> typeAmounts;


    public void setCatalogVersion(CatalogVersionModel catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public CatalogVersionModel getCatalogVersion()
    {
        return this.catalogVersion;
    }


    public void setTypeAmounts(Map<ComposedTypeModel, Long> typeAmounts)
    {
        this.typeAmounts = typeAmounts;
    }


    public Map<ComposedTypeModel, Long> getTypeAmounts()
    {
        return this.typeAmounts;
    }
}
