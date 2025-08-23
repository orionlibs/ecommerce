package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.SectionModel;
import org.zkoss.zul.Div;

public abstract class AbstractSectionComponent extends Div implements SectionComponent
{
    protected static final String EMPTY_TEXT = "Nothing to display";
    private final SectionModel sectionModel;


    public AbstractSectionComponent(SectionModel sectionModel)
    {
        if(sectionModel == null)
        {
            throw new IllegalArgumentException("Section model can not be null.");
        }
        this.sectionModel = sectionModel;
        setDroppable("PerspectiveDND");
    }


    public SectionModel getSectionModel()
    {
        return this.sectionModel;
    }
}
