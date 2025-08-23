package de.hybris.platform.solrfacetsearch.jalo.hmc;

import de.hybris.platform.jalo.SessionContext;

public class ComposedIndexedType extends GeneratedComposedIndexedType
{
    private String strComposedIndexedType;


    public String getIndexedType(SessionContext ctx)
    {
        return this.strComposedIndexedType;
    }


    public void setIndexedType(SessionContext ctx, String value)
    {
        this.strComposedIndexedType = value;
    }
}
