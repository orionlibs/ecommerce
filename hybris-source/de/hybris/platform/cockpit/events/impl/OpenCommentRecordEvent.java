package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.comments.model.AbstractCommentModel;

public class OpenCommentRecordEvent extends AbstractCockpitEvent
{
    private final AbstractCommentModel abstractComment;
    private final CommentLayerComponent clComponent;


    public OpenCommentRecordEvent(Object source, AbstractCommentModel abstractComment, CommentLayerComponent clComponent)
    {
        super(source);
        this.abstractComment = abstractComment;
        this.clComponent = clComponent;
    }


    public AbstractCommentModel getAbstractComment()
    {
        return this.abstractComment;
    }


    public CommentLayerComponent getClComponent()
    {
        return this.clComponent;
    }
}
