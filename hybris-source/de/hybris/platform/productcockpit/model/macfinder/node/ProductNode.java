package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.productcockpit.dao.AbstractDao;
import de.hybris.platform.productcockpit.dao.ProductDao;
import java.util.Locale;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;

public class ProductNode extends MacFinderTreeNodeAbstract
{
    private final AbstractDao productDao = (AbstractDao)new ProductDao();


    public AbstractDao getDao()
    {
        return this.productDao;
    }


    public String getDisplayedLabel()
    {
        Locale original = Locales.getCurrent();
        Locales.setThreadLocal(UISessionUtils.getCurrentSession().getGlobalDataLocale());
        String label = Labels.getLabel("catalogperspective.productnode");
        Locales.setThreadLocal(original);
        return label;
    }
}
