package de.hybris.platform.cockpit.comments.strategies;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.core.model.user.UserModel;

public interface CommentPermissionCheckStrategy
{
    boolean isUserPermitted(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);
}
