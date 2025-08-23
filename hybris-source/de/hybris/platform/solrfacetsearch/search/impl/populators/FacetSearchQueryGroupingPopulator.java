package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.GroupCommandField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchQueryGroupingPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    public static final String GROUPING_METHOD_PROPERTY = "solrfacetsearch.search.grouping.method";
    public static final String GROUPING_METHOD_GROUP = "group";
    public static final String GROUPING_METHOD_COLLAPSE_EXPAND = "collapse-expand";
    public static final String GROUP_PARAM = "group";
    public static final String GROUP_FIELD_PARAM = "group.field";
    public static final String GROUP_LIMIT_PARAM = "group.limit";
    public static final String GROUP_NGROUPS_PARAM = "group.ngroups";
    public static final String GROUP_FACET_PARAM = "group.facet";
    public static final String EXPAND_PARAM = "expand";
    public static final String EXPAND_FIELD_PARAM = "expand.field";
    public static final String EXPAND_SORT_PARAM = "expand.sort";
    public static final String EXPAND_ROWS_PARAM = "expand.rows";
    public static final String EXPAND_ROWS_DEFAULT_VALUE = Integer.toString(1000);
    private ConfigurationService configurationService;
    private FieldNameTranslator fieldNameTranslator;


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public FieldNameTranslator getFieldNameTranslator()
    {
        return this.fieldNameTranslator;
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        Configuration configuration = this.configurationService.getConfiguration();
        String groupingMethod = configuration.getString("solrfacetsearch.search.grouping.method", "collapse-expand");
        if(StringUtils.equals(groupingMethod, "group"))
        {
            populateUsingGroupMethod(source, target);
        }
        else
        {
            populateUsingCollapseExpandMethod(source, target);
        }
    }


    public void populateUsingGroupMethod(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        if(CollectionUtils.isNotEmpty(searchQuery.getGroupCommands()))
        {
            target.add("group", new String[] {Boolean.TRUE.toString()});
            target.add("group.facet", new String[] {Boolean.toString(searchQuery.isGroupFacets())});
            target.add("group.ngroups", new String[] {Boolean.TRUE.toString()});
            for(GroupCommandField groupCommand : searchQuery.getGroupCommands())
            {
                String groupField = this.fieldNameTranslator.translate(searchQuery, groupCommand.getField(), FieldNameProvider.FieldType.INDEX);
                target.add("group.field", new String[] {groupField});
                if(groupCommand.getGroupLimit() != null)
                {
                    target.add("group.limit", new String[] {groupCommand.getGroupLimit().toString()});
                    continue;
                }
                target.add("group.limit", new String[] {String.valueOf(-1)});
            }
        }
    }


    public void populateUsingCollapseExpandMethod(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        if(searchQuery.getGroupCommand() != null)
        {
            GroupCommandField groupCommand = searchQuery.getGroupCommand();
            String groupField = this.fieldNameTranslator.translate(searchQuery, groupCommand.getField(), FieldNameProvider.FieldType.INDEX);
            StringBuilder collapseFilter = new StringBuilder();
            collapseFilter.append("{!tag=fc}{!collapse field=");
            collapseFilter.append(groupField);
            collapseFilter.append(" sort=$sort nullPolicy=collapse}");
            target.addFilterQuery(new String[] {collapseFilter.toString()});
            target.add("expand", new String[] {Boolean.TRUE.toString()});
            target.add("expand.field", new String[] {groupField});
            if(groupCommand.getGroupLimit() != null && groupCommand.getGroupLimit().intValue() >= 0)
            {
                target.add("expand.rows", new String[] {(groupCommand.getGroupLimit().intValue() == 0) ? String.valueOf(groupCommand.getGroupLimit()) :
                                String.valueOf(groupCommand.getGroupLimit().intValue() - 1)});
            }
            else
            {
                target.add("expand.rows", new String[] {EXPAND_ROWS_DEFAULT_VALUE});
            }
        }
    }
}
