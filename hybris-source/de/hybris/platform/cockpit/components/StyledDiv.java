package de.hybris.platform.cockpit.components;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

public class StyledDiv extends Div
{
    private static final long serialVersionUID = -99478798528332938L;
    private final Div containerDiv;


    public StyledDiv()
    {
        Div div1 = new Div();
        div1.setSclass("z-groupbox-hl");
        super.insertBefore((Component)div1, null);
        Div div2 = new Div();
        div2.setSclass("z-groupbox-hr");
        div2.setParent((Component)div1);
        Div div3 = new Div();
        div3.setSclass("z-groupbox-hm");
        div3.setParent((Component)div2);
        Div div4 = new Div();
        div4.setSclass("z-groupbox-header");
        div4.setParent((Component)div3);
        this.containerDiv = new Div();
        this.containerDiv.setWidth("100%");
        this.containerDiv.setHeight("100%");
        this.containerDiv.setParent((Component)div4);
    }


    public boolean insertBefore(Component newChild, Component refChild)
    {
        return getContainerDiv().insertBefore(newChild, refChild);
    }


    public Div getContainerDiv()
    {
        return this.containerDiv;
    }
}
