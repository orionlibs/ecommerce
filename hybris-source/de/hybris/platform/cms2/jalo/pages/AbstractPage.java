package de.hybris.platform.cms2.jalo.pages;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.ContentSlotName;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForPage;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractPage extends GeneratedAbstractPage
{
    @Deprecated(since = "4.3")
    public List<ContentSlotForPage> getContentSlots(SessionContext ctx)
    {
        String query = "SELECT {" + Item.PK + "} FROM {" + GeneratedCms2Constants.TC.CONTENTSLOTFORPAGE + "} WHERE {page} = ?me";
        return getSession().getFlexibleSearch().search(ctx, query, Collections.singletonMap("me", this), ContentSlotForPage.class)
                        .getResult();
    }


    @Deprecated(since = "4.3")
    public List<String> getDefinedContentSlotPositions()
    {
        List<String> result = new ArrayList<>();
        for(ContentSlotForPage slot : getContentSlots())
        {
            result.add(slot.getPosition());
        }
        return result;
    }


    @Deprecated(since = "4.3")
    public String getType(SessionContext ctx)
    {
        return getComposedType().getName(ctx);
    }


    @Deprecated(since = "4.3")
    public Map<Language, String> getAllType(SessionContext ctx)
    {
        return getComposedType().getAllNames(ctx);
    }


    @Deprecated(since = "4.3")
    public String getTypeCode(SessionContext ctx)
    {
        return getComposedType().getCode();
    }


    @Deprecated(since = "4.3")
    public String getAvailableContentSlots(SessionContext ctx)
    {
        PageTemplate master = getMasterTemplate(ctx);
        if(master != null)
        {
            List<String> result = new ArrayList<>();
            for(ContentSlotName slot : master.getAvailableContentSlots(ctx))
            {
                result.add(slot.getName());
            }
            return StringUtils.join(result, "; ");
        }
        return "";
    }


    @Deprecated(since = "4.3")
    public String getMissingContentSlots(SessionContext ctx)
    {
        List<String> result = new ArrayList<>();
        PageTemplate master = getMasterTemplate();
        if(master != null)
        {
            Collection<ContentSlotName> slots = master.getAvailableContentSlots();
            List<String> predefined = master.getDefinedContentSlotPositions();
            List<String> defined = getDefinedContentSlotPositions();
            for(ContentSlotName slot : slots)
            {
                String name = slot.getName();
                if(!defined.contains(name) && !predefined.contains(name))
                {
                    result.add(name);
                }
            }
        }
        return StringUtils.join(result, "; ");
    }


    @Deprecated(since = "4.3")
    public String getView(SessionContext ctx)
    {
        PageTemplate template = getMasterTemplate();
        if(template == null)
        {
            return null;
        }
        return StringUtils.isNotBlank(template.getFrontendTemplateName()) ? template.getFrontendTemplateName() : template.getUid();
    }
}
