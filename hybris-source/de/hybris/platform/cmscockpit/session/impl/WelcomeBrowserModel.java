package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractAdvancedBrowserModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class WelcomeBrowserModel extends AbstractAdvancedBrowserModel
{
    private String welcomeTemplate;


    public WelcomeBrowserModel()
    {
        super(null);
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public Object clone() throws CloneNotSupportedException
    {
        WelcomeBrowserModel browserModel = new WelcomeBrowserModel();
        browserModel.setWelcomeTemplate(this.welcomeTemplate);
        return browserModel;
    }


    public TypedObject getItem(int index)
    {
        return null;
    }


    public List<TypedObject> getItems()
    {
        return Collections.EMPTY_LIST;
    }


    public void updateItems()
    {
        fireItemsChanged();
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new WelcomeContentBrowser();
    }


    @Required
    public void setWelcomeTemplate(String welcomeTemplate)
    {
        this.welcomeTemplate = welcomeTemplate;
    }


    public String getWelcomeTemplate()
    {
        return this.welcomeTemplate;
    }


    public String getLabel()
    {
        return Labels.getLabel("cmscockpit.welcome.title", (Object[])new String[] {UISessionUtils.getCurrentSession().getUser().getName()});
    }
}
