package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.session.BrowserModel;

public class ContextAreaTableModel extends DefaultTableModel
{
    BrowserModel browserModel;


    public ContextAreaTableModel(MutableListModel listComponentModel, MutableColumnModel columnComponentModel, BrowserModel browserModel)
    {
        super(listComponentModel, columnComponentModel);
        this.browserModel = browserModel;
    }


    public BrowserModel getBrowserModel()
    {
        return this.browserModel;
    }


    public void setBrowserModel(BrowserModel browserModel)
    {
        this.browserModel = browserModel;
    }
}
