package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collection;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;

public class CreatePageMandatoryPage extends MandatoryPage
{
    private boolean openInEditor = false;


    public CreatePageMandatoryPage(String pageTitle, CmsWizard wizard)
    {
        super(pageTitle, wizard);
    }


    protected void render(HtmlBasedComponent parent, Collection<String> requiredDescriptors)
    {
        super.render(parent, requiredDescriptors);
        this.openInEditor = BooleanUtils.toBoolean(UITools.getCockpitParameter("default.pagewizard.activateaftercreate",
                        Executions.getCurrent()));
        Div checkboxDiv = new Div();
        checkboxDiv.setSclass("openEditorRow");
        Checkbox checkbox = new Checkbox(Labels.getLabel("cockpit.wizard.cmssite.page.mandatory.openAfterCreate"));
        checkbox.setChecked(this.openInEditor);
        checkbox.setSclass("wizardRowGroupLabel");
        checkbox.addEventListener("onCheck", (EventListener)new Object(this));
        checkboxDiv.appendChild((Component)checkbox);
        parent.appendChild((Component)checkboxDiv);
    }


    public boolean isOpenInEditor()
    {
        return this.openInEditor;
    }


    public void setOpenInEditor(boolean openInEditor)
    {
        this.openInEditor = openInEditor;
    }
}
