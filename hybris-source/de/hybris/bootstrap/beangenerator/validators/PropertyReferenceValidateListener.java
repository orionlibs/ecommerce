package de.hybris.bootstrap.beangenerator.validators;

import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import de.hybris.bootstrap.beangenerator.definitions.model.BeanPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.ClassNameAware;
import de.hybris.bootstrap.beangenerator.definitions.model.EnumPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.Extension;
import de.hybris.bootstrap.beangenerator.definitions.model.PropertyPrototype;
import de.hybris.bootstrap.beangenerator.definitions.xml.Bean;
import de.hybris.bootstrap.beangenerator.definitions.xml.Enum;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class PropertyReferenceValidateListener extends PojoCreationValidator.PojoCreationValidationAdapter
{
    private static final Logger LOG = Logger.getLogger(PropertyReferenceValidateListener.class);
    private final Set<String> allCreatedBeansOrEnums = new HashSet<>(20);
    private final Map<String, String> potentialAttributeReferenceMissing = new HashMap<>(20);
    private final Map<String, String> potentialSuperClassReferenceMissing = new HashMap<>(20);


    public void afterCreatePojo(BeanPrototype beanProto, Bean bean, Extension ctx)
    {
        this.allCreatedBeansOrEnums.add(getOwnClassName((ClassNameAware)beanProto));
        if(beanProto.getSuperclassName() != null)
        {
            String superClassName = getSuperClassName(beanProto);
            if(isCustomType(superClassName) && !this.allCreatedBeansOrEnums.contains(superClassName))
            {
                this.potentialSuperClassReferenceMissing.put(superClassName, getOwnClassName((ClassNameAware)beanProto));
            }
        }
    }


    public void afterCreateEnum(EnumPrototype enumProto, Enum bean, Extension ctx)
    {
        this.allCreatedBeansOrEnums.add(getOwnClassName((ClassNameAware)enumProto));
    }


    public void afterCreateProperty(BeanPrototype bean, PropertyPrototype attr, Extension ctx)
    {
        if(attr.getType() != null)
        {
            if(isCustomType(attr.getType().getBaseClass()) && !this.allCreatedBeansOrEnums.contains(attr.getType()))
            {
                this.potentialAttributeReferenceMissing.put(attr.getType().getBaseClass(),
                                getOwnClassName((ClassNameAware)bean) + "#" + getOwnClassName((ClassNameAware)bean));
            }
        }
    }


    public void afterExtensionProcessed(Extension ctx)
    {
        for(Map.Entry<String, String> entry : this.potentialSuperClassReferenceMissing.entrySet())
        {
            String notExistingReferenceType = entry.getKey();
            if(!this.allCreatedBeansOrEnums.contains(notExistingReferenceType) && !classExistsInBoostrap(notExistingReferenceType))
            {
                LOG.warn("Super class definition for (" + (String)entry.getValue() + ") declared at <" +
                                toFriendlyExtName(ctx) + "> relies on the type (" + notExistingReferenceType + ") which is not declared yet, probably extension order issue.");
            }
        }
        this.potentialSuperClassReferenceMissing.clear();
        for(Map.Entry<String, String> entry : this.potentialAttributeReferenceMissing.entrySet())
        {
            String notExistingReferenceType = entry.getKey();
            if(!this.allCreatedBeansOrEnums.contains(notExistingReferenceType) && !classExistsInBoostrap(notExistingReferenceType))
            {
                LOG.warn("Attribute definition for (" + (String)entry.getValue() + ") declared at <" +
                                toFriendlyExtName(ctx) + "> relies on the type (" + notExistingReferenceType + ") which is not declared yet, probably extension order issue.");
            }
        }
        this.potentialAttributeReferenceMissing.clear();
    }


    protected boolean classExistsInBoostrap(String className)
    {
        try
        {
            return (Class.forName(className) != null);
        }
        catch(ClassNotFoundException e)
        {
            return false;
        }
    }


    protected boolean isCustomType(String type)
    {
        return (ClassNameUtil.needsImporting(type) &&
                        !ClassNameUtil.isPlatformModelOrEnum(type) && !ClassNameUtil.isJavaClass(type));
    }
}
