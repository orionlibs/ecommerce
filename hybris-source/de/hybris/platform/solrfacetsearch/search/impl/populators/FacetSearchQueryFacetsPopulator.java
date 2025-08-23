package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.GroupCommandField;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchQueryFacetsPopulator extends AbstractFacetSearchQueryPopulator
{
    public static final String FACET_MIN_COUNT_PROPERTY = "solrfacetsearch.search.facet.minCount";
    public static final Integer FACET_MIN_COUNT_DEFAULT_VALUE = Integer.valueOf(1);
    public static final String FACET_LIMIT_PROPERTY = "solrfacetsearch.search.facet.limit";
    public static final Integer FACET_LIMIT_DEFAULT_VALUE = Integer.valueOf(100);
    public static final String FACET_PARAM_NAME_PREFIX = "json.facet.";
    private ConfigurationService configurationService;


    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        Map<String, FacetInfo> facetInfos = buildFacetInfos(searchQuery);
        GroupCommandField groupCommand = searchQuery.getGroupCommand();
        if(groupCommand != null && StringUtils.isNotBlank(groupCommand.getField()))
        {
            target.add(buildFacetParamName("documentCount"), new String[] {buildDocumentCountFacetParamValue()});
        }
        int index = 0;
        for(FacetInfo facetInfo : facetInfos.values())
        {
            FacetField facetField = facetInfo.getFacetField();
            String translatedField = getFieldNameTranslator().translate(searchQuery, facetField.getField(), FieldNameProvider.FieldType.INDEX);
            FacetType facetType = facetField.getFacetType();
            if(CollectionUtils.isEmpty(facetInfo.getSelectedValues()))
            {
                target.add(buildFacetParamName(translatedField), new String[] {buildFacetParamValue(searchQuery, facetField, translatedField, null)});
            }
            else if(facetType == FacetType.MULTISELECTAND)
            {
                String tag = "f" + index;
                QueryField facetFilterQuery = new QueryField(facetField.getField(), SearchQuery.Operator.AND, facetInfo.getSelectedValues());
                String filterQuery = convertQueryField(searchQuery, facetFilterQuery);
                target.addFilterQuery(new String[] {"{!tag=" + tag + "}" + filterQuery});
                target.add(buildFacetParamName(translatedField), new String[] {buildFacetParamValue(searchQuery, facetField, translatedField, tag)});
            }
            else if(facetType == FacetType.MULTISELECTOR)
            {
                String tag = "f" + index;
                QueryField facetFilterQuery = new QueryField(facetField.getField(), SearchQuery.Operator.OR, facetInfo.getSelectedValues());
                String filterQuery = convertQueryField(searchQuery, facetFilterQuery);
                target.addFilterQuery(new String[] {"{!tag=" + tag + "}" + filterQuery});
                target.add(buildFacetParamName(translatedField), new String[] {buildFacetParamValue(searchQuery, facetField, translatedField, tag)});
            }
            else
            {
                QueryField facetFilterQuery = new QueryField(facetField.getField(), SearchQuery.Operator.AND, facetInfo.getSelectedValues());
                String filterQuery = convertQueryField(searchQuery, facetFilterQuery);
                target.addFilterQuery(new String[] {filterQuery});
                target.add(buildFacetParamName(translatedField), new String[] {buildFacetParamValue(searchQuery, facetField, translatedField, null)});
            }
            index++;
        }
    }


    protected Map<String, FacetInfo> buildFacetInfos(SearchQuery searchQuery)
    {
        Map<String, FacetInfo> facetInfos = new HashMap<>();
        for(FacetField facet : searchQuery.getFacets())
        {
            FacetInfo facetInfo = new FacetInfo(facet);
            facetInfos.put(facet.getField(), facetInfo);
        }
        for(FacetValueField facetValue : searchQuery.getFacetValues())
        {
            FacetInfo facetInfo = facetInfos.get(facetValue.getField());
            if(facetInfo == null)
            {
                FacetField facetField = new FacetField(facetValue.getField());
                facetInfo = new FacetInfo(facetField);
                facetInfos.put(facetValue.getField(), facetInfo);
            }
            if(CollectionUtils.isNotEmpty(facetValue.getValues()))
            {
                facetInfo.getSelectedValues().addAll(facetValue.getValues());
            }
        }
        return facetInfos;
    }


    protected String buildFacetParamName(String translatedField)
    {
        return "json.facet." + translatedField;
    }


    protected String buildFacetParamValue(SearchQuery searchQuery, FacetField facetField, String translatedField, String excludeTag)
    {
        StringBuilder facetJson = new StringBuilder();
        facetJson.append("{\"type\":\"terms\",\"field\":\"").append(translatedField).append("\"");
        Integer facetLimit = resolveFacetLimit(facetField);
        if(facetLimit != null)
        {
            facetJson.append(",\"limit\":").append(facetLimit);
        }
        Integer facetMinCount = resolveFacetMinCount(facetField);
        if(facetMinCount != null)
        {
            facetJson.append(",\"mincount\":").append(facetMinCount);
        }
        GroupCommandField groupCommand = searchQuery.getGroupCommand();
        if(groupCommand != null && StringUtils.isNotBlank(groupCommand.getField()) && searchQuery.isGroupFacets() &&
                        BooleanUtils.isNotFalse(facetField.getGroupFacet()))
        {
            String groupField = getFieldNameTranslator().translate(searchQuery, groupCommand.getField(), FieldNameProvider.FieldType.INDEX);
            facetJson.append(",\"facet\":{\"").append("groupCount").append("\":\"unique(")
                            .append(groupField).append(")\"}");
        }
        if(StringUtils.isNotBlank(excludeTag))
        {
            facetJson.append(",\"domain\":{\"excludeTags\":[\"fc\",\"").append(excludeTag).append("\"]}");
        }
        else
        {
            facetJson.append(",\"domain\":{\"excludeTags\":\"fc\"}");
        }
        facetJson.append('}');
        return facetJson.toString();
    }


    protected String buildDocumentCountFacetParamValue()
    {
        return "{\"type\":\"query\",\"q\":\"*:*\",\"domain\":{\"excludeTags\":\"fc\"}}";
    }


    protected Integer resolveFacetLimit(FacetField facetField)
    {
        if(facetField.getLimit() != null && facetField.getLimit().intValue() >= 0)
        {
            return facetField.getLimit();
        }
        return this.configurationService.getConfiguration().getInteger("solrfacetsearch.search.facet.limit", FACET_LIMIT_DEFAULT_VALUE);
    }


    protected Integer resolveFacetMinCount(FacetField facetField)
    {
        if(facetField.getMinCount() != null && facetField.getMinCount().intValue() >= 0)
        {
            return facetField.getMinCount();
        }
        return this.configurationService.getConfiguration().getInteger("solrfacetsearch.search.facet.minCount", FACET_MIN_COUNT_DEFAULT_VALUE);
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
