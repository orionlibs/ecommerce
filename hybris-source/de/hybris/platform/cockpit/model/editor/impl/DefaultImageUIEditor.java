package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class DefaultImageUIEditor extends AbstractUIEditor
{
    private static final String DEFAULT_IMG = "cockpit/images/stop_klein.jpg";


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Div mainContainer = new Div();
        mainContainer.setWidth("100%");
        mainContainer.setStyle("text-align:center");
        if(initialValue instanceof String && StringUtils.isNotEmpty((String)initialValue))
        {
            Img img = new Img();
            img.setDynamicProperty("src", initialValue);
            img.setStyle("max-height:100px");
            mainContainer.appendChild((Component)img);
        }
        else
        {
            Image zulImage = createImageInternal("cockpit/images/stop_klein.jpg");
            zulImage.setStyle("height:50px");
            mainContainer.appendChild((Component)zulImage);
        }
        return (HtmlBasedComponent)mainContainer;
    }


    private Image createImageInternal(String url)
    {
        Image regularImage = new Image(url);
        regularImage.setStyle("40px");
        return regularImage;
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "TEXT";
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        rootEditorComponent.setFocus(true);
    }
}
