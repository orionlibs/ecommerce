package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListViewSelectorController implements ComponentController
{
    private static final Logger log = LoggerFactory.getLogger(DefaultListViewSelectorController.class);
    private final AbstractReferenceSelectorModel referenceSelectorModel;
    private final UIListView view;
    private TableModelListener tableModelListener = null;
    private ListViewListener listViewListener = null;
    private ListComponentModelListener listModelListener = null;
    private ColumnModelListener columnModelListener = null;


    public DefaultListViewSelectorController(AbstractReferenceSelectorModel referenceSelectorModel, UIListView view)
    {
        this.referenceSelectorModel = referenceSelectorModel;
        this.view = view;
    }


    public void initialize()
    {
        this.columnModelListener = createColumnModelListener(this.referenceSelectorModel, this.view);
        this.listModelListener = createListComponentModelListener(this.referenceSelectorModel, this.view);
        this.tableModelListener = createTableModelListener(this.referenceSelectorModel, this.view);
        this.listViewListener = createListViewListener(this.referenceSelectorModel);
        this.referenceSelectorModel.getTableModel().getColumnComponentModel().addColumnModelListener(this.columnModelListener);
        this.referenceSelectorModel.getTableModel().getListComponentModel().addListComponentModelListener(this.listModelListener);
        this.referenceSelectorModel.getTableModel().addTableModelListener(this.tableModelListener);
        this.view.addListViewListener(this.listViewListener);
    }


    protected TableModelListener createTableModelListener(AbstractReferenceSelectorModel referenceSelectorModel, UIListView listView)
    {
        return (TableModelListener)new DefaultSelectorTableModelListener(referenceSelectorModel, listView);
    }


    protected ListViewListener createListViewListener(AbstractReferenceSelectorModel referenceSelectorModel)
    {
        return (ListViewListener)new DefaultSelectorListViewListener(referenceSelectorModel);
    }


    protected ListComponentModelListener createListComponentModelListener(AbstractReferenceSelectorModel referenceSelectorModel, UIListView listView)
    {
        return (ListComponentModelListener)new DefaultSelectorListComponentModelListener(referenceSelectorModel, listView);
    }


    protected ColumnModelListener createColumnModelListener(AbstractReferenceSelectorModel referenceSelectorModel, UIListView listView)
    {
        return (ColumnModelListener)new DefaultSelectorColumnModelListener(referenceSelectorModel, listView);
    }


    public void unregisterListeners()
    {
        log.info("Desktop removed - Unregistering all listeners...");
        if(this.referenceSelectorModel.getTableModel() != null)
        {
            this.referenceSelectorModel.getTableModel().getListComponentModel().removeListComponentModelListener(this.listModelListener);
            this.referenceSelectorModel.getTableModel().getColumnComponentModel()
                            .removeColumnModelListener(this.columnModelListener);
            this.referenceSelectorModel.getTableModel().removeTableModelListener(this.tableModelListener);
        }
        if(this.view != null)
        {
            this.view.removeListViewListener(this.listViewListener);
        }
    }
}
