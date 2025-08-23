package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;

public class SectionTableModel extends DefaultTableModel
{
    private BrowserSectionModel sectionBrowserModel;


    public SectionTableModel(MutableListModel listComponentModel, MutableColumnModel columnComponentModel, BrowserSectionModel sectionBrowserModel)
    {
        super(listComponentModel, columnComponentModel);
        this.sectionBrowserModel = sectionBrowserModel;
    }


    public BrowserSectionModel getModel()
    {
        return this.sectionBrowserModel;
    }


    public void setModel(BrowserSectionModel model)
    {
        this.sectionBrowserModel = model;
    }
}
