package de.hybris.platform.cockpit.comments.strategies.impl;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultUserCanDeleteCommentStrategy extends AbstractUserCommentsPermissionsCheckStrategy
{
    public boolean isUserPermitted(UserModel user, AbstractCommentModel comment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        ServicesUtil.validateParameterNotNullStandardMessage("comment", comment);
        if(getUserService().isAdmin(user))
        {
            return true;
        }
        String typeCode = getTypeService().wrapItem(comment).getType().getCode();
        boolean userHasPermissions = getSystemService().checkPermissionOn(typeCode, "remove");
        return (userHasPermissions && user.equals(comment.getAuthor()) && !abstractCommentHasReplies(comment));
    }
}
