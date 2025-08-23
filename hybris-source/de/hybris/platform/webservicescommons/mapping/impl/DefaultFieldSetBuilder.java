package de.hybris.platform.webservicescommons.mapping.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.webservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.mapping.SubclassRegistry;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.Cacheable;

public class DefaultFieldSetBuilder implements FieldSetBuilder
{
    private static final Logger LOG = Logger.getLogger(DefaultFieldSetBuilder.class);
    private int defaultRecurrencyLevel = 4;
    private int defaultMaxFieldSetSize = 50000;
    private Set<Class> simpleClassSet;
    private FieldSetLevelHelper fieldSetLevelHelper;
    private SubclassRegistry subclassRegistry;


    public DefaultFieldSetBuilder()
    {
        this.simpleClassSet = (Set)new HashSet<>(Arrays.asList(new Class[] {
                        Byte.class, Short.class, Integer.class, Long.class, Boolean.class, Character.class, Float.class, String.class, Date.class, Time.class,
                        Object.class}));
    }


    @Cacheable(value = {"fieldSetCache"}, key = "{#clazz,#fieldPrefix,#configuration}")
    public Set<String> createFieldSet(Class clazz, String fieldPrefix, String configuration)
    {
        return createFieldSet(clazz, fieldPrefix, configuration, null);
    }


    @Cacheable(value = {"fieldSetCache"}, key = "{#clazz,#fieldPrefix,#configuration,#context}")
    public Set<String> createFieldSet(Class clazz, String fieldPrefix, String configuration, FieldSetBuilderContext context)
    {
        FieldSetBuilderContext processedContext;
        if(context != null)
        {
            processedContext = context;
        }
        else
        {
            processedContext = new FieldSetBuilderContext();
            processedContext.setRecurrencyLevel(getDefaultRecurrencyLevel());
            processedContext.setMaxFieldSetSize(getDefaultMaxFieldSetSize());
            Map<String, Class<?>> typeVariableMap = buildTypeVariableMap(clazz);
            processedContext.setTypeVariableMap(typeVariableMap);
        }
        processedContext.resetFieldCounter();
        processedContext.resetRecurrencyMap();
        return createFieldSetInternal(clazz, fieldPrefix, configuration, processedContext);
    }


    protected Map<String, Class> buildTypeVariableMap(Class clazz)
    {
        Map<String, Class<?>> result = new HashMap<>();
        Type genericSuperclass = clazz.getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType)
        {
            ParameterizedType parameterizedSuperclass = (ParameterizedType)genericSuperclass;
            Type[] actualTypeArguments = parameterizedSuperclass.getActualTypeArguments();
            TypeVariable[] typeParameters = clazz.getSuperclass().getTypeParameters();
            for(int i = 0; i < actualTypeArguments.length; i++)
            {
                Type t = actualTypeArguments[i];
                if(t instanceof Class)
                {
                    result.put(typeParameters[i].getName(), (Class)t);
                }
            }
        }
        return result;
    }


    protected Set<String> createFieldSetInternal(Class clazz, String fieldPrefix, String configuration, FieldSetBuilderContext context)
    {
        Set<String> fieldSet = new HashSet<>();
        if(configuration == null || configuration.isEmpty())
        {
            return fieldSet;
        }
        String trimmedConfiguration = configuration.trim();
        if(trimmedConfiguration.charAt(trimmedConfiguration.length() - 1) == ',' || trimmedConfiguration
                        .charAt(trimmedConfiguration.length() - 1) == '(')
        {
            throw new ConversionException("Incorrect configuration");
        }
        int currentPos = 0;
        String elementName = "";
        while(currentPos < trimmedConfiguration.length())
        {
            elementName = getElementName(currentPos, trimmedConfiguration);
            currentPos += elementName.length();
            elementName = elementName.trim();
            if(getFieldSetLevelHelper().isLevelName(elementName, clazz))
            {
                fieldSet.addAll(createFieldSetForLevel(clazz, fieldPrefix, elementName, context));
            }
            else
            {
                Type fieldType = getFieldType(elementName, clazz);
                String fullFieldName = createFullFieldName(fieldPrefix, elementName);
                currentPos = parseComplexField(trimmedConfiguration, currentPos, fullFieldName, fieldType, fieldSet, context);
            }
            currentPos = omitComma(currentPos, trimmedConfiguration);
        }
        return fieldSet;
    }


    protected String getElementName(int startIndex, String configuration)
    {
        int elementEnd = findElementEnd(startIndex, configuration);
        return configuration.substring(startIndex, elementEnd);
    }


    protected static int findElementEnd(int startIndex, String configuration)
    {
        int index = startIndex;
        while(index < configuration.length())
        {
            char c = configuration.charAt(index);
            if(c == '(' || c == ',')
            {
                return index;
            }
            index++;
        }
        return configuration.length();
    }


    protected Set<String> createFieldSetForLevel(Class clazz, String prefix, String levelName, FieldSetBuilderContext context)
    {
        String levelDef = this.fieldSetLevelHelper.getLevelDefinitionForClass(clazz, levelName);
        if(levelDef == null)
        {
            if("BASIC".equals(levelName))
            {
                levelDef = this.fieldSetLevelHelper.createBasicLevelDefinition(clazz);
            }
            else if("DEFAULT".equals(levelName))
            {
                levelDef = this.fieldSetLevelHelper.createDefaultLevelDefinition(clazz);
            }
            else if("FULL".equals(levelName))
            {
                levelDef = this.fieldSetLevelHelper.createFullLevelDefinition(clazz);
            }
        }
        return createFieldSetInternal(clazz, prefix, levelDef, context);
    }


    protected int parseComplexField(String configuration, int currentPos, String fullFieldName, Type fieldType, Set<String> fieldSet, FieldSetBuilderContext context)
    {
        if(fieldType instanceof ParameterizedType)
        {
            return parseParametrizedTypeField(configuration, currentPos, fullFieldName, fieldType, fieldSet, context);
        }
        if(fieldType instanceof WildcardType)
        {
            return parseWildcardTypeField(configuration, currentPos, fullFieldName, fieldType, fieldSet, context);
        }
        if(fieldType instanceof TypeVariable)
        {
            return parseTypeVariableField(configuration, currentPos, fullFieldName, fieldType, fieldSet, context);
        }
        return parseField(configuration, currentPos, fullFieldName, fieldType, fieldSet, context);
    }


    protected int parseTypeVariableField(String configuration, int currentPos, String fullFieldName, Type fieldType, Set<String> fieldSet, FieldSetBuilderContext context)
    {
        TypeVariable typeVariable = (TypeVariable)fieldType;
        if(context.getTypeVariableMap() != null && context.getTypeVariableMap().containsKey(typeVariable.getName()))
        {
            return parseComplexField(configuration, currentPos, fullFieldName, (Type)context
                            .getTypeVariableMap().get(typeVariable.getName()), fieldSet, context);
        }
        return parseComplexField(configuration, currentPos, fullFieldName, Object.class, fieldSet, context);
    }


    protected int parseWildcardTypeField(String configuration, int currentPos, String fullFieldName, Type fieldType, Set<String> fieldSet, FieldSetBuilderContext context)
    {
        WildcardType wildcardType = (WildcardType)fieldType;
        Type[] lowerBounds = wildcardType.getLowerBounds();
        if(lowerBounds != null && lowerBounds.length > 0 && lowerBounds[0] != null)
        {
            return parseComplexField(configuration, currentPos, fullFieldName, lowerBounds[0], fieldSet, context);
        }
        return parseComplexField(configuration, currentPos, fullFieldName, wildcardType.getUpperBounds()[0], fieldSet, context);
    }


    protected int parseParametrizedTypeField(String configuration, int currentPos, String fullFieldName, Type fieldType, Set<String> fieldSet, FieldSetBuilderContext context)
    {
        ParameterizedType parametrizedType = (ParameterizedType)fieldType;
        Type rawType = parametrizedType.getRawType();
        if(Map.class.isAssignableFrom((Class)rawType))
        {
            return parseMapField(configuration, currentPos, fullFieldName, (ParameterizedType)fieldType, fieldSet, context);
        }
        if(Collection.class.isAssignableFrom((Class)rawType))
        {
            return parseComplexField(configuration, currentPos, fullFieldName, parametrizedType.getActualTypeArguments()[0], fieldSet, context);
        }
        return parseField(configuration, currentPos, fullFieldName, rawType, fieldSet, context);
    }


    protected int parseField(String configuration, int currentPos, String fullFieldName, Type fieldType, Set<String> fieldSet, FieldSetBuilderContext context)
    {
        Class fieldClass = getClassForType(fieldType);
        int processedCurrentPos = omitSpace(currentPos, configuration);
        if(processedCurrentPos < configuration.length() && configuration.charAt(processedCurrentPos) == '(')
        {
            int confEnd = findMatchingCloseBracket(configuration, processedCurrentPos);
            if(confEnd != -1)
            {
                String fieldConf = configuration.substring(processedCurrentPos + 1, confEnd);
                if(!fieldConf.isEmpty() && isSimpleClass(fieldClass))
                {
                    throw new ConversionException("Incorrect configuration : field '" + fullFieldName + "' don't need configuration");
                }
                if(!context.isRecurencyLevelExceeded(fieldClass))
                {
                    context.addToRecurrencyMap(fieldClass);
                    fieldSet.addAll(createFieldSetInternal(fieldClass, fullFieldName, fieldConf, context));
                    context.removeFromRecurrencyMap(fieldClass);
                }
            }
            else
            {
                throw new ConversionException("Incorrect configuration : Missing ')'");
            }
            processedCurrentPos = omitBracket(confEnd, configuration);
        }
        else if(!isSimpleClass(fieldClass) && !context.isRecurencyLevelExceeded(fieldClass))
        {
            context.addToRecurrencyMap(fieldClass);
            fieldSet.addAll(createFieldSetInternal(fieldClass, fullFieldName, "BASIC", context));
            context.removeFromRecurrencyMap(fieldClass);
        }
        addToFieldSet(fieldSet, fullFieldName, context);
        return processedCurrentPos;
    }


    protected void addToFieldSet(Set<String> fieldSet, String fullFieldName, FieldSetBuilderContext context)
    {
        if(!context.isMaxFieldSetSizeExceeded())
        {
            fieldSet.add(fullFieldName);
            context.incrementFieldCounter();
        }
        else
        {
            throw new ConversionException("Max field set size exceeded. Reason of that can be : too generic configuration, lack of properly defined BASIC field set level for data class, reccurency in data structure");
        }
    }


    protected Class getClassForType(Type fieldType)
    {
        Class<Object> fieldClass = Object.class;
        if(fieldType instanceof Class)
        {
            fieldClass = (Class<Object>)fieldType;
            if(fieldClass.isArray())
            {
                while(fieldClass.isArray())
                {
                    fieldClass = (Class)fieldClass.getComponentType();
                }
            }
        }
        return fieldClass;
    }


    protected int parseMapField(String configuration, int currentPos, String fieldName, ParameterizedType fieldType, Set<String> fieldSet, FieldSetBuilderContext context)
    {
        String mapConf = "";
        int processedCurrentPos = omitSpace(currentPos, configuration);
        if(processedCurrentPos < configuration.length() && configuration.charAt(processedCurrentPos) == '(')
        {
            int confEnd = findMatchingCloseBracket(configuration, processedCurrentPos);
            if(confEnd != -1)
            {
                mapConf = configuration.substring(processedCurrentPos + 1, confEnd).trim();
            }
            else
            {
                throw new ConversionException("Incorrect map configuration : Missing ')'");
            }
            processedCurrentPos = omitBracket(confEnd, configuration);
        }
        Type keyType = fieldType.getActualTypeArguments()[0];
        String keyFieldName = createFullFieldName(fieldName, "key");
        int pos = parseComplexField(mapConf, 0, keyFieldName, keyType, fieldSet, context);
        Type valueType = fieldType.getActualTypeArguments()[1];
        String valueFieldName = createFullFieldName(fieldName, "value");
        pos = parseComplexField(mapConf, pos, valueFieldName, valueType, fieldSet, context);
        if(pos < mapConf.length())
        {
            throw new ConversionException("Incorrect map configuration : '" + mapConf + "'");
        }
        addToFieldSet(fieldSet, fieldName, context);
        return processedCurrentPos;
    }


    protected Type getFieldType(String fieldName, Class objectClass)
    {
        if(fieldName == null || fieldName.isEmpty())
        {
            throw new ConversionException("Incorrect field: field name is empty string");
        }
        Class clazz = objectClass;
        while(clazz != null)
        {
            try
            {
                Field field = clazz.getDeclaredField(fieldName);
                return field.getGenericType();
            }
            catch(NoSuchFieldException e)
            {
                LOG.debug(e);
                clazz = clazz.getSuperclass();
            }
        }
        if(getSubclassRegistry() != null)
        {
            return searchFieldTypeInSubclasses(fieldName, objectClass);
        }
        throw new ConversionException("Incorrect field:'" + fieldName + "'");
    }


    protected Type searchFieldTypeInSubclasses(String fieldName, Class objectClass)
    {
        for(Class subclass : getSubclassRegistry().getAllSubclasses(objectClass))
        {
            try
            {
                Field field = subclass.getDeclaredField(fieldName);
                return field.getGenericType();
            }
            catch(NoSuchFieldException e)
            {
                LOG.debug(e);
            }
        }
        throw new ConversionException("Incorrect field:'" + fieldName + "'");
    }


    protected String createFullFieldName(String basePrefix, String fieldName)
    {
        String prefix;
        if(basePrefix == null || basePrefix.isEmpty())
        {
            prefix = fieldName;
        }
        else
        {
            prefix = basePrefix + "." + basePrefix;
        }
        return prefix;
    }


    protected boolean isSimpleClass(Class<?> clazz)
    {
        return (clazz.isPrimitive() || clazz.isEnum() || getSimpleClassSet().contains(clazz) || Number.class
                        .isAssignableFrom(clazz) || HybrisEnumValue.class.isAssignableFrom(clazz));
    }


    protected static int findMatchingCloseBracket(String configuration, int openPos)
    {
        int closePos = openPos + 1;
        int counter = 1;
        while(counter > 0 && closePos < configuration.length())
        {
            char c = configuration.charAt(closePos++);
            if(c == '(')
            {
                counter++;
                continue;
            }
            if(c == ')')
            {
                counter--;
            }
        }
        if(counter == 0)
        {
            return --closePos;
        }
        return -1;
    }


    protected static int omitSpace(int startIndex, String configuration)
    {
        int index = startIndex;
        while(index < configuration.length() && configuration.charAt(index) <= ' ')
        {
            index++;
        }
        return index;
    }


    protected int omitComma(int startIndex, String configuration)
    {
        int index = omitSpace(startIndex, configuration);
        if(index < configuration.length() && configuration.charAt(index) == ',')
        {
            index++;
        }
        return index;
    }


    protected int omitBracket(int startIndex, String configuration)
    {
        int index = omitSpace(startIndex, configuration);
        if(index < configuration.length() && configuration.charAt(index) == ')')
        {
            index++;
        }
        return index;
    }


    public Set<Class> getSimpleClassSet()
    {
        return this.simpleClassSet;
    }


    public void setSimpleClassSet(Set<Class<?>> simpleTypeSet)
    {
        this.simpleClassSet = simpleTypeSet;
    }


    public int getDefaultRecurrencyLevel()
    {
        return this.defaultRecurrencyLevel;
    }


    public void setDefaultRecurrencyLevel(int defaultRecurrencyLevel)
    {
        this.defaultRecurrencyLevel = defaultRecurrencyLevel;
    }


    public FieldSetLevelHelper getFieldSetLevelHelper()
    {
        return this.fieldSetLevelHelper;
    }


    @Required
    public void setFieldSetLevelHelper(FieldSetLevelHelper fieldSetLevelHelper)
    {
        this.fieldSetLevelHelper = fieldSetLevelHelper;
    }


    public int getDefaultMaxFieldSetSize()
    {
        return this.defaultMaxFieldSetSize;
    }


    public void setDefaultMaxFieldSetSize(int defaultMaxFieldSetSize)
    {
        this.defaultMaxFieldSetSize = defaultMaxFieldSetSize;
    }


    public SubclassRegistry getSubclassRegistry()
    {
        return this.subclassRegistry;
    }


    public void setSubclassRegistry(SubclassRegistry subclassRegistry)
    {
        this.subclassRegistry = subclassRegistry;
    }
}
