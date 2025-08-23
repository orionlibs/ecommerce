package de.hybris.platform.warehousingbackoffice.dtos;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.enums.DeclineReason;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.zkoss.zul.ListModelArray;

public class ConsignmentEntryToReallocateDto
{
    private ConsignmentEntryModel consignmentEntry = null;
    private Long quantityToReallocate;
    private String declineConsignmentEntryComment;
    private DeclineReason selectedReason;
    private WarehouseModel selectedLocation;
    private ListModelArray declineReasonsModel = new ListModelArray(new ArrayList());
    private ListModelArray possibleLocationsModel = new ListModelArray(new ArrayList());


    public ConsignmentEntryToReallocateDto(ConsignmentEntryModel consignmentEntry, List<String> reasons, Set<WarehouseModel> locations)
    {
        this.consignmentEntry = consignmentEntry;
        this.quantityToReallocate = Long.valueOf(0L);
        this.declineReasonsModel = new ListModelArray(reasons);
        this.possibleLocationsModel = new ListModelArray(locations.toArray());
    }


    public ConsignmentEntryModel getConsignmentEntry()
    {
        return this.consignmentEntry;
    }


    public void setConsignmentEntry(ConsignmentEntryModel consignmentEntry)
    {
        this.consignmentEntry = consignmentEntry;
    }


    public Long getQuantityToReallocate()
    {
        return this.quantityToReallocate;
    }


    public void setQuantityToReallocate(Long quantityToReallocate)
    {
        this.quantityToReallocate = quantityToReallocate;
    }


    public ListModelArray getDeclineReasonsModel()
    {
        return this.declineReasonsModel;
    }


    public void setDeclineReasonsModel(ListModelArray declineReasonsModel)
    {
        this.declineReasonsModel = declineReasonsModel;
    }


    public ListModelArray getPossibleLocationsModel()
    {
        return this.possibleLocationsModel;
    }


    public void setPossibleLocationsModel(ListModelArray possibleLocationsModel)
    {
        this.possibleLocationsModel = possibleLocationsModel;
    }


    public DeclineReason getSelectedReason()
    {
        return this.selectedReason;
    }


    public void setSelectedReason(DeclineReason selectedReason)
    {
        this.selectedReason = selectedReason;
    }


    public WarehouseModel getSelectedLocation()
    {
        return this.selectedLocation;
    }


    public void setSelectedLocation(WarehouseModel selectedLocation)
    {
        this.selectedLocation = selectedLocation;
    }


    public String getDeclineConsignmentEntryComment()
    {
        return this.declineConsignmentEntryComment;
    }


    public void setDeclineConsignmentEntryComment(String declineConsignmentEntryComment)
    {
        this.declineConsignmentEntryComment = declineConsignmentEntryComment;
    }
}
