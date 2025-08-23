package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableController;

public class EditorTableController extends DefaultTableController
{
    public EditorTableController(MutableTableModel model, UIListView view)
    {
        super(model, view);
    }
}
