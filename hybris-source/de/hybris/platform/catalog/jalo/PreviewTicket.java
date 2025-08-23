package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class PreviewTicket extends GeneratedPreviewTicket
{
    private static final Logger log = Logger.getLogger(PreviewTicket.class.getName());
    public static final String PREVIEW_TICKET_PREFIX = "{[y]PreviewTicket:";
    public static final String PREVIEW_TICKET_POSTFIX = ":[y]}";


    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.PreviewTicketPrepareInterceptor")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("validTo", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("previewCatalogVersion", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing attributes " + missing + " for creating a new " + type.getCode(), 0);
        }
        PreviewTicket ticket = (PreviewTicket)super.createItem(ctx, type, allAttributes);
        ticket.setPreviewCatalogVersion(ctx, (CatalogVersion)allAttributes.get("previewCatalogVersion"));
        ticket.setValidTo(ctx, (Date)allAttributes.get("validTo"));
        if(allAttributes.get("createdBy") == null)
        {
            ticket.setCreatedBy(ctx, ticket.getSession().getUser());
        }
        else
        {
            ticket.setCreatedBy(ctx, (User)allAttributes.get("createdBy"));
        }
        return (Item)ticket;
    }


    @SLDSafe
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap ret = super.getNonInitialAttributes(ctx, allAttributes);
        ret.remove("validTo");
        ret.remove("previewCatalogVersion");
        ret.remove("createdBy");
        return ret;
    }


    public void notifyTicketTaken(HttpServletRequest request, HttpServletResponse response, JaloSession appliesTo)
    {
        if(log.isDebugEnabled())
        {
            log.debug("ticket " + this + " is being used by request " + request.getRequestURL());
        }
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.PreviewTicketTicketCodeHandler", portingMethod = "get")
    public String getTicketCode()
    {
        return getTicketCode(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.PreviewTicketTicketCodeHandler", portingMethod = "get")
    public String getTicketCode(SessionContext ctx)
    {
        return "{[y]PreviewTicket:" + getPK().toString() + ":[y]}";
    }
}
