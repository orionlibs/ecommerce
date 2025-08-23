package de.hybris.platform.impex.header.model;

import java.util.Map;

public interface ValueElement extends DescriptorElement
{
    String getQualifier();


    Map<String, String> getModifiers();


    DescriptorElement getSpecifier();


    String getModifierValue(String paramString);
}
