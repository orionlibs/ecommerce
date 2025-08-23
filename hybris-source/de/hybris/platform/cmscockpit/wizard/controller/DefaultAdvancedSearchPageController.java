package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
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
    private boolean allowDuplicatesInResult = false;


    public DefaultAdvancedSearchPageController(TypedObject parentObject, PropertyDescriptor parentPropertyDescriptor, boolean allowDuplicatesInResult)
    {
        this(parentObject, parentPropertyDescriptor);
        this.allowDuplicatesInResult = allowDuplicatesInResult;
    }


    public DefaultAdvancedSearchPageController(TypedObject parentObject, PropertyDescriptor parentPropertyDescriptor)
    {
        this.parentObject = parentObject;
        this.parentPropertyDescriptor = parentPropertyDescriptor;
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
        if(page instanceof de.hybris.platform.cmscockpit.wizard.page.AdvancedSearchPage)
        {
            WizardPage wizardPage = next(wizard, page);
            if(wizardPage != null)
            {
                wizard.setShowDone(true);
                wizard.setShowNext(false);
            }
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


    protected void assignChildren(Collection<ItemModel> alredyDefined, Collection<ItemModel> currentSelected, boolean addSelectedItemsAtTop)
    {
        for(ItemModel candidate : currentSelected)
        {
            if(!this.allowDuplicatesInResult && alredyDefined.contains(candidate))
            {
                currentSelected.remove(candidate);
            }
        }
        if(addSelectedItemsAtTop)
        {
            Collection<ItemModel> temp = new ArrayList<>();
            temp.addAll(currentSelected);
            temp.addAll(alredyDefined);
            alredyDefined.clear();
            alredyDefined.addAll(temp);
            temp.clear();
        }
        else
        {
            alredyDefined.addAll(currentSelected);
        }
    }
}
