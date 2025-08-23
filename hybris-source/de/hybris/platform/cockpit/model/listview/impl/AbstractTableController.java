package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.session.EditableComponent;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public abstract class AbstractTableController implements ComponentController, CockpitEventAcceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTableController.class);
    protected final MutableTableModel model;
    protected final UIListView view;
    protected TableModelListener tableModelListener = null;
    protected ListViewListener listViewListener = null;
    protected ListComponentModelListener listModelListener = null;
    protected ColumnModelListener columnModelListener = null;


    public AbstractTableController(MutableTableModel model, UIListView view)
    {
        this.model = model;
        this.view = view;
    }


    public void initialize()
    {
        this.columnModelListener = createColumnModelListener();
        this.listModelListener = createListComponentModelListener();
        this.tableModelListener = createTableModelListener();
        this.listViewListener = createListViewListener();
        if(this.columnModelListener != null)
        {
            this.model.getColumnComponentModel().addColumnModelListener(this.columnModelListener);
        }
        if(this.listModelListener != null)
        {
            this.model.getListComponentModel().addListComponentModelListener(this.listModelListener);
        }
        if(this.tableModelListener != null)
        {
            this.model.addTableModelListener(this.tableModelListener);
        }
        if(this.listViewListener != null)
        {
            this.view.addListViewListener(this.listViewListener);
        }
        registerFocusListener();
    }


    protected abstract ListViewListener createListViewListener();


    protected abstract TableModelListener createTableModelListener();


    protected abstract ListComponentModelListener createListComponentModelListener();


    protected abstract ColumnModelListener createColumnModelListener();


    public void unregisterListeners()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Unregistering all listeners...");
        }
        if(this.model != null)
        {
            this.model.getListComponentModel().removeListComponentModelListener(this.listModelListener);
            this.model.getColumnComponentModel().removeColumnModelListener(this.columnModelListener);
            this.model.removeTableModelListener(this.tableModelListener);
        }
        if(this.view != null)
        {
            this.view.removeListViewListener(this.listViewListener);
        }
        unregisterFocusListener();
    }


    protected void registerFocusListener()
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().addCockpitEventAcceptor(this);
    }


    protected void unregisterFocusListener()
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().removeCockpitEventAcceptor(this);
    }


    protected UIListView getView()
    {
        return this.view;
    }


    protected MutableTableModel getModel()
    {
        return this.model;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof de.hybris.platform.cockpit.events.impl.FocusEvent && this.view instanceof EditableComponent && !UITools.isFromOtherDesktop((Component)this.view))
        {
            if(((EditableComponent)this.view).isEditing())
            {
                LOG.info("Switching to non-edit mode due to focus event.");
                ((EditableComponent)this.view).stopEditing();
            }
        }
    }
}
