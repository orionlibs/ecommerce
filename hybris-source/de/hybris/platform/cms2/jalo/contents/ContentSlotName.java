package de.hybris.platform.cms2.jalo.contents;

import de.hybris.platform.cms2.jalo.CMSComponentType;
import de.hybris.platform.cms2.jalo.pages.PageTemplate;
import de.hybris.platform.jalo.SessionContext;
import java.util.Date;
import java.util.Set;

public class ContentSlotName extends GeneratedContentSlotName
{
    public void setName(SessionContext ctx, String value)
    {
        super.setName(ctx, value);
        markTemplateModified(ctx);
    }


    public void setValidComponentTypes(SessionContext ctx, Set<CMSComponentType> value)
    {
        super.setValidComponentTypes(ctx, value);
        markTemplateModified(ctx);
    }


    protected void markTemplateModified(SessionContext ctx)
    {
        PageTemplate tmp = getTemplate(ctx);
        if(tmp != null)
        {
            tmp.setModificationTime(new Date());
        }
    }
}
