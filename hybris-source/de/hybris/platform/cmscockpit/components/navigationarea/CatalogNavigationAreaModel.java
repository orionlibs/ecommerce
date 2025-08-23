package de.hybris.platform.cmscockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;

public class CatalogNavigationAreaModel extends DefaultNavigationAreaModel
{
    public void update()
    {
        for(Section s : getSections())
        {
            if(s instanceof DefaultSectionSelectorSection)
            {
                ((DefaultSectionSelectorSection)s).updateItems();
            }
            sectionUpdated(s);
        }
        super.update();
    }
}
