package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.jalo.AbstractComment;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class CommentLabelProvider extends AbstractObjectLabelProvider<AbstractComment>
{
    protected String getIconPath(AbstractComment item)
    {
        return null;
    }


    protected String getIconPath(AbstractComment item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(AbstractComment item)
    {
        return "";
    }


    protected String getItemDescription(AbstractComment item, String languageIso)
    {
        return getItemDescription(item);
    }


    protected String getItemLabel(AbstractComment item)
    {
        return StringUtils.isBlank(item.getSubject()) ? StringUtils.substring(UITools.removeHtml(item.getText()), 0, 20) :
                        item.getSubject();
    }


    protected String getItemLabel(AbstractComment item, String languageIso)
    {
        return getItemLabel(item);
    }
}
