package de.hybris.platform.cockpit.components.mvc.commentlayer.controller;

import de.hybris.platform.cockpit.components.contentbrowser.comments.CommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerAwareModel;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerContext;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Menupopup;

public interface CommentLayerComponentController<T extends CommentLayerComponent>
{
    void changeCommentLayerMode(T paramT, String paramString);


    void changeCommentLayerMode(CommentLayerContext paramCommentLayerContext, String paramString);


    void changeCommentLayerToDefaultMode(CommentLayerContext paramCommentLayerContext);


    void changeCommentLayerToDefaultMode(T paramT);


    void addCommentIcon(T paramT, int paramInt1, int paramInt2);


    void deleteCommentFromCommentLayer(T paramT, CommentIcon paramCommentIcon);


    void drawCommentLayerOverArea(HtmlBasedComponent paramHtmlBasedComponent, T paramT);


    void selectCommentIcon(T paramT, CommentIcon paramCommentIcon);


    Menupopup createContextMenu(T paramT);


    Menupopup createIconContextMenu(T paramT, CommentIcon paramCommentIcon);


    CommentIcon getCommentIconForComment(T paramT, CommentModel paramCommentModel);


    boolean canEditComment(CommentModel paramCommentModel);


    boolean canReplyToComment(CommentModel paramCommentModel);


    boolean canCreateComment();


    boolean canDeleteComment(CommentModel paramCommentModel);


    boolean canMoveCommentIcon(CommentIcon paramCommentIcon);


    void moveCommentIcon(CommentLayerComponent paramCommentLayerComponent, CommentIcon paramCommentIcon, int paramInt1, int paramInt2);


    void replyComment(CommentLayerAwareModel paramCommentLayerAwareModel, AbstractCommentModel paramAbstractCommentModel);


    CommentIcon refreshCommentIcon(CommentIcon paramCommentIcon);


    void editCommentPopup(CommentLayerAwareModel paramCommentLayerAwareModel, AbstractCommentModel paramAbstractCommentModel);


    double[] getWidthAndHeight(CommentLayerComponent paramCommentLayerComponent, double paramDouble);


    void refreshContextList(CommentLayerAwareModel paramCommentLayerAwareModel, boolean paramBoolean);
}
