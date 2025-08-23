package de.hybris.platform.acceleratorservices.model.email;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

public class EmailMessageModel extends ItemModel
{
    public static final String _TYPECODE = "EmailMessage";
    public static final String _BUSINESSPROCESS2EMAILMESSAGEREL = "BusinessProcess2EmailMessageRel";
    public static final String SENT = "sent";
    public static final String REPLYTOADDRESS = "replyToAddress";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String BODYMEDIA = "bodyMedia";
    public static final String SENTDATE = "sentDate";
    public static final String SENTMESSAGEID = "sentMessageID";
    public static final String TOADDRESSES = "toAddresses";
    public static final String CCADDRESSES = "ccAddresses";
    public static final String BCCADDRESSES = "bccAddresses";
    public static final String FROMADDRESS = "fromAddress";
    public static final String ATTACHMENTS = "attachments";
    public static final String PROCESS = "process";


    public EmailMessageModel()
    {
    }


    public EmailMessageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailMessageModel(String _replyToAddress, String _subject)
    {
        setReplyToAddress(_replyToAddress);
        setSubject(_subject);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailMessageModel(ItemModel _owner, String _replyToAddress, String _subject)
    {
        setOwner(_owner);
        setReplyToAddress(_replyToAddress);
        setSubject(_subject);
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
    public List<EmailAttachmentModel> getAttachments()
    {
        return (List<EmailAttachmentModel>)getPersistenceContext().getPropertyValue("attachments");
    }


    @Accessor(qualifier = "bccAddresses", type = Accessor.Type.GETTER)
    public List<EmailAddressModel> getBccAddresses()
    {
        return (List<EmailAddressModel>)getPersistenceContext().getPropertyValue("bccAddresses");
    }


    @Accessor(qualifier = "body", type = Accessor.Type.GETTER)
    public String getBody()
    {
        return (String)getPersistenceContext().getPropertyValue("body");
    }


    @Accessor(qualifier = "bodyMedia", type = Accessor.Type.GETTER)
    public MediaModel getBodyMedia()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("bodyMedia");
    }


    @Accessor(qualifier = "ccAddresses", type = Accessor.Type.GETTER)
    public List<EmailAddressModel> getCcAddresses()
    {
        return (List<EmailAddressModel>)getPersistenceContext().getPropertyValue("ccAddresses");
    }


    @Accessor(qualifier = "fromAddress", type = Accessor.Type.GETTER)
    public EmailAddressModel getFromAddress()
    {
        return (EmailAddressModel)getPersistenceContext().getPropertyValue("fromAddress");
    }


    @Accessor(qualifier = "process", type = Accessor.Type.GETTER)
    public BusinessProcessModel getProcess()
    {
        return (BusinessProcessModel)getPersistenceContext().getPropertyValue("process");
    }


    @Accessor(qualifier = "replyToAddress", type = Accessor.Type.GETTER)
    public String getReplyToAddress()
    {
        return (String)getPersistenceContext().getPropertyValue("replyToAddress");
    }


    @Accessor(qualifier = "sentDate", type = Accessor.Type.GETTER)
    public Date getSentDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("sentDate");
    }


    @Accessor(qualifier = "sentMessageID", type = Accessor.Type.GETTER)
    public String getSentMessageID()
    {
        return (String)getPersistenceContext().getPropertyValue("sentMessageID");
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
    public String getSubject()
    {
        return (String)getPersistenceContext().getPropertyValue("subject");
    }


    @Accessor(qualifier = "toAddresses", type = Accessor.Type.GETTER)
    public List<EmailAddressModel> getToAddresses()
    {
        return (List<EmailAddressModel>)getPersistenceContext().getPropertyValue("toAddresses");
    }


    @Accessor(qualifier = "sent", type = Accessor.Type.GETTER)
    public boolean isSent()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("sent"));
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
    public void setAttachments(List<EmailAttachmentModel> value)
    {
        getPersistenceContext().setPropertyValue("attachments", value);
    }


    @Accessor(qualifier = "bccAddresses", type = Accessor.Type.SETTER)
    public void setBccAddresses(List<EmailAddressModel> value)
    {
        getPersistenceContext().setPropertyValue("bccAddresses", value);
    }


    @Accessor(qualifier = "body", type = Accessor.Type.SETTER)
    public void setBody(String value)
    {
        getPersistenceContext().setPropertyValue("body", value);
    }


    @Accessor(qualifier = "bodyMedia", type = Accessor.Type.SETTER)
    public void setBodyMedia(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("bodyMedia", value);
    }


    @Accessor(qualifier = "ccAddresses", type = Accessor.Type.SETTER)
    public void setCcAddresses(List<EmailAddressModel> value)
    {
        getPersistenceContext().setPropertyValue("ccAddresses", value);
    }


    @Accessor(qualifier = "fromAddress", type = Accessor.Type.SETTER)
    public void setFromAddress(EmailAddressModel value)
    {
        getPersistenceContext().setPropertyValue("fromAddress", value);
    }


    @Accessor(qualifier = "process", type = Accessor.Type.SETTER)
    public void setProcess(BusinessProcessModel value)
    {
        getPersistenceContext().setPropertyValue("process", value);
    }


    @Accessor(qualifier = "replyToAddress", type = Accessor.Type.SETTER)
    public void setReplyToAddress(String value)
    {
        getPersistenceContext().setPropertyValue("replyToAddress", value);
    }


    @Accessor(qualifier = "sent", type = Accessor.Type.SETTER)
    public void setSent(boolean value)
    {
        getPersistenceContext().setPropertyValue("sent", toObject(value));
    }


    @Accessor(qualifier = "sentDate", type = Accessor.Type.SETTER)
    public void setSentDate(Date value)
    {
        getPersistenceContext().setPropertyValue("sentDate", value);
    }


    @Accessor(qualifier = "sentMessageID", type = Accessor.Type.SETTER)
    public void setSentMessageID(String value)
    {
        getPersistenceContext().setPropertyValue("sentMessageID", value);
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
    public void setSubject(String value)
    {
        getPersistenceContext().setPropertyValue("subject", value);
    }


    @Accessor(qualifier = "toAddresses", type = Accessor.Type.SETTER)
    public void setToAddresses(List<EmailAddressModel> value)
    {
        getPersistenceContext().setPropertyValue("toAddresses", value);
    }
}
