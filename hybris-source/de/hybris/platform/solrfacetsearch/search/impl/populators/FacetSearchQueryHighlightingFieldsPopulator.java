package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchQueryHighlightingFieldsPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    public static final String HIGHLIGHTING_METHOD = "solrfacetsearch.search.highlighting.method";
    public static final String HIGHLIGHTING_METHOD_UNIFIED = "unified";
    public static final String HIGHLIGHTING_TAG_PRE = "solrfacetsearch.search.highlighting.tag.pre";
    public static final String HIGHLIGHTING_TAG_PRE_EM = "<em>";
    public static final String HIGHLIGHTING_TAG_POST = "solrfacetsearch.search.highlighting.tag.post";
    public static final String HIGHLIGHTING_TAG_POST_EM = "</em>";
    public static final String HIGHLIGHTING_REQUIRE_FIELD_MATCH = "solrfacetsearch.search.highlighting.requireFieldMatch";
    public static final String HIGHLIGHTING_SNIPPETS = "solrfacetsearch.search.highlighting.snippets";
    public static final int HIGHLIGHTING_SNIPPETS_DEFAULT = 3;
    public static final String SOLR_HIGHLIGHTING_METHOD_PARAM = "hl.method";
    public static final String SOLR_HIGHLIGHTING_TAG_PRE = "hl.tag.pre";
    public static final String SOLR_HIGHLIGHTING_TAG_POST = "hl.tag.post";
    private FieldNameTranslator fieldNameTranslator;
    private ConfigurationService configurationService;


    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        if(CollectionUtils.isEmpty(searchQuery.getHighlightingFields()))
        {
            return;
        }
        Configuration configuration = this.configurationService.getConfiguration();
        String highlightingMethod = configuration.getString("solrfacetsearch.search.highlighting.method", "unified");
        String highlightingTagPre = configuration.getString("solrfacetsearch.search.highlighting.tag.pre", "<em>");
        String highlightingTagPost = configuration.getString("solrfacetsearch.search.highlighting.tag.post", "</em>");
        int highlightingSnippets = configuration.getInt("solrfacetsearch.search.highlighting.snippets", 3);
        boolean requireFieldMatch = configuration.getBoolean("solrfacetsearch.search.highlighting.requireFieldMatch", true);
        searchQuery.getHighlightingFields().stream().forEach(field -> target.addHighlightField(this.fieldNameTranslator.translate(searchQuery, field, FieldNameProvider.FieldType.INDEX)));
        target.setHighlight(true);
        target.setHighlightSnippets(highlightingSnippets);
        target.setHighlightRequireFieldMatch(requireFieldMatch);
        target.add("hl.method", new String[] {highlightingMethod});
        target.add("hl.tag.pre", new String[] {highlightingTagPre});
        target.add("hl.tag.post", new String[] {highlightingTagPost});
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


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
