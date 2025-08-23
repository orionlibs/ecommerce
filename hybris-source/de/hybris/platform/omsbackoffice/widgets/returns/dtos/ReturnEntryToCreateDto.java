package de.hybris.platform.omsbackoffice.widgets.returns.dtos;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.ListModelArray;

public class ReturnEntryToCreateDto
{
    private RefundEntryModel refundEntry = null;
    private int returnableQuantity;
    private int quantityToReturn;
    private String refundEntryComment;
    private boolean discountApplied;
    private BigDecimal tax;
    private ListModelArray reasonsModel = new ListModelArray(new ArrayList());


    public ReturnEntryToCreateDto(AbstractOrderEntryModel orderEntry, int returnableQuantity, List<String> reasons)
    {
        RefundEntryModel defaultRefundEntry = new RefundEntryModel();
        defaultRefundEntry.setOrderEntry(orderEntry);
        defaultRefundEntry.setAmount(BigDecimal.ZERO);
        setRefundEntry(defaultRefundEntry);
        setReturnableQuantity(returnableQuantity);
        setQuantityToReturn(0);
        setReasonsModel(new ListModelArray(reasons));
        setDiscountApplied((orderEntry.getDiscountValues() != null && orderEntry.getDiscountValues().size() > 0));
    }


    public RefundEntryModel getRefundEntry()
    {
        return this.refundEntry;
    }


    public void setRefundEntry(RefundEntryModel refundEntry)
    {
        this.refundEntry = refundEntry;
    }


    public String getRefundEntryComment()
    {
        return this.refundEntryComment;
    }


    public void setRefundEntryComment(String refundEntryComment)
    {
        this.refundEntryComment = refundEntryComment;
    }


    public ListModelArray getReasonsModel()
    {
        return this.reasonsModel;
    }


    public void setReasonsModel(ListModelArray reasonsModel)
    {
        this.reasonsModel = reasonsModel;
    }


    public int getReturnableQuantity()
    {
        return this.returnableQuantity;
    }


    public void setReturnableQuantity(int returnableQuantity)
    {
        this.returnableQuantity = returnableQuantity;
    }


    public int getQuantityToReturn()
    {
        return this.quantityToReturn;
    }


    public void setQuantityToReturn(int quantityToReturn)
    {
        this.quantityToReturn = quantityToReturn;
    }


    public boolean isDiscountApplied()
    {
        return this.discountApplied;
    }


    public void setDiscountApplied(boolean discountApplied)
    {
        this.discountApplied = discountApplied;
    }


    public BigDecimal getTax()
    {
        return this.tax;
    }


    public void setTax(BigDecimal tax)
    {
        this.tax = tax;
    }
}
