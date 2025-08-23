package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultAdvancedSearchPageController extends DefaultPageController
{
    private final TypedObject parentObject;
    private final PropertyDescriptor parentPropertyDescriptor;
    private boolean fireSearch;


    public void setFireSearch(boolean fireSearch)
    {
        this.fireSearch = fireSearch;
    }


    public DefaultAdvancedSearchPageController()
    {
        this.parentObject = null;
        this.parentPropertyDescriptor = null;
        this.fireSearch = false;
    }


    public DefaultAdvancedSearchPageController(TypedObject parentObject, PropertyDescriptor parentPropertyDescriptor, boolean fireSearch)
    {
        this.parentObject = parentObject;
        this.parentPropertyDescriptor = parentPropertyDescriptor;
        this.fireSearch = fireSearch;
    }


    public TypedObject getParentObject()
    {
        return this.parentObject;
    }


    public PropertyDescriptor getParentPropertyDescriptor()
    {
        return this.parentPropertyDescriptor;
    }


    public void initPage(Wizard wizard, WizardPage page)
    {
        if(page instanceof AdvancedSearchPage)
        {
            WizardPage wizardPage = next(wizard, page);
            if(wizardPage != null)
            {
                wizard.setShowDone(true);
                wizard.setShowNext(false);
            }
            if(this.fireSearch)
            {
                ((AdvancedSearchPage)page).fireSearch();
            }
        }
    }


    public void done(Wizard wizard, WizardPage page)
    {
        AdvancedSearchPage advancedSearchPage = (AdvancedSearchPage)page;
        List<ItemModel> currentlySelected = extractSelectedChildren(advancedSearchPage.getTableModel());
        if(!currentlySelected.isEmpty())
        {
            advancedSearchPage.setSelectedValue(getTypeService().wrapItems(currentlySelected));
        }
    }


    protected ObjectValueContainer getObjectValueContainer()
    {
        return TypeTools.createValueContainer(this.parentObject, Collections.singleton(this.parentPropertyDescriptor),
                        UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
    }


    protected List<ItemModel> extractSelectedChildren(TableModel tableModel)
    {
        List<ItemModel> ret = new ArrayList<>();
        if(tableModel == null)
        {
            return ret;
        }
        for(Integer selectedIndex : tableModel.getListComponentModel().getSelectedIndexes())
        {
            Object selectedObject = tableModel.getListComponentModel().getListModel().elementAt(selectedIndex.intValue());
            if(selectedObject instanceof TypedObject)
            {
                ret.add((ItemModel)((TypedObject)selectedObject).getObject());
            }
        }
        return ret;
    }


    protected List<ItemModel> unwrapTypedObjects(Collection<TypedObject> wrappedCollection)
    {
        List<ItemModel> ret = new ArrayList<>();
        for(TypedObject typedObject : wrappedCollection)
        {
            ret.add((ItemModel)typedObject.getObject());
        }
        return ret;
    }


    protected void assignChildren(Collection<ItemModel> alredyDefined, Collection<ItemModel> currentSelected)
    {
        for(ItemModel candidate : currentSelected)
        {
            if(!alredyDefined.contains(candidate))
            {
                alredyDefined.add(candidate);
            }
        }
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
