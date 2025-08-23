package de.hybris.platform.cockpit.services.comments.impl;

import de.hybris.platform.cockpit.comments.strategies.CommentPermissionCheckStrategy;
import de.hybris.platform.cockpit.model.CommentMetadataModel;
import de.hybris.platform.cockpit.services.CockpitCommentService;
import de.hybris.platform.cockpit.services.comments.CommentLayerService;
import de.hybris.platform.cockpit.services.comments.modes.CommentModeExecutor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Executions;

public class DefaultCommentLayerService extends AbstractBusinessService implements CommentLayerService
{
    private CommentPermissionCheckStrategy userCanEditCommentStrategy;
    private CommentPermissionCheckStrategy userCanReplyCommentStrategy;
    private CommentPermissionCheckStrategy userCanDeleteCommentStrategy;
    private CommentPermissionCheckStrategy userCanMoveCommentStrategy;
    private CommentPermissionCheckStrategy userCanCreateCommentStrategy;
    private CockpitCommentService cockpitCommentService;
    private CommentService commentService;
    private Map<String, CommentModeExecutor> commentsCommands;


    public Map<String, CommentModeExecutor> getCommentLayerModes()
    {
        return this.commentsCommands;
    }


    public boolean canUserEditComment(UserModel user, AbstractCommentModel comment)
    {
        return this.userCanEditCommentStrategy.isUserPermitted(user, comment);
    }


    public boolean canUserReplyToComment(UserModel user, AbstractCommentModel comment)
    {
        return this.userCanReplyCommentStrategy.isUserPermitted(user, comment);
    }


    public boolean canUserCreateComment(UserModel user)
    {
        return this.userCanCreateCommentStrategy.isUserPermitted(user, null);
    }


    public boolean canUserMoveComment(UserModel user, CommentModel comment)
    {
        return this.userCanMoveCommentStrategy.isUserPermitted(user, (AbstractCommentModel)comment);
    }


    public boolean canUserDeleteComment(UserModel curentUser, AbstractCommentModel comment)
    {
        return this.userCanDeleteCommentStrategy.isUserPermitted(curentUser, comment);
    }


    public boolean deleteComment(UserModel user, AbstractCommentModel comment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        ServicesUtil.validateParameterNotNullStandardMessage("comment", comment);
        if(canUserDeleteComment(user, comment))
        {
            getModelService().remove(comment);
            return true;
        }
        return false;
    }


    public List<CommentModel> getCommentsForCommentLayer(UserModel user, ItemModel item)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        List<CommentModel> result = this.cockpitCommentService.getItemComments(item, null,
                        Collections.singletonList(getCurrentComponent()), Collections.singletonList(getCommentType()), 0, -1);
        if(result == null)
        {
            return Collections.EMPTY_LIST;
        }
        return filterCommentsWithoutPositionMetaData(result);
    }


    protected List<CommentModel> filterCommentsWithoutPositionMetaData(List<CommentModel> input)
    {
        List<CommentModel> output = new ArrayList<>(input.size());
        for(CommentModel comment : input)
        {
            Collection<CommentMetadataModel> commentMetadata = comment.getCommentMetadata();
            if(commentMetadata != null && !commentMetadata.isEmpty())
            {
                output.add(comment);
            }
        }
        return output;
    }


    public CommentModeExecutor getModeExecutor(String mode)
    {
        if(mode == null)
        {
            throw new IllegalArgumentException("mode cannot be null");
        }
        if(this.commentsCommands == null)
        {
            throw new IllegalStateException("No commends executoes were configured");
        }
        if(!this.commentsCommands.containsKey(mode))
        {
            throw new UnknownIdentifierException("No comment mode executor for mode [" + mode + "]");
        }
        return this.commentsCommands.get(mode);
    }


    public CommentMetadataModel getCommentLocationForPreview(CommentModel comment, MediaModel preview)
    {
        if(comment == null)
        {
            throw new IllegalArgumentException("Comment must not be null");
        }
        for(CommentMetadataModel metaData : comment.getCommentMetadata())
        {
            if(preview == null || preview.equals(metaData.getItem()))
            {
                return metaData;
            }
        }
        return null;
    }


    protected UserModel getCurentUser()
    {
        return UISessionUtils.getCurrentSession().getUser();
    }


    protected DomainModel getDomain()
    {
        String domainCode = UITools.getCockpitParameter("default.commentsection.domaincode", Executions.getCurrent());
        return this.commentService.getDomainForCode(domainCode);
    }


    protected ComponentModel getCurrentComponent()
    {
        String componentCode = UITools.getCockpitParameter("default.commentsection.componentcode", Executions.getCurrent());
        return this.commentService.getComponentForCode(getDomain(), componentCode);
    }


    protected CommentTypeModel getCommentType()
    {
        String commentTypeCode = UITools.getCockpitParameter("default.commentsection.commenttypecode",
                        Executions.getCurrent());
        return this.commentService.getCommentTypeForCode(getCurrentComponent(), commentTypeCode);
    }


    public void setCommentsCommands(Map<String, CommentModeExecutor> commentsCommands)
    {
        this.commentsCommands = commentsCommands;
    }


    public void replyToComment(String text, AbstractCommentModel comment)
    {
        this.cockpitCommentService.createReply(getCurentUser(), comment, text);
    }


    @Required
    public void setUserCanDeleteCommentStrategy(CommentPermissionCheckStrategy userCanDeleteCommentStrategy)
    {
        this.userCanDeleteCommentStrategy = userCanDeleteCommentStrategy;
    }


    @Required
    public void setUserCanMoveCommentStrategy(CommentPermissionCheckStrategy userCanMoveCommentStrategy)
    {
        this.userCanMoveCommentStrategy = userCanMoveCommentStrategy;
    }


    @Required
    public void setUserCanCreateCommentStrategy(CommentPermissionCheckStrategy userCanCreateCommentStrategy)
    {
        this.userCanCreateCommentStrategy = userCanCreateCommentStrategy;
    }


    @Required
    public void setUserCanEditCommentStrategy(CommentPermissionCheckStrategy userCanEditCommentStrategy)
    {
        this.userCanEditCommentStrategy = userCanEditCommentStrategy;
    }


    @Required
    public void setUserCanReplyCommentStrategy(CommentPermissionCheckStrategy userCanReplyCommentStrategy)
    {
        this.userCanReplyCommentStrategy = userCanReplyCommentStrategy;
    }


    @Required
    public void setCockpitCommentService(CockpitCommentService cockpitCommentService)
    {
        this.cockpitCommentService = cockpitCommentService;
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }
}
