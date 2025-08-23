package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.productcockpit.dao.AbstractDao;
import java.util.Locale;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;

public class ClassAttributeAssignmentNode extends MacFinderTreeNodeAbstract
{
    private final AbstractDao classAttributeAssignmentsDao = (AbstractDao)new ClassAttributeAssignmentDao();


    public AbstractDao getDao()
    {
        return this.classAttributeAssignmentsDao;
    }


    public String getDisplayedLabel()
    {
        Locale original = Locales.getCurrent();
        Locales.setThreadLocal(UISessionUtils.getCurrentSession().getGlobalDataLocale());
        String label = Labels.getLabel("catalogperspective.classattributeassignmentnode");
        Locales.setThreadLocal(original);
        return label;
    }


    public boolean isVisible()
    {
        CatalogVersionModel selectedCatalogVersion = null;
        if(!UISessionUtils.getCurrentSession().getSelectedCatalogVersions().isEmpty())
        {
            selectedCatalogVersion = UISessionUtils.getCurrentSession().getSelectedCatalogVersions().get(0);
        }
        return (selectedCatalogVersion != null && selectedCatalogVersion instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel);
    }
}
