package de.hybris.bootstrap.beangenerator.definitions.model;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BeanPrototype extends AttributeContainer<PropertyPrototype>
{
    private final String description;
    private final String type;
    private final String deprecated;
    private final String deprecatedSince;
    private ClassNamePrototype superclassName = null;
    private final List<String> declaredImports = new ArrayList<>();
    private String annotations;
    private boolean _abstract;
    private boolean superEquals;
    private final Map<String, String> hints = new HashMap<>();


    public BeanPrototype(String extensionName, String className, String superclassName, String description, String type, String deprecated, String deprecatedSince, boolean _abstract, boolean superEquals)
    {
        super(extensionName, className);
        if(StringUtils.isNotBlank(superclassName))
        {
            this.superclassName = ClassNameUtil.toPrototype(superclassName);
        }
        this.description = description;
        this.type = type;
        this.deprecated = deprecated;
        this.deprecatedSince = deprecatedSince;
        this._abstract = _abstract;
        this.superEquals = superEquals;
    }


    public String getType()
    {
        return this.type;
    }


    public String getDeprecated()
    {
        return this.deprecated;
    }


    public String getDeprecatedSince()
    {
        return this.deprecatedSince;
    }


    public String getDescription()
    {
        return this.description;
    }


    public ClassNamePrototype getSuperclassName()
    {
        return this.superclassName;
    }


    public Collection<PropertyPrototype> getProperties()
    {
        return Collections.unmodifiableCollection(this.attributes.values());
    }


    public Collection<PropertyPrototype> getEqualsProperties()
    {
        Collection<PropertyPrototype> ret = new ArrayList<>(this.attributes.size());
        for(PropertyPrototype prop : this.attributes.values())
        {
            if(prop.isEquals())
            {
                ret.add(prop);
            }
        }
        return Collections.unmodifiableCollection(ret);
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }


    public void addDeclaredImport(String type, boolean asStatic)
    {
        getDeclaredImports().add(asStatic ? ("static " + type) : type);
    }


    public List<String> getDeclaredImports()
    {
        return this.declaredImports;
    }


    public void setAnnotations(String value)
    {
        this.annotations = value;
    }


    public String getAnnotations()
    {
        return this.annotations;
    }


    public boolean isAbstract()
    {
        return this._abstract;
    }


    public void setAbstract(boolean _abstract)
    {
        this._abstract = _abstract;
    }


    public boolean isSuperEquals()
    {
        return this.superEquals;
    }


    public void setSuperEquals(boolean superEquals)
    {
        this.superEquals = superEquals;
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
