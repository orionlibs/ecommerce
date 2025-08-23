package de.hybris.platform.mediaconversion.web.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class ImageTag extends AbstractUrlTag
{
    private String cssClass;
    private String style;
    private String alt;
    private String title;
    private String id;
    private Align align;
    private String border;
    private boolean ismap;
    private String longdesc;
    private String name;
    private String vspace;
    private String usemap;
    private String width;
    private String height;


    public void doTag() throws JspException, IOException
    {
        print("<img ");
        attribute("src", retrieveURL());
        attribute("class", getCssClass());
        attribute("style", getStyle());
        attribute("width", getWidth());
        attribute("height", getHeight());
        attribute("alt", getAlt());
        attribute("title", getTitle());
        attribute("id", getId());
        attribute("align", getAlign());
        attribute("border", getBorder());
        if(isIsmap())
        {
            attribute("ismap", "ismap");
        }
        attribute("longdesc", getLongdesc());
        attribute("name", getName());
        attribute("vspace", getVspace());
        attribute("usemap", getUsemap());
        print("/>");
    }


    private void attribute(String attName, Object value) throws IOException
    {
        if(value != null)
        {
            JspWriter out = getJspContext().getOut();
            out.print(attName);
            out.print("=\"");
            out.print(value);
            out.print("\" ");
        }
    }


    private void print(String text) throws IOException
    {
        getJspContext().getOut().print(text);
    }


    public String getCssClass()
    {
        return this.cssClass;
    }


    public void setCssClass(String cssClass)
    {
        this.cssClass = cssClass;
    }


    public String getStyle()
    {
        return this.style;
    }


    public void setStyle(String style)
    {
        this.style = style;
    }


    public String getAlt()
    {
        return this.alt;
    }


    public void setAlt(String alt)
    {
        this.alt = alt;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getWidth()
    {
        return this.width;
    }


    public void setWidth(String width)
    {
        this.width = width;
    }


    public String getHeight()
    {
        return this.height;
    }


    public void setHeight(String height)
    {
        this.height = height;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public Align getAlign()
    {
        return this.align;
    }


    public void setAlign(String align)
    {
        this.align = Align.valueOf(align);
    }


    public String getBorder()
    {
        return this.border;
    }


    public void setBorder(String border)
    {
        this.border = border;
    }


    public boolean isIsmap()
    {
        return this.ismap;
    }


    public void setIsmap(boolean ismap)
    {
        this.ismap = ismap;
    }


    public String getLongdesc()
    {
        return this.longdesc;
    }


    public void setLongdesc(String longdesc)
    {
        this.longdesc = longdesc;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getVspace()
    {
        return this.vspace;
    }


    public void setVspace(String vspace)
    {
        this.vspace = vspace;
    }


    public String getUsemap()
    {
        return this.usemap;
    }


    public void setUsemap(String usemap)
    {
        this.usemap = usemap;
    }
}
