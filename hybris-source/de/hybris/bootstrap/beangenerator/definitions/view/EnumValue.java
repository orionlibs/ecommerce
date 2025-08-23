package de.hybris.bootstrap.beangenerator.definitions.view;

import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import de.hybris.bootstrap.beangenerator.definitions.model.EnumPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.EnumValuePrototype;

public class EnumValue
{
    private static final String COMMENT_TEMPLATE = "/** %s<i>Generated enum value</i> for <code>%s.%s</code> value defined at extension <code>%s</code>. */";
    private final String name;
    private final String comment;
    private final String description;


    public EnumValue(EnumPrototype bean, EnumValuePrototype enumValue)
    {
        this.name = enumValue.getName();
        String beanType = ClassNameUtil.shortenType(bean.getClassName().toString());
        this.comment = String.format("/** %s<i>Generated enum value</i> for <code>%s.%s</code> value defined at extension <code>%s</code>. */", new Object[] {(enumValue.getDescription() == null) ? "" : (enumValue.getDescription() + "<br/><br/>"), beanType, enumValue
                        .getName(), enumValue
                        .getExtensionName()});
        this.description = enumValue.getDescription();
    }


    public String getName()
    {
        return this.name;
    }


    public String getComment()
    {
        return this.comment;
    }


    public String getDescription()
    {
        return this.description;
    }
}
