package de.hybris.platform.platformbackoffice.widgets.compare;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.GroupDescriptor;
import com.hybris.cockpitng.widgets.compare.CompareViewController;
import de.hybris.platform.platformbackoffice.widgets.compare.model.BackofficeComparisonResult;
import java.util.Collections;
import java.util.Set;

public class BackofficeCompareViewController extends CompareViewController
{
    protected ComparisonResult createComparisonResult(Set<GroupDescriptor> groupDescriptors)
    {
        return (ComparisonResult)new BackofficeComparisonResult(getReferenceObjectId(), Collections.emptyMap(),
                        (groupDescriptors != null) ? groupDescriptors : Collections.emptySet(), Collections.emptyMap(), Collections.emptySet());
    }


    protected String getFallbackItemType()
    {
        return "Item";
    }
}
