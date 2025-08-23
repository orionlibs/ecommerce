package de.hybris.platform.cockpit.components.mvc.commentlayer;

import de.hybris.platform.cockpit.components.contentbrowser.comments.CommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentIconModel;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class DivCommentIcon extends Div implements CommentIcon
{
    private static final String TEST_ID_PREFIX = "CommentLayerIcon_";
    private static final String ICON_URI = "/cockpit/images/icon_func_comment_available.png";
    private static final String ICON_SELECTED_URI = "/cockpit/css/images/comment_selected.png";
    private Image icon;
    private final CommentIconModel model;


    public DivCommentIcon(CommentIconModel model)
    {
        this.icon = new Image("/cockpit/images/icon_func_comment_available.png");
        this.model = model;
        setTop(CommentLayerUtils.numericSizeToString(model.getY()));
        setLeft(CommentLayerUtils.numericSizeToString(model.getX()));
        setSclass(resolveCommentIconSClass(model.isSelected()));
        appendChild((Component)this.icon);
        UITools.applyTestID((Component)this, "CommentLayerIcon_" + model.getComment().getCode() + "_");
    }


    public String getImageURI()
    {
        return "/cockpit/images/icon_func_comment_available.png";
    }


    public CommentIconModel getModel()
    {
        return this.model;
    }


    public Image getIcon()
    {
        return this.icon;
    }


    public void refresh()
    {
        setTop(CommentLayerUtils.numericSizeToString(this.model.getY()));
        setLeft(CommentLayerUtils.numericSizeToString(this.model.getX()));
        setSclass(resolveCommentIconSClass(this.model.isSelected()));
        removeChild((Component)this.icon);
        this.icon = new Image(resolveCommentIconImageURI(this.model.isSelected()));
        appendChild((Component)this.icon);
        setVisible(this.model.isVisible());
        updateTestId(this);
    }


    private void updateTestId(DivCommentIcon icon)
    {
        String id = icon.getId();
        icon.setId(id.replaceAll("null", getModel().getComment().getCode()));
    }


    private String resolveCommentIconSClass(boolean selected)
    {
        return selected ? "commenticonselected" : "commenticonnotselected";
    }


    private String resolveCommentIconImageURI(boolean selected)
    {
        return selected ? "/cockpit/css/images/comment_selected.png" : "/cockpit/images/icon_func_comment_available.png";
    }


    public void scale(double xRatio, double yRatio)
    {
        int newX = this.model.getX();
        int newY = this.model.getY();
        newX = (int)(newX * xRatio);
        newY = (int)(newY * yRatio);
        this.model.setX(newX);
        this.model.setY(newY);
    }
}
