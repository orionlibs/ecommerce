package de.hybris.platform.cockpit.components.mvc.commentlayer;

import de.hybris.platform.cockpit.session.impl.ContextAreaCommentTreeModel;
import de.hybris.platform.comments.model.AbstractCommentModel;

public interface CommentLayerContextComponent
{
    void openCommentRecord(AbstractCommentModel paramAbstractCommentModel);


    boolean update();


    ContextAreaCommentTreeModel getCommentTreeModel();
}
