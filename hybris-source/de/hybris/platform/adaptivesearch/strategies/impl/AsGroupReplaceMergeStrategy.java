package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import java.util.List;

public class AsGroupReplaceMergeStrategy extends AbstractAsGroupMergeStrategy
{
    public void mergeGroup(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        target.setGroupMergeMode(AsGroupMergeMode.REPLACE);
        if(source.getGroup() == null)
        {
            target.setGroup(null);
            return;
        }
        AsConfigurationHolder<AsGroup, AsGroup> configurationHolder = cloneConfigurationHolder(source.getGroup());
        AsConfigurationHolder<AsGroup, AsGroup> replacedConfigurationHolder = target.getGroup();
        if(replacedConfigurationHolder != null)
        {
            List<AsGroup> replacedConfigurations = configurationHolder.getReplacedConfigurations();
            replacedConfigurations.add((AsGroup)replacedConfigurationHolder.getConfiguration());
        }
        target.setGroup(configurationHolder);
    }
}
