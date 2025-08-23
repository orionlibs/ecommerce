package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultSelectorColumnModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultSelectorTableModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.AbstractSimpleReferenceSelectorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListViewSimpleSelectorController implements ComponentController
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListViewSimpleSelectorController.class);
    private final AbstractSimpleReferenceSelectorModel simpleSelectorModel;
    private final transient UIListView view;
    private TableModelListener tableModelListener = null;
    private ListViewListener listViewListener = null;
    private ListComponentModelListener listModelListener = null;
    private ColumnModelListener columnModelListener = null;


    public DefaultListViewSimpleSelectorController(AbstractSimpleReferenceSelectorModel simpleSelectorModel, UIListView view)
    {
        this.simpleSelectorModel = simpleSelectorModel;
        this.view = view;
    }


    public void initialize()
    {
        this.columnModelListener = createColumnModelListener(this.view);
        this.listModelListener = createListComponentModelListener(this.view);
        this.tableModelListener = createTableModelListener(this.view);
        this.listViewListener = createListViewListener(this.simpleSelectorModel);
        this.simpleSelectorModel.getTableModel().getColumnComponentModel().addColumnModelListener(this.columnModelListener);
        this.simpleSelectorModel.getTableModel().getListComponentModel().addListComponentModelListener(this.listModelListener);
        this.simpleSelectorModel.getTableModel().addTableModelListener(this.tableModelListener);
        this.view.addListViewListener(this.listViewListener);
    }


    protected TableModelListener createTableModelListener(UIListView listView)
    {
        return (TableModelListener)new DefaultSelectorTableModelListener(null, listView);
    }


    protected ListViewListener createListViewListener(AbstractSimpleReferenceSelectorModel simpleSelectorModel)
    {
        return (ListViewListener)new DefaultSimpleSelectorListViewListener(simpleSelectorModel.getTableModel());
    }


    protected ListComponentModelListener createListComponentModelListener(UIListView listView)
    {
        return (ListComponentModelListener)new DefaultSimpleSelectorListComponentModelListener(listView);
    }


    protected ColumnModelListener createColumnModelListener(UIListView listView)
    {
        return (ColumnModelListener)new DefaultSelectorColumnModelListener(null, listView);
    }


    public void unregisterListeners()
    {
        LOG.info("Desktop removed - Unregistering all listeners...");
        if(this.simpleSelectorModel.getTableModel() != null)
        {
            this.simpleSelectorModel.getTableModel().getListComponentModel()
                            .removeListComponentModelListener(this.listModelListener);
            this.simpleSelectorModel.getTableModel().getColumnComponentModel().removeColumnModelListener(this.columnModelListener);
            this.simpleSelectorModel.getTableModel().removeTableModelListener(this.tableModelListener);
        }
        if(this.view != null)
        {
            this.view.removeListViewListener(this.listViewListener);
        }
    }
}
