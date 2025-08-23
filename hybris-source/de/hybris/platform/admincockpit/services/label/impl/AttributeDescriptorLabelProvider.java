package de.hybris.platform.admincockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import org.apache.commons.lang.StringUtils;

public class AttributeDescriptorLabelProvider extends AbstractObjectLabelProvider<AttributeDescriptor>
{
    private static final String TYPE_QUALIFIER_SEPARATOR = ".";


    protected String getItemLabel(AttributeDescriptor descriptor)
    {
        String type = (descriptor.getEnclosingType() != null) ? descriptor.getEnclosingType().getCode() : null;
        String qualifier = descriptor.getQualifier();
        StringBuilder labelBuilder = new StringBuilder(100);
        labelBuilder.append(descriptor.getName()).append(" (");
        labelBuilder.append(StringUtils.isEmpty(type) ? "" : type).append(".");
        labelBuilder.append(StringUtils.isEmpty(qualifier) ? "" : qualifier).append(")");
        return labelBuilder.toString();
    }


    protected String getItemLabel(AttributeDescriptor descriptor, String languageIso)
    {
        return getItemLabel(descriptor);
    }


    protected String getIconPath(AttributeDescriptor item)
    {
        return null;
    }


    protected String getIconPath(AttributeDescriptor item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(AttributeDescriptor item)
    {
        return "";
    }


    protected String getItemDescription(AttributeDescriptor item, String languageIso)
    {
        return "";
    }
}
