package de.hybris.platform.solr.search;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.LuceneQParser;
import org.apache.solr.search.SyntaxError;

public class MultiMaxScoreQParser extends LuceneQParser
{
    float tie = 0.0F;


    public MultiMaxScoreQParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req)
    {
        super(qstr, localParams, params, req);
        if(getParam("tie") != null)
        {
            this.tie = Float.parseFloat(getParam("tie"));
        }
    }


    public Query parse() throws SyntaxError
    {
        Query q = super.parse();
        if(!(q instanceof BooleanQuery))
        {
            return q;
        }
        BooleanQuery oldQuery = (BooleanQuery)q;
        BooleanQuery.Builder newQuery = new BooleanQuery.Builder();
        List<Query> disjuncts = null;
        for(BooleanClause clause : oldQuery.clauses())
        {
            if(clause.isProhibited() || clause.isRequired())
            {
                newQuery.add(clause);
                continue;
            }
            Query subQuery = clause.getQuery();
            if(!(subQuery instanceof BooleanQuery))
            {
                disjuncts = addClauseToDisjuncts(disjuncts, clause);
                continue;
            }
            addSubclauseToQuery(newQuery, (BooleanQuery)subQuery);
        }
        if(CollectionUtils.isNotEmpty(disjuncts))
        {
            DisjunctionMaxQuery disjunctionQuery = new DisjunctionMaxQuery(disjuncts, this.tie);
            newQuery.add((Query)disjunctionQuery, BooleanClause.Occur.SHOULD);
        }
        return (Query)newQuery.build();
    }


    private void addSubclauseToQuery(BooleanQuery.Builder newQuery, BooleanQuery subQuery)
    {
        List<Query> subQueriesList = new ArrayList<>();
        for(BooleanClause subQueryClause : subQuery.clauses())
        {
            subQueriesList.add(subQueryClause.getQuery());
        }
        DisjunctionMaxQuery subDmq = new DisjunctionMaxQuery(subQueriesList, this.tie);
        newQuery.add((Query)subDmq, BooleanClause.Occur.SHOULD);
    }


    private List<Query> addClauseToDisjuncts(List<Query> disjuncts, BooleanClause clause)
    {
        if(disjuncts == null)
        {
            disjuncts = new ArrayList<>();
        }
        disjuncts.add(clause.getQuery());
        return disjuncts;
    }
}
