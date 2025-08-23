package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Collection;
import java.util.List;

public class CustomerModel extends UserModel
{
    public static final String _TYPECODE = "Customer";
    public static final String CUSTOMERID = "customerID";
    public static final String PREVIEWCATALOGVERSIONS = "previewCatalogVersions";
    public static final String TITLE = "title";
    public static final String DEFAULTPAYMENTINFO = "defaultPaymentInfo";
    public static final String TOKEN = "token";
    public static final String CONTACTEMAIL = "contactEmail";
    public static final String ORIGINALUID = "originalUid";
    public static final String TYPE = "type";
    public static final String TICKETS = "tickets";
    public static final String SAPCONSUMERID = "sapConsumerID";
    public static final String SAPCONTACTID = "sapContactID";
    public static final String SAPISREPLICATED = "sapIsReplicated";
    public static final String SAPREPLICATIONINFO = "sapReplicationInfo";


    public CustomerModel()
    {
    }


    public CustomerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomerModel(boolean _loginDisabled, String _uid)
    {
        setLoginDisabled(_loginDisabled);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomerModel(Collection<CustomerReviewModel> _customerReviews, boolean _loginDisabled, ItemModel _owner, String _uid)
    {
        setCustomerReviews(_customerReviews);
        setLoginDisabled(_loginDisabled);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "contactEmail", type = Accessor.Type.GETTER)
    public String getContactEmail()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "contactEmail");
    }


    @Accessor(qualifier = "customerID", type = Accessor.Type.GETTER)
    public String getCustomerID()
    {
        return (String)getPersistenceContext().getPropertyValue("customerID");
    }


    @Accessor(qualifier = "defaultPaymentInfo", type = Accessor.Type.GETTER)
    public PaymentInfoModel getDefaultPaymentInfo()
    {
        return (PaymentInfoModel)getPersistenceContext().getPropertyValue("defaultPaymentInfo");
    }


    @Accessor(qualifier = "originalUid", type = Accessor.Type.GETTER)
    public String getOriginalUid()
    {
        return (String)getPersistenceContext().getPropertyValue("originalUid");
    }


    @Accessor(qualifier = "previewCatalogVersions", type = Accessor.Type.GETTER)
    public Collection<CatalogVersionModel> getPreviewCatalogVersions()
    {
        return (Collection<CatalogVersionModel>)getPersistenceContext().getPropertyValue("previewCatalogVersions");
    }


    @Accessor(qualifier = "sapConsumerID", type = Accessor.Type.GETTER)
    public String getSapConsumerID()
    {
        return (String)getPersistenceContext().getPropertyValue("sapConsumerID");
    }


    @Accessor(qualifier = "sapContactID", type = Accessor.Type.GETTER)
    public String getSapContactID()
    {
        return (String)getPersistenceContext().getPropertyValue("sapContactID");
    }


    @Accessor(qualifier = "sapIsReplicated", type = Accessor.Type.GETTER)
    public Boolean getSapIsReplicated()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sapIsReplicated");
    }


    @Accessor(qualifier = "sapReplicationInfo", type = Accessor.Type.GETTER)
    public String getSapReplicationInfo()
    {
        return (String)getPersistenceContext().getPropertyValue("sapReplicationInfo");
    }


    @Accessor(qualifier = "tickets", type = Accessor.Type.GETTER)
    public List<CsTicketModel> getTickets()
    {
        return (List<CsTicketModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "tickets");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public TitleModel getTitle()
    {
        return (TitleModel)getPersistenceContext().getPropertyValue("title");
    }


    @Accessor(qualifier = "token", type = Accessor.Type.GETTER)
    public String getToken()
    {
        return (String)getPersistenceContext().getPropertyValue("token");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public CustomerType getType()
    {
        return (CustomerType)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "customerID", type = Accessor.Type.SETTER)
    public void setCustomerID(String value)
    {
        getPersistenceContext().setPropertyValue("customerID", value);
    }


    @Accessor(qualifier = "defaultPaymentInfo", type = Accessor.Type.SETTER)
    public void setDefaultPaymentInfo(PaymentInfoModel value)
    {
        getPersistenceContext().setPropertyValue("defaultPaymentInfo", value);
    }


    @Accessor(qualifier = "originalUid", type = Accessor.Type.SETTER)
    public void setOriginalUid(String value)
    {
        getPersistenceContext().setPropertyValue("originalUid", value);
    }


    @Accessor(qualifier = "previewCatalogVersions", type = Accessor.Type.SETTER)
    public void setPreviewCatalogVersions(Collection<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("previewCatalogVersions", value);
    }


    @Accessor(qualifier = "sapConsumerID", type = Accessor.Type.SETTER)
    public void setSapConsumerID(String value)
    {
        getPersistenceContext().setPropertyValue("sapConsumerID", value);
    }


    @Accessor(qualifier = "sapContactID", type = Accessor.Type.SETTER)
    public void setSapContactID(String value)
    {
        getPersistenceContext().setPropertyValue("sapContactID", value);
    }


    @Accessor(qualifier = "sapIsReplicated", type = Accessor.Type.SETTER)
    public void setSapIsReplicated(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sapIsReplicated", value);
    }


    @Accessor(qualifier = "sapReplicationInfo", type = Accessor.Type.SETTER)
    public void setSapReplicationInfo(String value)
    {
        getPersistenceContext().setPropertyValue("sapReplicationInfo", value);
    }


    @Accessor(qualifier = "tickets", type = Accessor.Type.SETTER)
    public void setTickets(List<CsTicketModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "tickets", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(TitleModel value)
    {
        getPersistenceContext().setPropertyValue("title", value);
    }


    @Accessor(qualifier = "token", type = Accessor.Type.SETTER)
    public void setToken(String value)
    {
        getPersistenceContext().setPropertyValue("token", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(CustomerType value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
