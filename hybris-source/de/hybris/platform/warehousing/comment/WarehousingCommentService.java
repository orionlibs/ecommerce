package de.hybris.platform.warehousing.comment;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentEventType;

public interface WarehousingCommentService extends CommentService
{
    CommentModel createAndSaveComment(WarehousingCommentContext paramWarehousingCommentContext, String paramString);


    ComponentModel getOrCreateComponent(WarehousingCommentEventType paramWarehousingCommentEventType);


    CommentTypeModel getOrCreateCommentType(WarehousingCommentEventType paramWarehousingCommentEventType);


    DomainModel getOrCreateDomainForCodeWarehousing();
}
