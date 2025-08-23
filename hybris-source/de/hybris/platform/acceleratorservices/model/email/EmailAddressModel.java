package de.hybris.platform.acceleratorservices.model.email;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class EmailAddressModel extends ItemModel
{
    public static final String _TYPECODE = "EmailAddress";
    public static final String _EMAILMESSAGE2TOADDRESSESREL = "EmailMessage2ToAddressesRel";
    public static final String _EMAILMESSAGE2CCADDRESSESREL = "EmailMessage2CcAddressesRel";
    public static final String _EMAILMESSAGE2BCCADDRESSESREL = "EmailMessage2BccAddressesRel";
    public static final String _EMAILMESSAGE2FROMADDRESSREL = "EmailMessage2FromAddressRel";
    public static final String EMAILADDRESS = "emailAddress";
    public static final String DISPLAYNAME = "displayName";
    public static final String TOMESSAGES = "toMessages";
    public static final String CCMESSAGES = "ccMessages";
    public static final String BCCMESSAGES = "bccMessages";
    public static final String MESSAGESSENT = "messagesSent";


    public EmailAddressModel()
    {
    }


    public EmailAddressModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailAddressModel(String _displayName, String _emailAddress)
    {
        setDisplayName(_displayName);
        setEmailAddress(_emailAddress);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailAddressModel(String _displayName, String _emailAddress, ItemModel _owner)
    {
        setDisplayName(_displayName);
        setEmailAddress(_emailAddress);
        setOwner(_owner);
    }


    @Accessor(qualifier = "bccMessages", type = Accessor.Type.GETTER)
    public List<EmailMessageModel> getBccMessages()
    {
        return (List<EmailMessageModel>)getPersistenceContext().getPropertyValue("bccMessages");
    }


    @Accessor(qualifier = "ccMessages", type = Accessor.Type.GETTER)
    public List<EmailMessageModel> getCcMessages()
    {
        return (List<EmailMessageModel>)getPersistenceContext().getPropertyValue("ccMessages");
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
    public String getDisplayName()
    {
        return (String)getPersistenceContext().getPropertyValue("displayName");
    }


    @Accessor(qualifier = "emailAddress", type = Accessor.Type.GETTER)
    public String getEmailAddress()
    {
        return (String)getPersistenceContext().getPropertyValue("emailAddress");
    }


    @Accessor(qualifier = "messagesSent", type = Accessor.Type.GETTER)
    public List<EmailMessageModel> getMessagesSent()
    {
        return (List<EmailMessageModel>)getPersistenceContext().getPropertyValue("messagesSent");
    }


    @Accessor(qualifier = "toMessages", type = Accessor.Type.GETTER)
    public List<EmailMessageModel> getToMessages()
    {
        return (List<EmailMessageModel>)getPersistenceContext().getPropertyValue("toMessages");
    }


    @Accessor(qualifier = "bccMessages", type = Accessor.Type.SETTER)
    public void setBccMessages(List<EmailMessageModel> value)
    {
        getPersistenceContext().setPropertyValue("bccMessages", value);
    }


    @Accessor(qualifier = "ccMessages", type = Accessor.Type.SETTER)
    public void setCcMessages(List<EmailMessageModel> value)
    {
        getPersistenceContext().setPropertyValue("ccMessages", value);
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.SETTER)
    public void setDisplayName(String value)
    {
        getPersistenceContext().setPropertyValue("displayName", value);
    }


    @Accessor(qualifier = "emailAddress", type = Accessor.Type.SETTER)
    public void setEmailAddress(String value)
    {
        getPersistenceContext().setPropertyValue("emailAddress", value);
    }


    @Accessor(qualifier = "messagesSent", type = Accessor.Type.SETTER)
    public void setMessagesSent(List<EmailMessageModel> value)
    {
        getPersistenceContext().setPropertyValue("messagesSent", value);
    }


    @Accessor(qualifier = "toMessages", type = Accessor.Type.SETTER)
    public void setToMessages(List<EmailMessageModel> value)
    {
        getPersistenceContext().setPropertyValue("toMessages", value);
    }
}
