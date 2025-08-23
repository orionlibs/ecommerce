package de.hybris.platform.cmscockpit.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public interface NavigationNodeTreeService
{
    TypedObject getChild(TypedObject paramTypedObject, int paramInt);


    int getChildCount(TypedObject paramTypedObject);


    boolean isLeaf(TypedObject paramTypedObject);


    List<TypedObject> getRootNavigationNodes(CatalogVersionModel paramCatalogVersionModel);
}
