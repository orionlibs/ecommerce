package de.hybris.platform.cms2.misc;

import javax.servlet.Filter;

public interface CMSFilter extends Filter
{
    public static final String PREVIEW_TOKEN = "cx-preview";
    public static final String PREVIEW_TICKET_ID_PARAM = "cmsTicketId";
    public static final String CLEAR_CMSSITE_PARAM = "clear=true";
}
