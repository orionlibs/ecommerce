package de.hybris.bootstrap.beangenerator.definitions.model;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PropertyPrototype extends AttributePrototype
{
    private final String deprecatedMessage;
    private final String deprecatedSinceValue;
    private boolean overridden = false;
    private final ClassNamePrototype type;
    private final boolean _equals;
    private String memberAnnotations;
    private String getterAnnotations;
    private String setterAnnotations;
    private final Map<String, String> hints = new HashMap<>();


    public PropertyPrototype(String extensionName, String name, String type, boolean overridden, String description, boolean equals, String deprecatedMessage, String deprecatedSinceValue)
    {
        super(extensionName, name, description);
        if(type == null)
        {
            throw new IllegalArgumentException("type cannot be null");
        }
        this.type = ClassNameUtil.toPrototype(type);
        this.overridden = overridden;
        this._equals = equals;
        this.deprecatedMessage = deprecatedMessage;
        this.deprecatedSinceValue = deprecatedSinceValue;
    }


    public ClassNamePrototype getType()
    {
        return this.type;
    }


    public boolean isOverridden()
    {
        return this.overridden;
    }


    public String getMemberAnnotations()
    {
        return this.memberAnnotations;
    }


    public void setMemberAnnotations(String memberAnnotations)
    {
        this.memberAnnotations = memberAnnotations;
    }


    public String getGetterAnnotations()
    {
        return this.getterAnnotations;
    }


    public void setGetterAnnotations(String getterAnnotations)
    {
        this.getterAnnotations = getterAnnotations;
    }


    public String getSetterAnnotations()
    {
        return this.setterAnnotations;
    }


    public void setSetterAnnotations(String setterAnnotations)
    {
        this.setterAnnotations = setterAnnotations;
    }


    public String getDeprecatedMessage()
    {
        return this.deprecatedMessage;
    }


    public boolean isDeprecated()
    {
        return (this.deprecatedMessage != null);
    }


    public String getDeprecatedSinceValue()
    {
        return this.deprecatedSinceValue;
    }


    public boolean isDeprecatedSince()
    {
        return (this.deprecatedSinceValue != null);
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }


    public boolean isEquals()
    {
        return this._equals;
    }


    public void addHint(String name, String value)
    {
        Objects.requireNonNull(name, "name mustn't be null.");
        Objects.requireNonNull(name, "value mustn't be null.");
        this.hints.put(name, value);
    }


    public Map<String, String> getHints()
    {
        return (Map<String, String>)ImmutableMap.copyOf(this.hints);
    }


    public String getHintValue(String name)
    {
        return this.hints.get(name);
    }
}
