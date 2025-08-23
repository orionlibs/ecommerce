package de.hybris.bootstrap.beangenerator.validators;

import de.hybris.bootstrap.beangenerator.BeanGenerationException;
import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import de.hybris.bootstrap.beangenerator.definitions.model.Extension;
import de.hybris.bootstrap.beangenerator.definitions.xml.Bean;
import de.hybris.bootstrap.beangenerator.definitions.xml.Enum;
import de.hybris.bootstrap.beangenerator.definitions.xml.Property;
import org.apache.commons.lang.StringUtils;

public class JavaKeyWordValidator extends PojoCreationValidator.PojoCreationValidationAdapter
{
    public void beforeCreatePojo(Bean bean, Extension ctx)
    {
        if(isJavaKeyword(bean.getClazz()))
        {
            throw new BeanGenerationException("Class name " + bean.getClazz() + " at <" + toFriendlyExtName(ctx) + "> contains a java keyword");
        }
        if(isJavaKeyword(bean.getExtends()))
        {
            throw new BeanGenerationException("Super class name " + bean.getExtends() + " at <" + toFriendlyExtName(ctx) + "> contains a java keyword");
        }
    }


    public void beforeCreateEnum(Enum xmlEnum, Extension ctx)
    {
        if(isJavaKeyword(xmlEnum.getClazz()))
        {
            throw new BeanGenerationException("Enum name " + xmlEnum.getClazz() + " at <" + toFriendlyExtName(ctx) + "> contains a java keyword");
        }
    }


    public void beforeCreateProperty(Bean bean, Property attr, Extension ctx)
    {
        if(isJavaKeyword(attr.getName()))
        {
            throw new BeanGenerationException("Attribute name (" + bean.getClazz() + "#" + attr.getName() + ") at <" +
                            toFriendlyExtName(ctx) + "> contains a java keyword");
        }
    }


    protected String[] splitFullClass(String className)
    {
        if(StringUtils.isBlank(className))
        {
            return new String[0];
        }
        return className.split("\\.");
    }


    protected boolean isJavaKeyword(String identifier)
    {
        if(StringUtils.isBlank(identifier))
        {
            return false;
        }
        for(String singleIdentifier : splitFullClass(identifier))
        {
            if(ClassNameUtil.isJavaKeyword(singleIdentifier))
            {
                return true;
            }
        }
        return false;
    }
}
