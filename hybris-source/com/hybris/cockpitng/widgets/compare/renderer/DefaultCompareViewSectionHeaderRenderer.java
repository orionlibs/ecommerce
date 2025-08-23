/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.compare.impl.DefaultItemComparisonFacade;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.config.compareview.jaxb.Section;
import java.util.Collection;

public class DefaultCompareViewSectionHeaderRenderer extends AbstractCompareViewSectionHeaderRenderer<Section>
{
    @Override
    protected String getSectionName(final Section configuration)
    {
        return configuration.getName();
    }


    @Override
    protected String getTooltipText(final Section configuration)
    {
        return configuration.getTooltipText();
    }


    @Override
    protected boolean isNotEqual(final ComparisonResult result, final Section configuration)
    {
        return isNotEqual(result, getSectionName(configuration));
    }


    /**
     * Checks if there is any difference in a group for reference object
     *
     * @param result
     *           ComparisonResult loaded from
     *           engine{@link DefaultItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param groupName
     *           Group name
     * @return true if reference object has any difference in context of group, false otherwise
     */
    protected boolean isNotEqual(final ComparisonResult result, final String groupName)
    {
        return result.getGroupsWithDifferences().contains(groupName);
    }


    /**
     * Checks if there is any difference in a group for reference object
     *
     * @param result
     *           ComparisonResult loaded from
     *           engine{@link DefaultItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param section
     *           tested section for contains differences to reference object
     * @param itemId
     *           itemId which is compared to reference object
     * @return true if any of attributes from section of itemId has any difference in context of group(section) and
     *         reference object, false otherwise
     */
    @Override
    protected boolean isNotEqual(final ComparisonResult result, final Section section, final Object itemId)
    {
        return section.getAttribute().stream()
                        .anyMatch(attribute -> isItemAttributeDifferentThanCorrespondingReferenceItemAttribute(result, itemId, attribute));
    }
}
