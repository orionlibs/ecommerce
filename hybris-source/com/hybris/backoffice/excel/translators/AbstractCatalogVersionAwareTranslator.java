package com.hybris.backoffice.excel.translators;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCatalogVersionAwareTranslator<T> extends AbstractExcelValueTranslator<T>
{
    private CatalogTypeService catalogTypeService;
    private static final String PATTERN = "%s:%s";


    public String exportCatalogVersionData(CatalogVersionModel objectToExport)
    {
        if(objectToExport != null && objectToExport.getCatalog() != null && objectToExport.getCatalog().getId() != null && objectToExport
                        .getVersion() != null)
        {
            return String.format("%s:%s", new Object[] {objectToExport.getCatalog().getId(), objectToExport.getVersion()});
        }
        return ":";
    }


    protected String catalogVersionData(Map<String, String> params)
    {
        String version = params.get("version");
        String catalog = params.get("catalog");
        return (StringUtils.isNotBlank(version) && StringUtils.isNotBlank(catalog)) ? String.format("%s:%s", new Object[] {version, catalog}) : null;
    }


    protected String catalogVersionHeader(String typeCode)
    {
        String catalogVersionQualifier = getCatalogTypeService().getCatalogVersionContainerAttribute(typeCode);
        return String.format("%s(%s,%s(%s))", new Object[] {catalogVersionQualifier, "version", "catalog", "id"});
    }


    public String referenceCatalogVersionFormat()
    {
        return String.format("%s:%s", new Object[] {"catalog", "version"});
    }


    public CatalogTypeService getCatalogTypeService()
    {
        return this.catalogTypeService;
    }


    @Required
    public void setCatalogTypeService(CatalogTypeService catalogTypeService)
    {
        this.catalogTypeService = catalogTypeService;
    }
}
