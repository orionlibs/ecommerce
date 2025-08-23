package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.session.UISessionListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;

public class DefaultNavigationAreaModel extends AbstractNavigationAreaModel
{
    private boolean initialized = false;


    public DefaultNavigationAreaModel()
    {
    }


    public DefaultNavigationAreaModel(AbstractUINavigationArea area)
    {
        super(area);
    }


    public void initialize()
    {
        if(!this.initialized)
        {
            UISessionUtils.getCurrentSession().addSessionListener((UISessionListener)new Object(this));
            refreshSpecialCollections();
            addAllSpecialCollectionsStandard();
            refreshCollections();
            refreshSpecialCollections();
            refreshSavedQueries();
            refreshDynamicQueries();
            this.initialized = true;
        }
    }


    public void update()
    {
        for(Section section : getSections())
        {
            sectionUpdated(section);
        }
    }
}
