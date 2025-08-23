package com.hybris.backoffice.solrsearch.events;

import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class CronJobSolrIndexSynchronizationStrategy implements SolrIndexSynchronizationStrategy
{
    private static final String BACKOFFICESOLRSEARCH_UPDATE_MODIFIED_ITEM_ENABLED = "backofficesolrsearch.item.updateModified";
    protected ModelService modelService;


    public void updateItem(String typecode, long pk)
    {
        updateItems(typecode, Collections.singletonList(PK.fromLong(pk)));
    }


    public void updateItems(String typecode, List<PK> pkList)
    {
        if(shouldUpdateModifiedItem())
        {
            addModifiedItems(typecode, pkList, SolrItemModificationType.UPDATE);
        }
    }


    public void removeItem(String typecode, long pk)
    {
        addModifiedItem(typecode, pk, SolrItemModificationType.DELETE);
    }


    public void removeItems(String typecode, List<PK> pkList)
    {
        addModifiedItems(typecode, pkList, SolrItemModificationType.DELETE);
    }


    protected void addModifiedItem(String typecode, long pk, SolrItemModificationType modificationType)
    {
        PK pkInstance = PK.fromLong(pk);
        if(pkInstance != null)
        {
            SolrModifiedItemModel modifiedItem = createSolrModifiedItem(typecode, pkInstance, modificationType);
            this.modelService.save(modifiedItem);
        }
    }


    private SolrModifiedItemModel createSolrModifiedItem(String typecode, PK pk, SolrItemModificationType modificationType)
    {
        SolrModifiedItemModel modifiedItem = (SolrModifiedItemModel)getModelService().create(SolrModifiedItemModel.class);
        modifiedItem.setModificationType(modificationType);
        modifiedItem.setModifiedPk(pk.getLong());
        modifiedItem.setModifiedTypeCode(typecode);
        return modifiedItem;
    }


    protected void addModifiedItems(String typecode, List<PK> pks, SolrItemModificationType modificationType)
    {
        List<SolrModifiedItemModel> modifiedItems = (List<SolrModifiedItemModel>)pks.stream().map(pk -> createSolrModifiedItem(typecode, pk, modificationType)).collect(Collectors.toList());
        getModelService().saveAll(modifiedItems);
    }


    protected boolean shouldUpdateModifiedItem()
    {
        return Config.getBoolean("backofficesolrsearch.item.updateModified", false);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
