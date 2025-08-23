package de.hybris.bootstrap.beangenerator.validators;

import de.hybris.bootstrap.beangenerator.BeanGenerationException;
import de.hybris.bootstrap.beangenerator.definitions.model.Extension;
import de.hybris.bootstrap.beangenerator.definitions.xml.Enum;
import org.apache.commons.lang.StringUtils;

public class EnumValueNameValidator extends PojoCreationValidator.PojoCreationValidationAdapter
{
    public void beforeCreateEnumValue(Enum xmlEnum, String enumValue, Extension ctx)
    {
        if(StringUtils.isBlank(enumValue))
        {
            throw new BeanGenerationException("Found empty property name for type " + xmlEnum.getClazz() + "  in <" + ctx
                            .getHome().getName() + ">");
        }
    }
}
