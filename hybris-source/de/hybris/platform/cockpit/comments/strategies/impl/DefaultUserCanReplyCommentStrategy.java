package de.hybris.platform.cockpit.comments.strategies.impl;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultUserCanReplyCommentStrategy extends AbstractUserCommentsPermissionsCheckStrategy
{
    public boolean isUserPermitted(UserModel user, AbstractCommentModel comment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        ServicesUtil.validateParameterNotNullStandardMessage("comment", comment);
        boolean userNotTheAuthor = !user.equals(comment.getAuthor());
        boolean permission = getSystemService().checkPermissionOn("Reply", "create");
        return (permission && userNotTheAuthor);
    }
}
