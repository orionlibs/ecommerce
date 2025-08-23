package de.hybris.platform.cmscockpit.wizard.cmssite.controllers;

import de.hybris.platform.cmscockpit.wizard.controller.DefaultAdvancedSearchPageController;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.generic.AdvancedSearchPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class ContentCatalogCreatePageController extends DefaultAdvancedSearchPageController
{
    public ContentCatalogCreatePageController()
    {
        super(null, null);
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        AdvancedSearchPage advancedSearchPage = (AdvancedSearchPage)page;
        List<ItemModel> selected = extractSelectedChildren(advancedSearchPage.getTableModel());
        boolean ret = (selected != null && !selected.isEmpty());
        if(!ret)
        {
            Message msg = new Message(3, Labels.getLabel("wizard.contentcatalog.select"), null);
            wizard.addMessage(msg);
        }
        return ret;
    }


    public WizardPage next(Wizard wizard, WizardPage page)
    {
        AdvancedSearchPage advancedSearchPage = (AdvancedSearchPage)page;
        List<ItemModel> selected = extractSelectedChildren(advancedSearchPage.getTableModel());
        ((GenericItemWizard)wizard).addContextInformation("selectedcontentcatalogs", selected);
        return super.next(wizard, page);
    }
}
