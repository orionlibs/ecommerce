package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree;

import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import java.io.Serializable;
import java.util.Comparator;

public class ParentBundleTemplateComparator implements Comparator<BundleTemplateModel>, Serializable
{
    public int compare(BundleTemplateModel template1, BundleTemplateModel template2)
    {
        BundleTemplateStatusEnum status1 = template1.getStatus().getStatus();
        BundleTemplateStatusEnum status2 = template2.getStatus().getStatus();
        if(BundleTemplateStatusEnum.ARCHIVED.equals(status2) && !status1.equals(status2))
        {
            return -1;
        }
        if(BundleTemplateStatusEnum.ARCHIVED.equals(status1) && !status1.equals(status2))
        {
            return 1;
        }
        if(template1.getName() == null)
        {
            return (template2.getName() == null) ? 0 : -1;
        }
        return (template2.getName() == null) ? 1 : template1.getName().compareTo(template2.getName());
    }
}
