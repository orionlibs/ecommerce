package de.hybris.platform.impex.distributed;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.model.ImpexDocumentIdModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.util.CollectionUtils;

public class PersistableDocumentIDRegistry extends DocumentIDRegistry
{
    private static final String FIND_ITEMPK_QUERY = "SELECT {itemPK} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {docId}=?docid AND {resolved}=?resolved";
    private static final String FIND_ITEMPK_IN_PROCESS_QUERY = "SELECT {itemPK} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {docId}=?docid AND {resolved}=?resolved AND {processCode}=?processcode";
    private static final String FIND_DOCID_QUERY = "SELECT {docId} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {itemPK}=?itempk AND {resolved}=?resolved";
    private static final String FIND_DOCID_IN_PROCESS_QUERY = "SELECT {docId} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {itemPK}=?itempk AND {resolved}=?resolved AND {processCode}=?processcode";
    private static final String FIND_ALL_IMPEX_DOCUMENTIDS_QUERY = "SELECT {PK} FROM {ImpexDocumentId}";
    private static final String FIND_ALL_IMPEX_DOCUMENTIDS_IN_PROCESS_QUERY = "SELECT {PK} FROM {ImpexDocumentId} WHERE {processCode}=?processcode";
    private final String processCode;


    public PersistableDocumentIDRegistry(String processCode)
    {
        this.processCode = processCode;
    }


    public PersistableDocumentIDRegistry(CSVWriter documentIDWriter, String processCode)
    {
        super(documentIDWriter);
        this.processCode = processCode;
    }


    public PersistableDocumentIDRegistry(CSVReader documentIDReader, String processCode)
    {
        super(documentIDReader);
        this.processCode = processCode;
    }


    public PersistableDocumentIDRegistry(CSVReader documentIDReader, CSVWriter documentIDWriter, String processCode)
    {
        super(documentIDReader, documentIDWriter);
        this.processCode = processCode;
    }


    protected String getID(String qualifier, long pk, DocumentIDRegistry.MODE mode)
    {
        FlexibleSearchQuery fQuery = (this.processCode == null)
                        ? new FlexibleSearchQuery("SELECT {docId} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {itemPK}=?itempk AND {resolved}=?resolved")
                        : new FlexibleSearchQuery("SELECT {docId} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {itemPK}=?itempk AND {resolved}=?resolved AND {processCode}=?processcode", (Map)ImmutableMap.of("processcode", this.processCode));
        fQuery.addQueryParameter("qualifier", qualifier);
        fQuery.addQueryParameter("itempk", Long.valueOf(pk));
        fQuery.addQueryParameter("resolved", Boolean.valueOf(DocumentIDRegistry.MODE.RESOLVED.equals(mode)));
        List<String> result = getFlexibleSearchService().search(fQuery).getResult();
        return CollectionUtils.isEmpty(result) ? null : result.get(0);
    }


    protected long getPK(String qualifier, String documentID, DocumentIDRegistry.MODE mode)
    {
        FlexibleSearchQuery fQuery = (this.processCode == null)
                        ? new FlexibleSearchQuery("SELECT {itemPK} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {docId}=?docid AND {resolved}=?resolved")
                        : new FlexibleSearchQuery("SELECT {itemPK} FROM {ImpexDocumentId} WHERE {itemQualifier}=?qualifier AND {docId}=?docid AND {resolved}=?resolved AND {processCode}=?processcode", (Map)ImmutableMap.of("processcode", this.processCode));
        fQuery.addQueryParameter("qualifier", qualifier);
        fQuery.addQueryParameter("docid", documentID);
        fQuery.addQueryParameter("resolved", Boolean.valueOf(DocumentIDRegistry.MODE.RESOLVED.equals(mode)));
        fQuery.setResultClassList(Collections.singletonList(PK.class));
        List<PK> result = getFlexibleSearchService().search(fQuery).getResult();
        return CollectionUtils.isEmpty(result) ? -1L : ((PK)result.get(0)).getLongValue();
    }


    protected void addID(String qualifier, String documentID, long pk, DocumentIDRegistry.MODE mode) throws ImpExException
    {
        ModelService modelService = getModelService();
        ImpexDocumentIdModel docIdModel = (ImpexDocumentIdModel)modelService.create(ImpexDocumentIdModel.class);
        docIdModel.setItemQualifier(qualifier);
        docIdModel.setDocId(documentID);
        docIdModel.setResolved(Boolean.valueOf(DocumentIDRegistry.MODE.RESOLVED.equals(mode)));
        docIdModel.setItemPK(PK.fromLong(pk));
        docIdModel.setProcessCode(this.processCode);
        modelService.save(docIdModel);
        if(mode == DocumentIDRegistry.MODE.RESOLVED)
        {
            exportID(qualifier, documentID, pk);
        }
    }


    protected List<ImpexDocumentIdModel> getAllImpexDocumentIDs()
    {
        FlexibleSearchQuery fQuery = (this.processCode == null) ? new FlexibleSearchQuery("SELECT {PK} FROM {ImpexDocumentId}") : new FlexibleSearchQuery("SELECT {PK} FROM {ImpexDocumentId} WHERE {processCode}=?processcode", (Map)ImmutableMap.of("processcode", this.processCode));
        List<ImpexDocumentIdModel> result = getFlexibleSearchService().search(fQuery).getResult();
        return result;
    }


    public void clearAllDocumentIds()
    {
        List<ImpexDocumentIdModel> allImpexDocumentIDs = getAllImpexDocumentIDs();
        getModelService().removeAll(allImpexDocumentIDs);
    }


    protected Map<String, DualHashBidiMap<String, Long>> getQualifiersMap(DocumentIDRegistry.MODE mode)
    {
        return super.getQualifiersMap(mode);
    }


    private ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
    }


    private FlexibleSearchService getFlexibleSearchService()
    {
        return (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
    }
}
