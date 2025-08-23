package de.hybris.platform.commerceservices.model.consent;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ConsentModel extends ItemModel
{
    public static final String _TYPECODE = "Consent";
    public static final String CODE = "code";
    public static final String CUSTOMER = "customer";
    public static final String CONSENTTEMPLATE = "consentTemplate";
    public static final String CONSENTGIVENDATE = "consentGivenDate";
    public static final String CONSENTWITHDRAWNDATE = "consentWithdrawnDate";
    public static final String ACTIVE = "active";


    public ConsentModel()
    {
    }


    public ConsentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsentModel(String _code, ConsentTemplateModel _consentTemplate, CustomerModel _customer)
    {
        setCode(_code);
        setConsentTemplate(_consentTemplate);
        setCustomer(_customer);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsentModel(String _code, ConsentTemplateModel _consentTemplate, CustomerModel _customer, ItemModel _owner)
    {
        setCode(_code);
        setConsentTemplate(_consentTemplate);
        setCustomer(_customer);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "consentGivenDate", type = Accessor.Type.GETTER)
    public Date getConsentGivenDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("consentGivenDate");
    }


    @Accessor(qualifier = "consentTemplate", type = Accessor.Type.GETTER)
    public ConsentTemplateModel getConsentTemplate()
    {
        return (ConsentTemplateModel)getPersistenceContext().getPropertyValue("consentTemplate");
    }


    @Accessor(qualifier = "consentWithdrawnDate", type = Accessor.Type.GETTER)
    public Date getConsentWithdrawnDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("consentWithdrawnDate");
    }


    @Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
    public CustomerModel getCustomer()
    {
        return (CustomerModel)getPersistenceContext().getPropertyValue("customer");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public boolean isActive()
    {
        return toPrimitive((Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "active"));
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "consentGivenDate", type = Accessor.Type.SETTER)
    public void setConsentGivenDate(Date value)
    {
        getPersistenceContext().setPropertyValue("consentGivenDate", value);
    }


    @Accessor(qualifier = "consentTemplate", type = Accessor.Type.SETTER)
    public void setConsentTemplate(ConsentTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("consentTemplate", value);
    }


    @Accessor(qualifier = "consentWithdrawnDate", type = Accessor.Type.SETTER)
    public void setConsentWithdrawnDate(Date value)
    {
        getPersistenceContext().setPropertyValue("consentWithdrawnDate", value);
    }


    @Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
    public void setCustomer(CustomerModel value)
    {
        getPersistenceContext().setPropertyValue("customer", value);
    }
}
