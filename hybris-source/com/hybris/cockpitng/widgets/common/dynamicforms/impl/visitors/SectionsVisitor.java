/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Groupbox;

/**
 * Visitor of {@link Groupbox} from UI elements. Uses as a configuration {@link DynamicForms#getSection()}
 */
public class SectionsVisitor extends AbstractComponentsVisitor<Groupbox, DynamicSection>
{
    private static final Logger LOG = LoggerFactory.getLogger(SectionsVisitor.class);


    @Override
    protected boolean canHandle(final Component component)
    {
        if(component instanceof Groupbox)
        {
            final String sectionId = getComponentKey((Groupbox)component);
            if(sectionId != null)
            {
                final Predicate<DynamicSection> isDynamicSection = section -> (StringUtils.equals(section.getQualifier(), sectionId));
                return getDynamicElements().stream().anyMatch(isDynamicSection);
            }
        }
        return false;
    }


    @Override
    protected String getComponentKey(final Groupbox component)
    {
        final Object abstractSection = component.getAttribute(ComponentsVisitor.COMPONENT_CTX);
        if(abstractSection instanceof AbstractSection)
        {
            return ((AbstractSection)abstractSection).getName();
        }
        return null;
    }


    @Override
    protected List<DynamicSection> getDynamicElements()
    {
        return getDynamicForms().getSection();
    }


    @Override
    protected void visitComponents(final DynamicSection dynamicSection, final Object target, final boolean initial)
    {
        if(StringUtils.isNotEmpty(dynamicSection.getQualifier()))
        {
            final Collection<Groupbox> groupBoxes = getComponentKeyComponentsMap().get(dynamicSection.getQualifier());
            if(CollectionUtils.isNotEmpty(groupBoxes))
            {
                applyVisibleIf(groupBoxes, dynamicSection, target);
                if(StringUtils.isNotEmpty(dynamicSection.getDisabledIf()))
                {
                    LOG.warn("Disabling section is not supported, use visible option instead");
                }
            }
        }
    }


    protected void applyVisibleIf(final Collection<Groupbox> groupBoxes, final DynamicSection dynamicSection, final Object target)
    {
        if(StringUtils.isNotEmpty(dynamicSection.getVisibleIf()))
        {
            final boolean visible = isVisible(dynamicSection, target);
            groupBoxes.forEach(groupBox -> groupBox.setVisible(visible));
        }
    }
}
