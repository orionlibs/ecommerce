package de.hybris.platform.cockpit.comments.strategies.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultUserCanEditCommentStrategy extends AbstractUserCommentsPermissionsCheckStrategy
{
    public boolean isUserPermitted(UserModel user, AbstractCommentModel comment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        ServicesUtil.validateParameterNotNullStandardMessage("comment", comment);
        TypedObject wrappedItem = getTypeService().wrapItem(comment);
        boolean userHasWritePermissions = getUiAccessRightService().isWritable((ObjectType)wrappedItem.getType(), wrappedItem);
        return (userHasWritePermissions && user.equals(comment.getAuthor()) && !abstractCommentHasReplies(comment));
    }
}
