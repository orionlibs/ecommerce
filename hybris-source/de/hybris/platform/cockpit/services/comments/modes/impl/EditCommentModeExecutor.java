package de.hybris.platform.cockpit.services.comments.modes.impl;

import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.services.comments.modes.CommentModeExecutor;

public class EditCommentModeExecutor implements CommentModeExecutor
{
    public void executeCommentAction(CommentLayerComponent commentLayerComponent)
    {
        commentLayerComponent.getModel().setMode("editComment");
        commentLayerComponent.refresh();
    }
}
