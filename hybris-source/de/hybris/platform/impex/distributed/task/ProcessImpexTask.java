package de.hybris.platform.impex.distributed.task;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.distributed.batch.BatchingImpExCRUDHandler;
import de.hybris.platform.impex.distributed.batch.ImportBatchHandler;
import de.hybris.platform.impex.distributed.batch.impl.BatchData;
import de.hybris.platform.impex.distributed.batch.impl.BatchingExistingItemResolver;
import de.hybris.platform.impex.distributed.batch.impl.CRUDHandlerFactory;
import de.hybris.platform.impex.distributed.batch.impl.ImportBatchParser;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.impex.ProcessMode;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ProcessImpexTask
{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessImpexTask.class);
    private SessionService sessionService;
    private CommonI18NService commonI18NService;
    private I18NService i18nService;
    private ModelService modelService;
    private ImportBatchParser importBatchParser;
    private CRUDHandlerFactory crudHandlerFactory;
    private FlexibleSearchService flexibleSearchService;
    private UserService userService;


    public void execute(ImportBatchHandler batchHandler)
    {
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, batchHandler), (UserModel)this.userService
                        .getAdminUser());
    }


    private void adjustSessionSettingsBasedOnBatch(BatchData batchData, ImportBatchHandler batchHandler)
    {
        HeaderDescriptor headerDescriptor = batchData.getHeaderDescriptor();
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.putAll(headerDescriptor.getInterceptorRelatedParameters());
        String sldFromHandler = batchHandler.getProperty("sld.enabled");
        String sldFromHeader = headerDescriptor.getDescriptorData().getModifier("sld.enabled");
        Stream.<String>of(new String[] {sldFromHandler, sldFromHeader}).filter(s -> (s != null)).forEach(s -> {
            boolean legacyPersistence = !Boolean.parseBoolean(s);
            sessionAttributes.put("persistence.legacy.mode", Boolean.valueOf(legacyPersistence));
        });
        sessionAttributes.forEach((k, v) -> this.sessionService.setAttribute(k, v));
    }


    private void process(BatchData batchData, ImportBatchHandler batchHandler)
    {
        ProcessMode processMode = batchData.getProcessMode();
        List<BatchData.ImportData> toProcess = new ArrayList<>(batchData.getImportData());
        if(processMode == ProcessMode.REMOVE)
        {
            batchData.addUnresolved(processByCRUDHandler(toProcess, batchHandler, getCRUDHandler(processMode)));
        }
        else
        {
            List<BatchData.ImportData> toForceUpdate = batchData.getResolvedLines();
            if(CollectionUtils.isNotEmpty(toForceUpdate))
            {
                BatchingImpExCRUDHandler updateHandler = getCRUDHandler(ProcessMode.UPDATE);
                batchData.addUnresolved(processByCRUDHandler(toForceUpdate, batchHandler, updateHandler));
                Iterables.removeAll(toProcess, toForceUpdate);
            }
            if(CollectionUtils.isNotEmpty(toProcess))
            {
                batchData.addUnresolved(processByCRUDHandler(toProcess, batchHandler, getCRUDHandler(processMode)));
            }
        }
    }


    private List<BatchData.ImportData> processByCRUDHandler(List<BatchData.ImportData> toProcess, ImportBatchHandler batchHandler, BatchingImpExCRUDHandler crudHandler)
    {
        if(isImportByLine(batchHandler))
        {
            return crudHandler.processByLine(toProcess);
        }
        try
        {
            return crudHandler.processInBulk(toProcess);
        }
        catch(Exception e)
        {
            LOG.debug("Error saving batch in bulk mode [reason: {}]. Will try line-by-line mode.", e.getMessage());
            toProcess.stream()
                            .forEach(d -> d.markUnresolved("Error saving batch in bulk mode [reason: " + e.getMessage() + "]. Will try line-by-line mode."));
            ((BatchData.ImportData)toProcess.get(0)).getBatchData().setSkipAfterEachProcessing();
            batchHandler.setProperty("import.by.line", "true");
            return toProcess;
        }
    }


    private void prepareImportDataToProcess(BatchData batchData)
    {
        BatchingExistingItemResolver itemResolver = createExistingItemResolverForBatch(batchData);
        for(BatchData.ImportData importData : batchData.getImportData())
        {
            ValueLine valueLine = importData.getValueLine();
            if(valueLine.isUnrecoverable())
            {
                continue;
            }
            PK processedItemPK = valueLine.getProcessedItemPK();
            if(processedItemPK == null)
            {
                List<ItemModel> existingItems = itemResolver.findExisting(importData);
                if(importData.isInsertMode() && CollectionUtils.isNotEmpty(existingItems))
                {
                    ItemModel itemModel = existingItems.get(0);
                    importData.markUnrecoverable(itemModel);
                    importData.getValueLine().setConflictingItemPK(itemModel.getPk());
                    continue;
                }
                importData.setExistingItemsAndResolve(existingItems);
                continue;
            }
            ItemModel processedItem = (ItemModel)this.modelService.get(processedItemPK);
            importData.setExistingItemsAndResolve(Lists.newArrayList((Object[])new ItemModel[] {processedItem}));
        }
    }


    private BatchingExistingItemResolver createExistingItemResolverForBatch(BatchData batchData)
    {
        return new BatchingExistingItemResolver(this.flexibleSearchService, batchData, this.i18nService, this.commonI18NService);
    }


    private boolean isImportByLine(ImportBatchHandler batchHandler)
    {
        String importByLineValue = batchHandler.getProperty("import.by.line");
        return Boolean.parseBoolean(importByLineValue);
    }


    private BatchingImpExCRUDHandler getCRUDHandler(ProcessMode processMode)
    {
        return this.crudHandlerFactory.getHandlerFor(processMode);
    }


    private void adjustSessionSettings()
    {
        this.sessionService.setAttribute("disableRestrictions", Boolean.TRUE);
        this.sessionService.setAttribute("disableRestrictionGroupInheritance", Boolean.TRUE);
        this.sessionService.setAttribute("use.fast.algorithms", Boolean.TRUE);
        this.sessionService.setAttribute("import.mode", Boolean.TRUE);
        this.sessionService.setAttribute("disable.attribute.check", Boolean.TRUE);
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setImportBatchParser(ImportBatchParser importBatchParser)
    {
        this.importBatchParser = importBatchParser;
    }


    @Required
    public void setCrudHandlerFactory(CRUDHandlerFactory crudHandlerFactory)
    {
        this.crudHandlerFactory = crudHandlerFactory;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
