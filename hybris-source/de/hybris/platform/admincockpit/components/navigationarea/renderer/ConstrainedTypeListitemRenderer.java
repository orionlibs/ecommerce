package de.hybris.platform.admincockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class ConstrainedTypeListitemRenderer implements ListitemRenderer
{
    public void render(Listitem item, Object data) throws Exception
    {
        Listcell cell = new Listcell();
        Hbox hbox = new Hbox();
        hbox.setSpacing("0");
        hbox.setWidths("1,100%,1");
        Div div = new Div();
        div.setSclass("navigation-query-left");
        hbox.appendChild((Component)div);
        div = new Div();
        div.setSclass("navigation-query-center");
        hbox.appendChild((Component)div);
        div = new Div();
        div.setSclass("navigation-query-right");
        hbox.appendChild((Component)div);
        Div labelDiv = new Div();
        labelDiv.setSclass("navigation-query-label");
        Label label = new Label();
        TypedObject _data = (TypedObject)data;
        label.setValue(((ComposedTypeModel)_data.getObject()).getCode());
        labelDiv.appendChild((Component)label);
        cell.appendChild((Component)hbox);
        cell.appendChild((Component)labelDiv);
        item.setSclass("admincockpit_listitem");
        item.appendChild((Component)cell);
        item.setValue(data);
    }
}
