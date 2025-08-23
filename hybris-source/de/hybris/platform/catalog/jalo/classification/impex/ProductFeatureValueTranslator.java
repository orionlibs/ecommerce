package de.hybris.platform.catalog.jalo.classification.impex;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AtomicValueTranslator;
import de.hybris.platform.impex.jalo.translators.ItemPKTranslator;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.CSVUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

public class ProductFeatureValueTranslator extends SingleValueTranslator
{
    private static final Logger log = Logger.getLogger(ProductFeatureValueTranslator.class);
    private char collectionValueDelimiter = ',';
    private StandardColumnDescriptor descriptor;


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        super.init(columnDescriptor);
        this.descriptor = columnDescriptor;
        String customDelimiter = columnDescriptor.getDescriptorData().getModifier("collection-delimiter");
        if(customDelimiter != null && customDelimiter.length() > 0)
        {
            this.collectionValueDelimiter = customDelimiter.charAt(0);
        }
    }


    protected Object convertToJalo(String valueExpr, Item forItem)
    {
        List<String> parts = CSVUtils.splitAndUnescape(valueExpr, new char[] {this.collectionValueDelimiter}, false);
        if(parts.size() < 2)
        {
            throw new JaloInvalidParameterException("unsupported format: expected '[type]" + this.collectionValueDelimiter + "[value]' but was " + valueExpr, 0);
        }
        String type = parts.get(0);
        try
        {
            if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN.equalsIgnoreCase(type))
            {
                AtomicValueTranslator atomicValueTranslator = new AtomicValueTranslator(null, Boolean.class);
                atomicValueTranslator.init(this.descriptor);
                atomicValueTranslator.validate(this.descriptor);
                return atomicValueTranslator.importValue(parts.get(1), forItem);
            }
            if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER.equalsIgnoreCase(type))
            {
                AtomicValueTranslator atomicValueTranslator = new AtomicValueTranslator(null, classByName(parts.get(1)));
                atomicValueTranslator.init(this.descriptor);
                atomicValueTranslator.validate(this.descriptor);
                return atomicValueTranslator.importValue(parts.get(2), forItem);
            }
            if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.REFERENCE.equalsIgnoreCase(type))
            {
                return JaloSession.getCurrentSession().getItem(PK.parse(parts.get(1)));
            }
            if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.ENUM.equalsIgnoreCase(type))
            {
                if(parts.size() != 4)
                {
                    throw new JaloInvalidParameterException("unsupported format: expected 'enum" + this.collectionValueDelimiter + "[classSys]" + this.collectionValueDelimiter + "[classSysVer]" + this.collectionValueDelimiter + "[value]' but was " + valueExpr, 0);
                }
                ClassificationSystem system = CatalogManager.getInstance().getClassificationSystem(parts.get(1));
                if(system == null)
                {
                    setError();
                    return null;
                }
                ClassificationSystemVersion version = system.getSystemVersion(parts.get(2));
                if(version == null)
                {
                    setError();
                    return null;
                }
                ClassificationAttributeValue value = null;
                try
                {
                    value = version.getClassificationAttributeValue(parts.get(3));
                }
                catch(JaloItemNotFoundException e)
                {
                    setError();
                    return null;
                }
                if(value == null)
                {
                    setError();
                    return null;
                }
                return value;
            }
            if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING.equalsIgnoreCase(type))
            {
                AtomicValueTranslator atomicValueTranslator = new AtomicValueTranslator(null, String.class);
                atomicValueTranslator.init(this.descriptor);
                atomicValueTranslator.validate(this.descriptor);
                return atomicValueTranslator.importValue(parts.get(1), forItem);
            }
            if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.DATE.equalsIgnoreCase(type))
            {
                AtomicValueTranslator atomicValueTranslator = new AtomicValueTranslator(null, Date.class);
                atomicValueTranslator.init(this.descriptor);
                atomicValueTranslator.validate(this.descriptor);
                return atomicValueTranslator.importValue(parts.get(1), forItem);
            }
            log.warn("Unsupported attribute type " + type + ", will use type String");
            AtomicValueTranslator trans = new AtomicValueTranslator(null, String.class);
            trans.init(this.descriptor);
            trans.validate(this.descriptor);
            return trans.importValue(parts.get(1), forItem);
        }
        catch(HeaderValidationException e)
        {
            throw new JaloInvalidParameterException(e.getMessage(), 0);
        }
    }


    private Class<?> classByName(String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Cannot instantiate class" + className);
        }
    }


    protected String convertToString(Object value)
    {
        List<String> strings = new ArrayList<>();
        try
        {
            if(value instanceof Boolean)
            {
                strings.add(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN);
                AtomicValueTranslator trans = new AtomicValueTranslator(null, Boolean.class);
                trans.init(this.descriptor);
                trans.validate(this.descriptor);
                strings.add(trans.exportValue(value));
            }
            else if(value instanceof Number)
            {
                strings.add(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER);
                strings.add(value.getClass().getName());
                AtomicValueTranslator trans = new AtomicValueTranslator(null, value.getClass());
                trans.init(this.descriptor);
                trans.validate(this.descriptor);
                strings.add(trans.exportValue(value));
            }
            else if(value instanceof ClassificationAttributeValue)
            {
                strings.add(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.ENUM);
                ClassificationAttributeValue cValue = (ClassificationAttributeValue)value;
                strings.add(cValue.getSystemVersion().getClassificationSystem().getId());
                strings.add(cValue.getSystemVersion().getVersion());
                strings.add(cValue.getCode());
            }
            else if(value instanceof Item)
            {
                strings.add(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.REFERENCE);
                ItemPKTranslator translator = new ItemPKTranslator(((Item)value).getComposedType());
                translator.init(this.descriptor);
                translator.validate(this.descriptor);
                strings.add(translator.exportValue(value));
            }
            else if(value instanceof String)
            {
                strings.add(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING);
                AtomicValueTranslator trans = new AtomicValueTranslator(null, String.class);
                trans.init(this.descriptor);
                trans.validate(this.descriptor);
                strings.add(trans.exportValue(value));
            }
            else if(value instanceof Date)
            {
                strings.add(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.DATE);
                AtomicValueTranslator trans = new AtomicValueTranslator(null, Date.class);
                trans.init(this.descriptor);
                trans.validate(this.descriptor);
                strings.add(trans.exportValue(value));
            }
            else
            {
                throw new JaloInvalidParameterException("unknown value type", 0);
            }
        }
        catch(Exception e)
        {
            throw new JaloInvalidParameterException(e, 0);
        }
        return CSVUtils.joinAndEscape(strings, null, this.collectionValueDelimiter, false);
    }
}
