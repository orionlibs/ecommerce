package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.api.Listbox;

public class DefaultListboxWidget<T extends WidgetModel, U extends WidgetController> extends DefaultWidget<T, U> implements ListboxWidget<T, U>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListboxWidget.class);
    private transient Listbox listBox;


    public void setListBox(Listbox listbox)
    {
        this.listBox = listbox;
    }


    protected Listbox getListbox()
    {
        return this.listBox;
    }


    public void handleFocus(boolean focused)
    {
        super.handleFocus(focused);
        if(focused)
        {
            if(UITools.isFromOtherDesktop((Component)getListbox()))
            {
                LOG.info("Can not focus list box. Reason: Invalid desktop.");
            }
            else
            {
                Clients.evalJavaScript("focusListbox('" + getListbox().getUuid() + "');");
            }
        }
    }
}
