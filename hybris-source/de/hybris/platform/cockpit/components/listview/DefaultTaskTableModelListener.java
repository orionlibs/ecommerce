package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;

public class DefaultTaskTableModelListener extends TaskTableModelListener
{
    private TypeService typeService;


    public DefaultTaskTableModelListener(BrowserModel browser, UIListView view)
    {
        super(browser, view);
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
