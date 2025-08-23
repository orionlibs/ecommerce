package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;

public class DefaultListViewListener extends AbstractListViewListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListViewListener.class);


    public DefaultListViewListener(MutableTableModel model)
    {
        super(model);
    }


    public void move(int fromIndex, int toIndex)
    {
        LOG.warn("This listener does not support the move operation.");
    }


    public void remove(Collection<Integer> indexes)
    {
        LOG.warn("This listener does not support the remove operation.");
    }


    public void requestPaging(int offset)
    {
        LOG.warn("This listener does not support paging operations.");
    }


    public void openInContextEditor(int rowIndex, UIEditor editor, PropertyDescriptor propertyDescriptor)
    {
        LOG.warn("This listener does not support context area operations.");
    }


    public void markAllAsSelected(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        LOG.warn("This listener does not fully support the 'mark all as selected' operation.");
        this.model.setSelectedCells(colIndexes, rowIndexes);
    }


    public void multiEdit(int colIndex, List<Integer> rowIndexes, Object data)
    {
        List<TypedObject> items = new ArrayList<>();
        ColumnDescriptor colDescr = this.model.getColumnComponentModel().getVisibleColumn(colIndex);
        PropertyDescriptor propertyDescriptor = this.model.getColumnComponentModel().getPropertyDescriptor(colDescr);
        for(Integer rowIndex : new ArrayList(rowIndexes))
        {
            TypedObject typedObject = this.model.getListComponentModel().getListModel().getElements().get(rowIndex.intValue());
            items.add(typedObject);
        }
        if(colDescr.getLanguage() == null)
        {
            multiEdit(propertyDescriptor, items, data);
        }
        else
        {
            multiEdit(propertyDescriptor, colDescr.getLanguage().getIsocode(), items, data);
        }
        if(UISessionUtils.getCurrentSession().getCurrentPerspective() instanceof de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective && propertyDescriptor != null)
        {
            boolean threshold = (items.size() > Config.getInt("cockpit.changeevents.threshold", 3));
            if(threshold)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null, null,
                                Collections.singleton(propertyDescriptor)));
            }
            else
            {
                for(TypedObject item : items)
                {
                    UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null, item,
                                    Collections.singleton(propertyDescriptor)));
                }
            }
        }
    }


    private void multiEdit(PropertyDescriptor propertyDesc, List<TypedObject> objects, Object value)
    {
        multiEdit(propertyDesc, UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage().getIsocode(), objects, value);
    }


    private void multiEdit(PropertyDescriptor propertyDesc, String languageIso, List<TypedObject> objects, Object value)
    {
        List<String> errors = TypeTools.multiEdit(propertyDesc, languageIso, objects, value);
        if(!errors.isEmpty())
        {
            Notification notification = new Notification(errors);
            notification.setMessage(Labels.getLabel("multiedit.error.count", (Object[])new Integer[] {Integer.valueOf(errors.size())}));
            UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier().setDialogNotification(notification);
        }
    }


    public void drop(int fromIndex, int toIndex, DragAndDropContext ddContext)
    {
        LOG.warn("This listener does not support the drop operation.");
    }
}
