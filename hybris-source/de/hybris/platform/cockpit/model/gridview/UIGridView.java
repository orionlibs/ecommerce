package de.hybris.platform.cockpit.model.gridview;

import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public interface UIGridView extends UIItemView
{
    MutableListModel getModel();


    void setModel(MutableListModel paramMutableListModel);


    void setRootType(ObjectTemplate paramObjectTemplate);


    void setGridItemRenderer(GridItemRenderer paramGridItemRenderer);


    void setConfigContextCode(String paramString);


    void addGridViewListener(GridViewListener paramGridViewListener);


    void removeGridViewListener(GridViewListener paramGridViewListener);
}
