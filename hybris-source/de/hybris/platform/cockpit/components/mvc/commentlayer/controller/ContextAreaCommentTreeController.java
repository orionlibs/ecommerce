package de.hybris.platform.cockpit.components.mvc.commentlayer.controller;

import de.hybris.platform.cockpit.components.contentbrowser.comments.CommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.session.impl.ContextAreaCommentTreeModel;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;

public interface ContextAreaCommentTreeController
{
    boolean isCommentCurrentlySelected(ContextAreaCommentTreeModel paramContextAreaCommentTreeModel, AbstractCommentModel paramAbstractCommentModel);


    boolean isCommentVisible(ContextAreaCommentTreeModel paramContextAreaCommentTreeModel, CommentModel paramCommentModel);


    void selectComment(ContextAreaCommentTreeModel paramContextAreaCommentTreeModel, CommentModel paramCommentModel);


    void deleteComment(ContextAreaCommentTreeModel paramContextAreaCommentTreeModel, CommentModel paramCommentModel);


    void toggleCommentVisible(ContextAreaCommentTreeModel paramContextAreaCommentTreeModel, CommentModel paramCommentModel);


    CommentLayerComponent getOwningCommentLayer(ContextAreaCommentTreeModel paramContextAreaCommentTreeModel, CommentModel paramCommentModel);


    boolean getUserWorkingStatus(AbstractCommentModel paramAbstractCommentModel);


    void setUserWorkingStatus(AbstractCommentModel paramAbstractCommentModel, boolean paramBoolean);


    boolean isCommentRecordCollapsable(AbstractCommentModel paramAbstractCommentModel);


    CommentIcon getCommentIcon(ContextAreaCommentTreeModel paramContextAreaCommentTreeModel, CommentModel paramCommentModel);
}
