package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.jalo.CommentAttachment;

@Deprecated
public class CommentAttachmentLabelProvider extends AbstractObjectLabelProvider<CommentAttachment>
{
    protected String getIconPath(CommentAttachment attachment)
    {
        return null;
    }


    protected String getIconPath(CommentAttachment attachment, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CommentAttachment attachment)
    {
        return "";
    }


    protected String getItemDescription(CommentAttachment attachment, String languageIso)
    {
        return getItemDescription(attachment);
    }


    protected String getItemLabel(CommentAttachment attachment)
    {
        return UISessionUtils.getCurrentSession().getLabelService()
                        .getObjectTextLabel(UISessionUtils.getCurrentSession().getTypeService().wrapItem(attachment.getItem()));
    }


    protected String getItemLabel(CommentAttachment attachment, String languageIso)
    {
        return getItemLabel(attachment);
    }
}
