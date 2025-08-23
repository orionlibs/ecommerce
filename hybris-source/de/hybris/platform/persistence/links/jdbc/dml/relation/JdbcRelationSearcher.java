package de.hybris.platform.persistence.links.jdbc.dml.relation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.hybris.platform.persistence.links.jdbc.dml.Relation;
import de.hybris.platform.persistence.links.jdbc.dml.RelationsSearchParams;
import de.hybris.platform.persistence.links.jdbc.dml.RelationsSearcher;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class JdbcRelationSearcher implements RelationsSearcher
{
    private final JdbcTemplate jdbcTemplate;


    public JdbcRelationSearcher(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Iterable<Relation> search(RelationsSearchParams params)
    {
        Preconditions.checkNotNull(params);
        if(!params.areValid())
        {
            return (Iterable<Relation>)ImmutableList.of();
        }
        String query = getQuery(params);
        Object[] queryParams = getQueryParams(params);
        ResultSetExtractor<Iterable<Relation>> relationsExtractor = getRelationsExtractor(params);
        return (Iterable<Relation>)this.jdbcTemplate.query(query, queryParams, relationsExtractor);
    }


    private String getQuery(RelationsSearchParams params)
    {
        String pkColumnName = DatabaseHelper.getPkColumnName(params);
        String parentPKColumnName = DatabaseHelper.getParentPKColumnName(params);
        String parentToChildOrderColumnName = DatabaseHelper.getOrderColumn(params);
        StringBuilder result = new StringBuilder(
                        "select " + pkColumnName + ", " + DatabaseHelper.getLanguagePKColumnName(params) + ", " + DatabaseHelper.getSourcePKColumnName(params) + ", " + DatabaseHelper.getTargetPKColumnName(params) + ", " + DatabaseHelper.getSequenceNumberColumnName(params) + ", "
                                        + DatabaseHelper.getReverseSequenceColumnName(params) + ", " + DatabaseHelper.getVersionColumnName(params) + " from " + DatabaseHelper.getTableName(params) + " where " + DatabaseHelper.getQualifierColumnName(params) + "=? and " + parentPKColumnName);
        appendItemPKsPart(result, params);
        appendLanguagePKsPart(result, params);
        result.append(" order by ").append(parentToChildOrderColumnName).append(" ASC, ").append(pkColumnName).append(" ASC");
        return result.toString();
    }


    private Object[] getQueryParams(RelationsSearchParams params)
    {
        ImmutableList immutableList = ImmutableList.of(params.getRelationCode());
        Iterable<Long> parentParams = params.getParentPKs();
        Iterable<Long> languageParams = params.getLocalizableLanguagePKs();
        return Iterables.toArray(Iterables.concat((Iterable)immutableList, parentParams, languageParams), Object.class);
    }


    private ResultSetExtractor<Iterable<Relation>> getRelationsExtractor(RelationsSearchParams params)
    {
        return (ResultSetExtractor<Iterable<Relation>>)new RelationsExtractor(params);
    }


    private void appendItemPKsPart(StringBuilder builder, RelationsSearchParams params)
    {
        int numberOfParentPKs = params.getNumberOfParentPKs();
        if(numberOfParentPKs == 1)
        {
            builder.append("=?");
        }
        else
        {
            builder.append(" in (?").append(StringUtils.repeat(",?", numberOfParentPKs - 1)).append(")");
        }
    }


    private void appendLanguagePKsPart(StringBuilder result, RelationsSearchParams params)
    {
        if(params.isSearchForAllLanguages())
        {
            return;
        }
        boolean isQueryForNotLocalizedRows = params.isSearchForNonLocalizedRelations();
        int numberOfLanguages = params.getNumberOfLocalizableLanguagePKs();
        String languagePKColumnName = DatabaseHelper.getLanguagePKColumnName(params);
        result.append(" and ");
        if(isQueryForNotLocalizedRows)
        {
            result.append(languagePKColumnName).append(" is null");
            if(numberOfLanguages > 0)
            {
                result.append(" or ");
            }
            else
            {
                return;
            }
        }
        result.append(languagePKColumnName);
        if(numberOfLanguages == 1)
        {
            result.append("=?");
        }
        else
        {
            result.append(" in (?").append(StringUtils.repeat(",?", numberOfLanguages - 1)).append(")");
        }
    }
}
