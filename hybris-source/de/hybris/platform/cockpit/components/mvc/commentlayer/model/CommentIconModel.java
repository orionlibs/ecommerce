package de.hybris.platform.cockpit.components.mvc.commentlayer.model;

import de.hybris.platform.comments.model.CommentModel;

public class CommentIconModel
{
    private CommentModel comment;
    private int y_Position;
    private int x_Position;
    private boolean selected;
    private boolean visible;
    private String parentComponentId;


    public CommentIconModel(CommentModel comment, int x_Position, int y_Position)
    {
        this.comment = comment;
        this.y_Position = y_Position;
        this.x_Position = x_Position;
        this.selected = false;
        this.visible = true;
    }


    public CommentModel getComment()
    {
        return this.comment;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public int getY()
    {
        return this.y_Position;
    }


    public int getX()
    {
        return this.x_Position;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public void setY(int y_Position)
    {
        this.y_Position = y_Position;
    }


    public void setX(int x_Position)
    {
        this.x_Position = x_Position;
    }


    public void toggleSelected()
    {
        setSelected(!this.selected);
    }


    public void toggleVisible()
    {
        setVisible(!this.visible);
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public void setParentComponentId(String parentId)
    {
        this.parentComponentId = parentId;
    }


    public String getParentComponentId()
    {
        return this.parentComponentId;
    }


    public void setComment(CommentModel comment)
    {
        this.comment = comment;
    }
}
