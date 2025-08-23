package de.hybris.platform.cmscockpit.wizard.cmssite.pages;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class CreateContentCatalogPage extends AbstractGenericItemPage
{
    public CreateContentCatalogPage()
    {
    }


    public CreateContentCatalogPage(String pageType)
    {
        super(pageType);
    }


    public Component createRepresentationItself()
    {
        UITools.detachChildren((Component)this.pageContent);
        Div labelInfoContainer = new Div();
        labelInfoContainer.setSclass("wizardLabelContainer");
        Label infoLabel = new Label("contentcatalog.create.infottext");
        labelInfoContainer.appendChild((Component)infoLabel);
        Hbox horizontalContainer = new Hbox();
        horizontalContainer.setSclass("contentCatalogPageCnt");
        Label catalogNameLabel = new Label(Labels.getLabel("wizard.contentcatalog.name"));
        catalogNameLabel.setParent((Component)horizontalContainer);
        Textbox catalogNameInput = new Textbox();
        String contentCatalogName = (String)getWizard().getContext().get("contentcatalogname");
        if(StringUtils.isNotBlank(contentCatalogName))
        {
            catalogNameInput.setValue(contentCatalogName);
        }
        catalogNameInput.setParent((Component)horizontalContainer);
        horizontalContainer.setParent((Component)this.pageContent);
        setController((WizardPageController)new Object(this, catalogNameInput));
        return (Component)this.pageContainer;
    }
}
