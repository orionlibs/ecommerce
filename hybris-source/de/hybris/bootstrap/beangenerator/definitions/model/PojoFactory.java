package de.hybris.bootstrap.beangenerator.definitions.model;

import de.hybris.bootstrap.beangenerator.definitions.xml.AbstractPojo;
import de.hybris.bootstrap.beangenerator.definitions.xml.AbstractPojos;
import de.hybris.bootstrap.beangenerator.definitions.xml.Annotations;
import de.hybris.bootstrap.beangenerator.definitions.xml.Bean;
import de.hybris.bootstrap.beangenerator.definitions.xml.Enum;
import de.hybris.bootstrap.beangenerator.definitions.xml.Hint;
import de.hybris.bootstrap.beangenerator.definitions.xml.Import;
import de.hybris.bootstrap.beangenerator.definitions.xml.Property;
import de.hybris.bootstrap.beangenerator.validators.PojoCreationValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class PojoFactory
{
    private Extension currentExtension;
    private List<? extends PojoCreationValidator> validators = new ArrayList<>();
    private final BeanSet beanSet = new BeanSet();


    protected void setCurrentExtension(Extension currentExtension)
    {
        this.currentExtension = currentExtension;
    }


    public void mergePojos(Extension currentExtension, AbstractPojos xmlBeans)
    {
        setCurrentExtension(currentExtension);
        for(AbstractPojo pojo : xmlBeans.getBeanOrEnum())
        {
            createPojo(pojo);
        }
    }


    protected void createPojo(AbstractPojo pojoBean)
    {
        if(pojoBean instanceof Enum)
        {
            Enum xmlBean = (Enum)pojoBean;
            createEnum(xmlBean);
            for(String value : xmlBean.getValue())
            {
                createEnumValue(xmlBean, value);
            }
        }
        if(pojoBean instanceof Bean)
        {
            Bean xmlBean = (Bean)pojoBean;
            createBean(xmlBean);
            for(Property xmlAttribute : xmlBean.getProperty())
            {
                createBeanProperty(xmlBean, xmlAttribute);
            }
        }
    }


    protected BeanPrototype createBean(Bean xmlBean)
    {
        notifyBeforeBeanCreated(xmlBean);
        BeanPrototype created = (BeanPrototype)this.beanSet.getOrCreateBean(this.currentExtension, xmlBean.getClazz(), xmlBean.getExtends(), xmlBean
                        .getDescription(), xmlBean.getType(), xmlBean
                        .getDeprecated(), xmlBean.getDeprecatedSince(), xmlBean.isAbstract(), xmlBean
                        .isSuperEquals());
        if(xmlBean.getAnnotations() != null)
        {
            created.setAnnotations(xmlBean.getAnnotations().getValue());
        }
        List<Import> imports = xmlBean.getImports();
        if(imports != null)
        {
            for(Import imp : imports)
            {
                created.addDeclaredImport(imp.getType(), imp.isAsStatic());
            }
        }
        if(xmlBean.getHints() != null && xmlBean.getHints().getHints() != null)
        {
            for(Hint hint : xmlBean.getHints().getHints())
            {
                if(hint != null && StringUtils.isNotBlank(hint.getName()))
                {
                    String value = StringUtils.isBlank(hint.getValue()) ? Boolean.TRUE.toString() : StringUtils.trim(hint.getValue());
                    created.addHint(StringUtils.trim(hint.getName()), value);
                }
            }
        }
        notifyAfterBeanCreated(created, xmlBean);
        return created;
    }


    protected EnumPrototype createEnum(Enum xmlEnum)
    {
        notifyBeforeEnumCreated(xmlEnum);
        EnumPrototype created = (EnumPrototype)this.beanSet.getOrCreateEnum(this.currentExtension, xmlEnum.getClazz(), xmlEnum.getDescription(), xmlEnum
                        .getDeprecated(), xmlEnum.getDeprecatedSince());
        notifyAfterEnumCreated(created, xmlEnum);
        return created;
    }


    protected PropertyPrototype createBeanProperty(Bean xmlBean, Property xmlAttribute)
    {
        notifyBeforePropertyCreated(xmlBean, xmlAttribute);
        PropertyPrototype created = (PropertyPrototype)this.beanSet.createProperty(this.currentExtension, xmlBean, xmlAttribute);
        List<Annotations> annotations = xmlAttribute.getAnnotations();
        if(annotations != null)
        {
            for(Annotations a : annotations)
            {
                addAnnotations(created, a);
            }
        }
        if(xmlAttribute.getHints() != null && xmlAttribute.getHints().getHints() != null)
        {
            for(Hint hint : xmlAttribute.getHints().getHints())
            {
                if(hint != null && StringUtils.isNotBlank(hint.getName()))
                {
                    String value = StringUtils.isBlank(hint.getValue()) ? Boolean.TRUE.toString() : StringUtils.trim(hint.getValue());
                    created.addHint(StringUtils.trim(hint.getName()), value);
                }
            }
        }
        notifyAfterPropertyCreated((BeanPrototype)this.beanSet.getBean(xmlBean.getClazz()), created);
        return created;
    }


    protected void addAnnotations(PropertyPrototype created, Annotations a)
    {
        if(a.getScope() == null || "all".equalsIgnoreCase(a.getScope()))
        {
            created.setMemberAnnotations(a.getValue());
            created.setGetterAnnotations(a.getValue());
            created.setSetterAnnotations(a.getValue());
        }
        else if("getter".equalsIgnoreCase(a.getScope()))
        {
            created.setGetterAnnotations(a.getValue());
        }
        else if("setter".equalsIgnoreCase(a.getScope()))
        {
            created.setSetterAnnotations(a.getValue());
        }
        else if("member".equalsIgnoreCase(a.getScope()))
        {
            created.setMemberAnnotations(a.getValue());
        }
    }


    protected EnumValuePrototype createEnumValue(Enum xmlBean, String enumValue)
    {
        notifyBeforeEnumValueCreated(xmlBean, enumValue);
        EnumValuePrototype created = (EnumValuePrototype)this.beanSet.createEnumValue(this.currentExtension, xmlBean, enumValue);
        notifyAfterEnumValueCreated((EnumPrototype)this.beanSet.getBean(xmlBean.getClazz()), created);
        return created;
    }


    public Collection<? extends ClassNameAware> getBeans()
    {
        return this.beanSet.getBeans();
    }


    public void setValidators(List<? extends PojoCreationValidator> validators)
    {
        this.validators = validators;
    }


    public void notifyExtensionProcessed()
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.afterExtensionProcessed(this.currentExtension);
        }
    }


    protected void notifyBeforeEnumCreated(Enum enumXml)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.beforeCreateEnum(enumXml, this.currentExtension);
        }
    }


    protected void notifyAfterEnumCreated(EnumPrototype bean, Enum xmlEnum)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.afterCreateEnum(bean, xmlEnum, this.currentExtension);
        }
    }


    protected void notifyBeforeBeanCreated(Bean bean)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.beforeCreatePojo(bean, this.currentExtension);
        }
    }


    protected void notifyAfterBeanCreated(BeanPrototype bean, Bean xmlBean)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.afterCreatePojo(bean, xmlBean, this.currentExtension);
        }
    }


    protected void notifyBeforePropertyCreated(Bean bean, Property attribute)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.beforeCreateProperty(bean, attribute, this.currentExtension);
        }
    }


    protected void notifyAfterPropertyCreated(BeanPrototype bean, PropertyPrototype attribute)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.afterCreateProperty(bean, attribute, this.currentExtension);
        }
    }


    protected void notifyBeforeEnumValueCreated(Enum xmlEnum, String enumValue)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.beforeCreateEnumValue(xmlEnum, enumValue, this.currentExtension);
        }
    }


    protected void notifyAfterEnumValueCreated(EnumPrototype bean, EnumValuePrototype attribute)
    {
        for(PojoCreationValidator listener : this.validators)
        {
            listener.afterCreateEnumValue(bean, attribute, this.currentExtension);
        }
    }
}
