package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.ComponentsHelper;
import de.hybris.platform.cockpit.err.CockpitMarkAllException;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBrowserListViewListener extends AbstractListViewListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBrowserListViewListener.class);
    protected final AdvancedBrowserModel browser;


    public DefaultBrowserListViewListener(BrowserModel browser, MutableTableModel model)
    {
        super(model);
        if(browser instanceof AdvancedBrowserModel)
        {
            this.browser = (AdvancedBrowserModel)browser;
        }
        else
        {
            throw new IllegalArgumentException("Browser not of type 'AdvancedBrowserModel'.");
        }
    }


    public void changeSelection(int columnIndex, int rowIndex)
    {
        this.browser.setAllMarked(false);
        this.model.setSelectedCell(columnIndex, rowIndex);
    }


    public void changeSelection(List<Integer> columnIndexes, List<Integer> rowIndexes)
    {
        this.browser.setAllMarked(false);
        this.model.setSelectedCells(columnIndexes, rowIndexes);
    }


    public void move(int fromIndex, int toIndex)
    {
    }


    public void remove(Collection<Integer> indexes)
    {
        if(this.browser.isItemsRemovable())
        {
            String label = this.browser.getLabel();
            this.browser.removeItems(indexes);
            ComponentsHelper.displayCustomNotification(label, "listview.items.removed", new Object[] {label});
        }
        else
        {
            this.browser.blacklistItems(indexes);
            ComponentsHelper.displayNotification("listview.blacklist.caption", "listview.blacklist.items.added", new Object[0]);
        }
    }


    public void requestPaging(int offset)
    {
        if(this.browser instanceof PageableBrowserModel)
        {
            PageableBrowserModel pBr = (PageableBrowserModel)this.browser;
            int newPage = pBr.getCurrentPage() + offset;
            if(newPage >= 0 && newPage <= pBr.getLastPage())
            {
                pBr.updateItems(newPage);
            }
        }
    }


    public void openInContextEditor(int rowIndex, UIEditor editor, PropertyDescriptor propertyDescriptor)
    {
        TypedObject currentObject = this.browser.getItem(rowIndex);
        Object value = editor.getValue();
        if(value instanceof Collection && editor instanceof de.hybris.platform.cockpit.model.editor.ReferenceUIEditor)
        {
            ObjectTemplate type = TypeTools.getValueTypeAsObjectTemplate(propertyDescriptor,
                            UISessionUtils.getCurrentSession().getTypeService());
            UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(perspective instanceof BaseUICockpitPerspective)
            {
                Map<String, Object> newParameters = new HashMap<>();
                newParameters.put("propertyDescriptor", propertyDescriptor);
                if(((Collection)value).isEmpty() || !(((Collection)value).iterator().next() instanceof TypedObject))
                {
                    ((BaseUICockpitPerspective)perspective).openReferenceCollectionInBrowserContext(Collections.EMPTY_LIST, type, currentObject, newParameters);
                }
                else
                {
                    ((BaseUICockpitPerspective)perspective).openReferenceCollectionInBrowserContext((Collection)value, type, currentObject, newParameters);
                }
            }
        }
    }


    public void markAllAsSelected(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        try
        {
            this.browser.setAllMarked(true);
            this.model.setSelectedCells(colIndexes, rowIndexes);
        }
        catch(CockpitMarkAllException e)
        {
            LOG.info(e.getMessage());
        }
    }


    public void multiEdit(int colIndex, List<Integer> rowIndexes, Object data)
    {
        List<TypedObject> items = new ArrayList<>();
        UICockpitPerspective perspective = this.browser.getArea().getPerspective();
        ColumnDescriptor colDescr = this.model.getColumnComponentModel().getVisibleColumn(colIndex);
        PropertyDescriptor propertyDescriptor = this.model.getColumnComponentModel().getPropertyDescriptor(colDescr);
        if(this.browser.isAllMarked())
        {
            items.addAll(this.browser.getSelectedItems());
        }
        else
        {
            for(Integer rowIndex : new ArrayList(rowIndexes))
            {
                TypedObject typedObject = this.model.getListComponentModel().getListModel().getElements().get(rowIndex.intValue());
                items.add(typedObject);
            }
        }
        if(colDescr.getLanguage() == null)
        {
            this.browser.multiEdit(propertyDescriptor, items, data);
        }
        else
        {
            this.browser.multiEdit(propertyDescriptor, colDescr.getLanguage().getIsocode(), items, data);
        }
        if(perspective instanceof BaseUICockpitPerspective && propertyDescriptor != null)
        {
            boolean threshold = (items.size() > Config.getInt("cockpit.changeevents.threshold", 3));
            if(threshold)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this.browser
                                .getArea(), null, Collections.singleton(propertyDescriptor)));
            }
            else
            {
                for(TypedObject item : items)
                {
                    UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this.browser
                                    .getArea(), item, Collections.singleton(propertyDescriptor)));
                }
            }
        }
    }


    public void drop(int fromIndex, int toIndex, DragAndDropContext ddContext)
    {
    }
}
