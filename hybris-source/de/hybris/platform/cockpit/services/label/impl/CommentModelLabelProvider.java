package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.comments.model.AbstractCommentModel;
import org.apache.commons.lang.StringUtils;

public class CommentModelLabelProvider extends AbstractModelLabelProvider<AbstractCommentModel>
{
    protected String getIconPath(AbstractCommentModel item)
    {
        return null;
    }


    protected String getIconPath(AbstractCommentModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(AbstractCommentModel item)
    {
        return "";
    }


    protected String getItemDescription(AbstractCommentModel item, String languageIso)
    {
        return getItemDescription(item);
    }


    protected String getItemLabel(AbstractCommentModel item)
    {
        return StringUtils.isBlank(item.getSubject()) ? StringUtils.substring(item.getText(), 0, 20) : item.getSubject();
    }


    protected String getItemLabel(AbstractCommentModel item, String languageIso)
    {
        return getItemLabel(item);
    }
}
