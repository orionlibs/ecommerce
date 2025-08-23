package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.ListView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.session.CockpitListComponent;
import de.hybris.platform.cockpit.session.CockpitListComponentExt;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Executions;

public class ListViewHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(ListViewHelper.class);
    private static Boolean forceFireEvent = null;


    public static void cellChanged(UIListView view, int colIndex, int rowIndex, Object eventSource) throws IllegalArgumentException
    {
        if(view == null)
        {
            throw new IllegalArgumentException("View can not be null.");
        }
        if(view.getModel() == null)
        {
            throw new IllegalArgumentException("View does not have a model set.");
        }
        try
        {
            TypedObject changedItem = (TypedObject)view.getModel().getListComponentModel().getValueAt(rowIndex);
            PropertyDescriptor propDescr = view.getModel().getColumnComponentModel().getPropertyDescriptor(view.getModel().getColumnComponentModel().getVisibleColumn(colIndex));
            if(propDescr != null)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(eventSource, changedItem,
                                Collections.singleton(propDescr)));
            }
        }
        catch(Exception e)
        {
            LOG.warn("Could not retrieved changed item.", e);
        }
        view.updateCell(colIndex, rowIndex);
    }


    public static <E extends TypedObject> ListViewInfo loadListView(UIListView listView, String contextCode, ObjectTemplate oldRootType, ObjectTemplate newRootType, MutableTableModel tableModel, DefaultListModel<E> listModel, CockpitListComponent<E> cockpitListComp, ListenerHandler listenerHandler,
                    DragAndDropContext dndCtx) throws IllegalArgumentException, IllegalStateException
    {
        return loadListView(listView, contextCode, oldRootType, newRootType, tableModel, listModel, cockpitListComp, listenerHandler, dndCtx, null);
    }


    public static <E extends TypedObject> ListViewInfo loadListView(UIListView listView, String contextCode, ObjectTemplate oldRootType, ObjectTemplate newRootType, MutableTableModel tableModel, DefaultListModel<E> listModel, CockpitListComponent<E> cockpitListComp, ListenerHandler listenerHandler,
                    DragAndDropContext dndCtx, ListViewConfiguration listViewConf) throws IllegalArgumentException, IllegalStateException
    {
        if(StringUtils.isBlank(contextCode))
        {
            throw new IllegalArgumentException("context code must be specified.");
        }
        if(newRootType == null)
        {
            throw new IllegalArgumentException("new root type must be specified.");
        }
        if(tableModel == null || tableModel.getListComponentModel() == null)
        {
            throw new IllegalArgumentException("valid table model with list model must be specified.");
        }
        if(cockpitListComp == null)
        {
            throw new IllegalArgumentException("valid cockpit list component must be specified.");
        }
        DefaultListModel<E> internalListModel = null;
        if(listModel == null)
        {
            LOG.warn("No list model has been specified. Creating default one.");
            internalListModel = new DefaultListModel();
        }
        else
        {
            internalListModel = listModel;
        }
        ListViewInfo retInfo = new ListViewInfo();
        if(listView == null)
        {
            retInfo.setListView((UIListView)new ListView());
            if(dndCtx != null)
            {
                retInfo.getListView().setDDContext(dndCtx);
            }
            retInfo.setChanged(true);
        }
        else
        {
            retInfo.setListView(listView);
        }
        ListViewConfiguration config = listViewConf;
        if(config == null)
        {
            config = getListViewConfiguration(newRootType, contextCode);
        }
        if(config != null || oldRootType == null || !oldRootType.equals(newRootType) || tableModel
                        .getListComponentModel().getListModel() == null)
        {
            DefaultColumnModel defaultColumnModel;
            retInfo.setChanged(true);
            if(config == null)
            {
                throw new IllegalStateException("No list view configuration found for type " + newRootType + " and context code " + contextCode + ".");
            }
            retInfo.setInlineCreationEnabled(config.isAllowCreateInlineItems());
            retInfo.getListView().setModel(null);
            tableModel.getListComponentModel().setListModel(null);
            MutableColumnModel columnModel = null;
            if(newRootType.equals(oldRootType) && cockpitListComp instanceof AbstractBrowserModel &&
                            !BooleanUtils.toBoolean(UITools.getCockpitParameter("default.disableListViewCache", Executions.getCurrent())))
            {
                MutableColumnModel cacheView = ((AbstractBrowserModel)cockpitListComp).getCacheView();
                if(cacheView == null)
                {
                    defaultColumnModel = new DefaultColumnModel(config);
                }
                else
                {
                    columnModel = cacheView;
                }
            }
            else
            {
                defaultColumnModel = new DefaultColumnModel(config);
            }
            tableModel.getListComponentModel().setListModel((ListModel)internalListModel);
            tableModel.setColumnComponentModel((MutableColumnModel)defaultColumnModel);
            loadItems(cockpitListComp, internalListModel, isForceFireEvent());
            retInfo.getListView().setModel((TableModel)tableModel);
        }
        else if(cockpitListComp instanceof AbstractBrowserModel)
        {
            MutableColumnModel cacheView = ((AbstractBrowserModel)cockpitListComp).getCacheView();
            if(cacheView != null)
            {
                tableModel.setColumnComponentModel(cacheView);
            }
        }
        retInfo.setRootType(newRootType);
        if(retInfo.isChanged() && listenerHandler != null)
        {
            listenerHandler.handleListeners(retInfo.getListView());
        }
        else
        {
            loadItems(cockpitListComp, internalListModel, isForceFireEvent());
        }
        tableModel.updateDynamicColumns();
        return retInfo;
    }


    public static boolean isForceFireEvent()
    {
        if(forceFireEvent == null)
        {
            forceFireEvent = Boolean.valueOf(UITools.getCockpitParameter("listviewhelper.forcefireevent.enabled",
                            Executions.getCurrent()));
        }
        return forceFireEvent.booleanValue();
    }


    public static ListViewConfiguration getListViewConfiguration(ObjectTemplate objectTemplate, String contextCode)
    {
        UIConfigurationService uiConfigService = getUIConfigurationService();
        return (ListViewConfiguration)uiConfigService.getComponentConfiguration(objectTemplate, contextCode, ListViewConfiguration.class);
    }


    private static UIConfigurationService getUIConfigurationService()
    {
        return (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
    }


    private static <E> void loadItems(CockpitListComponent<E> cockpitListComp, DefaultListModel<? super E> listModel, boolean updateView)
    {
        if(cockpitListComp == null)
        {
            listModel.clear();
        }
        else
        {
            Collection<E> items = cockpitListComp.getItems();
            if(items != null)
            {
                boolean removable = false;
                boolean movable = false;
                if(cockpitListComp instanceof CockpitListComponentExt)
                {
                    removable = ((CockpitListComponentExt)cockpitListComp).isItemsRemovable();
                    movable = ((CockpitListComponentExt)cockpitListComp).isItemsMovable();
                }
                listModel.clearAndAddAll(items, removable, movable, updateView);
            }
        }
    }
}
