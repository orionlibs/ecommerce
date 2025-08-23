package de.hybris.bootstrap.beangenerator.validators;

import de.hybris.bootstrap.beangenerator.BeanGenerationException;
import de.hybris.bootstrap.beangenerator.definitions.model.Extension;
import de.hybris.bootstrap.beangenerator.definitions.xml.Bean;
import de.hybris.bootstrap.beangenerator.definitions.xml.Property;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class PropertyNameValidator extends PojoCreationValidator.PojoCreationValidationAdapter
{
    private final Map<String, Extension> existingBeanAttrs = new HashMap<>(20);


    public void beforeCreateProperty(Bean bean, Property attr, Extension ctx)
    {
        if(StringUtils.isBlank(attr.getName()))
        {
            throw new BeanGenerationException("Found empty property name for type " + bean.getClazz() + "  in <" + ctx
                            .getHome().getName() + ">");
        }
        Extension previousCtx = null;
        if((previousCtx = this.existingBeanAttrs.get(bean.getClazz() + "." + bean.getClazz())) != null)
        {
            throw new BeanGenerationException("Duplicate definition of attribute (" + bean.getClazz() + "#" + attr.getName() + ") in  <" +
                            toFriendlyExtName(ctx) + "> it has been already defined in <" +
                            toFriendlyExtName(previousCtx) + ">");
        }
        this.existingBeanAttrs.put(bean.getClazz() + "." + bean.getClazz(), ctx);
    }
}
