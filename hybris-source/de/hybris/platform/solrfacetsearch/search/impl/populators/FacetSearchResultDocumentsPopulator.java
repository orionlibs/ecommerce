package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroup;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroupCommand;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultDocument;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResultGroup;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResultGroupCommand;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.json.NestableJsonFacet;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchResultDocumentsPopulator implements Populator<SearchResultConverterData, SolrSearchResult>
{
    private FieldNameTranslator fieldNameTranslator;


    public FieldNameTranslator getFieldNameTranslator()
    {
        return this.fieldNameTranslator;
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    public void populate(SearchResultConverterData source, SolrSearchResult target)
    {
        SearchQuery searchQuery = source.getFacetSearchContext().getSearchQuery();
        QueryResponse queryResponse = source.getQueryResponse();
        FieldNameTranslator.FieldInfosMapping fieldInfosMapping = this.fieldNameTranslator.getFieldInfos(source.getFacetSearchContext());
        Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos = fieldInfosMapping.getInvertedFieldInfos();
        if(queryResponse == null)
        {
            return;
        }
        if(queryResponse.getGroupResponse() != null)
        {
            populateUsingGroupResponse(queryResponse, invertedFieldInfos, target);
        }
        else if(MapUtils.isNotEmpty(queryResponse.getExpandedResults()))
        {
            populateUsingExpandResponse(queryResponse, invertedFieldInfos, searchQuery, target);
        }
        else
        {
            populateUsingDefaultResponse(queryResponse, invertedFieldInfos, target);
        }
    }


    protected void populateUsingGroupResponse(QueryResponse queryResponse, Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos, SolrSearchResult target)
    {
        long numberOfResults = 0L;
        List<Document> documents = new ArrayList<>();
        List<SolrDocument> solrDocuments = new ArrayList<>();
        List<SearchResultGroupCommand> searchResultGroupCommands = new ArrayList<>();
        int groupCommandIndex = 0;
        GroupResponse groupResponse = queryResponse.getGroupResponse();
        for(GroupCommand groupCommand : groupResponse.getValues())
        {
            if(groupCommandIndex == 0)
            {
                numberOfResults = groupCommand.getNGroups().intValue();
            }
            List<SearchResultGroup> searchResultGroups = new ArrayList<>();
            populateSearchResultGroupsForGroupResponse(documents, solrDocuments, searchResultGroups, invertedFieldInfos, groupCommand, groupCommandIndex, queryResponse
                            .getHighlighting());
            String groupCommandName = resolveFieldName(groupCommand.getName(), invertedFieldInfos);
            SolrSearchResultGroupCommand searchResultGroupCommand = new SolrSearchResultGroupCommand();
            searchResultGroupCommand.setName(groupCommandName);
            searchResultGroupCommand.setNumberOfMatches(groupCommand.getMatches());
            searchResultGroupCommand.setNumberOfGroups(groupCommand.getNGroups().intValue());
            searchResultGroupCommand.setGroups(searchResultGroups);
            searchResultGroupCommands.add(searchResultGroupCommand);
            groupCommandIndex++;
        }
        target.setNumberOfResults(numberOfResults);
        target.setDocuments(documents);
        target.setSolrDocuments(solrDocuments);
        target.getGroupCommands().addAll(searchResultGroupCommands);
    }


    protected void populateSearchResultGroupsForGroupResponse(List<Document> documents, List<SolrDocument> solrDocuments, List<SearchResultGroup> searchResultGroups, Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos, GroupCommand groupCommand, int groupCommandIndex,
                    Map<String, Map<String, List<String>>> highlighting)
    {
        for(Group group : groupCommand.getValues())
        {
            List<Document> groupDocuments = new ArrayList<>();
            long groupDocumentIndex = 0L;
            SolrDocumentList groupResult = group.getResult();
            for(SolrDocument solrGroupDocument : groupResult)
            {
                Document groupDocument = convertDocument(solrGroupDocument, invertedFieldInfos, highlighting);
                groupDocuments.add(groupDocument);
                if(groupCommandIndex == 0 && groupDocumentIndex == 0L)
                {
                    documents.add(groupDocument);
                    solrDocuments.add(solrGroupDocument);
                }
                groupDocumentIndex++;
            }
            SolrSearchResultGroup searchResultGroup = new SolrSearchResultGroup();
            searchResultGroup.setGroupValue(group.getGroupValue());
            searchResultGroup.setNumberOfResults(groupResult.getNumFound());
            searchResultGroup.setDocuments(groupDocuments);
            searchResultGroups.add(searchResultGroup);
        }
    }


    protected void populateUsingExpandResponse(QueryResponse queryResponse, Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos, SearchQuery searchQuery, SolrSearchResult target)
    {
        String groupField = searchQuery.getGroupCommand().getField();
        SolrDocumentList results = queryResponse.getResults();
        long numberOfResults = results.getNumFound();
        List<Document> documents = new ArrayList<>();
        List<SolrDocument> solrDocuments = new ArrayList<>();
        List<SearchResultGroup> searchResultGroups = new ArrayList<>();
        if(CollectionUtils.isNotEmpty((Collection)results))
        {
            for(SolrDocument solrDocument : results)
            {
                Document document = convertDocument(solrDocument, invertedFieldInfos, queryResponse.getHighlighting());
                documents.add(document);
                solrDocuments.add(solrDocument);
                searchResultGroups.add(createSearchResulGroupForExpand(queryResponse, document, groupField, invertedFieldInfos));
            }
        }
        SolrSearchResultGroupCommand searchResultGroupCommand = new SolrSearchResultGroupCommand();
        searchResultGroupCommand.setName(groupField);
        searchResultGroupCommand.setNumberOfGroups(numberOfResults);
        searchResultGroupCommand.setGroups(searchResultGroups);
        NestableJsonFacet facetingResponse = queryResponse.getJsonFacetingResponse();
        if(facetingResponse != null)
        {
            NestableJsonFacet documentCountFacet = facetingResponse.getQueryFacet("documentCount");
            if(documentCountFacet != null)
            {
                searchResultGroupCommand.setNumberOfMatches(documentCountFacet.getCount());
            }
        }
        target.setNumberOfResults(numberOfResults);
        target.setDocuments(documents);
        target.setSolrDocuments(solrDocuments);
        target.setGroupCommandResult((SearchResultGroupCommand)searchResultGroupCommand);
    }


    protected SearchResultGroup createSearchResulGroupForExpand(QueryResponse queryResponse, Document document, String groupField, Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos)
    {
        List<Document> groupDocuments = new ArrayList<>();
        Object groupValue = document.getFieldValue(groupField);
        groupDocuments.add(document);
        SolrDocumentList groupResults = (SolrDocumentList)queryResponse.getExpandedResults().get(groupValue);
        if(CollectionUtils.isNotEmpty((Collection)groupResults))
        {
            for(SolrDocument groupSolrDocument : groupResults)
            {
                groupDocuments.add(convertDocument(groupSolrDocument, invertedFieldInfos, queryResponse.getHighlighting()));
            }
        }
        SolrSearchResultGroup searchResultGroup = new SolrSearchResultGroup();
        searchResultGroup.setGroupValue((groupValue == null) ? null : groupValue.toString());
        searchResultGroup.setDocuments(groupDocuments);
        return (SearchResultGroup)searchResultGroup;
    }


    protected void populateUsingDefaultResponse(QueryResponse queryResponse, Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos, SolrSearchResult target)
    {
        SolrDocumentList results = queryResponse.getResults();
        long numberOfResults = results.getNumFound();
        List<Document> documents = new ArrayList<>();
        List<SolrDocument> solrDocuments = new ArrayList<>();
        if(CollectionUtils.isNotEmpty((Collection)results))
        {
            for(SolrDocument solrDocument : results)
            {
                Document document = convertDocument(solrDocument, invertedFieldInfos, queryResponse.getHighlighting());
                documents.add(document);
                solrDocuments.add(solrDocument);
            }
        }
        target.setNumberOfResults(numberOfResults);
        target.setDocuments(documents);
        target.setSolrDocuments(solrDocuments);
    }


    protected String resolveFieldName(String fieldName, Map<String, FieldNameTranslator.FieldInfo> fieldInfos)
    {
        FieldNameTranslator.FieldInfo invertedFieldInfo = fieldInfos.get(fieldName);
        if(invertedFieldInfo != null)
        {
            return invertedFieldInfo.getFieldName();
        }
        return fieldName;
    }


    protected Document convertDocument(SolrDocument solrDocument, Map<String, FieldNameTranslator.FieldInfo> invertedFieldInfos, Map<String, Map<String, List<String>>> highlighting)
    {
        DefaultDocument document = new DefaultDocument();
        Map<String, Object> documentFields = document.getFields();
        documentFields.putAll((Map<? extends String, ?>)solrDocument);
        replaceWithHighlightedFields(highlighting, documentFields);
        for(FieldNameTranslator.FieldInfo invertedFieldInfo : invertedFieldInfos.values())
        {
            Object fieldValue = documentFields.get(invertedFieldInfo.getTranslatedFieldName());
            if(fieldValue != null)
            {
                documentFields.put(invertedFieldInfo.getFieldName(), fieldValue);
                documentFields.remove(invertedFieldInfo.getTranslatedFieldName());
            }
        }
        return (Document)document;
    }


    protected void replaceWithHighlightedFields(Map<String, Map<String, List<String>>> highlighting, Map<String, Object> documentFields)
    {
        Object id = documentFields.get("id");
        if(MapUtils.isEmpty(highlighting) || id == null)
        {
            return;
        }
        Map<String, List<String>> highlightingForDoc = highlighting.get(id);
        if(MapUtils.isEmpty(highlightingForDoc))
        {
            return;
        }
        highlightingForDoc.entrySet().stream().forEach(highlight -> {
            if(documentFields.get(highlight.getKey()) != null && CollectionUtils.isNotEmpty((Collection)highlight.getValue()))
            {
                documentFields.put(highlight.getKey(), ((List)highlight.getValue()).get(0));
            }
        });
    }
}
