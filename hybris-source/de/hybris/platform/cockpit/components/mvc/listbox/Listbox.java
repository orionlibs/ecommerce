package de.hybris.platform.cockpit.components.mvc.listbox;

import org.zkoss.zk.ui.event.EventListener;

public class Listbox<T> extends Listbox
{
    private ListboxController controller;


    public Listbox()
    {
        addEventListener("onDropAtListBox", (EventListener)new DropHandler(this));
        addEventListener("onKeyAtListBoxPressed", (EventListener)new KeyStrokeHandler(this));
        addEventListener("onDeleteAtListBox", (EventListener)new DeleteHandler(this));
        addEventListener("onSelect", (EventListener)new SelectHandler(this));
    }


    public Listbox(ListboxController controller)
    {
        this.controller = controller;
        addEventListener("onDropAtListBox", (EventListener)new DropHandler(this));
        addEventListener("onKeyAtListBoxPressed", (EventListener)new KeyStrokeHandler(this));
        addEventListener("onDeleteAtListBox", (EventListener)new DeleteHandler(this));
        addEventListener("onSelect", (EventListener)new SelectHandler(this));
    }


    protected ListboxController getController()
    {
        return this.controller;
    }


    public void setController(ListboxController controller)
    {
        this.controller = controller;
    }
}
