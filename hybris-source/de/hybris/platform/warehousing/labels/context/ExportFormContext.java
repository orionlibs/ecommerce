package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.data.shipping.ExportForm;
import de.hybris.platform.warehousing.data.shipping.ExportFormEntry;
import de.hybris.platform.warehousing.labels.strategy.ExportFormPriceStrategy;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExportFormContext extends CommonPrintLabelContext
{
    private ExportFormPriceStrategy exportFormPriceStrategy;
    private ExportForm exportForm;


    public void init(ConsignmentProcessModel businessProcessModel, DocumentPageModel documentPageModel)
    {
        super.init(businessProcessModel, documentPageModel);
        setExportForm(createExportFormContent(businessProcessModel.getConsignment()));
    }


    protected ExportForm createExportFormContent(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNull(consignment, "Consignment cannot be null");
        ExportForm exportFormContent = null;
        if(CollectionUtils.isNotEmpty(consignment.getConsignmentEntries()))
        {
            List<ExportFormEntry> exportFormEntries = (List<ExportFormEntry>)consignment.getConsignmentEntries().stream().map(this::createExportFormEntry).collect(Collectors.toList());
            BigDecimal totalPrice = exportFormEntries.stream().map(ExportFormEntry::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            exportFormContent = new ExportForm();
            exportFormContent.setFormEntries(exportFormEntries);
            exportFormContent.setTotalPrice(totalPrice);
        }
        return exportFormContent;
    }


    protected ExportFormEntry createExportFormEntry(ConsignmentEntryModel consignmentEntry)
    {
        ServicesUtil.validateParameterNotNull(consignmentEntry, "Consignment entry cannot be null");
        ExportFormEntry entry = new ExportFormEntry();
        entry.setConsignmentEntry(consignmentEntry);
        entry.setItemPrice(getExportFormPriceStrategy().calculateProductPrice(consignmentEntry));
        entry.setTotalPrice(getExportFormPriceStrategy().calculateTotalPrice(entry.getItemPrice(), consignmentEntry));
        return entry;
    }


    protected ExportFormPriceStrategy getExportFormPriceStrategy()
    {
        return this.exportFormPriceStrategy;
    }


    @Required
    public void setExportFormPriceStrategy(ExportFormPriceStrategy exportFormPriceStrategy)
    {
        this.exportFormPriceStrategy = exportFormPriceStrategy;
    }


    public ExportForm getExportForm()
    {
        return this.exportForm;
    }


    public void setExportForm(ExportForm exportForm)
    {
        this.exportForm = exportForm;
    }
}
