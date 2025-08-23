package de.hybris.platform.cockpit.components.mvc.commentlayer.model;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommentLayerComponentModel implements Serializable
{
    public static final String DEFAULT_MODE = "selectComment";
    private final TypedObject commentTarget;
    private final TypedObject commentLayerTarget;
    private CommentIconModel selectedIcon;
    private String mode;
    private double width;
    private double height;
    private double scaleFactor;
    private int currentRightClickX;
    private int currentRightClickY;
    private int zIndex;
    private boolean exposed;
    private final CommentLayerAwareModel commentLayerAwareModel;
    private final List<CommentIconModel> iconModels;
    private String scrollableContainerId;
    private int pageIndex;


    public CommentLayerComponentModel(TypedObject commentTarget, TypedObject commentLayerTarget, String mode, CommentLayerAwareModel commentLayerAwareModel)
    {
        this.mode = mode;
        this.commentTarget = commentTarget;
        this.commentLayerTarget = commentLayerTarget;
        this.commentLayerAwareModel = commentLayerAwareModel;
        this.iconModels = new ArrayList<>();
    }


    public CommentLayerComponentModel(TypedObject commentTarget, TypedObject commentLayerTarget, String mode, CommentLayerAwareModel commentLayerAwareModel, int pageIndex)
    {
        this.mode = mode;
        this.commentTarget = commentTarget;
        this.commentLayerTarget = commentLayerTarget;
        this.commentLayerAwareModel = commentLayerAwareModel;
        this.iconModels = new ArrayList<>();
        this.pageIndex = pageIndex;
    }


    public String getMode()
    {
        return this.mode;
    }


    public void setMode(String mode)
    {
        this.mode = mode;
    }


    public double getWidth()
    {
        return this.width;
    }


    public void setWidth(double width)
    {
        this.width = width;
    }


    public double getHeight()
    {
        return this.height;
    }


    public void setHeight(double height)
    {
        this.height = height;
    }


    public double getScaleFactor()
    {
        return this.scaleFactor;
    }


    public void setScaleFactor(double scaleFactor)
    {
        this.scaleFactor = scaleFactor;
    }


    public TypedObject getCommentTarget()
    {
        return this.commentTarget;
    }


    public CommentIconModel getSelectedIcon()
    {
        return this.selectedIcon;
    }


    public void setSelectedIcon(CommentIconModel selectedIcon)
    {
        this.selectedIcon = selectedIcon;
    }


    public TypedObject getCommentLayerTarget()
    {
        return this.commentLayerTarget;
    }


    public int getCurrentRightClickX()
    {
        return this.currentRightClickX;
    }


    public void setCurrentRightClickX(int currentRightClickX)
    {
        this.currentRightClickX = currentRightClickX;
    }


    public int getCurrentRightClickY()
    {
        return this.currentRightClickY;
    }


    public void setCurrentRightClickY(int currentRightClickY)
    {
        this.currentRightClickY = currentRightClickY;
    }


    public int getzIndex()
    {
        return this.zIndex;
    }


    public void setzIndex(int zIndex)
    {
        this.zIndex = zIndex;
    }


    public boolean isExposed()
    {
        return this.exposed;
    }


    public void setExposed(boolean exposed)
    {
        this.exposed = exposed;
    }


    public CommentLayerAwareModel getCommentLayerAwareModel()
    {
        return this.commentLayerAwareModel;
    }


    public List<CommentIconModel> getIconModels()
    {
        return this.iconModels;
    }


    public boolean addIconModel(CommentIconModel iconModel)
    {
        return this.iconModels.add(iconModel);
    }


    public boolean removeIcon(CommentIconModel iconModel)
    {
        return this.iconModels.remove(iconModel);
    }


    public String getScrollableContainerId()
    {
        return this.scrollableContainerId;
    }


    public void setScrollableContainerId(String scrollableContainerId)
    {
        this.scrollableContainerId = scrollableContainerId;
    }


    public int getPageIndex()
    {
        return this.pageIndex;
    }


    public void setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
    }
}
