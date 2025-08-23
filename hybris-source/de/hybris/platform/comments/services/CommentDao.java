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
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.List;

public interface CommentDao extends Dao
{
    List<CommentModel> findCommentsByUser(UserModel paramUserModel, Collection<ComponentModel> paramCollection, int paramInt1, int paramInt2);


    List<CommentModel> findCommentsByType(UserModel paramUserModel, Collection<ComponentModel> paramCollection, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    List<CommentModel> findCommentsByItem(UserModel paramUserModel, Collection<ComponentModel> paramCollection, ItemModel paramItemModel, int paramInt1, int paramInt2);


    List<CommentModel> findCommentsByItemAndType(UserModel paramUserModel, Collection<ComponentModel> paramCollection, ItemModel paramItemModel, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    List<ReplyModel> findDirectRepliesByComment(CommentModel paramCommentModel, int paramInt1, int paramInt2);


    List<CommentUserSettingModel> findUserSettingsByComment(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);


    List<ComponentModel> findComponentsByDomainAndCode(DomainModel paramDomainModel, String paramString);


    List<DomainModel> findDomainsByCode(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    SearchResult<CommentModel> findAll(UserModel paramUserModel, Collection<ComponentModel> paramCollection, int paramInt1, int paramInt2);


    @Deprecated(since = "ages", forRemoval = true)
    SearchResult<CommentModel> findAllByType(UserModel paramUserModel, Collection<ComponentModel> paramCollection, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    @Deprecated(since = "ages", forRemoval = true)
    SearchResult<CommentModel> findAllByItem(UserModel paramUserModel, Collection<ComponentModel> paramCollection, ItemModel paramItemModel, int paramInt1, int paramInt2);


    @Deprecated(since = "ages", forRemoval = true)
    SearchResult<CommentModel> findAllByItemAndType(UserModel paramUserModel, Collection<ComponentModel> paramCollection, ItemModel paramItemModel, Collection<CommentTypeModel> paramCollection1, int paramInt1, int paramInt2);


    @Deprecated(since = "ages", forRemoval = true)
    SearchResult<ReplyModel> findAllDirectReplies(CommentModel paramCommentModel, int paramInt1, int paramInt2);


    @Deprecated(since = "ages", forRemoval = true)
    SearchResult<CommentUserSettingModel> findUserSettingByComment(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);
}
