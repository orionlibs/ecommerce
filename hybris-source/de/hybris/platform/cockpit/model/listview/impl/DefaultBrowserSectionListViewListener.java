package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBrowserSectionListViewListener extends AbstractListViewListener
{
    protected final ListBrowserSectionModel sectionModel;


    public DefaultBrowserSectionListViewListener(ListBrowserSectionModel sectionModel, MutableTableModel model)
    {
        super(model);
        this.sectionModel = sectionModel;
    }


    public void move(int fromIndex, int toIndex)
    {
    }


    public void remove(Collection<Integer> indexes)
    {
    }


    public void requestPaging(int offset)
    {
    }


    public void openInContextEditor(int rowIndex, UIEditor editor, PropertyDescriptor propertyDescriptor)
    {
        if(this.sectionModel.getItems().size() > rowIndex)
        {
            TypedObject currentObject = this.sectionModel.getItems().get(rowIndex);
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
    }


    public void markAllAsSelected(List<Integer> columnIndexes, List<Integer> rowIndexes)
    {
    }


    public void multiEdit(int colIndex, List<Integer> rowIndexes, Object data)
    {
    }


    public void drop(int fromIndex, int toIndex, DragAndDropContext ddContext)
    {
    }
}
