package de.hybris.platform.cmscockpit.components.liveedit;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Window;

public abstract class AbstractLiveEditCaptionButtonHandler implements LiveEditCaptionButtonHandler
{
    protected Button createRightCaptionButton(String label, String sClass, HtmlBasedComponent parent, EventListener listener)
    {
        Button button = new Button(label);
        button.setSclass(sClass);
        button.addEventListener("onClick", listener);
        parent.appendChild((Component)button);
        return button;
    }


    protected Window createPopupWindow(Component parent)
    {
        Window popupwindow = new Window();
        popupwindow.setParent(parent);
        popupwindow.setSclass("popupwindow");
        popupwindow.setVisible(false);
        popupwindow.setWidth("800px");
        popupwindow.setHeight("800px");
        popupwindow.setBorder("none");
        popupwindow.setClosable(true);
        popupwindow.setMaximizable(false);
        popupwindow.setSizable(false);
        popupwindow.setShadow(false);
        popupwindow.setAction("onhide: anima.fade(#{self}); onshow: anima.appear(#{self});");
        Caption caption = new Caption();
        caption.setParent((Component)popupwindow);
        return popupwindow;
    }
}
