package de.hybris.platform.productcockpit.session;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.session.NavigationAreaListener;

public interface CatalogNavigationAreaListener extends NavigationAreaListener
{
    void favoriteCategorySelected();


    void catalogItemSelectionChanged(CatalogVersionModel paramCatalogVersionModel);
}
