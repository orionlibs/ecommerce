package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerAwareModel;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerContext;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public class ContextAreaCommentTreeModel extends CommunicationBrowserModel
{
    private List<TypedObject> comments;
    private final CommentLayerAwareModel commentContextAwareModel;


    public ContextAreaCommentTreeModel(CommentLayerAwareModel commentContextAwareModel)
    {
        this.commentContextAwareModel = commentContextAwareModel;
    }


    public List<TypedObject> getItems()
    {
        return this.comments;
    }


    public void setComments(List<TypedObject> comments)
    {
        this.comments = comments;
    }


    public CommentLayerAwareModel getCommentContextAwareModel()
    {
        return this.commentContextAwareModel;
    }


    public CommentLayerContext getCommentsContext()
    {
        return getCommentContextAwareModel().getCommentLayerContext();
    }


    public Object getCommentLayerTarget()
    {
        return getCommentContextAwareModel().getCommentLayerTarget();
    }
}
