package de.hybris.platform.cockpit.comments.strategies.impl;

import de.hybris.platform.cockpit.model.CommentMetadataModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultUserCanMoveCommentStrategy extends AbstractUserCommentsPermissionsCheckStrategy
{
    public boolean isUserPermitted(UserModel user, AbstractCommentModel abstractComment)
    {
        if(abstractComment instanceof CommentModel)
        {
            CommentModel comment = (CommentModel)abstractComment;
            ServicesUtil.validateParameterNotNullStandardMessage("user", user);
            ServicesUtil.validateParameterNotNullStandardMessage("comment", comment);
            if(comment.getCommentMetadata() == null || comment.getCommentMetadata().isEmpty())
            {
                throw new IllegalArgumentException("Given comment must have not empty comment meta-data information");
            }
            CommentMetadataModel metaData = comment.getCommentMetadata().iterator().next();
            TypedObject commentMetaData = getTypeService().wrapItem(metaData);
            boolean usrPermissions = getUiAccessRightService().isWritable((ObjectType)commentMetaData.getType(), commentMetaData);
            return (usrPermissions && user.equals(comment.getAuthor()) && !abstractCommentHasReplies((AbstractCommentModel)comment));
        }
        return false;
    }
}
