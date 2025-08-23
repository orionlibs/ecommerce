package de.hybris.platform.warehousingbackoffice.dtos;

import de.hybris.platform.warehousing.enums.StockLevelAdjustmentReason;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.ListModelArray;

public class StockAdjustmentDto
{
    private StockLevelAdjustmentReason selectedReason;
    private String localizedStringReason;
    private Long quantity;
    private String comment;
    private Boolean underEdition;
    private ListModelArray stockAdjustmentReasonsModel = new ListModelArray(new ArrayList());


    public StockAdjustmentDto(List<String> reasons)
    {
        this.quantity = Long.valueOf(0L);
        this.underEdition = Boolean.TRUE;
        this.stockAdjustmentReasonsModel = new ListModelArray(reasons);
    }


    public ListModelArray getStockAdjustmentReasonsModel()
    {
        return this.stockAdjustmentReasonsModel;
    }


    public void setStockAdjustmentReasonsModel(ListModelArray stockAdjustmentReasonsModel)
    {
        this.stockAdjustmentReasonsModel = stockAdjustmentReasonsModel;
    }


    public StockLevelAdjustmentReason getSelectedReason()
    {
        return this.selectedReason;
    }


    public void setSelectedReason(StockLevelAdjustmentReason selectedReason)
    {
        this.selectedReason = selectedReason;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public String getComment()
    {
        return this.comment;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public Boolean getUnderEdition()
    {
        return this.underEdition;
    }


    public void setUnderEdition(Boolean underEdition)
    {
        this.underEdition = underEdition;
    }


    public String getLocalizedStringReason()
    {
        return this.localizedStringReason;
    }


    public void setLocalizedStringReason(String localizedStringReason)
    {
        this.localizedStringReason = localizedStringReason;
    }
}
