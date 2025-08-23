package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OrderApprovalPermission", description = "Request body fields required and optional to operate on Order Approval Permission data.")
public class B2BPermissionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "active", value = "Boolean flag of whether Order Approval Permission is active", example = "true")
    private Boolean active;
    @ApiModelProperty(name = "orderApprovalPermissionType", value = "Type of the Order Approval Permission")
    private B2BPermissionTypeWsDTO orderApprovalPermissionType;
    @ApiModelProperty(name = "code", value = "Code of the Order Approval Permission", required = true, example = "Rustic_10K_USD_MONTH")
    private String code;
    @ApiModelProperty(name = "currency", value = "Currency of the Order Approval Permission, used for type B2BOrderThresholdPermission and B2BOrderThresholdTimespanPermission")
    private CurrencyWsDTO currency;
    @ApiModelProperty(name = "periodRange", value = "Period range of the Order Approval Permission, used for type B2BOrderThresholdTimespanPermission", example = "MONTH")
    private B2BPeriodRangeWsDTO periodRange;
    @ApiModelProperty(name = "orgUnit", value = "Order Approval Permission's organizational unit")
    private B2BUnitWsDTO orgUnit;
    @ApiModelProperty(name = "threshold", value = "Threshold value of the Order Approval Permission, used for type B2BOrderThresholdPermission and B2BOrderThresholdTimespanPermission", example = "10000")
    private Double threshold;
    @ApiModelProperty(name = "selected", value = "Boolean flag of whether the user is selected", example = "true")
    private Boolean selected;


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setOrderApprovalPermissionType(B2BPermissionTypeWsDTO orderApprovalPermissionType)
    {
        this.orderApprovalPermissionType = orderApprovalPermissionType;
    }


    public B2BPermissionTypeWsDTO getOrderApprovalPermissionType()
    {
        return this.orderApprovalPermissionType;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCurrency(CurrencyWsDTO currency)
    {
        this.currency = currency;
    }


    public CurrencyWsDTO getCurrency()
    {
        return this.currency;
    }


    public void setPeriodRange(B2BPeriodRangeWsDTO periodRange)
    {
        this.periodRange = periodRange;
    }


    public B2BPeriodRangeWsDTO getPeriodRange()
    {
        return this.periodRange;
    }


    public void setOrgUnit(B2BUnitWsDTO orgUnit)
    {
        this.orgUnit = orgUnit;
    }


    public B2BUnitWsDTO getOrgUnit()
    {
        return this.orgUnit;
    }


    public void setThreshold(Double threshold)
    {
        this.threshold = threshold;
    }


    public Double getThreshold()
    {
        return this.threshold;
    }


    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }


    public Boolean getSelected()
    {
        return this.selected;
    }
}
