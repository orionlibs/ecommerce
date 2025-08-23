package de.hybris.platform.platformbackoffice.editors.yenum;

import com.hybris.cockpitng.editor.defaultenum.EnumValueResolver;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformEnumValueResolver implements EnumValueResolver
{
    protected static final Pattern PATTERN_CLASSIFICATION_CLASS = Pattern.compile("^ClassificationEnum\\(([^/]*)/([^/]*)/([^.]*)\\.(.*)\\)$");
    protected static final Pattern PATTERN_HYBRIS_ENUM = Pattern.compile("java\\.lang\\.Enum(?:\\((.*)\\))?");
    private static final Logger LOG = LoggerFactory.getLogger(PlatformEnumValueResolver.class);
    private transient EnumerationService enumerationService;
    private transient ClassificationSystemService classificationSystemService;
    private ModelService modelService;


    public List<Object> getAllValues(String valueType, Object value)
    {
        if(StringUtils.isEmpty(valueType) && value == null)
        {
            throw new IllegalArgumentException("Neither 'valueType' nor 'value' is specified");
        }
        if(value instanceof HybrisEnumValue)
        {
            return (List)getAllValuesOfHybrisEnum((HybrisEnumValue)value);
        }
        if(StringUtils.isEmpty(valueType))
        {
            if(value.getClass().isEnum())
            {
                return Arrays.asList(value.getClass().getEnumConstants());
            }
            throw new IllegalArgumentException("value is not instance of java.lang.Enum and HybrisEnumValue");
        }
        Matcher hybrisEnumMatcher = PATTERN_HYBRIS_ENUM.matcher(valueType);
        if(hybrisEnumMatcher.matches() && hybrisEnumMatcher.groupCount() == 1 && hybrisEnumMatcher.group(1) != null)
        {
            return (List)getAllValuesForTypeName(hybrisEnumMatcher.group(1));
        }
        Matcher classificationMatcher = PATTERN_CLASSIFICATION_CLASS.matcher(valueType);
        if(classificationMatcher.matches())
        {
            return (List)getAllValuesOfClassificationAttribute(classificationMatcher);
        }
        return Collections.emptyList();
    }


    private List<?> getAllValuesForTypeName(String valueType)
    {
        if(valueType.contains("."))
        {
            try
            {
                Class<?> enumClass = Class.forName(valueType);
                if(HybrisEnumValue.class.isAssignableFrom(enumClass))
                {
                    return getAllValuesOfHybrisEnumClass((Class)enumClass);
                }
                if(enumClass.isEnum())
                {
                    return Arrays.asList(enumClass.getEnumConstants());
                }
            }
            catch(ClassNotFoundException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Skipping type \"%s\", because it is not an existing class name.", new Object[] {valueType}), e);
                }
            }
        }
        else
        {
            try
            {
                return getEnumerationService().getEnumerationValues(valueType);
            }
            catch(UnknownIdentifierException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Skipping type \"%s\", because it is not an existing hybris enumeration.", new Object[] {valueType}), (Throwable)e);
                }
            }
        }
        return Collections.emptyList();
    }


    private List<?> getAllValuesOfHybrisEnum(HybrisEnumValue value)
    {
        return getEnumerationService().getEnumerationValues(this.modelService.getModelType(value));
    }


    private List<? extends HybrisEnumValue> getAllValuesOfHybrisEnumClass(Class<? extends HybrisEnumValue> enumsClass)
    {
        return getEnumerationService().getEnumerationValues(this.modelService.getModelType(enumsClass));
    }


    private List<?> getAllValuesOfClassificationAttribute(Matcher matcher)
    {
        String systemName = matcher.group(1);
        String systemVersionName = matcher.group(2);
        String category = matcher.group(3);
        String feature = matcher.group(4);
        ClassificationSystemVersionModel classificationSystemVersion = getClassificationSystemService().getSystemVersion(systemName, systemVersionName);
        ClassificationClassModel theClass = getClassificationSystemService().getClassForCode(classificationSystemVersion, category);
        for(ClassAttributeAssignment caa : ((ClassificationClass)getModelService().getSource(theClass))
                        .getClassificationAttributeAssignments())
        {
            if(feature.equalsIgnoreCase(caa.getClassificationAttribute().getCode()))
            {
                List<ClassificationAttributeValue> attributeValues = caa.getAttributeValues();
                if(attributeValues != null && !attributeValues.isEmpty())
                {
                    return (List)getModelService().getAll(attributeValues, new ArrayList());
                }
                List<ClassificationAttributeValue> defaultValues = caa.getClassificationAttribute().getDefaultAttributeValues();
                return (List)getModelService().getAll(defaultValues, new ArrayList());
            }
        }
        return Collections.emptyList();
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    public ClassificationSystemService getClassificationSystemService()
    {
        return this.classificationSystemService;
    }


    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
