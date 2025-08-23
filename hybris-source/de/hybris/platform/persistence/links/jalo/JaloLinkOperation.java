package de.hybris.platform.persistence.links.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.link.LinkOperation;

abstract class JaloLinkOperation implements LinkOperation
{
    protected boolean skipChangingExistingLinks(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("dont.change.existing.links")));
    }
}
