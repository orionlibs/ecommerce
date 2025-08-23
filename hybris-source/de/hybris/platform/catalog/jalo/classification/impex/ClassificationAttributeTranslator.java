package de.hybris.platform.catalog.jalo.classification.impex;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.catalog.jalo.classification.util.TypedFeature;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.AtomicValueTranslator;
import de.hybris.platform.impex.jalo.translators.ItemExpressionTranslator;
import de.hybris.platform.impex.jalo.translators.NotifiedSpecialValueTranslator;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ClassificationAttributeTranslator extends AbstractSpecialValueTranslator implements NotifiedSpecialValueTranslator
{
    public static final String IMPEX_NONEXISTEND_CLSATTRVALUE_FALLBACK_KEY = "impex.nonexistend.clsattrvalue.fallback.enabled";
    private static final Logger LOG = Logger.getLogger(ClassificationAttributeTranslator.class);
    protected String qualfier;
    protected String systemName;
    protected String versionName;
    protected String className;
    protected Language lang;
    protected Locale locale;
    protected String dateFormatString;
    protected String numberFormatString;
    protected ClassificationAttribute classAttr;
    protected ClassAttributeAssignment classAttrAssignment;
    protected ClassificationSystemVersion classSystemVersion;
    protected char collectionDelimiter = ',';
    protected char attributeSeparator = ':';
    protected char[] TO_ESCAPE = new char[] {this.collectionDelimiter};
    protected PK allDoneFor = null;
    protected String currentCellValue;
    private SpecialColumnDescriptor columnDescriptor;
    private final transient Map<ClassAttributeAssignment, SingleValueTranslator> class2TranslatorCache;
    private SingleValueTranslator fallbackValueTranslator;
    private final transient Map<ClassificationClass, ClassAttributeAssignment> matchingClassCache;


    public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.init(columnDescriptor);
        this.columnDescriptor = columnDescriptor;
        if(!TypeManager.getInstance().getComposedType(Product.class).isAssignableFrom((Type)columnDescriptor.getHeader().getConfiguredComposedType()))
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "invalid header type " + columnDescriptor
                            .getHeader()
                            .getConfiguredComposedType()
                            .getCode() + " for ClassificationAttributeTranslator in column " + columnDescriptor
                            .getValuePosition() + ":" + columnDescriptor.getQualifier() + " - expected Product or any of its subtypes", 0);
        }
        if(columnDescriptor.getDescriptorData().getModifier("lang") != null)
        {
            this.lang = StandardColumnDescriptor.findLanguage(columnDescriptor.getHeader(), columnDescriptor.getDescriptorData()
                            .getModifier("lang"));
        }
        this.locale = columnDescriptor.getHeader().getReader().getLocale();
        if(this.lang != null)
        {
            this.locale = new Locale(this.lang.toString());
        }
        this.qualfier = columnDescriptor.getQualifier();
        if(this.qualfier.startsWith("@"))
        {
            this.qualfier = this.qualfier.substring("@".length()).trim();
        }
        this.systemName = columnDescriptor.getDescriptorData().getModifier("system");
        this.versionName = columnDescriptor.getDescriptorData().getModifier("version");
        this.className = columnDescriptor.getDescriptorData().getModifier("class");
        lookupAttributeOrAssignment(this.systemName, this.versionName, this.className, this.qualfier, columnDescriptor);
        String customDelimiter = columnDescriptor.getDescriptorData().getModifier("collection-delimiter");
        if(customDelimiter != null && customDelimiter.length() > 0)
        {
            this.collectionDelimiter = customDelimiter.charAt(0);
            this.TO_ESCAPE = new char[] {this.collectionDelimiter};
        }
        this.dateFormatString = columnDescriptor.getDescriptorData().getModifier("dateformat");
        this.numberFormatString = columnDescriptor.getDescriptorData().getModifier("numberformat");
    }


    private void lookupAttributeOrAssignment(String systemName, String versionName, String className, String qualifier, SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        ClassificationSystem sys = CatalogManager.getInstance().getClassificationSystem(systemName);
        if(sys == null)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "unknown classification system '" + systemName + "' in column " + columnDescriptor
                            .getValuePosition() + ":" + columnDescriptor.getQualifier(), 0);
        }
        this.classSystemVersion = (ClassificationSystemVersion)sys.getCatalogVersion(versionName);
        if(this.classSystemVersion == null)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "unknown classification system version '" + systemName + "." + versionName + "' in column " + columnDescriptor
                            .getValuePosition() + ":" + columnDescriptor.getQualifier(), 0);
        }
        try
        {
            this.classAttr = this.classSystemVersion.getClassificationAttribute(qualifier);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "unknown classification attribute " + qualifier + " within system version '" + systemName + "." + versionName + "' in column " + columnDescriptor
                            .getValuePosition() + ":" + columnDescriptor.getQualifier(), 0);
        }
        if(StringUtils.isNotBlank(className))
        {
            ClassificationClass classificationClass = null;
            try
            {
                classificationClass = this.classSystemVersion.getClassificationClass(className);
            }
            catch(JaloItemNotFoundException e)
            {
                throw new HeaderValidationException(columnDescriptor.getHeader(), "unknown classification class " + className + " within system version '" + systemName + "." + versionName + "' in column " + columnDescriptor
                                .getValuePosition() + ":" + columnDescriptor.getQualifier(), 0);
            }
            try
            {
                this.classAttrAssignment = classificationClass.getAttributeAssignment(this.classAttr);
            }
            catch(JaloItemNotFoundException e)
            {
                throw new HeaderValidationException(columnDescriptor.getHeader(), "unknown attribute assignment " + className + "." + qualifier + " within system version '" + systemName + "." + versionName + "' in column " + columnDescriptor
                                .getValuePosition() + ":" + columnDescriptor.getQualifier(), 0);
            }
        }
    }


    public ClassificationAttributeTranslator()
    {
        this.class2TranslatorCache = new HashMap<>();
        this.fallbackValueTranslator = null;
        this.matchingClassCache = new HashMap<>();
    }


    public ClassificationAttributeTranslator(ClassificationSystemVersion sysVer, ClassificationAttribute attr, char delimiter, Language lang)
    {
        this.class2TranslatorCache = new HashMap<>();
        this.fallbackValueTranslator = null;
        this.matchingClassCache = new HashMap<>();
        this.classSystemVersion = sysVer;
        this.systemName = this.classSystemVersion.getCatalog().getId();
        this.versionName = this.classSystemVersion.getVersion();
        this.lang = lang;
        this.classAttr = attr;
        this.qualfier = attr.getCode();
        this.collectionDelimiter = delimiter;
        this.TO_ESCAPE = new char[] {delimiter};
    }


    protected AbstractValueTranslator getSingleCellValueTranslator(ClassAttributeAssignment assignment) throws HeaderValidationException
    {
        AtomicValueTranslator atomicValueTranslator;
        SingleValueTranslator ret = this.class2TranslatorCache.get(assignment);
        if(ret == null)
        {
            String type = assignment.getAttributeType().getCode();
            if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN.equalsIgnoreCase(type))
            {
                AtomicValueTranslator trans = new AtomicValueTranslator(null, Boolean.class);
                trans.setLocale(this.locale);
                atomicValueTranslator = trans;
            }
            else if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER.equalsIgnoreCase(type))
            {
                AtomicValueTranslator trans = new AtomicValueTranslator(null, Double.class);
                trans.setLocale(this.locale);
                if(this.numberFormatString != null)
                {
                    trans.setNumberFormat(Utilities.getDecimalFormat(this.numberFormatString, this.locale));
                }
                atomicValueTranslator = trans;
            }
            else if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.ENUM.equalsIgnoreCase(type))
            {
                ClassificationAttributeValueTranslator classificationAttributeValueTranslator = new ClassificationAttributeValueTranslator(assignment);
            }
            else if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.REFERENCE.equalsIgnoreCase(type))
            {
                List[] arrayOfList = ((AbstractDescriptor.ColumnParams)this.columnDescriptor.getDescriptorData()).getItemPatternLists();
                ComposedType referenceType = assignment.getReferenceType();
                ItemExpressionTranslator itemExpressionTranslator = new ItemExpressionTranslator(referenceType, arrayOfList[0]);
            }
            else if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING.equalsIgnoreCase(type))
            {
                AtomicValueTranslator trans = new AtomicValueTranslator(null, String.class);
                trans.setLocale(this.locale);
                atomicValueTranslator = trans;
            }
            else if(GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.DATE.equalsIgnoreCase(type))
            {
                AtomicValueTranslator trans = new AtomicValueTranslator(null, Date.class);
                trans.setLocale(this.locale);
                if(this.dateFormatString != null)
                {
                    trans.setDateFormat(Utilities.getSimpleDateFormat(this.dateFormatString, this.locale));
                }
                atomicValueTranslator = trans;
            }
            else
            {
                LOG.warn("Unsupported attribute type " + type + ", will use type String");
                AtomicValueTranslator trans = new AtomicValueTranslator(null, String.class);
                trans.setLocale(this.locale);
                atomicValueTranslator = trans;
            }
            this.class2TranslatorCache.put(assignment, atomicValueTranslator);
        }
        return (AbstractValueTranslator)atomicValueTranslator;
    }


    protected AbstractValueTranslator getFallbackValueTranslator()
    {
        if(this.fallbackValueTranslator == null)
        {
            this.fallbackValueTranslator = (SingleValueTranslator)new AtomicValueTranslator(null, String.class);
        }
        return (AbstractValueTranslator)this.fallbackValueTranslator;
    }


    protected ClassAttributeAssignment getAssignment()
    {
        return this.classAttrAssignment;
    }


    protected ClassAttributeAssignment matchAssignment(Collection<ClassificationClass> classes)
    {
        for(ClassificationClass myClass : classes)
        {
            ClassAttributeAssignment match = this.matchingClassCache.get(myClass);
            if(match == null)
            {
                try
                {
                    if(this.classAttr != null)
                    {
                        match = myClass.getAttributeAssignment(this.classAttr);
                        this.matchingClassCache.put(myClass, match);
                    }
                }
                catch(JaloItemNotFoundException jaloItemNotFoundException)
                {
                }
            }
            if(match != null)
            {
                return match;
            }
        }
        return null;
    }


    protected SessionContext getValueCtx(boolean localized) throws HeaderValidationException
    {
        SessionContext ret = JaloSession.getCurrentSession().getSessionContext();
        if(localized)
        {
            if(this.lang != null)
            {
                ret = new SessionContext(ret);
                ret.setLanguage(this.lang);
            }
            else if(ret.getLanguage() == null)
            {
                throw new HeaderValidationException("neither session nor colum provided language for localized classification attribute " + this.classAttr, 0);
            }
        }
        return ret;
    }


    protected TypedFeature getFeature(Product product)
    {
        ClassAttributeAssignment assignment = matchAssignment(
                        CatalogManager.getInstance().getClassificationClasses(product));
        return (assignment != null) ? TypedFeature.loadTyped(product, assignment) : null;
    }


    public String performExport(Item item) throws ImpExException
    {
        if(item != null && item.isAlive())
        {
            TypedFeature<? extends Object> feat = getFeature((Product)item);
            if(feat != null)
            {
                ClassAttributeAssignment assignment = feat.getClassAttributeAssignment();
                boolean localized = assignment.isLocalizedAsPrimitive();
                AbstractValueTranslator trans = getSingleCellValueTranslator(assignment);
                int index = 0;
                StringBuilder stringBuilder = new StringBuilder();
                for(FeatureValue fv : feat.getValues(getValueCtx(localized)))
                {
                    if(index > 0)
                    {
                        stringBuilder.append(this.collectionDelimiter);
                    }
                    stringBuilder.append(CSVUtils.escapeString(trans.exportValue(fv.getValue()), this.TO_ESCAPE, true));
                    index++;
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }


    public void performImport(String cellValue, Item processedItem) throws ImpExException
    {
        this.allDoneFor = null;
        this.currentCellValue = cellValue;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Object> translateCurrentValues(ValueLine line, ClassAttributeAssignment assignment, Product processedItem) throws HeaderValidationException
    {
        Collection<UnitAwareValue> resultTyped = translateCurrentUnitAwareValues(line, assignment, processedItem);
        if(resultTyped == null)
        {
            return null;
        }
        Collection<Object> result = new LinkedList();
        for(UnitAwareValue unitAwareValue : resultTyped)
        {
            result.add(unitAwareValue.getValue());
        }
        return result;
    }


    public Collection<UnitAwareValue> translateCurrentUnitAwareValues(ValueLine line, ClassAttributeAssignment assignment, Product processedItem) throws HeaderValidationException
    {
        if(assignment == null)
        {
            return null;
        }
        AbstractValueTranslator trans = getSingleCellValueTranslator(assignment);
        Collection<UnitAwareValue> values = null;
        if(this.currentCellValue != null && this.currentCellValue.length() > 0)
        {
            for(Object o : splitValues(assignment, this.currentCellValue))
            {
                String singleStr = (String)o;
                if(StringUtils.isEmpty(singleStr))
                {
                    continue;
                }
                if(values == null)
                {
                    values = new LinkedList<>();
                }
                UnitAwareValue unitAwareValue = translateCurrentUnitAwareValue(line, assignment, processedItem, trans, singleStr);
                if(unitAwareValue != null)
                {
                    values.add(unitAwareValue);
                }
            }
        }
        return values;
    }


    private UnitAwareValue translateCurrentUnitAwareValue(ValueLine line, ClassAttributeAssignment assignment, Product processedItem, AbstractValueTranslator trans, String singleStr)
    {
        if(StringUtils.isEmpty(singleStr))
        {
            return null;
        }
        try
        {
            ClassificationAttributeUnit classificationAttributeUnit = getUnit(assignment, singleStr, this.systemName, this.versionName);
            String value = (classificationAttributeUnit != null) ? extractValueWithoutUnit(assignment, singleStr) : singleStr;
            Object transValue = trans.importValue(CSVUtils.unescapeString(value, this.TO_ESCAPE, true), (Item)processedItem);
            if(transValue == null && trans.wasUnresolved())
            {
                line.getValueEntry(this.columnDescriptor.getValuePosition())
                                .markUnresolved("Classification attribute assigment: " + assignment + " for value: " + singleStr + " does not exist");
                return null;
            }
            return new UnitAwareValue(transValue, classificationAttributeUnit);
        }
        catch(JaloInvalidParameterException e)
        {
            if(Config.getBoolean("impex.nonexistend.clsattrvalue.fallback.enabled", false))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Fallback ENABLED");
                }
                LOG.warn("Value " + singleStr + " is not of type " + assignment.getAttributeType().getCode() + " will use type string as fallback (" + e
                                .getMessage() + ")");
                Object transValue = getFallbackValueTranslator().importValue(CSVUtils.unescapeString(singleStr, this.TO_ESCAPE, true), (Item)processedItem);
                return new UnitAwareValue(transValue, null);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Fallback DISABLED. Marking line as unresolved. Will try to import value in another pass");
            }
            line.getValueEntry(this.columnDescriptor.getValuePosition()).markUnresolved(e.getMessage());
            return null;
        }
    }


    private String extractValueWithoutUnit(ClassAttributeAssignment assignment, String singleStr)
    {
        if(assignment.getUnit() == null || singleStr == null || singleStr.indexOf(this.attributeSeparator) == -1 || singleStr
                        .indexOf(this.attributeSeparator) != singleStr.lastIndexOf(this.attributeSeparator))
        {
            return singleStr;
        }
        return singleStr.split(String.valueOf(this.attributeSeparator))[0];
    }


    private String extractUnitName(ClassAttributeAssignment assignment, String singleStr)
    {
        String unitName = null;
        boolean warn = false;
        if(assignment.getUnit() != null && singleStr != null && singleStr.indexOf(this.attributeSeparator) != -1)
        {
            if(singleStr.lastIndexOf(this.attributeSeparator) == singleStr.indexOf(this.attributeSeparator))
            {
                return singleStr.split(String.valueOf(this.attributeSeparator))[1];
            }
            warn = true;
        }
        String msg = "Invalid classification attribute unit syntax - was: [" + singleStr + "], expected format [value" + this.attributeSeparator + "unit].";
        if(((AbstractDescriptor.ColumnParams)this.columnDescriptor.getDescriptorData()).hasItemPattern())
        {
            for(List<AbstractDescriptor.ColumnParams> columnParamsList : ((AbstractDescriptor.ColumnParams)this.columnDescriptor.getDescriptorData())
                            .getItemPatternLists())
            {
                for(AbstractDescriptor.ColumnParams columnParams : columnParamsList)
                {
                    if(columnParams.getQualifier().equals("unit") && columnParams
                                    .getModifier("default") != null)
                    {
                        if(warn)
                        {
                            LOG.warn(msg + " Classification attribute unit from script header [" + msg + "] will be used instead.");
                        }
                        return columnParams.getModifier("default");
                    }
                }
            }
        }
        if(warn && assignment.getUnit() != null)
        {
            LOG.warn(msg + " Classification attribute unit from attribute assignment [" + msg + "] will be used instead.");
        }
        return unitName;
    }


    protected List splitValues(ClassAttributeAssignment assignment, String valueCollection)
    {
        if(assignment.isMultiValuedAsPrimitive() || assignment.isRangeAsPrimitive())
        {
            return CSVUtils.splitAndUnescape(valueCollection, new char[] {this.collectionDelimiter}, false);
        }
        return Collections.singletonList(CSVUtils.unescapeString(valueCollection, this.TO_ESCAPE, false));
    }


    public void notifyTranslationEnd(ValueLine line, HeaderDescriptor header, Item processedItem) throws ImpExException
    {
        ClassificationLineImporter.importFeatures(line, header, (Product)processedItem);
    }


    private ClassificationAttributeUnit getUnit(ClassAttributeAssignment assignment, String singleStr, String systemName, String versionName)
    {
        String unitName = extractUnitName(assignment, singleStr);
        if(unitName == null)
        {
            return null;
        }
        try
        {
            ClassificationSystem classificationSystem = CatalogManager.getInstance().getClassificationSystem(systemName);
            if(classificationSystem == null)
            {
                LOG.warn("Classification system with id [" + systemName + "] not found.");
                return null;
            }
            ClassificationSystemVersion version = classificationSystem.getSystemVersion(versionName);
            if(version == null)
            {
                LOG.warn("Classification system version with name [" + versionName + "] not found in classification system [" + systemName + "].");
                return null;
            }
            return version.getAttributeUnit(unitName);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("Classification attribute unit with code [" + unitName + "] not found in classification system [" + systemName + ":" + versionName + "].");
            return null;
        }
    }
}
