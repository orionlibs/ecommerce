package de.hybris.platform.configurablebundlecockpits.admincockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.configurablebundlecockpits.admincockpit.session.impl.AdminBundleCockpitNavigationArea;

public class AdminBundleCockpitNavigationAreaModel extends DefaultNavigationAreaModel
{
    public AdminBundleCockpitNavigationAreaModel()
    {
    }


    public AdminBundleCockpitNavigationAreaModel(AbstractUINavigationArea area)
    {
        super(area);
    }


    public AdminBundleCockpitNavigationArea getNavigationArea()
    {
        return (AdminBundleCockpitNavigationArea)super.getNavigationArea();
    }
}
