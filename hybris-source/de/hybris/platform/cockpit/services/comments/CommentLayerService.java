package de.hybris.platform.cockpit.services.comments;

import de.hybris.platform.cockpit.model.CommentMetadataModel;
import de.hybris.platform.cockpit.services.comments.modes.CommentModeExecutor;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.List;
import java.util.Map;

public interface CommentLayerService
{
    Map<String, CommentModeExecutor> getCommentLayerModes();


    CommentModeExecutor getModeExecutor(String paramString);


    boolean deleteComment(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);


    List<CommentModel> getCommentsForCommentLayer(UserModel paramUserModel, ItemModel paramItemModel);


    CommentMetadataModel getCommentLocationForPreview(CommentModel paramCommentModel, MediaModel paramMediaModel);


    boolean canUserEditComment(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);


    boolean canUserReplyToComment(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);


    boolean canUserDeleteComment(UserModel paramUserModel, AbstractCommentModel paramAbstractCommentModel);


    void replyToComment(String paramString, AbstractCommentModel paramAbstractCommentModel);


    boolean canUserMoveComment(UserModel paramUserModel, CommentModel paramCommentModel);


    boolean canUserCreateComment(UserModel paramUserModel);
}
