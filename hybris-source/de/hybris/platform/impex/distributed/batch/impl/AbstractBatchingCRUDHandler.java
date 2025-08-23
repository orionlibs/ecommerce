package de.hybris.platform.impex.distributed.batch.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.distributed.batch.BatchingImpExCRUDHandler;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.DocumentIDColumnDescriptor;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ProcessMode;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBatchingCRUDHandler implements BatchingImpExCRUDHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBatchingCRUDHandler.class);
    private final ModelService modelService;
    private final UserService userService;
    private final SessionService sessionService;


    protected AbstractBatchingCRUDHandler(ModelService modelService, UserService userService, SessionService sessionService)
    {
        this.modelService = modelService;
        this.userService = userService;
        this.sessionService = sessionService;
    }


    public List<BatchData.ImportData> processInBulk(List<BatchData.ImportData> importDatas)
    {
        Map<BatchData.ImportData, ItemModel> toProcess = new HashMap<>();
        Set<BatchData.ImportData> unresolved = process(importDatas, (model, importData, _unresolved) -> toProcess.put(importData, model));
        Map<String, Object> sessionAttributes = getSessionAttributes(toProcess.values());
        this.sessionService.executeInLocalViewWithParams(sessionAttributes, (SessionExecutionBody)new Object(this, toProcess));
        return (List<BatchData.ImportData>)ImmutableList.copyOf(unresolved);
    }


    public List<BatchData.ImportData> processByLine(List<BatchData.ImportData> importDatas)
    {
        AtomicInteger numSaved = new AtomicInteger(0);
        Set<BatchData.ImportData> result = process(importDatas, (model, importData, unresolved) -> {
            try
            {
                Map<String, Object> sessionAttributes = getSessionAttributes(Collections.singletonList(model));
                this.sessionService.executeInLocalViewWithParams(sessionAttributes, (SessionExecutionBody)new Object(this, importData, model));
                numSaved.incrementAndGet();
            }
            catch(Exception e)
            {
                LOG.debug("Cannot import line for {} - added to dump", importData);
                importData.markUnresolved(e.getMessage());
                unresolved.add(importData);
            }
        });
        LOG.info("Number of models saved {} out of {}", Integer.valueOf(numSaved.get()), Integer.valueOf(importDatas.size()));
        return (List<BatchData.ImportData>)ImmutableList.copyOf(result);
    }


    protected Map<String, Object> getSessionAttributes(Collection<ItemModel> models)
    {
        return Collections.emptyMap();
    }


    protected void bulkCommit(Map<BatchData.ImportData, ItemModel> itemModels)
    {
        if(itemModels.isEmpty())
        {
            return;
        }
        Collection<ItemModel> toSave = itemModels.values();
        getModelService().saveAll(toSave);
        LOG.info("Number of models saved in bulk: {}", Integer.valueOf(toSave.size()));
        for(Map.Entry<BatchData.ImportData, ItemModel> entry : itemModels.entrySet())
        {
            BatchData.ImportData importData = entry.getKey();
            ItemModel itemModel = entry.getValue();
            importData.setExistingItemsAndResolve(Lists.newArrayList((Object[])new ItemModel[] {itemModel}));
            registerDocumentIds(importData, itemModel);
            handleSpecialColumns(importData, itemModel);
        }
    }


    protected void commit(BatchData.ImportData importData, ItemModel itemModel)
    {
        getModelService().save(itemModel);
        importData.setExistingItemsAndResolve(Lists.newArrayList((Object[])new ItemModel[] {itemModel}));
        registerDocumentIds(importData, itemModel);
        handleSpecialColumns(importData, itemModel);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected void fillModel(ItemModel itemModel, Map<StandardColumnDescriptor, Object> currentModeValues, ProcessMode processMode)
    {
        for(Map.Entry<StandardColumnDescriptor, Object> entry : currentModeValues.entrySet())
        {
            StandardColumnDescriptor desc = entry.getKey();
            Object translatedValue = entry.getValue();
            if(processMode == ProcessMode.UPDATE && desc.isInitialOnly())
            {
                LOG.debug("Descriptor {} is initial only but the current process mode is UPDATE, skipping value {}", desc, translatedValue);
                continue;
            }
            if(desc.isLocalized())
            {
                setLocalizedModelValue(itemModel, desc.getQualifier(), translatedValue);
                continue;
            }
            setModelValue(itemModel, desc.getQualifier(), translatedValue);
        }
    }


    protected void setModelValue(ItemModel model, String attribute, Object value)
    {
        if(model instanceof de.hybris.platform.core.model.link.LinkModel && value instanceof PK)
        {
            this.modelService.setAttributeValue(model, attribute, this.modelService.get((PK)value));
        }
        else if(model instanceof UserModel && "password".equalsIgnoreCase(attribute))
        {
            this.userService.setPassword((UserModel)model, (String)value, "*");
        }
        else
        {
            this.modelService.setAttributeValue(model, attribute, this.modelService.toModelLayer(value));
        }
    }


    protected void setLocalizedModelValue(ItemModel model, String attribute, Object value)
    {
        Map<LanguageModel, Object> combinedSLValues = (Map<LanguageModel, Object>)this.modelService.toModelLayer(value);
        this.modelService.setAttributeValue(model, attribute, combinedSLValues);
    }


    protected void registerDocumentIds(BatchData.ImportData importData, ItemModel itemModel)
    {
        for(DocumentIDColumnDescriptor desc : importData.getCurrentHeader()
                        .getSpecificColumns(DocumentIDColumnDescriptor.class))
        {
            ValueLine.ValueEntry valueEntry = importData.getValueLine().getValueEntry(desc.getValuePosition());
            try
            {
                desc.registerIDForItem(valueEntry.getCellValue(), itemModel.getPk());
            }
            catch(ImpExException e)
            {
                throw new SystemException(e);
            }
        }
    }


    protected void handleSpecialColumns(BatchData.ImportData importData, ItemModel itemModel)
    {
        for(SpecialColumnDescriptor desc : importData.getCurrentHeader().getSpecificColumns(SpecialColumnDescriptor.class))
        {
            ValueLine.ValueEntry valueEntry = importData.getValueLine().getValueEntry(desc.getValuePosition());
            try
            {
                desc.performImport((valueEntry != null) ? valueEntry.getCellValue() : null, itemModel.getPk());
            }
            catch(ImpExException e)
            {
                throw new SystemException(e);
            }
        }
        notifySpecialColumns(importData, itemModel);
    }


    protected void notifySpecialColumns(BatchData.ImportData importData, ItemModel itemModel)
    {
        for(SpecialColumnDescriptor desc : importData.getCurrentHeader().getSpecificColumns(SpecialColumnDescriptor.class))
        {
            try
            {
                desc.notifyTranslationEnd(importData.getValueLine(), importData.getCurrentHeader(), itemModel.getPk());
            }
            catch(ImpExException e)
            {
                throw new SystemException(e);
            }
        }
    }


    protected abstract Set<BatchData.ImportData> process(Collection<BatchData.ImportData> paramCollection, TriConsumer<ItemModel, BatchData.ImportData, Set<BatchData.ImportData>> paramTriConsumer);
}
