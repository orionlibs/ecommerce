package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import java.io.Serializable;

public class CommerceCommentParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ItemModel item;
    private UserModel author;
    private String text;
    private String domainCode;
    private String componentCode;
    private String commentTypeCode;


    public void setItem(ItemModel item)
    {
        this.item = item;
    }


    public ItemModel getItem()
    {
        return this.item;
    }


    public void setAuthor(UserModel author)
    {
        this.author = author;
    }


    public UserModel getAuthor()
    {
        return this.author;
    }


    public void setText(String text)
    {
        this.text = text;
    }


    public String getText()
    {
        return this.text;
    }


    public void setDomainCode(String domainCode)
    {
        this.domainCode = domainCode;
    }


    public String getDomainCode()
    {
        return this.domainCode;
    }


    public void setComponentCode(String componentCode)
    {
        this.componentCode = componentCode;
    }


    public String getComponentCode()
    {
        return this.componentCode;
    }


    public void setCommentTypeCode(String commentTypeCode)
    {
        this.commentTypeCode = commentTypeCode;
    }


    public String getCommentTypeCode()
    {
        return this.commentTypeCode;
    }
}
