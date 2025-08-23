package de.hybris.platform.cockpit.comments.strategies.impl;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.core.model.user.UserModel;

public class DefaultUserCanCreateCommentStrategy extends AbstractUserCommentsPermissionsCheckStrategy
{
    public boolean isUserPermitted(UserModel user, AbstractCommentModel comment)
    {
        return getSystemService().checkPermissionOn("Comment", "create");
    }
}
