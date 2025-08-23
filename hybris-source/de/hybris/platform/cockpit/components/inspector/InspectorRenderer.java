package de.hybris.platform.cockpit.components.inspector;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.util.ListProvider;
import org.zkoss.zk.ui.Component;

public interface InspectorRenderer
{
    void renderEmpty(Component paramComponent);


    void render(Component paramComponent, TypedObject paramTypedObject);


    void render(Component paramComponent, ListProvider<TypedObject> paramListProvider);
}
