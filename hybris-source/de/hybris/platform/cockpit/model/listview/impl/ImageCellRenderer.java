package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.media.MediaModel;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Image;
import org.zkoss.zul.Popup;

public class ImageCellRenderer implements CellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(ImageCellRenderer.class);
    private static final String LISTVIEW_STOP_IMAGE_POPUP_VISIBLE = "listview.stop.image.popup.visible";
    private Boolean stopImageVisible;


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null");
        }
        try
        {
            Object value = model.getValueAt(colIndex, rowIndex);
            String imgSrc = "cockpit/images/stop_klein.jpg";
            if(value instanceof TypedObject)
            {
                Object object = ((TypedObject)value).getObject();
                if(object instanceof MediaModel)
                {
                    imgSrc = ((MediaModel)object).getDownloadURL();
                }
                else
                {
                    LOG.warn("Could not render cell. Reason: Item not a media.");
                }
            }
            else if(value instanceof String)
            {
                imgSrc = (String)value;
            }
            else if(value != null)
            {
                LOG.warn("Could not render cell. Reason: Value not a TypedObject.");
            }
            String displayImgUrl = "cockpit/images/stop_klein.jpg";
            if(StringUtils.isNotBlank(imgSrc) && !StringUtils.equals(imgSrc, "cockpit/images/stop_klein.jpg"))
            {
                try
                {
                    URI uri = new URI(imgSrc);
                    displayImgUrl = uri.isAbsolute() ? imgSrc : ("~" + imgSrc);
                }
                catch(URISyntaxException uRISyntaxException)
                {
                }
            }
            Image img = new Image(displayImgUrl);
            img.setParent(parent);
            img.setSclass("listViewCellImage");
            if(!"cockpit/images/stop_klein.jpg".equals(displayImgUrl) || isStopImagePopupVisible())
            {
                Image popupImage = new Image(displayImgUrl);
                Popup popup = new Popup();
                popup.appendChild((Component)popupImage);
                parent.appendChild((Component)popup);
                img.setTooltip(popup);
            }
        }
        catch(IllegalArgumentException iae)
        {
            LOG.warn("Could not render cell using " + ImageCellRenderer.class.getSimpleName() + ".", iae);
        }
    }


    private boolean isStopImagePopupVisible()
    {
        if(this.stopImageVisible == null)
        {
            String param = UITools.getCockpitParameter("listview.stop.image.popup.visible", Executions.getCurrent());
            if(StringUtils.isNotBlank(param))
            {
                this.stopImageVisible = Boolean.valueOf(BooleanUtils.toBoolean(param));
            }
            else
            {
                this.stopImageVisible = Boolean.TRUE;
            }
        }
        return this.stopImageVisible.booleanValue();
    }
}
