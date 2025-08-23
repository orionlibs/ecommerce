package de.hybris.platform.cockpit.components.contentbrowser.comments;

import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentIconModel;
import org.zkoss.zul.Image;
import org.zkoss.zul.impl.api.XulElement;

public interface CommentIcon extends XulElement
{
    String getImageURI();


    void refresh();


    Image getIcon();


    CommentIconModel getModel();


    void scale(double paramDouble1, double paramDouble2);


    String getId();
}
