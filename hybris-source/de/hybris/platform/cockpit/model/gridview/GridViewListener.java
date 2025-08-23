package de.hybris.platform.cockpit.model.gridview;

import java.util.Collection;

public interface GridViewListener
{
    void remove(Collection<Integer> paramCollection);


    void markAll(boolean paramBoolean);
}
