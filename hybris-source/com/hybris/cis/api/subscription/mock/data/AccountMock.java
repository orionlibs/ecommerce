package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountMock implements Serializable
{
    private static final long serialVersionUID = 3650127360103575485L;
    private String accountId;
    private String emailAddress;
    private String customerName;
    private AddressMock address;
    private String languagePreference;
    private String company;
    private List<SubscriptionMock> subscriptions;
    private List<PaymentMethodMock> paymentMethods;
    private List<CustomerUsageMock> customerUsages;


    public String getAccountId()
    {
        return this.accountId;
    }


    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }


    public String getEmailAddress()
    {
        return this.emailAddress;
    }


    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }


    public String getCustomerName()
    {
        return this.customerName;
    }


    public void setCustomerName(String name)
    {
        this.customerName = name;
    }


    public AddressMock getAddress()
    {
        return this.address;
    }


    public void setAddress(AddressMock address)
    {
        this.address = address;
    }


    public List<PaymentMethodMock> getPaymentMethods()
    {
        if(this.paymentMethods == null)
        {
            this.paymentMethods = new ArrayList<>();
        }
        return this.paymentMethods;
    }


    public void setPaymentMethods(List<PaymentMethodMock> paymentMethods)
    {
        this.paymentMethods = paymentMethods;
    }


    public List<SubscriptionMock> getSubscriptions()
    {
        if(this.subscriptions == null)
        {
            this.subscriptions = new ArrayList<>();
        }
        return this.subscriptions;
    }


    public void setSubscriptions(List<SubscriptionMock> subscriptions)
    {
        this.subscriptions = subscriptions;
    }


    public List<CustomerUsageMock> getCustomerUsages()
    {
        if(this.customerUsages == null)
        {
            this.customerUsages = new ArrayList<>();
        }
        return this.customerUsages;
    }


    public void setCustomerUsages(List<CustomerUsageMock> customerUsages)
    {
        this.customerUsages = customerUsages;
    }


    public String getLanguagePreference()
    {
        return this.languagePreference;
    }


    public void setLanguagePreference(String languagePreference)
    {
        this.languagePreference = languagePreference;
    }


    public String getCompany()
    {
        return this.company;
    }


    public void setCompany(String company)
    {
        this.company = company;
    }


    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("AccountId: ").append(this.accountId).append(System.lineSeparator());
        buf.append("Name:      ").append(this.customerName).append(System.lineSeparator());
        buf.append("Email:     ").append(this.emailAddress).append(System.lineSeparator());
        buf.append("Language:  ").append(this.languagePreference).append(System.lineSeparator());
        buf.append("Company:   ").append(this.company).append(System.lineSeparator());
        buf.append("Address:   ").append(this.address.toString()).append(System.lineSeparator());
        return buf.toString();
    }
}
