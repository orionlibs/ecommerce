package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public interface ListSectionModel extends SectionModel
{
    MutableTableModel getTableModel();


    void setTableModel(MutableTableModel paramMutableTableModel);


    ObjectTemplate getRootType();


    void setRootType(ObjectTemplate paramObjectTemplate);


    void setListViewConfigurationCode(String paramString);


    String getListViewConfigurationCode();
}
