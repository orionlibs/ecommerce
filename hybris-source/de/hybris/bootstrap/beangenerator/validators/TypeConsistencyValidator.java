package de.hybris.bootstrap.beangenerator.validators;

import de.hybris.bootstrap.beangenerator.BeanGenerationException;
import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import de.hybris.bootstrap.beangenerator.definitions.model.Extension;
import de.hybris.bootstrap.beangenerator.definitions.xml.Bean;
import de.hybris.bootstrap.beangenerator.definitions.xml.Enum;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class TypeConsistencyValidator extends PojoCreationValidator.PojoCreationValidationAdapter
{
    private static final Logger LOG = Logger.getLogger(TypeConsistencyValidator.class);
    private final Map<String, Extension> createdBeans = new HashMap<>(20);
    private final Map<String, Extension> createdEnums = new HashMap<>(20);


    public void beforeCreatePojo(Bean xmlBean, Extension ctx)
    {
        String className = getTypeErasuredClassName(xmlBean.getClazz());
        if(this.createdEnums.containsKey(className))
        {
            throw new BeanGenerationException("Given 'bean' definition for (" + xmlBean.getClazz() + ") at <" +
                            toFriendlyExtName(ctx) + "> clashes with existing 'enum' definition with the same class name, declared at <" + ((Extension)this.createdEnums
                            .get(className)).getHome().getName() + ">");
        }
        this.createdBeans.put(className, ctx);
    }


    public void beforeCreateEnum(Enum xmlEnum, Extension ctx)
    {
        String className = getTypeErasuredClassName(xmlEnum.getClazz());
        if(this.createdBeans.containsKey(className))
        {
            throw new BeanGenerationException("Given 'enum' definition for (" + xmlEnum.getClazz() + ") at <" +
                            toFriendlyExtName(ctx) + "> clashes with existing 'bean' definition with the same class name, declared at <" + ((Extension)this.createdBeans
                            .get(className)).getHome().getName() + ">");
        }
        this.createdEnums.put(className, ctx);
    }


    private String getTypeErasuredClassName(String className)
    {
        return ClassNameUtil.erasureType(className);
    }
}
