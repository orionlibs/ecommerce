package de.hybris.platform.platformbackoffice.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogVersionReportDTO
{
    private String catalog;
    private String version;
    private Boolean active;
    private List<String> languages;
    private List<String> readPrincipals;
    private List<String> writePrincipals;
    private List<String> rootCategories;
    private Map<ComposedTypeModel, Long> typeStatistics;


    public CatalogVersionReportDTO(String catalog, String version)
    {
        this.catalog = catalog;
        this.version = version;
    }


    public String getCatalog()
    {
        return this.catalog;
    }


    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public List<String> getLanguages()
    {
        return this.languages;
    }


    public void setLanguages(List<String> languages)
    {
        this.languages = languages;
    }


    public List<String> getReadPrincipals()
    {
        return this.readPrincipals;
    }


    public void setReadPrincipals(List<String> readPrincipals)
    {
        this.readPrincipals = readPrincipals;
    }


    public List<String> getWritePrincipals()
    {
        return this.writePrincipals;
    }


    public void setWritePrincipals(List<String> writePrincipals)
    {
        this.writePrincipals = writePrincipals;
    }


    public List<String> getRootCategories()
    {
        return this.rootCategories;
    }


    public void setRootCategories(List<String> rootCategories)
    {
        this.rootCategories = rootCategories;
    }


    public Map<ComposedTypeModel, Long> getTypeStatistics()
    {
        return this.typeStatistics;
    }


    public void setTypeStatistics(Map<ComposedTypeModel, Long> typeStatistics)
    {
        this.typeStatistics = typeStatistics;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Catalog: ").append(getCatalog()).append("\n");
        sb.append("Version: ").append(getVersion()).append("\n");
        sb.append("----------------------------------------------\n");
        sb.append("Active: ").append(getActive()).append("\n");
        sb.append("Languages: ").append(getLanguages()).append("\n");
        sb.append("Permissions - read: ").append(getReadPrincipals()).append("\n");
        sb.append("Permissions - write: ").append(getWritePrincipals()).append("\n");
        sb.append("Root Categories: ").append(getRootCategories()).append("\n");
        sb.append("Types - instances: ")
                        .append(
                                        getTypeStatistics().entrySet().stream().map(e -> ((ComposedTypeModel)e.getKey()).getCode() + " - " + ((ComposedTypeModel)e.getKey()).getCode())
                                                        .collect(Collectors.toList())).append("\n");
        return sb.toString();
    }
}
