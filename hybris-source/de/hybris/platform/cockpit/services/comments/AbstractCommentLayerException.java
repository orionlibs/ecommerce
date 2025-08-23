package de.hybris.platform.cockpit.services.comments;

import de.hybris.platform.comments.model.AbstractCommentModel;

public abstract class AbstractCommentLayerException extends Exception
{
    private final AbstractCommentModel comment;


    public AbstractCommentLayerException(String errMsg, Throwable throwable, AbstractCommentModel comment)
    {
        super(errMsg, throwable);
        this.comment = comment;
    }


    public AbstractCommentLayerException(String errMsg, AbstractCommentModel comment)
    {
        super(errMsg);
        this.comment = comment;
    }


    public AbstractCommentModel getComment()
    {
        return this.comment;
    }
}
