package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.acceleratorservices.document.context.AbstractDocumentContext;
import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.warehousing.data.pickslip.ConsolidatedPickSlipFormEntry;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.model.InventoryEventModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.springframework.beans.factory.annotation.Required;

public class ConsolidatedPickSlipContext extends AbstractDocumentContext<BusinessProcessModel>
{
    private List<ConsolidatedPickSlipFormEntry> formEntries;
    private InventoryEventService inventoryEventService;
    private List<ConsignmentModel> consignmentList;
    private Comparator<ConsolidatedPickSlipFormEntry> consolidatedPickSlipComparator;
    private DateTool date;
    private EscapeTool escapeTool;


    public void init(BusinessProcessModel businessProcessModel, DocumentPageModel documentPageModel)
    {
        super.init(businessProcessModel, documentPageModel);
        this.consignmentList = extractConsignments(businessProcessModel);
        if(CollectionUtils.isNotEmpty(this.consignmentList))
        {
            List<ConsignmentEntryModel> consignmentEntries = (List<ConsignmentEntryModel>)this.consignmentList.stream().flatMap(c -> c.getConsignmentEntries().stream()).collect(Collectors.toList());
            setFormEntries(createPickSlipFormEntries(consignmentEntries));
        }
        this.date = new DateTool();
        this.escapeTool = new EscapeTool();
    }


    public String escapeHtml(String stringToEscape)
    {
        return this.escapeTool.html(stringToEscape);
    }


    protected List<ConsolidatedPickSlipFormEntry> createPickSlipFormEntries(List<ConsignmentEntryModel> consignmentEntries)
    {
        List<ConsolidatedPickSlipFormEntry> formEntryList = new ArrayList<>();
        List<AllocationEventModel> allocationEvents = (List<AllocationEventModel>)consignmentEntries.stream().map(this::extractAllocationEvents).flatMap(Collection::stream).collect(Collectors.toList());
        Map<ProductModel, Map<String, List<AllocationEventModel>>> eventMapMap = (Map<ProductModel, Map<String, List<AllocationEventModel>>>)allocationEvents.stream()
                        .collect(Collectors.groupingBy(entry -> entry.getConsignmentEntry().getOrderEntry().getProduct(), Collectors.groupingBy(this::extractBin)));
        for(Map.Entry<ProductModel, Map<String, List<AllocationEventModel>>> entry : eventMapMap.entrySet())
        {
            ProductModel product = entry.getKey();
            Map<String, List<AllocationEventModel>> binsEntries = entry.getValue();
            for(Map.Entry<String, List<AllocationEventModel>> binEntries : binsEntries.entrySet())
            {
                ConsolidatedPickSlipFormEntry formEntry = new ConsolidatedPickSlipFormEntry();
                List<AllocationEventModel> entries = binEntries.getValue();
                formEntry.setProduct(product);
                formEntry.setBin(binEntries.getKey());
                formEntry.setQuantity(Long.valueOf(entries.stream().mapToLong(InventoryEventModel::getQuantity).sum()));
                formEntry.setAllocationEvents(entries);
                formEntryList.add(formEntry);
            }
        }
        formEntryList.sort(getConsolidatedPickSlipComparator());
        return formEntryList;
    }


    protected String extractBin(AllocationEventModel allocationEventModel)
    {
        return (allocationEventModel.getStockLevel().getBin() != null) ? allocationEventModel.getStockLevel().getBin() : "";
    }


    protected List<ConsignmentModel> extractConsignments(BusinessProcessModel businessProcessModel)
    {
        List<ConsignmentModel> consignments = new ArrayList<>();
        if(businessProcessModel.getContextParameters().iterator().hasNext())
        {
            BusinessProcessParameterModel param = businessProcessModel.getContextParameters().iterator().next();
            if("ConsolidatedConsignmentModels".equals(param.getName()))
            {
                consignments.addAll((List)param.getValue());
            }
        }
        return consignments;
    }


    protected BaseSiteModel getSite(BusinessProcessModel businessProcessModel)
    {
        List<ConsignmentModel> consignments = extractConsignments(businessProcessModel);
        return CollectionUtils.isNotEmpty(consignments) ? ((ConsignmentModel)consignments.iterator().next()).getOrder().getSite() : null;
    }


    protected LanguageModel getDocumentLanguage(BusinessProcessModel businessProcessModel)
    {
        BaseSiteModel baseSite = getSite(businessProcessModel);
        return (baseSite != null) ? baseSite.getDefaultLanguage() : null;
    }


    protected Collection<AllocationEventModel> extractAllocationEvents(ConsignmentEntryModel consignmentEntryModel)
    {
        Collection<AllocationEventModel> allocationEvents = getInventoryEventService().getAllocationEventsForConsignmentEntry(consignmentEntryModel);
        return allocationEvents;
    }


    public String getProductImageURL(ProductModel productModel)
    {
        return (productModel.getThumbnail() != null) ? productModel.getThumbnail().getDownloadURL() : "";
    }


    public int getRowQuantity(int columns)
    {
        return (columns != 0) ? ((int)Math.ceil(this.consignmentList.size() / columns) - 1) : 0;
    }


    public DateTool getDate()
    {
        return this.date;
    }


    public List<ConsolidatedPickSlipFormEntry> getFormEntries()
    {
        return this.formEntries;
    }


    public void setFormEntries(List<ConsolidatedPickSlipFormEntry> formEntries)
    {
        this.formEntries = formEntries;
    }


    public List<ConsignmentModel> getConsignmentList()
    {
        return this.consignmentList;
    }


    public void setConsignmentList(List<ConsignmentModel> consignmentList)
    {
        this.consignmentList = consignmentList;
    }


    protected Comparator<ConsolidatedPickSlipFormEntry> getConsolidatedPickSlipComparator()
    {
        return this.consolidatedPickSlipComparator;
    }


    @Required
    public void setConsolidatedPickSlipComparator(Comparator<ConsolidatedPickSlipFormEntry> consolidatedPickSlipComparator)
    {
        this.consolidatedPickSlipComparator = consolidatedPickSlipComparator;
    }


    protected InventoryEventService getInventoryEventService()
    {
        return this.inventoryEventService;
    }


    @Required
    public void setInventoryEventService(InventoryEventService inventoryEventService)
    {
        this.inventoryEventService = inventoryEventService;
    }
}
