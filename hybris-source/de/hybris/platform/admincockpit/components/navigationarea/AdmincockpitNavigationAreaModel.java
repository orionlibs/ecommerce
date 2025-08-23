package de.hybris.platform.admincockpit.components.navigationarea;

import de.hybris.platform.admincockpit.session.impl.AdmincockpitNavigationArea;
import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;

public class AdmincockpitNavigationAreaModel extends DefaultNavigationAreaModel
{
    public AdmincockpitNavigationAreaModel()
    {
    }


    public AdmincockpitNavigationAreaModel(AbstractUINavigationArea area)
    {
        super(area);
    }


    public AdmincockpitNavigationArea getNavigationArea()
    {
        return (AdmincockpitNavigationArea)super.getNavigationArea();
    }
}
