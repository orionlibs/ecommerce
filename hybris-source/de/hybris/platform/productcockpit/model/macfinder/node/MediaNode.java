package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.productcockpit.dao.AbstractDao;
import de.hybris.platform.productcockpit.dao.MediaDao;
import java.util.Locale;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;

public class MediaNode extends MacFinderTreeNodeAbstract
{
    private final AbstractDao mediaDao = (AbstractDao)new MediaDao();


    public AbstractDao getDao()
    {
        return this.mediaDao;
    }


    public String getDisplayedLabel()
    {
        Locale original = Locales.getCurrent();
        Locales.setThreadLocal(UISessionUtils.getCurrentSession().getGlobalDataLocale());
        String label = Labels.getLabel("catalogperspective.medianode");
        Locales.setThreadLocal(original);
        return label;
    }
}
