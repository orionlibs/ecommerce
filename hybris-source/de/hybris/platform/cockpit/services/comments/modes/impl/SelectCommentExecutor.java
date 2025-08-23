package de.hybris.platform.cockpit.services.comments.modes.impl;

import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerComponentModel;
import de.hybris.platform.cockpit.services.comments.modes.CommentModeExecutor;

public class SelectCommentExecutor implements CommentModeExecutor
{
    public void executeCommentAction(CommentLayerComponent commentLayerComponent)
    {
        CommentLayerComponentModel model = commentLayerComponent.getModel();
        model.setMode("selectComment");
        model.setExposed(false);
        commentLayerComponent.refresh();
    }
}
