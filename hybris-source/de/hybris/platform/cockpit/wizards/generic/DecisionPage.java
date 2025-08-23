package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class DecisionPage extends AbstractGenericItemPage
{
    public static final String DEFAULT_ELEMENT_IMAGE = "/cockpit/images/defaultWizardNode.gif";
    protected static final String DECISION_CMSWIZARD_PAGE_SCLASS = "decisionCmsWizardPage";
    protected static final String SCLASS_ELEMENT_NAME = "contentElementName";
    protected static final String DECISION_CMSWIZARD_ROW_SCLASS = "decisionSelectorRow";
    protected static final String SCLASS_ELEMENT_DESC = "contentElementDescription";
    protected static final String SCLASS_ELEMENT_BOX = "contentElementBox";
    protected static final String SCLASS_ELEMENT_IMAGE = "contentElementImage";
    private static final String COCKPIT_ID_CREATEWEBSITE_CONTENTCATALOG = "CreateWebsite_ContentCatalog_";
    private final DefaultDecisionPageController defaultPageController = new DefaultDecisionPageController();
    protected Decision currentDecsison = null;
    protected List<Decision> decisions = new ArrayList<>();


    public DecisionPage(String pageTitle, GenericItemWizard wizard)
    {
        super(pageTitle, (Wizard)wizard);
        setController((WizardPageController)this.defaultPageController);
    }


    public DecisionPage(String pageTitle)
    {
        this(pageTitle, null);
    }


    public DecisionPage()
    {
        this(null, null);
    }


    public Decision getCurrentDecsison()
    {
        return this.currentDecsison;
    }


    public void setCurrentDecsison(Decision currentDecsison)
    {
        this.currentDecsison = currentDecsison;
    }


    public void setDecisions(List<Decision> decisions)
    {
        if(this.decisions != decisions)
        {
            this.decisions = decisions;
        }
    }


    public List<Decision> getDecisions()
    {
        return this.decisions;
    }


    public WizardPageController getController()
    {
        return (super.getController() == null) ? (WizardPageController)this.defaultPageController : super.getController();
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
