package de.hybris.platform.cmscockpit.wizard.cmssite.pages;

import de.hybris.platform.cockpit.wizards.generic.DecisionPage;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class CMSSiteDecisionPage extends DecisionPage
{
    public CMSSiteDecisionPage()
    {
    }


    public CMSSiteDecisionPage(String pageTitle)
    {
        super(pageTitle);
    }


    public Component createRepresentationItself()
    {
        List<DecisionPage.Decision> decisions = new ArrayList<>();
        decisions.add(new DecisionPage.Decision(this, "advancedSearchPage", Labels.getLabel("cockpit.wizard.cmssite.decision.decision.existing"), "/cockpit/images/defaultWizardNode.gif"));
        decisions.add(new DecisionPage.Decision(this, "provideName", Labels.getLabel("cockpit.wizard.cmssite.decision.decision.create"), "/cockpit/images/defaultWizardNode.gif"));
        setDecisions(decisions);
        Component cmp = super.createRepresentationItself();
        Div labelContainer = new Div();
        labelContainer.setSclass("wizardLabelContainer");
        this.pageContent.insertBefore((Component)labelContainer, this.pageContent.getFirstChild());
        Label infoLabel = new Label(Labels.getLabel("cockpit.wizard.cmssite.decision.infolabel"));
        labelContainer.appendChild((Component)infoLabel);
        return cmp;
    }
}
