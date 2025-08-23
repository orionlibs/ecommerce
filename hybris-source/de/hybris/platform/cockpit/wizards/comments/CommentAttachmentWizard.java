package de.hybris.platform.cockpit.wizards.comments;

import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;

public class CommentAttachmentWizard extends GenericItemWizard
{
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
        if(this.section != null)
        {
            this.section.refreshView();
        }
    }
}
