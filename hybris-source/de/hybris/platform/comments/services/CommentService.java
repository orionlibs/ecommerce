package de.hybris.platform.comments.services;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import java.util.List;

public interface CommentService
{
    List<CommentModel> getComments(UserModel paramUserModel, DomainModel paramDomainModel, int paramInt1, int paramInt2);


    List<CommentModel> getComments(UserModel paramUserModel, Collection<ComponentModel> paramCollection, int paramInt1, int paramInt2);


    List<CommentModel> getComments(UserModel paramUserModel, DomainModel paramDomainModel, Collection<CommentTypeModel> paramCollection, int paramInt1, int paramInt2);


    List<CommentModel> getComments(UserModel paramUserModel, Collection<ComponentModel> paramCollection, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    List<CommentModel> getItemComments(ItemModel paramItemModel, UserModel paramUserModel, DomainModel paramDomainModel, int paramInt1, int paramInt2);


    List<CommentModel> getItemComments(ItemModel paramItemModel, UserModel paramUserModel, Collection<ComponentModel> paramCollection, int paramInt1, int paramInt2);


    List<CommentModel> getItemComments(ItemModel paramItemModel, UserModel paramUserModel, DomainModel paramDomainModel, Collection<CommentTypeModel> paramCollection, int paramInt1, int paramInt2);


    List<CommentModel> getItemComments(ItemModel paramItemModel, UserModel paramUserModel, Collection<ComponentModel> paramCollection, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    List<ReplyModel> getDirectReplies(CommentModel paramCommentModel, int paramInt1, int paramInt2);


    DomainModel getDomainForCode(String paramString);


    ComponentModel getComponentForCode(DomainModel paramDomainModel, String paramString);


    CommentTypeModel getCommentTypeForCode(ComponentModel paramComponentModel, String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    DomainModel getDomainByCode(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    ComponentModel getComponentByCode(DomainModel paramDomainModel, String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    CommentTypeModel getCommentTypeByCode(ComponentModel paramComponentModel, String paramString);


    CommentUserSettingModel getUserSetting(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);


    ReplyModel createReply(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel, String paramString);


    Collection<CommentTypeModel> getAvailableCommentTypes(ComponentModel paramComponentModel);
}
