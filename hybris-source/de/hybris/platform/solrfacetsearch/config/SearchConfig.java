package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import java.util.Collection;

public class SearchConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Collection<String> defaultSortOrder;
    private int pageSize;
    private boolean allFacetValuesInResponse;
    private boolean restrictFieldsInResponse;
    private boolean enableHighlighting;
    private boolean legacyMode;


    public void setDefaultSortOrder(Collection<String> defaultSortOrder)
    {
        this.defaultSortOrder = defaultSortOrder;
    }


    public Collection<String> getDefaultSortOrder()
    {
        return this.defaultSortOrder;
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public void setAllFacetValuesInResponse(boolean allFacetValuesInResponse)
    {
        this.allFacetValuesInResponse = allFacetValuesInResponse;
    }


    public boolean isAllFacetValuesInResponse()
    {
        return this.allFacetValuesInResponse;
    }


    public void setRestrictFieldsInResponse(boolean restrictFieldsInResponse)
    {
        this.restrictFieldsInResponse = restrictFieldsInResponse;
    }


    public boolean isRestrictFieldsInResponse()
    {
        return this.restrictFieldsInResponse;
    }


    public void setEnableHighlighting(boolean enableHighlighting)
    {
        this.enableHighlighting = enableHighlighting;
    }


    public boolean isEnableHighlighting()
    {
        return this.enableHighlighting;
    }


    @Deprecated
    public void setLegacyMode(boolean legacyMode)
    {
        this.legacyMode = legacyMode;
    }


    @Deprecated
    public boolean isLegacyMode()
    {
        return this.legacyMode;
    }
}
