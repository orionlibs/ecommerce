package de.hybris.platform.cockpit.components.mvc.commentlayer.model;

import org.zkoss.zk.ui.Component;

public interface CommentLayerAwareModel
{
    CommentLayerContext getCommentLayerContext();


    Object getCommentLayerTarget();


    boolean isCommentLayerActive();


    Component getParentAreaComponent();
}
