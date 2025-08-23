package de.hybris.platform.warehousing.asn.service.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.asn.dao.AsnDao;
import de.hybris.platform.warehousing.asn.service.AsnService;
import de.hybris.platform.warehousing.asn.service.AsnWorkflowService;
import de.hybris.platform.warehousing.asn.strategy.AsnReleaseDateStrategy;
import de.hybris.platform.warehousing.asn.strategy.BinSelectionStrategy;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import de.hybris.platform.warehousing.stock.services.WarehouseStockService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultAsnService implements AsnService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAsnService.class);
    private ModelService modelService;
    private BinSelectionStrategy binSelectionStrategy;
    private AsnReleaseDateStrategy asnReleaseDateStrategy;
    private WarehouseStockService warehouseStockService;
    private AsnDao asnDao;
    private AsnWorkflowService asnWorkflowService;


    public void processAsn(AdvancedShippingNoticeModel asn)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("asn", asn);
        Assert.isTrue(CollectionUtils.isNotEmpty(asn.getAsnEntries()),
                        String.format("No entries found in given ASN: [%s]", new Object[] {asn.getInternalId()}));
        WarehouseModel warehouse = asn.getWarehouse();
        asn.getAsnEntries().forEach(asnEntry -> createStockLevel(asnEntry, warehouse, getAsnReleaseDateStrategy().getReleaseDateForStockLevel(asnEntry)));
    }


    public AdvancedShippingNoticeModel confirmAsnReceipt(String internalId)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("internalId", internalId);
        AdvancedShippingNoticeModel asn = getAsnForInternalId(internalId);
        if(!AsnStatus.CREATED.equals(asn.getStatus()))
        {
            throw new IllegalArgumentException("Only ASN in CREATED status can be updated as RECEIVED");
        }
        asn.setStatus(AsnStatus.RECEIVED);
        getModelService().save(asn);
        LOGGER.info("ASN identified by InternalID: {} has been received", asn.getInternalId());
        return asn;
    }


    public AdvancedShippingNoticeModel getAsnForInternalId(String internalId)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("internalId", internalId);
        return getAsnDao().getAsnForInternalId(internalId);
    }


    public List<StockLevelModel> getStockLevelsForAsn(AdvancedShippingNoticeModel advancedShippingNotice)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("advancedShippingNotice", advancedShippingNotice);
        return getAsnDao().getStockLevelsForAsn(advancedShippingNotice);
    }


    public AdvancedShippingNoticeModel cancelAsn(String internalId)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("internalId", internalId);
        AdvancedShippingNoticeModel asn = getAsnForInternalId(internalId);
        if(!AsnStatus.CREATED.equals(asn.getStatus()))
        {
            throw new IllegalArgumentException("Only ASN in CREATED status can be cancelled");
        }
        asn.setStatus(AsnStatus.CANCELLED);
        getModelService().save(asn);
        getAsnWorkflowService().startAsnCancellationWorkflow(asn);
        LOGGER.info("ASN identified by InternalID: {} has been cancelled", asn.getInternalId());
        return asn;
    }


    protected void createStockLevel(AdvancedShippingNoticeEntryModel asnEntry, WarehouseModel warehouse, Date releaseDate)
    {
        Map<String, Integer> bins = getBinSelectionStrategy().getBinsForAsnEntry(asnEntry);
        if(bins != null)
        {
            bins.entrySet().forEach(entry -> createStockLevel(asnEntry, warehouse, ((Integer)entry.getValue()).intValue(), releaseDate, (String)entry.getKey()));
        }
    }


    protected void createStockLevel(AdvancedShippingNoticeEntryModel asnEntry, WarehouseModel warehouse, int productQuantity, Date releaseDate, String bin)
    {
        if(StringUtils.isNotEmpty(asnEntry.getProductCode()))
        {
            StockLevelModel stockLevel = getWarehouseStockService().createStockLevel(asnEntry.getProductCode(), warehouse, productQuantity, InStockStatus.NOTSPECIFIED, releaseDate, bin);
            stockLevel.setAsnEntry(asnEntry);
            getModelService().save(stockLevel);
        }
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected BinSelectionStrategy getBinSelectionStrategy()
    {
        return this.binSelectionStrategy;
    }


    @Required
    public void setBinSelectionStrategy(BinSelectionStrategy binSelectionStrategy)
    {
        this.binSelectionStrategy = binSelectionStrategy;
    }


    protected AsnReleaseDateStrategy getAsnReleaseDateStrategy()
    {
        return this.asnReleaseDateStrategy;
    }


    @Required
    public void setAsnReleaseDateStrategy(AsnReleaseDateStrategy asnReleaseDateStrategy)
    {
        this.asnReleaseDateStrategy = asnReleaseDateStrategy;
    }


    protected WarehouseStockService getWarehouseStockService()
    {
        return this.warehouseStockService;
    }


    @Required
    public void setWarehouseStockService(WarehouseStockService warehouseStockService)
    {
        this.warehouseStockService = warehouseStockService;
    }


    protected AsnDao getAsnDao()
    {
        return this.asnDao;
    }


    @Required
    public void setAsnDao(AsnDao asnDao)
    {
        this.asnDao = asnDao;
    }


    protected AsnWorkflowService getAsnWorkflowService()
    {
        return this.asnWorkflowService;
    }


    @Required
    public void setAsnWorkflowService(AsnWorkflowService asnWorkflowService)
    {
        this.asnWorkflowService = asnWorkflowService;
    }
}
