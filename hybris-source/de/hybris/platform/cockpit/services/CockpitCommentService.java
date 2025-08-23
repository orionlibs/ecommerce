package de.hybris.platform.cockpit.services;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import java.util.List;
import org.zkoss.zk.ui.Desktop;

public interface CockpitCommentService
{
    List<CommentModel> getItemComments(ItemModel paramItemModel, UserModel paramUserModel, Collection<ComponentModel> paramCollection, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    Query getItemCommentsQuery(ItemModel paramItemModel, UserModel paramUserModel, Collection<ComponentModel> paramCollection, Collection<CommentTypeModel> paramCollection1);


    Query getCurrentUserCommentsQuery();


    ReplyModel createReply(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel, String paramString);


    CommentModel createItemComment(UserModel paramUserModel, ComponentModel paramComponentModel, CommentTypeModel paramCommentTypeModel, Collection<ItemModel> paramCollection, String paramString1, String paramString2);


    List<ReplyModel> getDirectReplies(CommentModel paramCommentModel, int paramInt1, int paramInt2);


    List<CommentModel> getComments(UserModel paramUserModel, Collection<ComponentModel> paramCollection, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    List<CommentModel> getCurrentUserComments();


    List<CommentModel> getCurrentUserComments(Desktop paramDesktop);


    List<ItemModel> getItemsFromCommentAttachments(AbstractCommentModel paramAbstractCommentModel);


    boolean isRead(TypedObject paramTypedObject);


    void setRead(TypedObject paramTypedObject, boolean paramBoolean);


    void saveChangedComments(List<CommentModel> paramList);
}
