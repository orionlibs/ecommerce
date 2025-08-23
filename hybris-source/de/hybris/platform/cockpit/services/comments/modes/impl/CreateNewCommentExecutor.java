package de.hybris.platform.cockpit.services.comments.modes.impl;

import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerComponentModel;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.services.comments.modes.CommentModeExecutor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import org.zkoss.util.resource.Labels;

public class CreateNewCommentExecutor implements CommentModeExecutor
{
    public void executeCommentAction(CommentLayerComponent commentLayerComponent)
    {
        CommentLayerComponentModel model = commentLayerComponent.getModel();
        model.setMode("createComment");
        model.setExposed(false);
        commentLayerComponent.refresh();
        Notification notification = new Notification(Labels.getLabel("commentlayer.setposition"));
        BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
        if(basePerspective.getNotifier() != null)
        {
            basePerspective.getNotifier().setNotification(notification);
        }
    }
}
