package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class ColumnLayoutComponent extends Div
{
    private static final String STYLE = "style=";
    private static final Logger LOG = LoggerFactory.getLogger(ColumnLayoutComponent.class);
    private static final String SPLITTER_ACTION = "onmousedown: splitterActionMouseDown(this, event); return false;";
    private final Div innerDiv;
    private final Div fillingBackgroundDiv;


    public ColumnLayoutComponent()
    {
        setSclass("columnLayoutComponent");
        setStyle("overflow: auto; white-space: nowrap; position: relative; text-align: left;");
        this.fillingBackgroundDiv = new Div();
        UITools.maximize((HtmlBasedComponent)this.fillingBackgroundDiv);
        this.fillingBackgroundDiv.setStyle("position: absolute;");
        this.innerDiv = new Div();
        this.innerDiv.setStyle("position: relative;");
        this.innerDiv.setSclass("clearfix innerDiv");
        this.innerDiv.addEventListener("onUser", (EventListener)new Object(this));
        addEventListener("onResetLater", (EventListener)new Object(this));
        Div mainContainer = new Div();
        mainContainer.setStyle("position:relative;");
        mainContainer.setSclass("columnMainCnt");
        appendChild((Component)mainContainer);
        mainContainer.appendChild((Component)this.fillingBackgroundDiv);
        mainContainer.appendChild((Component)this.innerDiv);
        Div splitterGhost = new Div();
        UITools.modifySClass((HtmlBasedComponent)splitterGhost, "clcSplitterGhost", true);
        mainContainer.appendChild((Component)splitterGhost);
        resetHackImage();
    }


    protected void resetHackImage()
    {
        Events.echoEvent("onResetLater", (Component)this, null);
    }


    protected void resetHackImageLater()
    {
        Image oldImg = null;
        for(Object child : getChildren())
        {
            if(child instanceof Image)
            {
                oldImg = (Image)child;
                break;
            }
        }
        if(oldImg != null)
        {
            oldImg.detach();
        }
        Image adjustWidthHackImage = new Image("hack_no_image");
        adjustWidthHackImage.setAction("onerror: adjustWidths(\"" + this.innerDiv.getUuid() + "\");");
        adjustWidthHackImage.setStyle("position:absolute; top: -10000px");
        adjustWidthHackImage.setWidth("1px");
        adjustWidthHackImage.setHeight("1px");
        appendChild((Component)adjustWidthHackImage);
    }


    protected Component createColumnComponent(Component content)
    {
        Div column = new Div();
        UITools.modifySClass((HtmlBasedComponent)column, "clcColumn", true);
        column.addEventListener("onUser", (EventListener)new Object(this, column));
        column.appendChild(content);
        Div splitterDiv = new Div();
        column.appendChild((Component)splitterDiv);
        UITools.modifySClass((HtmlBasedComponent)splitterDiv, "clcSplitter", true);
        splitterDiv.setAction("onmousedown: splitterActionMouseDown(this, event); return false;");
        return (Component)column;
    }


    public void setFillBackground(Component comp)
    {
        this.fillingBackgroundDiv.getChildren().clear();
        this.fillingBackgroundDiv.appendChild(comp);
    }


    public void appendColumn(Component comp)
    {
        this.innerDiv.appendChild(createColumnComponent(comp));
    }


    public void removeColumn(int index)
    {
        if(this.innerDiv != null)
        {
            List children = this.innerDiv.getChildren();
            if(index < children.size())
            {
                children.remove(index);
            }
        }
    }


    public void insertColumn(Component comp, int index)
    {
        if(this.innerDiv != null)
        {
            List children = this.innerDiv.getChildren();
            if(index < children.size())
            {
                Object object = children.get(index);
                this.innerDiv.insertBefore(createColumnComponent(comp), (Component)object);
            }
        }
    }


    public void clear()
    {
        if(this.innerDiv != null)
        {
            this.innerDiv.getChildren().clear();
        }
        resetHackImage();
    }


    public void setWidths(List<String> widths)
    {
        Iterator<String> widthIter = widths.iterator();
        for(Object object : this.innerDiv.getChildren())
        {
            if(widthIter.hasNext())
            {
                String width = widthIter.next();
                if(width != null)
                {
                    width = width.trim();
                    if(!"none".equals(width))
                    {
                        int scIndex = width.indexOf("style=");
                        if(scIndex >= 0)
                        {
                            String sclass = width.substring(scIndex + "style=".length());
                            UITools.modifySClass((HtmlBasedComponent)object, sclass, true);
                            int delimIndex = width.indexOf(';');
                            if(delimIndex >= 0)
                            {
                                width = width.substring(0, delimIndex);
                                ((HtmlBasedComponent)object).setWidth(width);
                            }
                            continue;
                        }
                        ((HtmlBasedComponent)object).setWidth(width);
                    }
                }
            }
        }
        resetHackImage();
    }


    public void adjustColumnWidths(AdjustType adjustType)
    {
        if(AdjustType.SPREADLAST.equals(adjustType))
        {
            Clients.evalJavaScript("spreadLast(\"" + this.innerDiv.getUuid() + "\");");
        }
        else
        {
            resetHackImage();
        }
    }


    public List<? extends Component> getColumns()
    {
        List<Component> ret = new ArrayList<>();
        if(this.innerDiv != null)
        {
            List children = this.innerDiv.getChildren();
            for(Object object : children)
            {
                Component col = (Component)object;
                ret.add(col.getFirstChild());
            }
        }
        return ret;
    }


    public int size()
    {
        if(this.innerDiv != null)
        {
            return this.innerDiv.getChildren().size();
        }
        return -1;
    }
}
