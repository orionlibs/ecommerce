package de.hybris.bootstrap.beangenerator.definitions.model;

import de.hybris.bootstrap.beangenerator.definitions.xml.Bean;
import de.hybris.bootstrap.beangenerator.definitions.xml.Enum;
import de.hybris.bootstrap.beangenerator.definitions.xml.Property;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeanSet
{
    private final Map<String, ClassNameAware> beans = new HashMap<>();


    public <T extends ClassNameAware> T getBean(String className)
    {
        return (T)this.beans.get(className);
    }


    public <T extends ClassNameAware> T getOrCreateBean(Extension currentExtension, String className, String superclassName, String description, String type, String deprecated, String deprecatedSince, boolean _abstract, boolean superEquals)
    {
        BeanPrototype beanPrototype;
        ClassNameAware classNameAware = this.beans.get(className);
        if(classNameAware == null)
        {
            beanPrototype = new BeanPrototype(currentExtension.getName(), className, superclassName, description, type, deprecated, deprecatedSince, _abstract, superEquals);
            this.beans.put(className, beanPrototype);
        }
        return (T)beanPrototype;
    }


    public <T extends ClassNameAware> T getOrCreateEnum(Extension currentExtension, String className, String description, String deprecated, String deprecatedSince)
    {
        EnumPrototype enumPrototype;
        ClassNameAware classNameAware = this.beans.get(className);
        if(classNameAware == null)
        {
            enumPrototype = new EnumPrototype(currentExtension.getName(), className, description, deprecated, deprecatedSince);
            this.beans.put(className, enumPrototype);
        }
        return (T)enumPrototype;
    }


    public <T extends AttributePrototype> T createProperty(Extension currentExtension, Bean xmlBean, Property xmlAttribute)
    {
        BeanPrototype bean = getBean(xmlBean.getClazz());
        PropertyPrototype createdAttribute = new PropertyPrototype(currentExtension.getName(), xmlAttribute.getName(), xmlAttribute.getType(), isOverriddenAttribute(this, bean, xmlAttribute.getName()), xmlAttribute.getDescription(), xmlAttribute.isEquals(), xmlAttribute.getDeprecated(),
                        xmlAttribute.getDeprecatedSince());
        bean.addAttribute((AttributePrototype)createdAttribute);
        return (T)createdAttribute;
    }


    public <T extends AttributePrototype> T createEnumValue(Extension currentExtension, Enum xmlEnum, String enumValue)
    {
        EnumPrototype bean = getBean(xmlEnum.getClazz());
        EnumValuePrototype createdAttribute = new EnumValuePrototype(currentExtension.getName(), enumValue, null);
        bean.addAttribute(createdAttribute);
        return (T)createdAttribute;
    }


    protected boolean isOverriddenAttribute(BeanSet beanSet, BeanPrototype bean, String attribute)
    {
        if(bean.getSuperclassName() != null)
        {
            BeanPrototype superBean = beanSet.<BeanPrototype>getBean(bean.getSuperclassName().getBaseClass());
            while(superBean != null)
            {
                if(superBean.hasAttribute(attribute))
                {
                    return true;
                }
                if(superBean.getSuperclassName() != null)
                {
                    superBean = beanSet.<BeanPrototype>getBean(superBean.getSuperclassName().getBaseClass());
                    continue;
                }
                superBean = null;
            }
        }
        return false;
    }


    public Collection<? extends ClassNameAware> getBeans()
    {
        return Collections.unmodifiableCollection(this.beans.values());
    }
}
