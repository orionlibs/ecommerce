package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class CoupledQueryField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String coupleId;
    private final QueryField field1;
    private final QueryField field2;
    private final SearchQuery.Operator innerCouplingOperator;
    private final SearchQuery.Operator outerCouplingOperator;


    public CoupledQueryField(String coupleId, QueryField field1, QueryField field2, SearchQuery.Operator innerCouplingOperator, SearchQuery.Operator outerCouplingOperator)
    {
        this.coupleId = coupleId;
        this.field1 = field1;
        this.field2 = field2;
        this.innerCouplingOperator = innerCouplingOperator;
        this.outerCouplingOperator = outerCouplingOperator;
    }


    public String getCoupleId()
    {
        return this.coupleId;
    }


    public QueryField getField1()
    {
        return this.field1;
    }


    public QueryField getField2()
    {
        return this.field2;
    }


    public SearchQuery.Operator getInnerCouplingOperator()
    {
        return this.innerCouplingOperator;
    }


    public SearchQuery.Operator getOuterCouplingOperator()
    {
        return this.outerCouplingOperator;
    }
}
