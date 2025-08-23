package de.hybris.platform.cockpit.components.mvc.commentlayer;

import de.hybris.platform.cockpit.components.canvas.Canvas;
import de.hybris.platform.cockpit.components.contentbrowser.comments.CommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.controller.CommentLayerComponentController;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerComponentModel;
import de.hybris.platform.comments.model.CommentModel;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;

public class CommentLayerComponent extends Div
{
    private static final Logger LOG = LoggerFactory.getLogger(CommentLayerComponent.class);
    private final CommentLayerComponentController controller;
    private CommentLayerComponentModel model;
    private final List<CommentIcon> iconComponents;
    private Canvas canvas;
    private final HtmlBasedComponent parent;
    private final EventListener selectIconListener;
    private final EventListener commentIconOnUserListener;
    private final EventListener onUserListener;
    private final EventListener onDropEventListener;


    public CommentLayerComponent(CommentLayerComponentController controller, CommentLayerComponentModel model, HtmlBasedComponent parent)
    {
        this.controller = controller;
        this.model = model;
        setWidth(CommentLayerUtils.numericSizeToString(getModel().getWidth()));
        setHeight(CommentLayerUtils.numericSizeToString(getModel().getHeight()));
        this.iconComponents = new ArrayList<>();
        this.selectIconListener = (EventListener)new SelectCommentIconEventListener(this);
        this.commentIconOnUserListener = (EventListener)new CommentIconOnUserListener(this);
        this.onUserListener = (EventListener)new OnUserListener(this);
        this.onDropEventListener = (EventListener)new OnDropEventListener(this);
        this.parent = parent;
        setDroppable("true");
    }


    public void setModel(CommentLayerComponentModel model)
    {
        this.model = model;
    }


    public CommentLayerComponentModel getModel()
    {
        return this.model;
    }


    public CommentLayerComponentController getController()
    {
        return this.controller;
    }


    public void refresh(CommentLayerComponentModel model)
    {
        setWidth(CommentLayerUtils.numericSizeToString(model.getWidth()));
        setHeight(CommentLayerUtils.numericSizeToString(model.getHeight()));
        List<CommentIcon> iconsToREmove = new ArrayList<>();
        for(CommentIcon icon : this.iconComponents)
        {
            if(null == this.controller.refreshCommentIcon(icon))
            {
                iconsToREmove.add(icon);
            }
        }
        for(CommentIcon iconToRemove : iconsToREmove)
        {
            removeIcon(iconToRemove);
            model.removeIcon(iconToRemove.getModel());
        }
        refreshCanvas();
        setListeners();
    }


    protected void refreshCanvas()
    {
        if("editComment".equals(getModel().getMode()) && this.canvas != null)
        {
            this.canvas.refresh();
        }
        else
        {
            clearCanvas();
        }
    }


    public void refresh()
    {
        refresh(getModel());
    }


    public void resize(double scaleFactor)
    {
        this.model.setScaleFactor(scaleFactor);
        double[] scaledWidthAndHeight = this.controller.getWidthAndHeight(this, scaleFactor);
        double xShiftRatio = scaledWidthAndHeight[0] / this.model.getWidth();
        double yShiftRatio = scaledWidthAndHeight[1] / this.model.getHeight();
        this.model.setWidth(scaledWidthAndHeight[0]);
        this.model.setHeight(scaledWidthAndHeight[1]);
        if(this.canvas != null)
        {
            double previousDimension = this.canvas.getModel().getWidth();
            double resizeRatio = scaledWidthAndHeight[0] / previousDimension;
            this.canvas.resize(resizeRatio);
        }
        for(CommentIcon icon : getIconComponents())
        {
            icon.scale(xShiftRatio, yShiftRatio);
        }
    }


    public List<CommentIcon> getIconComponents()
    {
        return this.iconComponents;
    }


    public boolean addIconComponent(CommentIcon icon)
    {
        appendChild((Component)icon);
        return this.iconComponents.add(icon);
    }


    public boolean removeIcon(CommentIcon icon)
    {
        removeChild((Component)icon);
        return this.iconComponents.remove(icon);
    }


    public boolean containsComment(CommentModel commentModel)
    {
        for(CommentIcon icon : getIconComponents())
        {
            if(commentModel.equals(icon.getModel().getComment()))
            {
                return true;
            }
        }
        return false;
    }


    public CommentIcon getCommentIcon(CommentModel commentModel)
    {
        for(CommentIcon icon : getIconComponents())
        {
            if(commentModel.equals(icon.getModel().getComment()))
            {
                return icon;
            }
        }
        return null;
    }


    public CommentIcon getCommentIcon(String id)
    {
        for(CommentIcon icon : getIconComponents())
        {
            if(id.equals(icon.getId()))
            {
                return icon;
            }
        }
        return null;
    }


    public Canvas getCanvas()
    {
        return this.canvas;
    }


    public void appendCanvas(Canvas canvas)
    {
        if(canvas == null)
        {
            throw new IllegalArgumentException("Canvas cannot be null");
        }
        this.canvas = canvas;
        appendChild((Component)canvas);
    }


    public HtmlBasedComponent getParent()
    {
        return this.parent;
    }


    protected Menupopup getContextMenu()
    {
        return this.controller.createContextMenu(this);
    }


    protected Menupopup getIconContextMenu(CommentIcon icon)
    {
        return this.controller.createIconContextMenu(this, icon);
    }


    protected void setListeners()
    {
        clearEventListeners();
        addEventListener("onUser", this.onUserListener);
        if("createComment".equals(getModel().getMode()))
        {
            String appendIconOnClkAction = "onclick : return onClikCommentLayer(this, event);";
            setAction("onclick : return onClikCommentLayer(this, event);");
        }
        else if("selectComment".equals(getModel().getMode()))
        {
            String defaultModeDblClkLayerAction = "return onDblClikCommentLayer(this, event);";
            String defaultModeContextMenuLayerAction = "return onContextMenuCommentLayer(this, event);";
            String defaultModeClientAction = " ondblclick : return onDblClikCommentLayer(this, event); oncontextmenu : return onContextMenuCommentLayer(this, event);";
            setAction(" ondblclick : return onDblClikCommentLayer(this, event); oncontextmenu : return onContextMenuCommentLayer(this, event);");
            addEventListener("onDrop", this.onDropEventListener);
            Menupopup menu = this.controller.createContextMenu(this);
            setContext((Popup)menu);
            menu.setParent((Component)this);
        }
        setIconsListeners();
    }


    private void clearEventListeners()
    {
        String defaultClientActions = "ondblclick : return false ; onclick : return false; oncontextmenu : return false;";
        setAction("ondblclick : return false ; onclick : return false; oncontextmenu : return false;");
        removeEventListener("onDrop", this.onDropEventListener);
        setContext((Popup)null);
    }


    private void setIconsListeners()
    {
        for(CommentIcon icon : getIconComponents())
        {
            setIconListeners(icon);
        }
    }


    private void setIconListeners(CommentIcon icon)
    {
        String dblClkAction = "ondblclick : return dblClikCommentIcon(this, event);";
        String defaultDblClkAction = "ondblclick : return false;";
        CommentLayerUtils.clearEventListeners((Component)icon, new String[] {"onClick", "onRightClick", "onUser"});
        icon.setAction("ondblclick : return false;");
        icon.addEventListener("onUser", this.commentIconOnUserListener);
        if("selectComment".equals(getModel().getMode()))
        {
            icon.addEventListener("onClick", this.selectIconListener);
            icon.setAction("ondblclick : return dblClikCommentIcon(this, event);");
        }
        Menupopup iconContextMenu = getIconContextMenu(icon);
        iconContextMenu.setParent((Component)icon);
        icon.setContext((Popup)iconContextMenu);
    }


    private void clearCanvas()
    {
        if(this.canvas != null)
        {
            removeChild((Component)this.canvas);
            this.canvas.cleanUp();
            this.canvas.detach();
            this.canvas = null;
        }
    }
}
