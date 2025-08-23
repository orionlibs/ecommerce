package de.hybris.bootstrap.beangenerator.definitions.view;

import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import de.hybris.bootstrap.beangenerator.definitions.model.BeanPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.PropertyPrototype;
import java.util.Map;

public class MemberVariable
{
    private static final String COMMENT_TEMPLATE = "/** %s<i>Generated property</i> for <code>%s.%s</code> property defined at extension <code>%s</code>. */";
    private boolean overridden = false;
    private final String type;
    private final String name;
    private final String comment;
    private final String description;
    private boolean deprecated = false;
    private final String deprecatedMessage;
    private boolean deprecatedSince = false;
    private final String deprecatedSinceValue;
    private final String memberAnnotations;
    private final String getterAnnotations;
    private final String setterAnnotations;
    private final Map<String, String> hints;


    public MemberVariable(BeanPrototype bean, PropertyPrototype property)
    {
        this.type = ClassNameUtil.shortenType(property.getType());
        this.name = property.getName();
        this.overridden = property.isOverridden();
        this.deprecated = property.isDeprecated();
        this.deprecatedMessage = property.getDeprecatedMessage();
        this.deprecatedSince = property.isDeprecatedSince();
        this.deprecatedSinceValue = property.getDeprecatedSinceValue();
        this.hints = property.getHints();
        String beanType = ClassNameUtil.shortenType(bean.getClassName().toString());
        this.comment = String.format("/** %s<i>Generated property</i> for <code>%s.%s</code> property defined at extension <code>%s</code>. */", new Object[] {(property.getDescription() == null) ? "" : (property.getDescription() + "<br/><br/>"), beanType, property
                        .getName(), property
                        .getExtensionName()});
        this.description = property.getDescription();
        this.memberAnnotations = property.getMemberAnnotations();
        this.getterAnnotations = property.getGetterAnnotations();
        this.setterAnnotations = property.getSetterAnnotations();
    }


    public String getName()
    {
        return this.name;
    }


    public String getType()
    {
        return this.type;
    }


    public boolean isOverridden()
    {
        return this.overridden;
    }


    public String getComment()
    {
        return this.comment;
    }


    public String getDescription()
    {
        return this.description;
    }


    public boolean isDeprecated()
    {
        return this.deprecated;
    }


    public String getDeprecatedMessage()
    {
        return this.deprecatedMessage;
    }


    public boolean isDeprecatedSince()
    {
        return this.deprecatedSince;
    }


    public String getDeprecatedSinceValue()
    {
        return this.deprecatedSinceValue;
    }


    public String getMemberAnnotations()
    {
        return this.memberAnnotations;
    }


    public String getGetterAnnotations()
    {
        return this.getterAnnotations;
    }


    public String getSetterAnnotations()
    {
        return this.setterAnnotations;
    }


    public Map<String, String> getHints()
    {
        return this.hints;
    }
}
