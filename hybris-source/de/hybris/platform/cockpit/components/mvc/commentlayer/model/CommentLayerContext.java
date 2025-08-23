package de.hybris.platform.cockpit.components.mvc.commentlayer.model;

import de.hybris.platform.cockpit.components.contentbrowser.comments.CommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerContextComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.controller.CommentLayerComponentController;
import de.hybris.platform.comments.model.CommentModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommentLayerContext
{
    private Collection<? extends CommentLayerComponent> commentLayerComponents = new ArrayList<>();
    private CommentIconModel selectedCommentIconModel;
    private CommentLayerContextComponent contextAreaComponent;
    private CommentLayerComponentController<? extends CommentLayerComponent> commentLayerComponentController;


    public CommentLayerComponentController<? extends CommentLayerComponent> getCommentLayerComponentController()
    {
        return this.commentLayerComponentController;
    }


    public void setCommentLayerComponentController(CommentLayerComponentController<? extends CommentLayerComponent> commentLayerComponentController)
    {
        this.commentLayerComponentController = commentLayerComponentController;
    }


    public CommentIconModel getSelectedCommentIconModel()
    {
        return this.selectedCommentIconModel;
    }


    public CommentModel getActiveComment()
    {
        CommentIconModel activeIconModel = getSelectedCommentIconModel();
        if(activeIconModel != null)
        {
            return activeIconModel.getComment();
        }
        return null;
    }


    public void setSelectedCommentModel(CommentIconModel selectedCommentIconModel)
    {
        this.selectedCommentIconModel = selectedCommentIconModel;
    }


    public Collection<? extends CommentLayerComponent> getCommentLayerComponents()
    {
        return this.commentLayerComponents;
    }


    public void setCommentLayerComponents(Collection<? extends CommentLayerComponent> commentLayerComponents)
    {
        this.commentLayerComponents = commentLayerComponents;
    }


    public List<CommentIcon> getCommentIcons()
    {
        if(this.commentLayerComponents != null)
        {
            List<CommentIcon> result = new ArrayList<>();
            for(CommentLayerComponent clComponent : this.commentLayerComponents)
            {
                result.addAll(clComponent.getIconComponents());
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }


    public void setCommentContextAreaComponent(CommentLayerContextComponent component)
    {
        this.contextAreaComponent = component;
    }


    public CommentLayerContextComponent getCommentContextAreaComponent()
    {
        return this.contextAreaComponent;
    }
}
