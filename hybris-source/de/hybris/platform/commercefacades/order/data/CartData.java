package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.acceleratorservices.enums.ImportStatus;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCommentData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.quote.data.QuoteData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import java.util.Date;
import java.util.List;

public class CartData extends AbstractOrderData
{
    private List<PromotionResultData> potentialOrderPromotions;
    private List<PromotionResultData> potentialProductPromotions;
    private Date saveTime;
    private PrincipalData savedBy;
    private QuoteData quoteData;
    private ImportStatus importStatus;
    private B2BCostCenterData costCenter;
    private B2BPaymentTypeData paymentType;
    private String purchaseOrderNumber;
    private B2BCommentData b2BComment;
    private CustomerData b2bCustomerData;
    private Boolean quoteAllowed;
    private boolean shipByGround;
    private boolean heavyOrder;
    private HeavyOrderQuestionsCartData heavyOrderQuestions;
    private String displayTotalUnitCount;


    public void setPotentialOrderPromotions(List<PromotionResultData> potentialOrderPromotions)
    {
        this.potentialOrderPromotions = potentialOrderPromotions;
    }


    public List<PromotionResultData> getPotentialOrderPromotions()
    {
        return this.potentialOrderPromotions;
    }


    public void setPotentialProductPromotions(List<PromotionResultData> potentialProductPromotions)
    {
        this.potentialProductPromotions = potentialProductPromotions;
    }


    public List<PromotionResultData> getPotentialProductPromotions()
    {
        return this.potentialProductPromotions;
    }


    public void setSaveTime(Date saveTime)
    {
        this.saveTime = saveTime;
    }


    public Date getSaveTime()
    {
        return this.saveTime;
    }


    public void setSavedBy(PrincipalData savedBy)
    {
        this.savedBy = savedBy;
    }


    public PrincipalData getSavedBy()
    {
        return this.savedBy;
    }


    public void setQuoteData(QuoteData quoteData)
    {
        this.quoteData = quoteData;
    }


    public QuoteData getQuoteData()
    {
        return this.quoteData;
    }


    public void setImportStatus(ImportStatus importStatus)
    {
        this.importStatus = importStatus;
    }


    public ImportStatus getImportStatus()
    {
        return this.importStatus;
    }


    public void setCostCenter(B2BCostCenterData costCenter)
    {
        this.costCenter = costCenter;
    }


    public B2BCostCenterData getCostCenter()
    {
        return this.costCenter;
    }


    public void setPaymentType(B2BPaymentTypeData paymentType)
    {
        this.paymentType = paymentType;
    }


    public B2BPaymentTypeData getPaymentType()
    {
        return this.paymentType;
    }


    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber()
    {
        return this.purchaseOrderNumber;
    }


    public void setB2BComment(B2BCommentData b2BComment)
    {
        this.b2BComment = b2BComment;
    }


    public B2BCommentData getB2BComment()
    {
        return this.b2BComment;
    }


    public void setB2bCustomerData(CustomerData b2bCustomerData)
    {
        this.b2bCustomerData = b2bCustomerData;
    }


    public CustomerData getB2bCustomerData()
    {
        return this.b2bCustomerData;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public void setQuoteAllowed(Boolean quoteAllowed)
    {
        this.quoteAllowed = quoteAllowed;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public Boolean getQuoteAllowed()
    {
        return this.quoteAllowed;
    }


    public void setShipByGround(boolean shipByGround)
    {
        this.shipByGround = shipByGround;
    }


    public boolean isShipByGround()
    {
        return this.shipByGround;
    }


    public void setHeavyOrder(boolean heavyOrder)
    {
        this.heavyOrder = heavyOrder;
    }


    public boolean isHeavyOrder()
    {
        return this.heavyOrder;
    }


    public void setHeavyOrderQuestions(HeavyOrderQuestionsCartData heavyOrderQuestions)
    {
        this.heavyOrderQuestions = heavyOrderQuestions;
    }


    public HeavyOrderQuestionsCartData getHeavyOrderQuestions()
    {
        return this.heavyOrderQuestions;
    }


    public void setDisplayTotalUnitCount(String displayTotalUnitCount)
    {
        this.displayTotalUnitCount = displayTotalUnitCount;
    }


    public String getDisplayTotalUnitCount()
    {
        return this.displayTotalUnitCount;
    }
}
