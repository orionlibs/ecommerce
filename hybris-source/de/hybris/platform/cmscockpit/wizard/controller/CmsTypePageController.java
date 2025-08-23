package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.AdvancedSearchPage;
import de.hybris.platform.cmscockpit.wizard.page.TypeSelectorPage;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import org.zkoss.util.resource.Labels;

public class CmsTypePageController extends CmsPageController
{
    private SystemService systemService;
    private TypeService typeService;


    public boolean validate(Wizard wizard, WizardPage page)
    {
        boolean ret = false;
        if(page instanceof TypeSelectorPage)
        {
            ret = (((TypeSelectorPage)page).getChosenType() != null);
            if(!ret)
            {
                Message msg = new Message(3, Labels.getLabel("wizard.common.missingType"), null);
                wizard.addMessage(msg);
            }
        }
        return ret;
    }


    public WizardPage next(Wizard wizard, WizardPage page)
    {
        if(((TypeSelectorPage)page).getChosenType() == null)
        {
            return null;
        }
        boolean canCreate = true;
        AdvancedSearchPage asPage = (AdvancedSearchPage)wizard.getPage("advancedSearchPage");
        ObjectType type = ((CmsWizard)wizard).getCurrentType();
        if(asPage != null && type != null)
        {
            BaseType baseType;
            asPage.setRootSearchType(getTypeService().getObjectType(type.getCode()));
            if(type instanceof ObjectTemplate)
            {
                baseType = ((ObjectTemplate)type).getBaseType();
            }
            if(baseType.isAbstract())
            {
                canCreate = false;
            }
            else
            {
                canCreate = getSystemService().checkPermissionOn(baseType.getCode(), "create");
            }
        }
        if(canCreate)
        {
            return super.next(wizard, page);
        }
        return (WizardPage)asPage;
    }


    protected SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
