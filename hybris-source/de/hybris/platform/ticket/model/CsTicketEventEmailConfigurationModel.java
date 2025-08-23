package de.hybris.platform.ticket.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsEmailRecipients;
import java.util.Set;

public class CsTicketEventEmailConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "CsTicketEventEmailConfiguration";
    public static final String CODE = "code";
    public static final String PLAINTEXTTEMPLATE = "plainTextTemplate";
    public static final String HTMLTEMPLATE = "htmlTemplate";
    public static final String SUBJECT = "subject";
    public static final String EVENTTYPE = "eventType";
    public static final String ALTEREDATTRIBUTES = "alteredAttributes";
    public static final String RECIPIENTTYPE = "recipientType";


    public CsTicketEventEmailConfigurationModel()
    {
    }


    public CsTicketEventEmailConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketEventEmailConfigurationModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketEventEmailConfigurationModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "alteredAttributes", type = Accessor.Type.GETTER)
    public Set<AttributeDescriptorModel> getAlteredAttributes()
    {
        return (Set<AttributeDescriptorModel>)getPersistenceContext().getPropertyValue("alteredAttributes");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "eventType", type = Accessor.Type.GETTER)
    public CommentTypeModel getEventType()
    {
        return (CommentTypeModel)getPersistenceContext().getPropertyValue("eventType");
    }


    @Accessor(qualifier = "htmlTemplate", type = Accessor.Type.GETTER)
    public RendererTemplateModel getHtmlTemplate()
    {
        return (RendererTemplateModel)getPersistenceContext().getPropertyValue("htmlTemplate");
    }


    @Accessor(qualifier = "plainTextTemplate", type = Accessor.Type.GETTER)
    public RendererTemplateModel getPlainTextTemplate()
    {
        return (RendererTemplateModel)getPersistenceContext().getPropertyValue("plainTextTemplate");
    }


    @Accessor(qualifier = "recipientType", type = Accessor.Type.GETTER)
    public CsEmailRecipients getRecipientType()
    {
        return (CsEmailRecipients)getPersistenceContext().getPropertyValue("recipientType");
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
    public String getSubject()
    {
        return (String)getPersistenceContext().getPropertyValue("subject");
    }


    @Accessor(qualifier = "alteredAttributes", type = Accessor.Type.SETTER)
    public void setAlteredAttributes(Set<AttributeDescriptorModel> value)
    {
        getPersistenceContext().setPropertyValue("alteredAttributes", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "eventType", type = Accessor.Type.SETTER)
    public void setEventType(CommentTypeModel value)
    {
        getPersistenceContext().setPropertyValue("eventType", value);
    }


    @Accessor(qualifier = "htmlTemplate", type = Accessor.Type.SETTER)
    public void setHtmlTemplate(RendererTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("htmlTemplate", value);
    }


    @Accessor(qualifier = "plainTextTemplate", type = Accessor.Type.SETTER)
    public void setPlainTextTemplate(RendererTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("plainTextTemplate", value);
    }


    @Accessor(qualifier = "recipientType", type = Accessor.Type.SETTER)
    public void setRecipientType(CsEmailRecipients value)
    {
        getPersistenceContext().setPropertyValue("recipientType", value);
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
    public void setSubject(String value)
    {
        getPersistenceContext().setPropertyValue("subject", value);
    }
}
