package de.hybris.platform.cmscockpit.services.label.impl;

import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;

public class CMSNavigationEntryLabelProvider extends AbstractModelLabelProvider<CMSNavigationEntryModel>
{
    private LabelService labelService;
    private TypeService typeService;


    protected String getItemLabel(CMSNavigationEntryModel entry)
    {
        return this.labelService.getObjectTextLabelForTypedObject(wrapTargetItem(entry));
    }


    protected String getItemLabel(CMSNavigationEntryModel entry, String languageIso)
    {
        return getItemLabel(entry);
    }


    protected String getItemDescription(CMSNavigationEntryModel entry)
    {
        return this.labelService.getObjectDescriptionForTypedObject(wrapTargetItem(entry));
    }


    protected String getItemDescription(CMSNavigationEntryModel entry, String languageIso)
    {
        return getItemDescription(entry);
    }


    protected String getIconPath(CMSNavigationEntryModel entry)
    {
        return this.labelService.getObjectIconPathForTypedObject(wrapTargetItem(entry));
    }


    protected String getIconPath(CMSNavigationEntryModel entry, String languageIso)
    {
        return getIconPath(entry);
    }


    protected TypedObject wrapTargetItem(CMSNavigationEntryModel entry)
    {
        return this.typeService.wrapItem(entry.getItem());
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }
}
