package de.hybris.platform.impex.header.model.impl;

import de.hybris.platform.impex.header.model.DescriptorElement;
import de.hybris.platform.impex.header.model.ValueElement;
import java.util.Map;

public class ConveredValueElement implements ValueElement
{
    private final String qualifier;
    private final Map<String, String> modifiers;
    private final DescriptorElement specifier;


    protected ConveredValueElement(String qualifier, Map<String, String> modifiers, DescriptorElement specifier)
    {
        this.qualifier = qualifier;
        this.modifiers = modifiers;
        this.specifier = specifier;
    }


    public String toString()
    {
        return "Value:[" + this.qualifier + "," + this.modifiers + "," + this.specifier + "]";
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public Map<String, String> getModifiers()
    {
        return this.modifiers;
    }


    public DescriptorElement getSpecifier()
    {
        return this.specifier;
    }


    public String getModifierValue(String key)
    {
        return getModifiers().get(key);
    }
}
