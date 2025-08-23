package com.hybris.cis.api.model;

import com.hybris.cis.api.validation.XSSSafe;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisLineItem
{
    @XmlAttribute(name = "id", required = true)
    private Integer id;
    @XmlElement(name = "itemCode", required = true)
    @XSSSafe
    private String itemCode;
    @XmlElement(name = "taxCode")
    @XSSSafe
    private String taxCode;
    @XmlElement(name = "quantity", required = true)
    private Integer quantity;
    @XmlElement(name = "unitPrice", required = true)
    private BigDecimal unitPrice = BigDecimal.ZERO;
    @XmlElement(name = "productDescription", required = true)
    @XSSSafe
    private String productDescription;
    @XmlElement(name = "vendorParameters")
    @Valid
    private AnnotationHashMap vendorParameters;


    public String getProductDescription()
    {
        return this.productDescription;
    }


    public void setProductDescription(String productDescription)
    {
        this.productDescription = productDescription;
    }


    public Integer getQuantity()
    {
        return this.quantity;
    }


    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }


    public Integer getId()
    {
        return this.id;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }


    public BigDecimal getUnitPrice()
    {
        return this.unitPrice;
    }


    public void setUnitPrice(BigDecimal total)
    {
        this.unitPrice = total;
    }


    public String getItemCode()
    {
        return this.itemCode;
    }


    public void setItemCode(String itemCode)
    {
        this.itemCode = itemCode;
    }


    public String getTaxCode()
    {
        return this.taxCode;
    }


    public void setTaxCode(String taxCode)
    {
        this.taxCode = taxCode;
    }


    public AnnotationHashMap getVendorParameters()
    {
        return this.vendorParameters;
    }


    public void setVendorParameters(AnnotationHashMap vendorParameters)
    {
        this.vendorParameters = vendorParameters;
    }
}
