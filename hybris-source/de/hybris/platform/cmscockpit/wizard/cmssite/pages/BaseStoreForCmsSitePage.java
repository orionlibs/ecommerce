package de.hybris.platform.cmscockpit.wizard.cmssite.pages;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import java.util.Collections;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class BaseStoreForCmsSitePage extends AbstractGenericItemPage
{
    protected static final String WIZARD_BASESTORES_CONTAINER = "wizardBaseStoresContainer";
    protected static final String BASE_STORE_CHECKBOX_CHOOSEER = "baseStoresEditor";
    protected static final String CMSSITE_BASESTORES_QUALIFIER = "CMSSite.stores";


    public BaseStoreForCmsSitePage()
    {
    }


    public BaseStoreForCmsSitePage(String pageTitle)
    {
        super(pageTitle);
    }


    public BaseStoreForCmsSitePage(String pageTitle, GenericItemWizard wizard)
    {
        super(pageTitle, (Wizard)wizard);
    }


    public Component createRepresentationItself()
    {
        UITools.detachChildren((Component)this.pageContent);
        Div lalbelContainer = new Div();
        lalbelContainer.setParent((Component)this.pageContent);
        lalbelContainer.setSclass("wizardLabelContainer");
        Label infoLabel = new Label(Labels.getLabel("wizard.basestores.infolabel"));
        lalbelContainer.appendChild((Component)infoLabel);
        Div baseStoresContainer = new Div();
        baseStoresContainer.setParent((Component)this.pageContent);
        baseStoresContainer.setSclass("wizardBaseStoresContainer");
        PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("CMSSite.stores");
        EditorHelper.createEditor(getWizard().getCurrentTypeEmptyModel(), propertyDescriptor, (HtmlBasedComponent)baseStoresContainer, getWizard()
                        .getObjectValueContainer(), false, "baseStoresEditor", Collections.EMPTY_MAP);
        return (Component)this.pageContainer;
    }


    public WizardPageController getController()
    {
        WizardPageController controller = super.getController();
        if(controller == null)
        {
            setController((WizardPageController)new Object(this));
        }
        return controller;
    }
}
