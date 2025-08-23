package de.hybris.platform.cockpit.model.general;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.List;

public interface ListComponentModelListener
{
    void activationChanged(Collection<TypedObject> paramCollection);


    void selectionChanged(List<Integer> paramList);


    void itemMoved(int paramInt1, int paramInt2);


    void itemsRemoved(Collection<? extends Object> paramCollection);


    void itemsChanged();


    void changed();


    void onEvent(String paramString, Object paramObject);
}
