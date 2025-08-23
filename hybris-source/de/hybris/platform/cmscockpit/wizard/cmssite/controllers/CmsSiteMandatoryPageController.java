package de.hybris.platform.cmscockpit.wizard.cmssite.controllers;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.generic.DefaultGenericItemMandatoryPageController;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;

public class CmsSiteMandatoryPageController extends DefaultGenericItemMandatoryPageController
{
    public boolean validate(Wizard wizard, WizardPage page)
    {
        if(page instanceof GenericItemMandatoryPage)
        {
            BaseType baseType;
            boolean ret = false;
            ObjectValueContainer objectValueContainer = ((GenericItemMandatoryPage)page).getWizard().getObjectValueContainer();
            ObjectType currentType = ((GenericItemMandatoryPage)page).getWizard().getCurrentType();
            if(currentType instanceof ObjectTemplate)
            {
                baseType = ((ObjectTemplate)currentType).getBaseType();
            }
            if(baseType != null)
            {
                PropertyDescriptor namePropDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(baseType.getCode() + ".name");
                for(ObjectValueContainer.ObjectValueHolder valueHolder : objectValueContainer.getAllValues())
                {
                    if(valueHolder.getPropertyDescriptor().equals(namePropDesc) && valueHolder.getCurrentValue() instanceof String &&
                                    StringUtils.isNotBlank((String)valueHolder.getCurrentValue()))
                    {
                        ret = true;
                    }
                }
            }
            if(!ret)
            {
                wizard.addMessage(new Message(3, Labels.getLabel("cockpit.wizard.cmssite.error.mandatory"), null));
            }
            return ret;
        }
        return super.validate(wizard, page);
    }
}
