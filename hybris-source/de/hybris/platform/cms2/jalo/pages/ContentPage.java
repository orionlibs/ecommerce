package de.hybris.platform.cms2.jalo.pages;

import de.hybris.platform.jalo.SessionContext;
import org.apache.commons.lang.StringUtils;

public class ContentPage extends GeneratedContentPage
{
    @Deprecated(since = "4.3")
    public String getLabelOrId(SessionContext ctx)
    {
        String label = getLabel();
        if(StringUtils.isEmpty(label))
        {
            return getUid();
        }
        return label;
    }
}
