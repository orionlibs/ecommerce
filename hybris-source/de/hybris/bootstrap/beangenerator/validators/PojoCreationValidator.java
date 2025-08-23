package de.hybris.bootstrap.beangenerator.validators;

import de.hybris.bootstrap.beangenerator.definitions.model.BeanPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.EnumPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.EnumValuePrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.Extension;
import de.hybris.bootstrap.beangenerator.definitions.model.PropertyPrototype;
import de.hybris.bootstrap.beangenerator.definitions.xml.Bean;
import de.hybris.bootstrap.beangenerator.definitions.xml.Enum;
import de.hybris.bootstrap.beangenerator.definitions.xml.Property;

public interface PojoCreationValidator
{
    void beforeCreateEnum(Enum paramEnum, Extension paramExtension);


    void afterCreateEnum(EnumPrototype paramEnumPrototype, Enum paramEnum, Extension paramExtension);


    void beforeCreatePojo(Bean paramBean, Extension paramExtension);


    void afterCreatePojo(BeanPrototype paramBeanPrototype, Bean paramBean, Extension paramExtension);


    void beforeCreateProperty(Bean paramBean, Property paramProperty, Extension paramExtension);


    void afterCreateProperty(BeanPrototype paramBeanPrototype, PropertyPrototype paramPropertyPrototype, Extension paramExtension);


    void beforeCreateEnumValue(Enum paramEnum, String paramString, Extension paramExtension);


    void afterCreateEnumValue(EnumPrototype paramEnumPrototype, EnumValuePrototype paramEnumValuePrototype, Extension paramExtension);


    void afterExtensionProcessed(Extension paramExtension);
}
