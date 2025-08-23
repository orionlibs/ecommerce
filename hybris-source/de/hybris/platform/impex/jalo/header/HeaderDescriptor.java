package de.hybris.platform.impex.jalo.header;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.constants.GeneratedImpExConstants;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.translators.AbstractTypeTranslator;
import de.hybris.platform.impex.jalo.translators.DefaultTypeTranslator;
import de.hybris.platform.impex.jalo.translators.HeaderCellTranslator;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;

public class HeaderDescriptor extends AbstractDescriptor
{
    private static final Logger log = Logger.getLogger(HeaderDescriptor.class.getName());
    private final ImpExReader reader;
    private final AttributeConstraintFilter attributeConstraintFilter;
    private List<String> rawColumns;
    private List<AbstractColumnDescriptor> columns;
    private boolean columnsNotYetSet = false;
    private Map<String, PermittedTypeMapping> permittedTypes;
    private Map<PK, List<String>> omittedTypesMessages;
    private final String location;
    private ComposedType configuredType;
    private ComposedType defaultType;
    private EnumerationValue myMode = null;
    private final DocumentIDRegistry docIDRegistry;
    private final AtomicBoolean warningAlreadyPrinted = new AtomicBoolean(false);
    private final HeaderValidationException invalidHeaderException;


    public boolean isValid()
    {
        return (this.invalidHeaderException == null);
    }


    public HeaderValidationException getInvalidHeaderException()
    {
        return this.invalidHeaderException;
    }


    private Map<String, Object> interceptorRelatedParameters = null;


    public HeaderDescriptor(ImpExReader reader, String expr, List<String> columnExpressions, String location, DocumentIDRegistry docIDRegistry) throws HeaderValidationException
    {
        this(reader, expr, columnExpressions, location, docIDRegistry, null);
    }


    public HeaderDescriptor(ImpExReader reader, String expr, AbstractDescriptor.DescriptorParams headerParams, String location, DocumentIDRegistry docIDRegistry) throws HeaderValidationException
    {
        super(expr, headerParams);
        this.reader = reader;
        this.location = location;
        this.docIDRegistry = docIDRegistry;
        AttributeConstraintFilter f = (reader != null) ? reader.getAttributeConstraintFilter() : null;
        this.attributeConstraintFilter = (f != null) ? f : (AttributeConstraintFilter)new DefaultAttributeConstraintFilter();
        this.configuredType = findConfiguredComposedType();
        this.columnsNotYetSet = true;
        this.columns = new ArrayList<>();
        this.permittedTypes = (Map<String, PermittedTypeMapping>)new CaseInsensitiveMap();
        this.invalidHeaderException = null;
    }


    public HeaderDescriptor(ImpExReader reader, String expr, List<String> columnExpressions, String location, DocumentIDRegistry docIDRegistry, HeaderValidationException headerException) throws HeaderValidationException
    {
        super(expr);
        this.reader = reader;
        this.location = location;
        this.docIDRegistry = docIDRegistry;
        AttributeConstraintFilter f = reader.getAttributeConstraintFilter();
        this.attributeConstraintFilter = (f != null) ? f : (AttributeConstraintFilter)new DefaultAttributeConstraintFilter();
        this.invalidHeaderException = headerException;
        this.rawColumns = columnExpressions;
        if(this.invalidHeaderException == null)
        {
            this.configuredType = findConfiguredComposedType();
            boolean rethrowExceptionIfValid = (headerException == null);
            this.columns = Collections.unmodifiableList(((AbstractTypeTranslator)getDescriptorTranslator())
                            .translateColumnDescriptors(this, columnExpressions, rethrowExceptionIfValid));
            this.permittedTypes = calculatePermittedTypes();
            this.defaultType = calculateDefaultComposedType();
            adjustValuePositions();
        }
    }


    public void setColumns(List<AbstractColumnDescriptor> columnDescriptors) throws HeaderValidationException
    {
        if(!this.columnsNotYetSet)
        {
            throw new IllegalStateException("columns already set");
        }
        this.columns.addAll(columnDescriptors);
        this.permittedTypes.putAll(calculatePermittedTypes());
        this.defaultType = calculateDefaultComposedType();
        this.columnsNotYetSet = false;
        adjustValuePositions();
    }


    protected void adjustValuePositions() throws HeaderValidationException
    {
        Set<Integer> usedPositions = null;
        int offset = 0;
        for(int i = 0; i < this.columns.size(); i++)
        {
            AbstractColumnDescriptor cd = this.columns.get(i);
            if(cd != null)
            {
                if(cd instanceof StandardColumnDescriptor && ((StandardColumnDescriptor)cd).isVirtual())
                {
                    offset--;
                    cd.changePosition(cd.getValuePosition() * -1);
                }
                else
                {
                    if(offset < 0)
                    {
                        cd.shiftPosition(offset);
                    }
                    String pos = cd.getDescriptorData().getModifier("pos");
                    if(pos != null)
                    {
                        if(usedPositions == null)
                        {
                            if(i != 0)
                            {
                                throw new HeaderValidationException("At least one column, but not all columns use the modifier 'pos'. Wether use it for all or nothing!!", 0);
                            }
                            usedPositions = new HashSet();
                        }
                        int newPos = Integer.parseInt(pos);
                        if(usedPositions.add(Integer.valueOf(newPos)))
                        {
                            cd.changePosition(newPos);
                        }
                        else
                        {
                            throw new HeaderValidationException("Position " + newPos + " of column " + cd
                                            .getQualifier() + " is already used by another column!!", 0);
                        }
                    }
                }
            }
        }
    }


    public ImpExReader getReader()
    {
        return this.reader;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isDebugEnabled()
    {
        return log.isDebugEnabled();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void debug(String msg)
    {
        log.debug(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isInfoEnabled()
    {
        return log.isInfoEnabled();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void info(String msg)
    {
        log.info(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void warn(String msg)
    {
        log.warn(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void error(String msg)
    {
        log.error(msg);
    }


    protected HeaderCellTranslator createTranslator(String expr)
    {
        return (HeaderCellTranslator)new DefaultTypeTranslator();
    }


    public boolean isBatchMode()
    {
        return "true".equalsIgnoreCase(getDescriptorData().getModifier("batchmode"));
    }


    public boolean isInsertMode()
    {
        return GeneratedImpExConstants.Enumerations.ImpExProcessModeEnum.INSERT
                        .equalsIgnoreCase(((AbstractDescriptor.HeaderParams)getDescriptorData()).getMode());
    }


    public boolean isUpdateMode()
    {
        return GeneratedImpExConstants.Enumerations.ImpExProcessModeEnum.UPDATE
                        .equalsIgnoreCase(((AbstractDescriptor.HeaderParams)getDescriptorData()).getMode());
    }


    public boolean isInsertUpdateMode()
    {
        return GeneratedImpExConstants.Enumerations.ImpExProcessModeEnum.INSERT_UPDATE
                        .equalsIgnoreCase(((AbstractDescriptor.HeaderParams)getDescriptorData()).getMode());
    }


    public boolean isRemoveMode()
    {
        return GeneratedImpExConstants.Enumerations.ImpExProcessModeEnum.REMOVE
                        .equalsIgnoreCase(((AbstractDescriptor.HeaderParams)getDescriptorData()).getMode());
    }


    public List<AbstractColumnDescriptor> getColumns()
    {
        return this.columns;
    }


    public int getNumCols()
    {
        return this.columns.size();
    }


    public <T extends AbstractColumnDescriptor> Collection<T> getSpecificColumns(Class<T> descriptor)
    {
        Collection<AbstractColumnDescriptor> ret = null;
        for(int i = 0; i < this.columns.size(); i++)
        {
            AbstractColumnDescriptor cd = this.columns.get(i);
            if(cd != null && descriptor.isInstance(cd))
            {
                if(ret == null)
                {
                    ret = new LinkedList();
                }
                ret.add(cd);
            }
        }
        return (ret != null) ? (Collection)ret : Collections.EMPTY_LIST;
    }


    public String getTypeCode()
    {
        return ((AbstractDescriptor.HeaderParams)getDescriptorData()).getType();
    }


    protected ComposedType findConfiguredComposedType() throws HeaderValidationException
    {
        try
        {
            return TypeManager.getInstance().getComposedType(getTypeCode());
        }
        catch(JaloItemNotFoundException e)
        {
            throw new HeaderValidationException("unknown type '" + getTypeCode() + "' in header '" + getDefinitionSrc() + "'", 1);
        }
    }


    public ComposedType getConfiguredComposedType()
    {
        return this.configuredType;
    }


    public String getConfiguredComposedTypeCode()
    {
        return (this.configuredType == null) ? null : this.configuredType.getCode();
    }


    protected ComposedType calculateDefaultComposedType()
    {
        Collection<ComposedType> toCheck = Collections.singleton(getConfiguredComposedType());
        while(!toCheck.isEmpty())
        {
            Collection<ComposedType> next = new ArrayList<>();
            for(Iterator<ComposedType> iter = toCheck.iterator(); iter.hasNext(); )
            {
                ComposedType ct = iter.next();
                if(isPermittedType(ct) && !ct.isAbstract() && !ct.isJaloOnly())
                {
                    return ct;
                }
                next.addAll(ct.getSubTypes());
            }
            toCheck = next;
        }
        return null;
    }


    public ComposedType getDefaultComposedType()
    {
        return this.defaultType;
    }


    public boolean isDefaultComposedTypeSingleton()
    {
        return this.defaultType.isSingleton();
    }


    public String getDefaultComposedTypeCode()
    {
        return this.defaultType.getCode();
    }


    public Set<String> getPermittedTypeCodes()
    {
        Set<String> ret = new HashSet<>();
        for(Map.Entry<String, PermittedTypeMapping> entry : this.permittedTypes.entrySet())
        {
            ret.add(entry.getKey());
        }
        return ret;
    }


    public boolean isPermittedType(ComposedType ct)
    {
        return (this.permittedTypes.get(ct.getCode()) != null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isPermittedTypeForInsert(ComposedType ct)
    {
        return isPermittedType(ct.getCode(), false);
    }


    public boolean isPermittedTypeForInsert(String ct)
    {
        return isPermittedType(ct, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isPermittedTypeForUpdate(ComposedType ct)
    {
        return isPermittedType(ct.getCode(), true);
    }


    public boolean isPermittedTypeForUpdate(String ct)
    {
        return isPermittedType(ct, true);
    }


    private boolean isPermittedType(String code, boolean isForUpdateOnly)
    {
        boolean isPermitted = false;
        PermittedTypeMapping pt = this.permittedTypes.get(code);
        if(pt != null && (isForUpdateOnly || !pt.isOnlyForUpdate()))
        {
            isPermitted = true;
        }
        return isPermitted;
    }


    public boolean isPermittedType(String code)
    {
        return (this.permittedTypes.get(code) != null);
    }


    public EnumerationValue getMode()
    {
        if(this.myMode == null)
        {
            this.myMode = EnumerationManager.getInstance().getEnumerationValue(GeneratedImpExConstants.TC.IMPEXPROCESSMODEENUM, ((AbstractDescriptor.HeaderParams)
                            getDescriptorData()).getMode());
        }
        return this.myMode;
    }


    public Map<Integer, String> dump()
    {
        Map<Integer, String> ret = new HashMap<>();
        ret.put(Integer.valueOf(0), getDefinitionSrc());
        if(this.invalidHeaderException != null)
        {
            int colIdx = 1;
            for(String col : this.rawColumns)
            {
                ret.put(Integer.valueOf(colIdx), col);
                colIdx++;
            }
        }
        else
        {
            int offset = 0;
            for(int i = 0; i < this.columns.size(); i++)
            {
                AbstractColumnDescriptor col = this.columns.get(i);
                if(col != null)
                {
                    if(col instanceof StandardColumnDescriptor && ((StandardColumnDescriptor)col).isVirtual())
                    {
                        ret.put(Integer.valueOf(-1 * col.getValuePosition()), col.getDefinitionSrc());
                        offset++;
                    }
                    else
                    {
                        ret.put(Integer.valueOf(col.getValuePosition() + offset), col.getDefinitionSrc());
                    }
                }
            }
        }
        return ret;
    }


    protected void addOmittedTypeMessage(PK typePK, String msg)
    {
        if(this.omittedTypesMessages == null)
        {
            this.omittedTypesMessages = new HashMap<>();
        }
        List<String> messages = this.omittedTypesMessages.get(typePK);
        if(messages == null)
        {
            messages = new LinkedList<>();
            this.omittedTypesMessages.put(typePK, messages);
        }
        messages.add(msg);
    }


    protected PermittedTypeMapping calculatePermittedType(ComposedType ct)
    {
        if(log.isDebugEnabled())
        {
            log.debug("\n######################################################################################\nCalculating permitted types for: " + ct
                            .getCode() + "\n######################################################################################\n\n");
        }
        if(ImpExUtils.isStrictMode(this.reader.getValidationMode()))
        {
            if(ct.isAbstract())
            {
                String msg = "type " + ct.getCode() + " is abstract  - no permitted type for mode " + this.reader.getValidationMode().getCode();
                if(log.isDebugEnabled())
                {
                    log.debug(msg);
                }
                addOmittedTypeMessage(ct.getPK(), msg);
                return null;
            }
            if(ct.isJaloOnly())
            {
                String msg = "type " + ct.getCode() + " is jaloOnly - no permitted type for mode " + this.reader.getValidationMode().getCode();
                if(log.isDebugEnabled())
                {
                    log.debug(msg);
                }
                addOmittedTypeMessage(ct.getPK(), msg);
                return null;
            }
            Collection<StandardColumnDescriptor> uniqueColumns = calculateUniqueAttributeColumns(ct);
            if(!isInsertMode() && uniqueColumns.isEmpty() && !ct.isSingleton())
            {
                String msg = "type " + ct.getCode() + " has no unique columns - no permitted type for mode " + this.reader.getValidationMode().getCode();
                if(log.isDebugEnabled())
                {
                    log.debug(msg);
                }
                addOmittedTypeMessage(ct.getPK(), msg);
                return null;
            }
            PermittedTypeMapping permittedTypeMapping = new PermittedTypeMapping(this, ct);
            if(isRemoveMode())
            {
                permittedTypeMapping.enableColumns(uniqueColumns);
            }
            else
            {
                boolean creationAttributesRequired = (isInsertMode() || isInsertUpdateMode());
                Set<String> missing = new HashSet<>();
                for(Iterator<AttributeDescriptor> iter = ct.getAttributeDescriptorsIncludingPrivate().iterator(); iter.hasNext(); )
                {
                    AttributeDescriptor ad = iter.next();
                    Collection<StandardColumnDescriptor> fittingColumns = getColumnsByAttribute(ad);
                    permittedTypeMapping.enableColumns(fittingColumns);
                    if(fittingColumns.isEmpty() && creationAttributesRequired)
                    {
                        String q = ad.getQualifier();
                        if(Item.PK.equalsIgnoreCase(q) || Item.CREATION_TIME.equalsIgnoreCase(q) || Item.TYPE.equalsIgnoreCase(q))
                        {
                            continue;
                        }
                        boolean initialOnly = this.attributeConstraintFilter.isInitialOnly(ad);
                        if(!ad.isWritable() && (!creationAttributesRequired || !initialOnly))
                        {
                            continue;
                        }
                        if(this.attributeConstraintFilter.isMandatory(ad))
                        {
                            if(ad.getDefaultValue(null) == null)
                            {
                                missing.add(ad.getQualifier());
                                continue;
                            }
                            if(log.isDebugEnabled())
                            {
                                log.debug("ignoring missing mandatory creation attribute " + ad.getEnclosingType()
                                                .getCode() + "." + q + " since it provides a default value");
                            }
                            continue;
                        }
                        if(creationAttributesRequired && initialOnly && log.isDebugEnabled() && !Item.OWNER.equalsIgnoreCase(q))
                        {
                            log.debug("initial-only attribute " + ad.getEnclosingType().getCode() + "." + q + " is missing - ignored since it is not mandatory");
                        }
                    }
                }
                if(!missing.isEmpty())
                {
                    String msg = "type " + ct.getCode() + " requires missing column" + ((missing.size() == 1) ? " " : "s ") + missing;
                    if(log.isDebugEnabled())
                    {
                        log.debug(msg);
                    }
                    addOmittedTypeMessage(ct.getPK(), msg);
                    permittedTypeMapping.setOnlyForUpdate(true);
                    return permittedTypeMapping;
                }
            }
            return permittedTypeMapping;
        }
        PermittedTypeMapping ret = new PermittedTypeMapping(this, ct);
        for(AttributeDescriptor ad : ct.getAttributeDescriptorsIncludingPrivate())
        {
            Collection<StandardColumnDescriptor> toEnable = getColumnsByAttribute(ad);
            ret.enableColumns(toEnable);
        }
        return ret;
    }


    protected Map<String, PermittedTypeMapping> calculatePermittedTypes()
    {
        CaseInsensitiveMap<String, PermittedTypeMapping> caseInsensitiveMap = new CaseInsensitiveMap();
        ComposedType myType = getConfiguredComposedType();
        if(log.isDebugEnabled())
        {
            log.debug("Calculate permitted types for " + myType.getCode());
        }
        PermittedTypeMapping tm = calculatePermittedType(myType);
        if(tm != null)
        {
            caseInsensitiveMap.put(myType.getCode(), tm);
        }
        for(ComposedType subtype : myType.getAllSubTypes())
        {
            if(log.isDebugEnabled())
            {
                log.debug("Check subtype " + subtype.getCode() + " of " + myType.getCode() + " as permitted type");
            }
            tm = calculatePermittedType(subtype);
            if(tm != null)
            {
                caseInsensitiveMap.put(subtype.getCode(), tm);
            }
        }
        return (Map<String, PermittedTypeMapping>)caseInsensitiveMap;
    }


    public void validate() throws HeaderValidationException
    {
        CaseInsensitiveMap<String, List> caseInsensitiveMap;
        Collection<String> unknown = null;
        boolean atLeastOneUniqueModifierFound = false;
        Map<String, List<StandardColumnDescriptor>> clashing = null;
        for(Iterator<AbstractColumnDescriptor> iter = getColumns().iterator(); iter.hasNext(); )
        {
            AbstractColumnDescriptor acd = iter.next();
            if(acd != null)
            {
                acd.validate();
                if(acd instanceof StandardColumnDescriptor)
                {
                    StandardColumnDescriptor cd = (StandardColumnDescriptor)acd;
                    if((isInsertUpdateMode() || isUpdateMode()) && !atLeastOneUniqueModifierFound && cd.isUnique())
                    {
                        atLeastOneUniqueModifierFound = true;
                    }
                    if(cd.getAttributeDescriptor() == null)
                    {
                        if(unknown == null)
                        {
                            unknown = new HashSet<>();
                        }
                        String tc = cd.getComposedTypeCode();
                        String str1 = cd.getQualifier();
                        unknown.add((((tc != null) ? tc : getTypeCode()) + "." + ((tc != null) ? tc : getTypeCode())).toLowerCase(LocaleHelper.getPersistenceLocale()));
                        continue;
                    }
                    String q = cd.getQualifier();
                    String ctCode1 = (cd.getComposedTypeCode() != null) ? cd.getComposedTypeCode() : getTypeCode();
                    for(Iterator<AbstractColumnDescriptor> iterator = getColumnsByQualifier(cd.getQualifier()).iterator(); iterator.hasNext(); )
                    {
                        AbstractColumnDescriptor otherCD = iterator.next();
                        if(cd == otherCD || !(otherCD instanceof StandardColumnDescriptor))
                        {
                            continue;
                        }
                        StandardColumnDescriptor sameCD = (StandardColumnDescriptor)otherCD;
                        if(sameCD.getAttributeDescriptor() == null)
                        {
                            continue;
                        }
                        String ctCode2 = (sameCD.getComposedTypeCode() != null) ? sameCD.getComposedTypeCode() : getTypeCode();
                        if((ctCode1 == ctCode2 || (ctCode1 != null && ctCode1
                                        .equalsIgnoreCase(ctCode2))) && ((
                                        isUpdateMode() && ((!cd.isLocalized() && cd.isUnique() == sameCD.isUnique()) || (cd
                                                        .isLocalized() && cd.getLanguageModifier() != null && cd
                                                        .getLanguageModifier().equalsIgnoreCase(sameCD.getLanguageModifier()) && cd
                                                        .isUnique() == sameCD.isUnique()))) || (
                                        !isUpdateMode() && (
                                                        !cd.isLocalized() || (cd.isLocalized() && cd.getLanguageModifier() != null && cd
                                                                        .getLanguageModifier().equalsIgnoreCase(sameCD.getLanguageModifier()))))))
                        {
                            if(clashing == null)
                            {
                                caseInsensitiveMap = new CaseInsensitiveMap();
                            }
                            List<StandardColumnDescriptor> columnsList = (List)caseInsensitiveMap.get(q);
                            if(columnsList == null)
                            {
                                columnsList = new ArrayList(Collections.singleton(cd));
                                caseInsensitiveMap.put(q, columnsList);
                            }
                            columnsList.add(sameCD);
                        }
                    }
                }
            }
        }
        if(ImpExUtils.isStrictMode(this.reader.getValidationMode()) && caseInsensitiveMap != null)
        {
            throw new HeaderValidationException(this, "ambiguous columns " + caseInsensitiveMap + "'", 3);
        }
        if(unknown != null)
        {
            throw new HeaderValidationException(this, "unknown column attributes " + unknown + "'", 4);
        }
        if(!this.reader.getValidationMode().equals(ImpExManager.getExportOnlyMode()) && (isInsertUpdateMode() || isUpdateMode()) && !atLeastOneUniqueModifierFound &&
                        !getConfiguredComposedType().isSingleton())
        {
            throw new HeaderValidationException(this, "missing unique modifier inside '" +
                            dump() + "' - at least one attribute has to be declared as unique (attributename[unique=true])", 11);
        }
        if(isBatchMode() && !isUpdateMode() && !isRemoveMode())
        {
            throw new HeaderValidationException(this, "illegal use of modifier in '" +
                            dump() + "' - batch mode is only valid for UPDATE or REMOVE", 0);
        }
        if(this.permittedTypes == null || this.permittedTypes.isEmpty())
        {
            throw new HeaderValidationException(this, "no permitted type found for configured columns. omitted types: " + this.omittedTypesMessages + ", definition: " +
                            dump(), 8);
        }
        if(getDefaultComposedType() == null)
        {
            throw new HeaderValidationException(this, "no permitted type found, configured type is " + (
                            getConfiguredComposedType().isAbstract() ? " abstract" : "") + (
                            getConfiguredComposedType().isJaloOnly() ? " jaloonly" : "") + ".", 8);
        }
        if(isInsertMode() && !isPermittedTypeForInsert(getDefaultComposedType()))
        {
            throw new HeaderValidationException(this, "no permitted type found for configured columns. omitted types: " + this.omittedTypesMessages + ", definition: " +
                            dump(), 8);
        }
        if(!getConfiguredComposedType().equals(getDefaultComposedType()) &&
                        !this.reader.getValidationMode().equals(ImpExManager.getExportOnlyMode()))
        {
            String reason = getOmittedTypesMessages(getConfiguredComposedType().getPK());
            log.warn("Configured type " + getConfiguredComposedType().getCode() + " is not permitted, will use permitted subtype " +
                            getDefaultComposedType().getCode() + " as default" + ((reason == null) ? "" : (", because " + reason)) + ".");
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<StandardColumnDescriptor> getColumnsForUpdate(ComposedType targetType)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetType.getCode());
        return (tm != null) ? tm.getColumnsForUpdate() : Collections.EMPTY_SET;
    }


    public Set<StandardColumnDescriptor> getColumnsForUpdate(String targetTypeCode)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetTypeCode);
        return (tm != null) ? tm.getColumnsForUpdate() : Collections.EMPTY_SET;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<StandardColumnDescriptor> getColumnsForCreation(ComposedType targetType)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetType.getCode());
        return (tm != null) ? tm.getColumnsForCreation() : Collections.EMPTY_SET;
    }


    public Set<StandardColumnDescriptor> getColumnsForCreation(String targetTypeCode)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetTypeCode);
        return (tm != null) ? tm.getColumnsForCreation() : Collections.EMPTY_SET;
    }


    public Set<StandardColumnDescriptor> getPartOfColumns(ComposedType targetType)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetType.getCode());
        return (tm != null) ? tm.getPartOfColumns() : Collections.EMPTY_SET;
    }


    public Set<StandardColumnDescriptor> getPartOfColumns(String targetTypeCode)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetTypeCode);
        return (tm != null) ? tm.getPartOfColumns() : Collections.EMPTY_SET;
    }


    public Set<StandardColumnDescriptor> getAllColumns(ComposedType targetType)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetType.getCode());
        return (tm != null) ? tm.getAllColumns() : Collections.EMPTY_SET;
    }


    public Set<StandardColumnDescriptor> getUniqueAttributeColumns(ComposedType targetType)
    {
        return getUniqueAttributeColumns(targetType.getCode());
    }


    public Set<StandardColumnDescriptor> getUniqueAttributeColumns(String targetTypeCode)
    {
        PermittedTypeMapping tm = this.permittedTypes.get(targetTypeCode);
        return (tm != null) ? tm.getUniqueColumns() : Collections.EMPTY_SET;
    }


    protected Set<StandardColumnDescriptor> calculateUniqueAttributeColumns(ComposedType targetType)
    {
        Set<StandardColumnDescriptor> ret = null;
        for(int i = 0; i < this.columns.size(); i++)
        {
            AbstractColumnDescriptor cd = this.columns.get(i);
            if(cd instanceof StandardColumnDescriptor && ((StandardColumnDescriptor)cd).isUnique() && ((StandardColumnDescriptor)cd)
                            .getAttributeDescriptor() != null && ((StandardColumnDescriptor)cd)
                            .getAttributeDescriptor().getEnclosingType().isAssignableFrom((Type)targetType))
            {
                if(ret == null)
                {
                    ret = new HashSet<>();
                }
                ret.add((StandardColumnDescriptor)cd);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<StandardColumnDescriptor> calculateUniqueAttributeColumns()
    {
        return calculateUniqueAttributeColumns(getDefaultComposedType());
    }


    public List<String> getAllColumnQualifiers()
    {
        if(this.columns == null)
        {
            return new ArrayList<>();
        }
        List<AbstractColumnDescriptor> temp = new ArrayList<>(this.columns);
        List<String> ret = new ArrayList<>(this.columns.size());
        for(int i = 0; i < temp.size(); i++)
        {
            AbstractColumnDescriptor cd = temp.get(i);
            if(cd != null)
            {
                ret.add(cd.getQualifier());
            }
        }
        return ret;
    }


    protected Collection<StandardColumnDescriptor> getColumnsByAttribute(AttributeDescriptor ad)
    {
        String q = ad.getQualifier();
        List<StandardColumnDescriptor> ret = null;
        for(int i = 0; i < this.columns.size(); i++)
        {
            AbstractColumnDescriptor d = this.columns.get(i);
            if(d instanceof StandardColumnDescriptor && d.getQualifier().equalsIgnoreCase(q))
            {
                StandardColumnDescriptor cd = (StandardColumnDescriptor)d;
                ComposedType colEnclType = cd.getAttributeDescriptor().getEnclosingType();
                for(ComposedType enclType = ad.getEnclosingType(); enclType != null; enclType = enclType.getSuperType())
                {
                    if(colEnclType.equals(enclType))
                    {
                        if(ret == null)
                        {
                            ret = new ArrayList<>();
                        }
                        ret.add(cd);
                    }
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public Collection<AbstractColumnDescriptor> getColumnsByQualifier(String qualifier)
    {
        List<AbstractColumnDescriptor> ret = null;
        for(int i = 0; i < this.columns.size(); i++)
        {
            AbstractColumnDescriptor cd = this.columns.get(i);
            if(cd != null && qualifier.equalsIgnoreCase(cd.getQualifier()))
            {
                if(ret == null)
                {
                    ret = new ArrayList<>();
                }
                ret.add(cd);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public String getLocation()
    {
        return this.location;
    }


    public DocumentIDRegistry getDocumentIDRegistry()
    {
        return this.docIDRegistry;
    }


    public String toString()
    {
        return "HeaderDescriptor[" + getLocation() + ", " + ((AbstractDescriptor.HeaderParams)getDescriptorData()).getMode() + ", " + getTypeCode() + ", " +
                        getDescriptorData().getModifiers() + ", " + getAllColumnQualifiers() + " ]";
    }


    public String getOmittedTypesMessages(PK typePK)
    {
        if(this.omittedTypesMessages == null)
        {
            return null;
        }
        List<String> messages = this.omittedTypesMessages.get(typePK);
        if(messages == null)
        {
            return null;
        }
        if(messages.isEmpty())
        {
            return messages.get(0);
        }
        StringBuilder ret = new StringBuilder();
        for(String reason : messages)
        {
            ret.append(reason + ", ");
        }
        return ret.toString();
    }


    public Map<String, Object> getInterceptorRelatedParameters()
    {
        if(this.interceptorRelatedParameters == null)
        {
            Set<InterceptorExecutionPolicy.InterceptorType> disabledInterceptorDisabledTypes = extractDisabledInterceptorsFromHeader();
            Set<String> disabledInterceptorBeans = extractDisabledBeansFromHeader();
            Set<String> typesWithDisabledUniqueAttributeValidator = extractTypesWithDisabledUniqueAttributeValidatorFromHeader();
            Map<String, Object> interceptorRelatedParameters = new HashMap<>();
            interceptorRelatedParameters.put("disable.interceptor.types", disabledInterceptorDisabledTypes);
            interceptorRelatedParameters.put("disable.interceptor.beans", disabledInterceptorBeans);
            interceptorRelatedParameters.put("disable.UniqueAttributesValidator.for.types", typesWithDisabledUniqueAttributeValidator);
            this.interceptorRelatedParameters = interceptorRelatedParameters;
        }
        return this.interceptorRelatedParameters;
    }


    private static final Splitter COMMA_SEPARATED_VALUES_SPLITTER = Splitter.on(',').trimResults();


    private <T> Set<T> extractSetFromHeader(String key, Function<String, T> valueTransformer)
    {
        AbstractDescriptor.DescriptorParams descriptorData = getDescriptorData();
        String commaSeparatedValues = descriptorData.getModifier(key);
        if(Strings.isNullOrEmpty(commaSeparatedValues))
        {
            return Collections.emptySet();
        }
        Iterable<String> splitedValues = COMMA_SEPARATED_VALUES_SPLITTER.split(commaSeparatedValues);
        return (Set<T>)StreamSupport.<String>stream(splitedValues.spliterator(), false).<T>map(valueTransformer).collect(Collectors.toSet());
    }


    private Set<InterceptorExecutionPolicy.InterceptorType> extractDisabledInterceptorsFromHeader()
    {
        return extractSetFromHeader("disable.interceptor.types", s -> InterceptorExecutionPolicy.InterceptorType.fromString(s));
    }


    private Set<String> extractDisabledBeansFromHeader()
    {
        return extractSetFromHeader("disable.interceptor.beans", s -> s);
    }


    private Set<String> extractTypesWithDisabledUniqueAttributeValidatorFromHeader()
    {
        return extractSetFromHeader("disable.UniqueAttributesValidator.for.types", s -> s);
    }
}
