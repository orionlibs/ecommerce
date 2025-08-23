package de.hybris.platform.cockpit.components.mvc.listbox;

import de.hybris.platform.cockpit.components.mvc.SelectableComponent;
import org.zkoss.zul.Listitem;

public interface ListboxController<T> extends SelectableComponent<Listbox, Listitem, T>
{
    void move(Listbox paramListbox, T paramT1, T paramT2);


    void delete(Listbox paramListbox, T paramT);
}
