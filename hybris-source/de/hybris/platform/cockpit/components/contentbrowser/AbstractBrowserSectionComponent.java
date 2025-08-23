package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.SectionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBrowserSectionComponent extends AbstractSectionComponent implements BrowserSectionComponent
{
    protected static final String EMPTY_TEXT = "Nothing to display";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBrowserSectionComponent.class);


    public AbstractBrowserSectionComponent(BrowserSectionModel sectionModel)
    {
        super((SectionModel)sectionModel);
        if(sectionModel == null)
        {
            throw new IllegalArgumentException("Section model can not be null.");
        }
        setDroppable("PerspectiveDND");
    }


    public BrowserModel getModel()
    {
        return (BrowserModel)getSectionModel().getSectionBrowserModel();
    }


    public void setModel(BrowserModel model)
    {
        LOG.error("Can not change the browser model of a section browser component");
    }


    public BrowserSectionModel getSectionModel()
    {
        return (BrowserSectionModel)super.getSectionModel();
    }
}
