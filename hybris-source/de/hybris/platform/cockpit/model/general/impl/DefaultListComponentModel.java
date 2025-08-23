package de.hybris.platform.cockpit.model.general.impl;

import de.hybris.platform.cockpit.components.listeditor.DefaultListSelectionModel;
import de.hybris.platform.cockpit.components.listeditor.ListSelectionModel;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.ListModelDataListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DefaultListComponentModel extends AbstractListComponentModel
{
    private boolean editable;
    private boolean selectable;
    private boolean multiple;
    private boolean activatable;
    private ListModel<? extends TypedObject> listModel;
    private final ListSelectionModel selectionModel = (ListSelectionModel)new DefaultListSelectionModel();
    private final ListModelDataListener listener = (ListModelDataListener)new MyListModelDataListener(this);
    private boolean forceRenderOnSelChanged = false;


    public void setListModel(ListModel<? extends TypedObject> model)
    {
        if(this.listModel != model && (this.listModel == null || !this.listModel.equals(model)))
        {
            if(this.listModel != null)
            {
                this.listModel.removeListModelDataListener(this.listener);
            }
            this.listModel = model;
            if(this.listModel != null)
            {
                this.listModel.addListModelDataListener(this.listener);
            }
        }
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public void setSelectable(boolean selectable)
    {
        this.selectable = selectable;
    }


    public void setMultiple(boolean multiple)
    {
        this.multiple = multiple;
    }


    public void setActivatable(boolean activatable)
    {
        this.activatable = activatable;
    }


    public void setActiveItem(TypedObject item)
    {
        if(isActivatable() && this.listModel != null)
        {
            if(getListModel().getElements().contains(item))
            {
                fireItemsActivated(Collections.singletonList(item));
            }
        }
    }


    public void setActiveItems(Collection<TypedObject> items)
    {
        if(isActivatable() && this.listModel != null)
        {
            fireItemsActivated(items);
        }
    }


    public void setSelectedIndex(int selectedIndex)
    {
        if(setSelectedIndexDirectly(selectedIndex))
        {
            fireSelectionChanged(this.selectionModel.getAllSelectedIndexes());
        }
    }


    public boolean setSelectedIndexDirectly(int selectedIndex)
    {
        boolean changed = false;
        if(isSelectable() && this.selectionModel != null)
        {
            if(selectedIndex >= 0 && selectedIndex < this.listModel.size() && !this.selectionModel.isSelectedIndex(selectedIndex))
            {
                this.selectionModel.setSelectionInterval(selectedIndex, selectedIndex);
                changed = true;
            }
        }
        return changed;
    }


    public void setSelectedIndexes(List<Integer> indexes)
    {
        if(setSelectedIndexesDirectly(indexes))
        {
            fireSelectionChanged(this.selectionModel.getAllSelectedIndexes());
        }
    }


    public boolean setSelectedIndexesDirectly(List<Integer> indexes)
    {
        boolean changed = false;
        if((indexes == null || indexes.isEmpty()) && this.selectionModel != null && this.selectionModel
                        .getMaxSelectionIndex() >= 0)
        {
            this.selectionModel.clearSelection();
            changed = true;
        }
        else if(isSelectable() && this.selectionModel != null && indexes != null)
        {
            this.selectionModel.clearSelection();
            for(Integer index : indexes)
            {
                this.selectionModel.addSelectionInterval(index.intValue(), index.intValue());
            }
            changed = true;
        }
        return changed;
    }


    public void setForceRenderOnSelectionChanged(boolean forceRender)
    {
        this.forceRenderOnSelChanged = forceRender;
    }


    public boolean isForceRenderOnSelectionChanged()
    {
        return this.forceRenderOnSelChanged;
    }


    public TypedObject getActiveItem()
    {
        if(!getActiveItems().isEmpty())
        {
            Iterator<TypedObject> iterator = getActiveItems().iterator();
            if(iterator.hasNext())
            {
                TypedObject item = iterator.next();
                return item;
            }
        }
        return null;
    }


    public Collection<TypedObject> getActiveItems()
    {
        UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        TypedObject activeItem = null;
        if(currentPerspective instanceof BaseUICockpitPerspective)
        {
            activeItem = ((BaseUICockpitPerspective)currentPerspective).getActiveItem();
        }
        return (activeItem == null) ? Collections.EMPTY_LIST : Collections.<TypedObject>singletonList(activeItem);
    }


    public ListModel<? extends TypedObject> getListModel()
    {
        return this.listModel;
    }


    public Integer getSelectedIndex()
    {
        List<Integer> selectedIndexes = this.selectionModel.getAllSelectedIndexes();
        if(!selectedIndexes.isEmpty())
        {
            return selectedIndexes.get(0);
        }
        return null;
    }


    public List<Integer> getSelectedIndexes()
    {
        return Collections.unmodifiableList(this.selectionModel.getAllSelectedIndexes());
    }


    public Object getValueAt(int index)
    {
        return getListModel().elementAt(index);
    }


    public boolean isActivatable()
    {
        return this.activatable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public boolean isMultiple()
    {
        return this.multiple;
    }


    public boolean isSelectable()
    {
        return this.selectable;
    }


    public boolean isSelected(int index)
    {
        return this.selectionModel.isSelectedIndex(index);
    }
}
