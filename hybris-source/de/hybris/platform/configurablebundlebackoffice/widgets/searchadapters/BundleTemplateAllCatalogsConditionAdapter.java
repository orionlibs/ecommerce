package de.hybris.platform.configurablebundlebackoffice.widgets.searchadapters;

import com.hybris.backoffice.navigation.NavigationNode;
import org.apache.commons.lang3.StringUtils;

public class BundleTemplateAllCatalogsConditionAdapter extends BundleTemplateConditionAdapter
{
    public boolean canHandle(NavigationNode node)
    {
        return StringUtils.endsWith(node.getId(), "allCatalogs");
    }
}
