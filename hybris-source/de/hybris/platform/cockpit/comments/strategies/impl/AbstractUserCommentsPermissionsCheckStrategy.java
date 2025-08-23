package de.hybris.platform.cockpit.comments.strategies.impl;

import de.hybris.platform.cockpit.comments.strategies.CommentPermissionCheckStrategy;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractUserCommentsPermissionsCheckStrategy implements CommentPermissionCheckStrategy
{
    private UIAccessRightService uiAccessRightService;
    private TypeService typeService;
    private SystemService systemService;
    private UserService userService;


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected SystemService getSystemService()
    {
        return this.systemService;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected UIAccessRightService getUiAccessRightService()
    {
        return this.uiAccessRightService;
    }


    @Required
    public void setUiAccessRightService(UIAccessRightService uiAccessRightService)
    {
        this.uiAccessRightService = uiAccessRightService;
    }


    protected boolean abstractCommentHasReplies(AbstractCommentModel abstractComment)
    {
        List<ReplyModel> replies = new ArrayList<>();
        if(abstractComment instanceof CommentModel)
        {
            replies.addAll(((CommentModel)abstractComment).getReplies());
        }
        else if(abstractComment instanceof ReplyModel)
        {
            replies.addAll(((ReplyModel)abstractComment).getReplies());
        }
        return !replies.isEmpty();
    }
}
