package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.util.Assert;

@XmlRootElement(name = "paymentMethodMockData")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentMethodMockData implements Serializable
{
    private static final long serialVersionUID = -6969224394685339265L;
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "cardNumber")
    private String cardNumber;
    @XmlElement(name = "cardType")
    private String cardType;
    @XmlElement(name = "accountHolderName")
    private String accountHolderName;
    @XmlElement(name = "startDate")
    private String startDate;
    @XmlElement(name = "expiryDate")
    private String expiryDate;
    @XmlElement(name = "issueNumber")
    private String issueNumber;
    @XmlElement(name = "addressMock")
    private AddressMock addressMock;


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getCardNumber()
    {
        return this.cardNumber;
    }


    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }


    public String getCardType()
    {
        return this.cardType;
    }


    public void setCardType(String cardType)
    {
        this.cardType = cardType;
    }


    public String getAccountHolderName()
    {
        return this.accountHolderName;
    }


    public void setAccountHolderName(String accountHolderName)
    {
        this.accountHolderName = accountHolderName;
    }


    public String getStartDate()
    {
        return this.startDate;
    }


    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }


    public String getExpiryDate()
    {
        return this.expiryDate;
    }


    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }


    public String getIssueNumber()
    {
        return this.issueNumber;
    }


    public void setIssueNumber(String issueNumber)
    {
        this.issueNumber = issueNumber;
    }


    public AddressMock getAddressMock()
    {
        return this.addressMock;
    }


    public void setAddressMock(AddressMock addressMock)
    {
        this.addressMock = addressMock;
    }


    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("AccountHolderName: ").append(this.accountHolderName).append(System.lineSeparator());
        buf.append("CardNumber:        ").append(this.cardNumber).append(System.lineSeparator());
        buf.append("CardType:          ").append(this.cardType).append(System.lineSeparator());
        buf.append("StartDate:         ").append(this.startDate).append(System.lineSeparator());
        buf.append("ExpiryDate:        ").append(this.expiryDate).append(System.lineSeparator());
        buf.append("IssueNumber:       ").append(this.issueNumber).append(System.lineSeparator());
        if(this.addressMock != null)
        {
            buf.append("Address:").append(System.lineSeparator()).append(this.addressMock.toString());
        }
        return buf.toString();
    }


    public Map<String, String> getMap()
    {
        Map<String, String> map = new HashMap<>();
        map.put("accountHolderName", this.accountHolderName);
        map.put("cardNumber", this.cardNumber);
        map.put("cardType", this.cardType);
        map.put("startDate", this.startDate);
        map.put("expiryDate", this.expiryDate);
        map.put("issueNumber", this.issueNumber);
        if(this.addressMock != null)
        {
            map.putAll(this.addressMock.getMap());
        }
        return map;
    }


    public static PaymentMethodMockData copyInstance(PaymentMethodMockData instance)
    {
        Assert.notNull(instance, "Parameter instance may not be null.");
        PaymentMethodMockData copy = new PaymentMethodMockData();
        copy.setAccountHolderName(instance.getAccountHolderName());
        copy.setCardNumber(instance.getCardNumber());
        copy.setCardType(instance.getCardType());
        copy.setExpiryDate(instance.getExpiryDate());
        copy.setId(instance.getId());
        copy.setIssueNumber(instance.getIssueNumber());
        copy.setStartDate(instance.getStartDate());
        copy.setAddressMock(AddressMock.copyInstance(instance.getAddressMock()));
        return copy;
    }
}
