package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSelectorSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSelectorSection extends NavigationPanelSection implements SelectorSection, CockpitEventAcceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSelectorSection.class);
    protected final List<TypedObject> selectedItems = new ArrayList<>();
    private final List<TypedObject> items = new ArrayList<>();
    private boolean multiple = false;
    private AbstractNavigationAreaModel navAreaModel = null;
    private boolean initiallySelected = true;


    public List<TypedObject> getItems()
    {
        return Collections.unmodifiableList(this.items);
    }


    public boolean isMultiple()
    {
        return this.multiple;
    }


    public void setItems(List<TypedObject> items)
    {
        this.items.clear();
        if(items != null && !items.isEmpty())
        {
            this.items.addAll(items);
        }
    }


    public void setMultiple(boolean multi)
    {
        this.multiple = multi;
    }


    public void setSelectedItem(TypedObject selectedItem)
    {
        if(this.items.contains(selectedItem) && ((
                        getSelectedItem() == null && selectedItem != null) || (getSelectedItem() != null &&
                        !getSelectedItem().equals(selectedItem))))
        {
            this.selectedItems.clear();
            this.selectedItems.add(selectedItem);
            selectionChanged();
        }
    }


    public void setSelectedItems(List<TypedObject> selectedItems)
    {
        setSelectedItemsDirectly(selectedItems);
        selectionChanged();
    }


    public void setSelectedItemsDirectly(List<TypedObject> selectedItems)
    {
        this.selectedItems.clear();
        if(selectedItems != null && !selectedItems.isEmpty())
        {
            this.selectedItems.addAll(selectedItems);
        }
    }


    public List<TypedObject> getSelectedItems()
    {
        return Collections.unmodifiableList(this.selectedItems);
    }


    public TypedObject getSelectedItem()
    {
        TypedObject selectedItem = null;
        if(this.selectedItems != null && !this.selectedItems.isEmpty())
        {
            selectedItem = this.selectedItems.iterator().next();
        }
        return selectedItem;
    }


    public void setNavigationAreaModel(AbstractNavigationAreaModel navAreaModel)
    {
        this.navAreaModel = navAreaModel;
    }


    public AbstractNavigationAreaModel getNavigationAreaModel()
    {
        return this.navAreaModel;
    }


    public SectionRenderer getRenderer()
    {
        if(super.getRenderer() == null)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("No section renderer specified. Using default fallback renderer.");
            }
            setRenderer((SectionRenderer)new DefaultSelectorSectionRenderer());
        }
        return super.getRenderer();
    }


    public abstract void selectionChanged();


    public boolean isItemActive(TypedObject item)
    {
        return false;
    }


    @Deprecated
    public static List<TypedObject> wrapItems(Collection<? extends ItemModel> itemModels)
    {
        List<TypedObject> wrappedItems = new ArrayList<>();
        if(itemModels != null)
        {
            for(ItemModel itemModel : itemModels)
            {
                TypedObject wrappedItem = UISessionUtils.getCurrentSession().getTypeService().wrapItem(itemModel.getPk());
                if(!wrappedItems.contains(wrappedItem))
                {
                    wrappedItems.add(wrappedItem);
                }
            }
        }
        return wrappedItems;
    }


    public void setInitiallySelected(boolean initSelection)
    {
        this.initiallySelected = initSelection;
    }


    public boolean isInitiallySelected()
    {
        return this.initiallySelected;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
    }
}
