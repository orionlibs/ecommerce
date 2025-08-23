package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.components.listview.impl.ContextAreaValueContainer;
import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public abstract class AbstractListViewAction implements AdvancedListViewAction
{
    protected boolean alwaysEnabled = false;
    protected String IN_EDITOR_AREA_SECTION_PANEL = "inEditorAreaSectionPanel";


    public boolean isAlwaysEnabled()
    {
        return this.alwaysEnabled;
    }


    public void setAlwaysEnabled(boolean alwaysEnabled)
    {
        this.alwaysEnabled = alwaysEnabled;
    }


    public ListViewAction.Context createContext(TableModel model, TypedObject item, ColumnDescriptor column)
    {
        ListViewAction.Context context = new ListViewAction.Context();
        context.setModel(model);
        context.setItem(item);
        context.setColumn(column);
        doCreateContext(context);
        return context;
    }


    public ListViewAction.Context createContext(ListComponentModel listModel, TypedObject item)
    {
        ListViewAction.Context context = new ListViewAction.Context();
        context.setListModel(listModel);
        context.setItem(item);
        doCreateContext(context);
        return context;
    }


    public void sendEvent(String event, ContextAreaValueContainer cavc, ListViewAction.Context context)
    {
        if(context.getModel() != null)
        {
            context.getModel().fireEvent(event, cavc);
        }
        else if(context.getListModel() != null)
        {
            context.getListModel().fireEvent(event, cavc);
        }
    }


    protected void sendUpdateItemsEvent(ListViewAction.Context context)
    {
        if(context.getBrowserModel() != null)
        {
            BrowserModel browserModel = context.getBrowserModel();
            if(browserModel instanceof AbstractPageableBrowserModel)
            {
                AbstractPageableBrowserModel pageBrowserModel = (AbstractPageableBrowserModel)browserModel;
                pageBrowserModel.updateItems(pageBrowserModel.getCurrentPage());
            }
            else
            {
                context.getBrowserModel().updateItems();
            }
        }
        else if(context.getModel() != null)
        {
            context.getModel().fireEvent("updateitems", null);
        }
        else if(context.getListModel() != null)
        {
            context.getListModel().fireEvent("updateitems", null);
        }
        else if(UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser() instanceof de.hybris.platform.cockpit.session.impl.CommunicationBrowserModel)
        {
            UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser().updateItems();
        }
    }


    protected abstract void doCreateContext(ListViewAction.Context paramContext);


    public String getStatusCode(ListViewAction.Context context)
    {
        return "";
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getMultiSelectPopup(ListViewAction.Context context)
    {
        return null;
    }


    public List<TypedObject> getSelectedItems(ListViewAction.Context context)
    {
        MultiSelectContextModelWrapper wrapper = new MultiSelectContextModelWrapper(this, context);
        return wrapper.getSelectedItems();
    }


    protected TypedObject getItem(ListViewAction.Context context)
    {
        TypedObject item = null;
        if(context.getBrowserModel() != null && CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedIndexes()) && context
                        .getBrowserModel().getSelectedIndexes().size() == 1)
        {
            item = context.getBrowserModel().getSelectedItems().get(0);
        }
        else if(context.getItem() != null)
        {
            item = context.getItem();
        }
        else if(context.getListModel() != null && CollectionUtils.isNotEmpty(context.getListModel().getSelectedIndexes()) && context
                        .getListModel().getSelectedIndexes().size() == 1)
        {
            Integer itemIndex = context.getListModel().getSelectedIndexes().get(0);
            item = (TypedObject)context.getListModel().getValueAt(itemIndex.intValue());
        }
        return item;
    }
}
