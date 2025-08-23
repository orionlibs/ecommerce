package de.hybris.platform.cockpit.components.mvc.commentlayer.controller.impl;

import de.hybris.platform.cockpit.components.contentbrowser.comments.CommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.controller.CommentLayerComponentController;
import de.hybris.platform.cockpit.components.mvc.commentlayer.controller.ContextAreaCommentTreeController;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentIconModel;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerContext;
import de.hybris.platform.cockpit.helpers.ModelHelper;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.ContextAreaCommentTreeModel;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultContextAreaCommentTreeController implements ContextAreaCommentTreeController
{
    private CommentLayerComponentController commentLayerComponentController;
    private CommentService commentService;
    private ModelHelper modelHelper;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultContextAreaCommentTreeController.class);


    public boolean isCommentCurrentlySelected(ContextAreaCommentTreeModel model, AbstractCommentModel commentItemModel)
    {
        CommentLayerContext commentsContext = model.getCommentsContext();
        if(commentsContext != null)
        {
            CommentIconModel selectedIconModel = commentsContext.getSelectedCommentIconModel();
            return (selectedIconModel != null && commentItemModel != null && commentItemModel.equals(selectedIconModel.getComment()));
        }
        return false;
    }


    public void selectComment(ContextAreaCommentTreeModel model, CommentModel commentItemModel)
    {
        CommentLayerComponent clComponent = getOwningCommentLayer(model, commentItemModel);
        if(clComponent != null)
        {
            CommentIcon icon = this.commentLayerComponentController.getCommentIconForComment(clComponent, commentItemModel);
            this.commentLayerComponentController.selectCommentIcon(clComponent, icon);
        }
    }


    public void toggleCommentVisible(ContextAreaCommentTreeModel model, CommentModel comment)
    {
        CommentLayerComponent clComponent = getOwningCommentLayer(model, comment);
        if(clComponent != null)
        {
            CommentIconModel iconModel = getCorrespondingComponentModel(clComponent, comment);
            if(iconModel != null)
            {
                try
                {
                    iconModel.toggleVisible();
                    CommentUserSettingModel userSettingModel = this.commentService.getUserSetting(UISessionUtils.getCurrentSession()
                                    .getUser(), (AbstractCommentModel)comment);
                    userSettingModel.setHidden(Boolean.valueOf(!iconModel.isVisible()));
                    this.modelHelper.saveModel((ItemModel)userSettingModel, false);
                }
                catch(ValueHandlerException e)
                {
                    LOG.error(e.getMessage(), (Throwable)e);
                }
            }
            clComponent.refresh();
        }
    }


    public boolean isCommentVisible(ContextAreaCommentTreeModel model, CommentModel comment)
    {
        CommentLayerContext context = model.getCommentsContext();
        if(context != null)
        {
            for(CommentIcon icon : context.getCommentIcons())
            {
                if(icon.getModel().getComment().equals(comment))
                {
                    CommentUserSettingModel userSettingModel = this.commentService.getUserSetting(UISessionUtils.getCurrentSession()
                                    .getUser(), (AbstractCommentModel)comment);
                    Boolean isHidden = userSettingModel.getHidden();
                    icon.getModel().setVisible((isHidden == null) ? true : (!isHidden.booleanValue()));
                    return icon.getModel().isVisible();
                }
            }
        }
        return true;
    }


    @Required
    public void setCommentLayerComponentController(CommentLayerComponentController commentLayerComponentController)
    {
        this.commentLayerComponentController = commentLayerComponentController;
    }


    public CommentLayerComponent getOwningCommentLayer(ContextAreaCommentTreeModel model, CommentModel commentItemModel)
    {
        CommentLayerContext context = model.getCommentsContext();
        if(context != null)
        {
            for(CommentLayerComponent clComponent : context.getCommentLayerComponents())
            {
                if(clComponent.containsComment(commentItemModel))
                {
                    return clComponent;
                }
            }
        }
        return null;
    }


    private CommentIconModel getCorrespondingComponentModel(CommentLayerComponent clComponent, CommentModel commentModel)
    {
        if(clComponent != null)
        {
            List<CommentIconModel> iconModels = clComponent.getModel().getIconModels();
            for(CommentIconModel iconModel : iconModels)
            {
                if(commentModel.equals(iconModel.getComment()))
                {
                    return iconModel;
                }
            }
        }
        return null;
    }


    private CommentUserSettingModel getCommentSettingForCurrentUser(AbstractCommentModel comment)
    {
        return this.commentService.getUserSetting(UISessionUtils.getCurrentSession().getUser(), comment);
    }


    public boolean getUserWorkingStatus(AbstractCommentModel comment)
    {
        CommentUserSettingModel setting = getCommentSettingForCurrentUser(comment);
        if(setting != null)
        {
            return (setting.getWorkStatus() == null) ? false : setting.getWorkStatus().booleanValue();
        }
        return false;
    }


    public void setUserWorkingStatus(AbstractCommentModel comment, boolean status)
    {
        CommentUserSettingModel setting = getCommentSettingForCurrentUser(comment);
        if(setting != null)
        {
            try
            {
                setting.setWorkStatus(Boolean.valueOf(status));
                this.modelHelper.saveModel((ItemModel)setting, false);
            }
            catch(ValueHandlerException e)
            {
                LOG.error("Could not save working status for comment [" + comment + "]", (Throwable)e);
            }
        }
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }


    @Required
    public void setModelHelper(ModelHelper modelHelper)
    {
        this.modelHelper = modelHelper;
    }


    public void deleteComment(ContextAreaCommentTreeModel model, CommentModel commentItemModel)
    {
        CommentLayerComponent clComponent = getOwningCommentLayer(model, commentItemModel);
        if(clComponent != null)
        {
            CommentIcon icon = this.commentLayerComponentController.getCommentIconForComment(clComponent, commentItemModel);
            this.commentLayerComponentController.deleteCommentFromCommentLayer(clComponent, icon);
        }
    }


    public boolean isCommentRecordCollapsable(AbstractCommentModel abstractComment)
    {
        Collection<ReplyModel> replies = null;
        if(abstractComment instanceof CommentModel)
        {
            replies = this.commentService.getDirectReplies((CommentModel)abstractComment, 0, -1);
        }
        else if(abstractComment instanceof ReplyModel)
        {
            replies = ((ReplyModel)abstractComment).getReplies();
        }
        return (replies != null && !replies.isEmpty());
    }


    public CommentIcon getCommentIcon(ContextAreaCommentTreeModel model, CommentModel comment)
    {
        CommentLayerComponent clComponent = getOwningCommentLayer(model, comment);
        if(clComponent != null)
        {
            return clComponent.getCommentIcon(comment);
        }
        return null;
    }
}
