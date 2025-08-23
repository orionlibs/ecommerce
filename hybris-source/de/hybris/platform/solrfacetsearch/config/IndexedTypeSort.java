package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.solrfacetsearch.model.SolrSortModel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class IndexedTypeSort implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SolrSortModel sort;
    private String code;
    private String name;
    private Map<String, String> localizedName;
    private boolean applyPromotedItems;
    private boolean highlightPromotedItems;
    private List<IndexedTypeSortField> fields;


    @Deprecated
    public void setSort(SolrSortModel sort)
    {
        this.sort = sort;
    }


    @Deprecated
    public SolrSortModel getSort()
    {
        return this.sort;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setLocalizedName(Map<String, String> localizedName)
    {
        this.localizedName = localizedName;
    }


    public Map<String, String> getLocalizedName()
    {
        return this.localizedName;
    }


    public void setApplyPromotedItems(boolean applyPromotedItems)
    {
        this.applyPromotedItems = applyPromotedItems;
    }


    public boolean isApplyPromotedItems()
    {
        return this.applyPromotedItems;
    }


    public void setHighlightPromotedItems(boolean highlightPromotedItems)
    {
        this.highlightPromotedItems = highlightPromotedItems;
    }


    public boolean isHighlightPromotedItems()
    {
        return this.highlightPromotedItems;
    }


    public void setFields(List<IndexedTypeSortField> fields)
    {
        this.fields = fields;
    }


    public List<IndexedTypeSortField> getFields()
    {
        return this.fields;
    }
}
