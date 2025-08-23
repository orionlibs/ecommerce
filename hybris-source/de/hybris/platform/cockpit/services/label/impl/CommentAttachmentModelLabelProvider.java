package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.model.CommentAttachmentModel;

public class CommentAttachmentModelLabelProvider extends AbstractModelLabelProvider<CommentAttachmentModel>
{
    protected String getIconPath(CommentAttachmentModel attachment)
    {
        return null;
    }


    protected String getIconPath(CommentAttachmentModel attachment, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CommentAttachmentModel attachment)
    {
        return "";
    }


    protected String getItemDescription(CommentAttachmentModel attachment, String languageIso)
    {
        return getItemDescription(attachment);
    }


    protected String getItemLabel(CommentAttachmentModel attachment)
    {
        return UISessionUtils.getCurrentSession().getLabelService()
                        .getObjectTextLabelForTypedObject(UISessionUtils.getCurrentSession().getTypeService().wrapItem(attachment.getItem()));
    }


    protected String getItemLabel(CommentAttachmentModel attachment, String languageIso)
    {
        return getItemLabel(attachment);
    }
}
