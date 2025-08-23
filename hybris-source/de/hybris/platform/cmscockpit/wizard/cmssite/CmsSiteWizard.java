package de.hybris.platform.cmscockpit.wizard.cmssite;

import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class CmsSiteWizard extends GenericItemWizard
{
    public static final String WIZARD_LABEL_CONTAINER = "wizardLabelContainer";
    private DefaultSectionSelectorSection section;


    public DefaultSectionSelectorSection getSection()
    {
        return this.section;
    }


    public void setSection(DefaultSectionSelectorSection section)
    {
        this.section = section;
    }


    public void doAfterDone(AbstractGenericItemPage page)
    {
        this.section.refreshView();
    }


    protected void evaluateScript()
    {
        super.evaluateScript();
        Notification notification = new Notification(Labels.getLabel("cockpit.wizard.cmssite.success"));
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier().setNotification(notification);
        List<UICockpitPerspective> availablePerspectives = UISessionUtils.getCurrentSession().getAvailablePerspectives();
        for(UICockpitPerspective uiCockpitPerspective : availablePerspectives)
        {
            uiCockpitPerspective.getNavigationArea().update();
        }
    }
}
