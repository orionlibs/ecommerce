package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Set;
import org.zkoss.zul.api.Div;

public interface CockpitComponent extends Div
{
    boolean initialize();


    boolean update();


    void updateItem(TypedObject paramTypedObject, Set<PropertyDescriptor> paramSet);


    void setActiveItem(TypedObject paramTypedObject);


    void updateActiveItems();


    void updateSelectedItems();
}
