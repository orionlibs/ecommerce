package de.hybris.platform.b2bapprovalprocessfacades.company.data;

import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import java.io.Serializable;

public class B2BPermissionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private B2BPermissionTypeData b2BPermissionTypeData;
    private String code;
    private String normalizedId;
    private String originalCode;
    private Double value;
    private String timeSpan;
    private boolean selected;
    private boolean active;
    private B2BUnitData unit;
    private CurrencyData currency;
    private B2BPeriodRange periodRange;


    public void setB2BPermissionTypeData(B2BPermissionTypeData b2BPermissionTypeData)
    {
        this.b2BPermissionTypeData = b2BPermissionTypeData;
    }


    public B2BPermissionTypeData getB2BPermissionTypeData()
    {
        return this.b2BPermissionTypeData;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setNormalizedId(String normalizedId)
    {
        this.normalizedId = normalizedId;
    }


    public String getNormalizedId()
    {
        return this.normalizedId;
    }


    public void setOriginalCode(String originalCode)
    {
        this.originalCode = originalCode;
    }


    public String getOriginalCode()
    {
        return this.originalCode;
    }


    public void setValue(Double value)
    {
        this.value = value;
    }


    public Double getValue()
    {
        return this.value;
    }


    public void setTimeSpan(String timeSpan)
    {
        this.timeSpan = timeSpan;
    }


    public String getTimeSpan()
    {
        return this.timeSpan;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setUnit(B2BUnitData unit)
    {
        this.unit = unit;
    }


    public B2BUnitData getUnit()
    {
        return this.unit;
    }


    public void setCurrency(CurrencyData currency)
    {
        this.currency = currency;
    }


    public CurrencyData getCurrency()
    {
        return this.currency;
    }


    public void setPeriodRange(B2BPeriodRange periodRange)
    {
        this.periodRange = periodRange;
    }


    public B2BPeriodRange getPeriodRange()
    {
        return this.periodRange;
    }
}
