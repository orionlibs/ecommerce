package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cockpit.wizards.Wizard;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class DecisionPage extends AbstractCmsWizardPage
{
    protected static final String DECISION_CMSWIZARD_PAGE_SCLASS = "decisionCmsWizardPage";
    protected static final String DECISION_CMSWIZARD_ROW_SCLASS = "decisionSelectorRow";
    protected static final String SCLASS_ELEMENT_NAME = "contentElementName";
    protected static final String SCLASS_ELEMENT_DESC = "contentElementDescription";
    protected static final String SCLASS_ELEMENT_BOX = "contentElementBox";
    protected static final String SCLASS_ELEMENT_IMAGE = "contentElementImage";
    protected Decision currentDecsison = null;
    protected String nextPageWizadId;
    protected List<Decision> decisions = new ArrayList<>();


    public Decision getCurrentDecsison()
    {
        return this.currentDecsison;
    }


    public void setCurrentDecsison(Decision currentDecsison)
    {
        this.currentDecsison = currentDecsison;
    }


    public String getNextPageWizadId()
    {
        return this.nextPageWizadId;
    }


    public void setDecisions(List<Decision> decisions)
    {
        if(!this.decisions.equals(decisions))
        {
            this.decisions = decisions;
        }
    }


    public List<Decision> getDecisions()
    {
        return this.decisions;
    }


    public DecisionPage(String pageTitle, CmsWizard wizard)
    {
        super(pageTitle, (Wizard)wizard);
    }


    public Component createRepresentationItself()
    {
        this.pageContent.getChildren().clear();
        Div firstStep = new Div();
        firstStep.setParent((Component)this.pageContent);
        Listbox listbox = new Listbox();
        listbox.setSclass("decisionCmsWizardPage");
        listbox.setWidth("100%");
        listbox.setParent((Component)firstStep);
        listbox.setModel((ListModel)new SimpleListModel(this.decisions));
        listbox.setItemRenderer((ListitemRenderer)new Object(this));
        return (Component)this.pageContainer;
    }
}
