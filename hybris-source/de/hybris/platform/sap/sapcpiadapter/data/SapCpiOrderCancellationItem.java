package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiOrderCancellationItem")
public class SapCpiOrderCancellationItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String productCode;
    private String entryNumber;


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setEntryNumber(String entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public String getEntryNumber()
    {
        return this.entryNumber;
    }
}
