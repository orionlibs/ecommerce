package de.hybris.platform.impex.jalo.translators;

import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.StandardSearchResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ItemExpressionTranslator extends SingleValueTranslator
{
    private static final Logger LOG = Logger.getLogger(ItemExpressionTranslator.class);
    private int tableAliasCounter = 0;
    private final List<List<ExportPatternElement>> exportQualifierPaths;
    private final Map<Integer, List<ExportPatternEndElement>> exportLeafExpressions;
    private int leafElementCount = 0;
    private boolean islinkSourceOrTarget = false;
    private final ExpressionQuery _query;
    private char customDelimiter = ':';
    private Set<String> missingAttributes;
    private final String documentIDQualifier;
    private DocumentIDRegistry documentIdRegistry = null;


    public ItemExpressionTranslator(ComposedType targetType, List<AbstractDescriptor.ColumnParams> itemPatternList) throws HeaderValidationException
    {
        this.exportQualifierPaths = new ArrayList<>();
        this.exportLeafExpressions = new HashMap<>();
        if(containsDocumentID(itemPatternList))
        {
            if(itemPatternList.size() != 1)
            {
                throw new HeaderValidationException("More than one column qualifier in item expression while using document id label", 0);
            }
            this.documentIDQualifier = ((AbstractDescriptor.ColumnParams)itemPatternList.toArray()[0]).getQualifier().substring(1);
            this._query = new ExpressionQuery(targetType, null, null, null);
        }
        else
        {
            this.documentIDQualifier = null;
            this._query = buildQuery(targetType, itemPatternList);
        }
    }


    private boolean containsDocumentID(List<AbstractDescriptor.ColumnParams> itemPatternList)
    {
        if(itemPatternList == null || itemPatternList.isEmpty())
        {
            return false;
        }
        for(AbstractDescriptor.ColumnParams patternElement : itemPatternList)
        {
            if(patternElement.getQualifier().startsWith("&"))
            {
                return true;
            }
        }
        return false;
    }


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        if(columnDescriptor != null)
        {
            HeaderDescriptor header = columnDescriptor.getHeader();
            String qualifier = columnDescriptor.getQualifier();
            this
                            .islinkSourceOrTarget = (("target".equalsIgnoreCase(qualifier) || "source".equalsIgnoreCase(qualifier)) && Link.class.isAssignableFrom(header.getConfiguredComposedType().getJaloClass()));
            if(columnDescriptor.getDescriptorData().getModifier("path-delimiter") != null)
            {
                String delimiter = columnDescriptor.getDescriptorData().getModifier("path-delimiter");
                if(delimiter != null && delimiter.length() > 0)
                {
                    this.customDelimiter = delimiter.charAt(0);
                }
            }
            this.documentIdRegistry = columnDescriptor.getHeader().getDocumentIDRegistry();
            for(List<ExportPatternEndElement> endList : this.exportLeafExpressions.values())
            {
                for(ExportPatternEndElement endElement : endList)
                {
                    if(endElement.getValueTranslator() instanceof DocumentIDTranslator)
                    {
                        ((DocumentIDTranslator)endElement.getValueTranslator()).setRegistry(this.documentIdRegistry);
                    }
                }
            }
        }
        for(Map.Entry<Integer, List<ExportPatternEndElement>> e : this.exportLeafExpressions.entrySet())
        {
            for(ExportPatternEndElement endElement : e.getValue())
            {
                AtomicValueTranslator avt = endElement.getValueTranslator();
                if(columnDescriptor != null)
                {
                    avt.setLocale(columnDescriptor.getHeader().getReader().getLocale());
                }
                avt.setNumberFormat(endElement.getModifier("numberformat"));
                avt.setDateFormat(endElement.getModifier("dateformat"));
            }
        }
        super.init(columnDescriptor);
    }


    protected ExpressionQuery getQuery()
    {
        return this._query;
    }


    protected ComposedType getTargetType()
    {
        return getQuery().getTargetType();
    }


    protected String createNewTableAlias()
    {
        String alias = "t" + this.tableAliasCounter;
        this.tableAliasCounter++;
        return alias;
    }


    protected ExpressionQuery buildQuery(ComposedType targetType, List<AbstractDescriptor.ColumnParams> itemPatternList) throws HeaderValidationException
    {
        StringBuilder stringBuilder = new StringBuilder();
        Set<ComposedType> returnTypes = new HashSet<>();
        String alias = createNewTableAlias();
        if(itemPatternList != null)
        {
            for(Iterator<AbstractDescriptor.ColumnParams> iter = itemPatternList.iterator(); iter.hasNext(); )
            {
                AbstractDescriptor.ColumnParams patternElement = iter.next();
                ComposedType type = createSubTranslator(stringBuilder, targetType, patternElement, Collections.EMPTY_LIST, alias, false);
                if(type != null)
                {
                    returnTypes.add(type);
                }
                if(iter.hasNext())
                {
                    stringBuilder.append(" AND ");
                }
            }
        }
        ComposedType mostSpecific = getMostSpecific(targetType, returnTypes);
        stringBuilder.insert(0, " FROM {" + mostSpecific.getCode() + " AS " + alias + "} WHERE ");
        return new ExpressionQuery(mostSpecific, "SELECT {" + alias + "." + Item.PK + "}", stringBuilder.toString(), alias);
    }


    protected ComposedType getMostSpecific(ComposedType baseType, Set<ComposedType> types) throws HeaderValidationException
    {
        ComposedType mostSpecific = baseType;
        for(ComposedType ct : types)
        {
            if(ct.equals(mostSpecific))
            {
                continue;
            }
            if(mostSpecific.isAssignableFrom((Type)ct))
            {
                mostSpecific = ct;
                continue;
            }
            if(!ct.isAssignableFrom((Type)mostSpecific))
            {
                throw new HeaderValidationException("incompatible reference types " + mostSpecific.getCode() + " vs " + ct, 7);
            }
        }
        return mostSpecific;
    }


    protected AttributeDescriptor resolveAttribute(ComposedType type, AbstractDescriptor.ColumnParams patternElement) throws HeaderValidationException
    {
        int delimiter = patternElement.getQualifier().indexOf('.');
        if(delimiter != -1)
        {
            ComposedType explicitType = null;
            try
            {
                explicitType = TypeManager.getInstance().getComposedType(patternElement.getQualifier().substring(0, delimiter).trim());
            }
            catch(JaloItemNotFoundException e)
            {
                throw new HeaderValidationException("invalid reference attribute expression '" + patternElement.getQualifier() + "' - " + e
                                .getMessage(), 7);
            }
            if(!type.isAssignableFrom((Type)explicitType))
            {
                throw new HeaderValidationException("invalid reference attribute type " + explicitType.getCode() + " - no assignable from actual attribute type " + type
                                .getCode(), 7);
            }
            try
            {
                return explicitType.getAttributeDescriptorIncludingPrivate(patternElement.getQualifier().substring(delimiter + 1)
                                .trim());
            }
            catch(JaloItemNotFoundException e)
            {
                if(this.missingAttributes == null)
                {
                    this.missingAttributes = new HashSet<>();
                }
                this.missingAttributes.add(patternElement.getQualifier());
                return null;
            }
        }
        try
        {
            return type.getAttributeDescriptorIncludingPrivate(patternElement.getQualifier());
        }
        catch(JaloItemNotFoundException e)
        {
            if(this.missingAttributes == null)
            {
                this.missingAttributes = new HashSet<>();
            }
            this.missingAttributes.add(type.getCode() + "." + type.getCode());
            return null;
        }
    }


    protected ExportPatternEndElement addValuePath(List<AttributeDescriptor> path, AtomicValueTranslator trans, Map modifiers, boolean isAlternative)
    {
        String presetValue = (String)modifiers.get("default");
        if(!isAlternative)
        {
            int j = this.exportQualifierPaths.size();
            List<ExportPatternElement> exportPath = new ArrayList(path);
            for(int k = 0, m = exportPath.size(); k < m; k++)
            {
                exportPath.set(k, (k + 1 < m) ? (ExportPatternElement)new ExportPatternPathElement(path.get(k)) :
                                (ExportPatternElement)new ExportPatternEndElement(this, j, path.get(k), trans, presetValue, modifiers));
            }
            this.exportQualifierPaths.add(exportPath);
            return (ExportPatternEndElement)exportPath.get(exportPath.size() - 1);
        }
        if(this.exportQualifierPaths.isEmpty())
        {
            throw new IllegalStateException("cannot add alternative path without having at least one real path yet");
        }
        int offset = 0;
        int valuePos = this.exportQualifierPaths.size() - 1;
        List<ExportPatternElement> currentPath = this.exportQualifierPaths.get(this.exportQualifierPaths.size() - 1);
        for(int i = 0, s = path.size(); i < s; i++)
        {
            int pos = i - offset;
            AttributeDescriptor attributeDescriptor = path.get(i);
            Object currentElement = (pos < currentPath.size()) ? currentPath.get(pos) : null;
            if(currentElement != null)
            {
                if(currentElement instanceof ExportPatternElement)
                {
                    if(!attributeDescriptor.equals(((ExportPatternElement)currentElement).getAttribute()))
                    {
                        List[] arrayOfList = new List[2];
                        arrayOfList[0] = cutOff(currentPath, pos);
                        arrayOfList[1] = new ArrayList();
                        arrayOfList[1].add((i + 1 < s) ? new ExportPatternPathElement(attributeDescriptor) :
                                        new ExportPatternEndElement(this, valuePos, attributeDescriptor, trans, presetValue, modifiers));
                        currentPath.add(arrayOfList);
                        currentPath = arrayOfList[1];
                        offset = i;
                    }
                }
                else
                {
                    List[] junction = (List[])currentElement;
                    boolean foundPath = false;
                    for(int ii = 0; !foundPath && ii < junction.length; ii++)
                    {
                        ExportPatternElement alternativeAd = junction[ii].isEmpty() ? null : junction[ii].get(0);
                        if(attributeDescriptor == null || attributeDescriptor.equals(alternativeAd.getAttribute()))
                        {
                            foundPath = true;
                            currentPath = junction[ii];
                            if(attributeDescriptor == null)
                            {
                                junction[ii].add((i + 1 < s) ? new ExportPatternPathElement(null) :
                                                new ExportPatternEndElement(this, valuePos, null, trans, presetValue, modifiers));
                            }
                            offset = i;
                        }
                    }
                    if(!foundPath)
                    {
                        List[] arrayOfList = new List[junction.length + 1];
                        System.arraycopy(junction, 0, arrayOfList, 0, junction.length);
                        arrayOfList[junction.length] = new ArrayList();
                        arrayOfList[junction.length].add((i + 1 < s) ? new ExportPatternPathElement(attributeDescriptor) :
                                        new ExportPatternEndElement(this, valuePos, attributeDescriptor, trans, presetValue, modifiers));
                        currentPath.set(pos, arrayOfList);
                        currentPath = arrayOfList[junction.length];
                        offset = i;
                    }
                }
            }
            else
            {
                currentPath.add(attributeDescriptor);
            }
        }
        return (ExportPatternEndElement)currentPath.get(currentPath.size() - 1);
    }


    protected List<ExportPatternElement> cutOff(List<ExportPatternElement> original, int cutPosition)
    {
        List<ExportPatternElement> ret = new ArrayList<>(original.size() - cutPosition);
        int i, s;
        for(i = cutPosition, s = original.size(); i < s; i++)
        {
            ret.add(original.get(i));
        }
        for(i = original.size() - 1; i >= cutPosition; i--)
        {
            original.remove(i);
        }
        return ret;
    }


    protected ComposedType createSubTranslator(StringBuilder stringBuilder, ComposedType type, AbstractDescriptor.ColumnParams patternElement, List<AttributeDescriptor> qualifierPath, String enclosingTableAlias, boolean isAlternative) throws HeaderValidationException
    {
        AttributeDescriptor attributeDescriptor = resolveAttribute(type, patternElement);
        if(attributeDescriptor == null)
        {
            return null;
        }
        String qualifier = attributeDescriptor.getQualifier();
        List<AttributeDescriptor> extendedQualifierPath = new ArrayList<>(qualifierPath);
        extendedQualifierPath.add(attributeDescriptor);
        Type attrType = attributeDescriptor.isLocalized() ? ((MapType)attributeDescriptor.getRealAttributeType()).getReturnType() : attributeDescriptor.getAttributeType();
        String did = getDocumentIDFromItemPattern(patternElement);
        if(did != null)
        {
            createDocumentIDSubTranslator(attributeDescriptor, stringBuilder, type, patternElement, extendedQualifierPath, enclosingTableAlias, isAlternative, did);
        }
        else if(attributeDescriptor instanceof RelationDescriptor &&
                        !attributeDescriptor.isProperty() && attributeDescriptor.getDatabaseColumn() == null)
        {
            createRelationAttributeSubTranslator((RelationDescriptor)attributeDescriptor, stringBuilder,
                            ((RelationDescriptor)attributeDescriptor).isSource() ? (
                                            (RelationDescriptor)attributeDescriptor).getRelationType().getTargetType() : (
                                            (RelationDescriptor)attributeDescriptor).getRelationType().getSourceType(), patternElement, extendedQualifierPath, enclosingTableAlias, isAlternative);
        }
        else if(attrType instanceof AtomicType)
        {
            createAtomicAttributeSubTranslator(attributeDescriptor, stringBuilder, type, patternElement, extendedQualifierPath, enclosingTableAlias, isAlternative);
        }
        else if(attrType instanceof ComposedType)
        {
            createItemAttributeSubTranslator(qualifier, (ComposedType)attrType, stringBuilder, type, patternElement, extendedQualifierPath, enclosingTableAlias, isAlternative);
        }
        else
        {
            throw new HeaderValidationException("cannot reference type " + type.getCode() + " via attribute " + attributeDescriptor
                            .getEnclosingType()
                            .getCode() + "." + qualifier + " since attribute type " + attrType
                            .getCode() + " is not supported", 0);
        }
        return type.equals(attributeDescriptor.getEnclosingType()) ? null : attributeDescriptor.getEnclosingType();
    }


    protected String getDocumentIDFromItemPattern(AbstractDescriptor.ColumnParams pattern) throws HeaderValidationException
    {
        if(pattern.getItemPatternLists() == null)
        {
            return null;
        }
        String ret = null;
        for(int i = 0; i < (pattern.getItemPatternLists()).length; i++)
        {
            for(AbstractDescriptor.ColumnParams params : pattern.getItemPatternLists()[i])
            {
                if(params.getQualifier().startsWith("&"))
                {
                    if(i > 0)
                    {
                        throw new HeaderValidationException("No alternative patter allowed when using document IDs", 0);
                    }
                    if(ret != null)
                    {
                        throw new HeaderValidationException("No attribute allowed when using document ID within item expression", 0);
                    }
                    ret = params.getQualifier();
                }
            }
        }
        return ret;
    }


    protected void createRelationAttributeSubTranslator(RelationDescriptor relationDescriptor, StringBuilder stringBuilder, ComposedType relatedType, AbstractDescriptor.ColumnParams patternElement, List<AttributeDescriptor> qualifierPath, String enclosingTableAlias, boolean isAlternative)
                    throws HeaderValidationException
    {
        String relType = relationDescriptor.getRelationName();
        boolean isFromSourceType = relationDescriptor.isSource();
        String enclosingTypeEnd = isFromSourceType ? "source" : "target";
        String relatedTypeEnd = isFromSourceType ? "target" : "source";
        if(!patternElement.hasItemPattern())
        {
            if(!relationDescriptor.getRelationType().isAbstract())
            {
                String relAlias = createNewTableAlias();
                String relatedTypeAlias = createNewTableAlias();
                stringBuilder.append("EXISTS ({{");
                stringBuilder.append("SELECT {").append(relAlias).append(".").append(Item.PK).append("} ");
                stringBuilder.append("FROM {").append(relType).append(" AS ").append(relAlias).append(" ");
                stringBuilder.append("JOIN ").append(relatedType.getCode()).append(" AS ").append(relatedTypeAlias).append(" ");
                stringBuilder.append("ON {").append(relAlias).append(".").append(relatedTypeEnd).append("}={")
                                .append(relatedTypeAlias).append(".").append(Item.PK).append("} } ");
                stringBuilder.append("WHERE ");
                stringBuilder.append("{").append(relAlias).append(".").append(enclosingTypeEnd).append("}={")
                                .append(enclosingTableAlias).append(".").append(Item.PK).append("} AND ");
                createSubTranslator(stringBuilder, relatedType, new AbstractDescriptor.ColumnParams(Item.PK, null), qualifierPath, relatedTypeAlias, false);
                stringBuilder.append(" }} )");
            }
            else
            {
                String otherEndQualifier = relationDescriptor.isSource() ? relationDescriptor.getRelationType().getTargetTypeRole() : relationDescriptor.getRelationType().getSourceTypeRole();
                String otherTypeAlias = createNewTableAlias();
                stringBuilder.append("EXISTS ({{");
                stringBuilder.append("SELECT {").append(otherTypeAlias).append(".").append(Item.PK).append("} ");
                stringBuilder.append("FROM {").append(relatedType).append(" AS ").append(otherTypeAlias).append("} ");
                stringBuilder.append("WHERE ");
                stringBuilder.append("{").append(otherTypeAlias).append(".").append(otherEndQualifier).append("}={")
                                .append(enclosingTableAlias).append(".").append(Item.PK).append("} AND ");
                createSubTranslator(stringBuilder, relatedType, new AbstractDescriptor.ColumnParams(Item.PK, null), qualifierPath, otherTypeAlias, false);
                stringBuilder.append(" }} )");
            }
        }
        else
        {
            List[] arrayOfList = patternElement.getItemPatternLists();
            String relAlias = createNewTableAlias();
            String relatedTypeAlias = createNewTableAlias();
            for(int i = 0; i < arrayOfList.length; i++)
            {
                List<AbstractDescriptor.ColumnParams> pattern = arrayOfList[i];
                int pos = stringBuilder.length();
                Set<ComposedType> returnTypes = new HashSet<>();
                for(Iterator<AbstractDescriptor.ColumnParams> iter = pattern.iterator(); iter.hasNext(); )
                {
                    AbstractDescriptor.ColumnParams subPatternElement = iter.next();
                    ComposedType returnType = createSubTranslator(stringBuilder, relatedType, subPatternElement, qualifierPath, relatedTypeAlias, (i > 0));
                    if(returnType != null)
                    {
                        returnTypes.add(returnType);
                    }
                    if(iter.hasNext())
                    {
                        stringBuilder.append(" AND ");
                    }
                }
                stringBuilder.append(" }}) ");
                if(i + 1 < arrayOfList.length)
                {
                    stringBuilder.append(" OR ");
                }
                ComposedType mostSpecific = getMostSpecific(relatedType, returnTypes);
                StringBuilder toInsert = new StringBuilder();
                if(!relationDescriptor.getRelationType().isAbstract())
                {
                    toInsert.append("EXISTS ({{");
                    toInsert.append("SELECT {").append(relAlias).append(".").append(Item.PK).append("} ");
                    toInsert.append("FROM {").append(relType).append(" AS ").append(relAlias).append(" ");
                    toInsert.append("JOIN ").append(mostSpecific.getCode()).append(" AS ").append(relatedTypeAlias).append(" ");
                    toInsert.append("ON {")
                                    .append(relAlias)
                                    .append(".")
                                    .append(relatedTypeEnd)
                                    .append("}={")
                                    .append(relatedTypeAlias)
                                    .append(".")
                                    .append(Item.PK)
                                    .append("} } ");
                    toInsert.append("WHERE ");
                    toInsert.append("{").append(relAlias).append(".").append(enclosingTypeEnd).append("}={")
                                    .append(enclosingTableAlias).append(".").append(Item.PK).append("} AND ");
                }
                else
                {
                    String otherEndQualifier = relationDescriptor.isSource() ? relationDescriptor.getRelationType().getTargetTypeRole() : relationDescriptor.getRelationType().getSourceTypeRole();
                    toInsert.append("EXISTS ({{");
                    toInsert.append("SELECT {").append(relatedTypeAlias).append(".").append(Item.PK).append("} ");
                    toInsert.append("FROM {").append(mostSpecific.getCode()).append(" AS ").append(relatedTypeAlias).append("} ");
                    toInsert.append("WHERE ");
                    toInsert.append("{").append(relatedTypeAlias).append(".").append(otherEndQualifier).append("}={")
                                    .append(enclosingTableAlias).append(".").append(Item.PK).append("} AND ");
                }
                stringBuilder.insert(pos, toInsert.toString());
            }
        }
    }


    protected void createItemAttributeSubTranslator(String qualifier, ComposedType attrType, StringBuilder stringBuilder, ComposedType type, AbstractDescriptor.ColumnParams patternElement, List<AttributeDescriptor> qualifierPath, String enclosingTableAlias, boolean isAlternative)
                    throws HeaderValidationException
    {
        if(!patternElement.hasItemPattern())
        {
            stringBuilder.append("{").append(enclosingTableAlias).append(".").append(qualifier).append("} IN ({{ ");
            String newAlias = createNewTableAlias();
            stringBuilder.append("SELECT {").append(newAlias).append(".").append(Item.PK).append("} ");
            stringBuilder.append("FROM {").append(attrType.getCode()).append(" AS ").append(newAlias).append("} ");
            stringBuilder.append("WHERE ");
            createSubTranslator(stringBuilder, attrType, new AbstractDescriptor.ColumnParams(Item.PK, null), qualifierPath, newAlias, false);
            stringBuilder.append("}}) ");
        }
        else
        {
            stringBuilder.append("{").append(enclosingTableAlias).append(".").append(qualifier).append("} IN (");
            List[] patterns = patternElement.getItemPatternLists();
            for(int i = 0; i < patterns.length; i++)
            {
                List<AbstractDescriptor.ColumnParams> pattern = patterns[i];
                String newAlias = createNewTableAlias();
                int pos = stringBuilder.length();
                Set<ComposedType> returnTypes = new HashSet<>();
                for(Iterator<AbstractDescriptor.ColumnParams> iter = pattern.iterator(); iter.hasNext(); )
                {
                    AbstractDescriptor.ColumnParams subPatternElement = iter.next();
                    ComposedType returnType = createSubTranslator(stringBuilder, attrType, subPatternElement, qualifierPath, newAlias, (i > 0));
                    if(returnType != null)
                    {
                        returnTypes.add(returnType);
                    }
                    if(iter.hasNext())
                    {
                        stringBuilder.append(" AND ");
                    }
                }
                stringBuilder.append(" }}");
                if(i + 1 < patterns.length)
                {
                    stringBuilder.append(",");
                }
                ComposedType mostSpecific = getMostSpecific(attrType, returnTypes);
                stringBuilder.insert(pos, "{{ SELECT {" + newAlias + "." + Item.PK + "} FROM {" + mostSpecific.getCode() + " AS " + newAlias + "} WHERE ");
            }
            stringBuilder.append(")");
        }
    }


    protected void createAtomicAttributeSubTranslator(AttributeDescriptor attributeDescriptor, StringBuilder stringBuilder, ComposedType type, AbstractDescriptor.ColumnParams patternElement, List<AttributeDescriptor> qualifierPath, String enclosingTableAlias, boolean isAlternative)
                    throws HeaderValidationException
    {
        String qualifier = attributeDescriptor.getQualifier();
        stringBuilder.append("{").append(enclosingTableAlias).append(".").append(qualifier);
        Type attrType = attributeDescriptor.isLocalized() ? ((MapType)attributeDescriptor.getRealAttributeType()).getReturnType() : attributeDescriptor.getAttributeType();
        if(attributeDescriptor.isLocalized())
        {
            String langStr = patternElement.getModifier("lang");
            if(langStr == null || langStr.length() == 0)
            {
                throw new HeaderValidationException("cannot reference type " + type.getCode() + " via localized attribute " + qualifier + " without specifying a language", 0);
            }
            try
            {
                C2LManager.getInstance().getLanguageByIsoCode(langStr);
            }
            catch(JaloItemNotFoundException e)
            {
                throw new HeaderValidationException("cannot reference type " + type.getCode() + " via localized attribute " + qualifier + " using non-existent language '" + langStr + "'", 0);
            }
            stringBuilder.append("[").append(langStr).append("]");
        }
        ExportPatternEndElement endExpr = addValuePath(qualifierPath, new AtomicValueTranslator(attributeDescriptor, ((AtomicType)attrType)
                        .getJavaClass()), patternElement
                        .getModifiers(), isAlternative);
        stringBuilder.append("}=?").append(endExpr.getValueID()).append(" ");
    }


    protected void createDocumentIDSubTranslator(AttributeDescriptor attributeDescriptor, StringBuilder stringBuilder, ComposedType type, AbstractDescriptor.ColumnParams patternElement, List<AttributeDescriptor> qualifierPath, String enclosingTableAlias, boolean isAlternative, String documentID)
                    throws HeaderValidationException
    {
        String qualifier = attributeDescriptor.getQualifier();
        stringBuilder.append("{").append(enclosingTableAlias).append(".").append(qualifier);
        ExportPatternEndElement endExpr = addValuePath(qualifierPath, (AtomicValueTranslator)new DocumentIDTranslator(attributeDescriptor, documentID), patternElement
                        .getModifiers(), isAlternative);
        stringBuilder.append("}=?").append(endExpr.getValueID()).append(" ");
    }


    public void validate(StandardColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.validate(columnDescriptor);
        HeaderDescriptor header = columnDescriptor.getHeader();
        if(this.missingAttributes != null && !this.missingAttributes.isEmpty())
        {
            throw new HeaderValidationException(header, "unknown attributes " + this.missingAttributes + " - cannot resolve item reference", 4);
        }
        for(Map.Entry<Integer, List<ExportPatternEndElement>> e : this.exportLeafExpressions.entrySet())
        {
            for(ExportPatternEndElement endElement : e.getValue())
            {
                AtomicValueTranslator avt = endElement.getValueTranslator();
                checkResolvableAttribute(header, avt.getAttributeDescriptor());
            }
        }
        if(this.documentIDQualifier != null && this.documentIdRegistry == null)
        {
            throw new HeaderValidationException("A document id attribute is used but no registry is given", 5);
        }
    }


    protected void checkResolvableAttribute(HeaderDescriptor header, AttributeDescriptor attributeDescriptor) throws HeaderValidationException
    {
        if(ImpExUtils.isStrictMode(header.getReader().getValidationMode()) &&
                        !attributeDescriptor.isSearchable() && attributeDescriptor.getDatabaseColumn() == null)
        {
            throw new HeaderValidationException(header, "attribute " + attributeDescriptor.getEnclosingType().getCode() + "." + attributeDescriptor
                            .getQualifier() + " is not searchable - cannot use to resolve item reference via pattern", 7);
        }
        if(!(attributeDescriptor instanceof RelationDescriptor))
        {
            Type attrType = attributeDescriptor.isLocalized() ? ((MapType)attributeDescriptor.getRealAttributeType()).getReturnType() : attributeDescriptor.getAttributeType();
            if(!(attrType instanceof AtomicType) && !(attrType instanceof ComposedType))
            {
                throw new HeaderValidationException(header, "attribute " + attributeDescriptor.getEnclosingType().getCode() + "." + attributeDescriptor
                                .getQualifier() + " must be either atomic or composed type - cannot use to resolve item reference", 5);
            }
            if(ImpExUtils.isStrictMode(header.getReader().getValidationMode()) && attrType instanceof ComposedType && ((ComposedType)attrType)
                            .isJaloOnly())
            {
                throw new HeaderValidationException(header, "attribute " + attributeDescriptor.getEnclosingType().getCode() + "." + attributeDescriptor
                                .getQualifier() + " is jalo-only - cannot use to resolve item reference", 7);
            }
        }
    }


    protected Map<String, Object> getFullDefaultValueMap()
    {
        Map<String, Object> ret = null;
        for(int i = 0; i < this.exportLeafExpressions.size(); i++)
        {
            List<ExportPatternEndElement> endExpressions = this.exportLeafExpressions.get(Integer.valueOf(i));
            if(endExpressions.size() > 1)
            {
                return null;
            }
            ExportPatternEndElement end = endExpressions.get(0);
            if(end.getPreset() == null)
            {
                return null;
            }
            if(ret == null)
            {
                ret = new HashMap<>();
            }
            ret.put(end.getValueID(), end.getValueTranslator().importValue(end.getPreset(), null));
        }
        return ret;
    }


    protected Map<String, Object> mergeDefaultValues(List<String> parsedValues)
    {
        int required = this.exportQualifierPaths.size();
        int parsed = parsedValues.size();
        if(parsed <= required && (parsed != 1 || required <= 1))
        {
            Map<String, Object> ret = new HashMap<>();
            for(int i = 0; i < required; i++)
            {
                String parsedValue = (i < parsedValues.size()) ? parsedValues.get(i) : null;
                for(ExportPatternEndElement end : this.exportLeafExpressions.get(Integer.valueOf(i)))
                {
                    if(parsedValue != null)
                    {
                        if("<null>".equalsIgnoreCase(parsedValue))
                        {
                            ret.put(end.getValueID(), null);
                            continue;
                        }
                        ret.put(end.getValueID(), end.getValueTranslator().importValue(parsedValue, null));
                        continue;
                    }
                    if(end.getPreset() != null)
                    {
                        if("<null>".equalsIgnoreCase(end.getPreset()))
                        {
                            ret.put(end.getValueID(), null);
                            continue;
                        }
                        ret.put(end.getValueID(), end.getValueTranslator().importValue(end.getPreset(), null));
                        continue;
                    }
                    throw new JaloInvalidParameterException("missing item reference value at " + i + " for attribute " + end
                                    .getAttribute()
                                    .getEnclosingType()
                                    .getCode() + "." + end.getAttribute().getQualifier(), 0);
                }
            }
            return ret;
        }
        if(parsed == 1)
        {
            Map<String, Object> ret = new HashMap<>();
            String singleValue = parsedValues.get(0);
            parsedValues.clear();
            int nullPos = -1;
            for(int i = 0; i < required; i++)
            {
                for(ExportPatternEndElement end : this.exportLeafExpressions.get(Integer.valueOf(i)))
                {
                    if(end.getPreset() == null)
                    {
                        if(nullPos != -1)
                        {
                            if(nullPos == i)
                            {
                                if("<null>".equalsIgnoreCase(singleValue))
                                {
                                    ret.put(end.getValueID(), null);
                                    continue;
                                }
                                ret.put(end.getValueID(), end.getValueTranslator().importValue(singleValue, null));
                                continue;
                            }
                            throw new JaloInvalidParameterException("item reference " + singleValue + " for attribute " + end
                                            .getAttribute()
                                            .getEnclosingType()
                                            .getCode() + "." + end.getAttribute()
                                            .getQualifier() + " does not provide enough values at position " + i, 0);
                        }
                        if("<null>".equalsIgnoreCase(singleValue))
                        {
                            ret.put(end.getValueID(), null);
                        }
                        else
                        {
                            ret.put(end.getValueID(), end.getValueTranslator().importValue(singleValue, null));
                        }
                        nullPos = i;
                        continue;
                    }
                    if("<null>".equalsIgnoreCase(end.getPreset()))
                    {
                        ret.put(end.getValueID(), null);
                        continue;
                    }
                    ret.put(end.getValueID(), end.getValueTranslator().importValue(end.getPreset(), null));
                }
            }
            if(nullPos == -1)
            {
                for(ExportPatternEndElement end : this.exportLeafExpressions.get(Integer.valueOf(0)))
                {
                    ret.put(end.getValueID(), end.getValueTranslator().importValue(singleValue, null));
                }
            }
            return ret;
        }
        throw new JaloInvalidParameterException("incompatible item reference values " + parsedValues + " ( need " + required + " values, got " + parsed + " ) within " + this, 0);
    }


    protected String convertToString(Object value)
    {
        if(!(value instanceof Item) || !getTargetType().isInstance(value))
        {
            throw new JaloInvalidParameterException("illegal value type " + value.getClass().getName() + " of value " + value + " for item reference type " +
                            getTargetType().getCode(), 0);
        }
        if(this.documentIDQualifier != null)
        {
            return this.documentIdRegistry.lookupPK(this.documentIDQualifier, ((Item)value).getPK().getLongValue());
        }
        return toString((Item)value);
    }


    protected String toString(Item item)
    {
        List<String> pathStrings = new ArrayList();
        for(Iterator<List<ExportPatternElement>> it = this.exportQualifierPaths.iterator(); it.hasNext(); )
        {
            Item current = item;
            for(ExportPathSelector sel = new ExportPathSelector(it.next()); sel.hasNext(); )
            {
                ExportPatternElement patternElement = sel.selectNext(current);
                ComposedType type = patternElement.getAttribute().getEnclosingType();
                String qualifier = patternElement.getAttribute().getQualifier();
                if(current != null && !type.isInstance(current))
                {
                    throw new JaloInvalidParameterException("item " + current + " (type=" + current.getComposedType().getCode() + " is not compatible with reference pattern attribute " + type
                                    .getCode() + "." + qualifier, 0);
                }
                if(patternElement instanceof ExportPatternEndElement)
                {
                    try
                    {
                        if(current != null)
                        {
                            Object value;
                            if(isLocalizedAndLanguageModifierIsSet((ExportPatternEndElement)patternElement))
                            {
                                SessionContext locCtx = JaloSession.getCurrentSession().createLocalSessionContext();
                                try
                                {
                                    locCtx.setLanguage(getLanguageByIsoCode((ExportPatternEndElement)patternElement));
                                    value = current.getAttribute(locCtx, qualifier);
                                }
                                finally
                                {
                                    JaloSession.getCurrentSession().removeLocalSessionContext();
                                }
                            }
                            else
                            {
                                value = current.getAttribute(qualifier);
                            }
                            pathStrings.add(((ExportPatternEndElement)patternElement).getValueTranslator().exportValue(value));
                            continue;
                        }
                        pathStrings.add(null);
                    }
                    catch(JaloSecurityException e)
                    {
                        Object value;
                        throw new JaloInvalidParameterException("cannot read attribute " + type
                                        .getCode() + "." + qualifier + " from " + current + " ( path = " + sel
                                        .getRecordedPath() + " ) due to " + value.getMessage(), 0);
                    }
                    continue;
                }
                try
                {
                    current = (current != null) ? (Item)current.getAttribute(qualifier) : null;
                }
                catch(JaloSecurityException e)
                {
                    throw new JaloInvalidParameterException("cannot read attribute " + type
                                    .getCode() + "." + qualifier + " from " + current + " ( path = " + sel
                                    .getRecordedPath() + " ) due to " + e.getMessage(), 0);
                }
            }
        }
        return CSVUtils.joinAndEscape(pathStrings, null, this.customDelimiter, false);
    }


    protected Language getLanguageByIsoCode(ExportPatternEndElement patternElement)
    {
        return C2LManager.getInstance().getLanguageByIsoCode(patternElement
                        .getModifier("lang"));
    }


    protected boolean isLocalizedAndLanguageModifierIsSet(ExportPatternEndElement patternElement)
    {
        return (patternElement.getModifier("lang") != null && patternElement
                        .getAttribute().isLocalized());
    }


    protected Object getEmptyValue()
    {
        Map<String, Object> searchValues = getFullDefaultValueMap();
        return (searchValues != null) ? searchItem(searchValues, "<empty>") : null;
    }


    protected Object searchItem(Map values, String valueExpr)
    {
        ExpressionQuery expressionQuery = getQuery();
        StandardSearchResult<Object> standardsearchResult = (StandardSearchResult<Object>)getFlexibleSearch().search(expressionQuery
                                        .getSelect() + expressionQuery.getSelect(), values,
                        this.islinkSourceOrTarget ? PK.class :
                                        expressionQuery.getTargetType().getJaloClass());
        List<Object> items = standardsearchResult.getResult();
        if(items.isEmpty())
        {
            setError();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("could not resolve '" + valueExpr + "' via query '" + expressionQuery + "' and values " + values);
            }
            return null;
        }
        if(items.size() > 1)
        {
            throw new JaloInvalidParameterException("more than one item found for '" + valueExpr + "' using query '" + expressionQuery + "' with values " + values + " - got " + items, 0);
        }
        return items.get(0);
    }


    protected static String inserIsNullIfNecessary(String queryBody, Map<String, Object> valuesIncludingNull)
    {
        String ret = queryBody;
        for(Map.Entry<String, Object> e : valuesIncludingNull.entrySet())
        {
            if(e.getValue() == null)
            {
                ret = ret.replaceAll("=\\?" + (String)e.getKey(), " IS NULL ");
            }
        }
        return ret;
    }


    protected static void mergeValues(Map<String, Object> merged, Map<String, Object> original, int tokenNumber)
    {
        if(original != null && !original.isEmpty())
        {
            for(Map.Entry<String, Object> e : original.entrySet())
            {
                merged.put("t" + tokenNumber + (String)e.getKey(), e.getValue());
            }
        }
    }


    public static List<CombinedSearchResult> convertAllToJalo(Item forItem, List<ItemExpressionTranslator> translators, List<String> expressions)
    {
        return convertAllToJalo(translators, expressions, ImpExUtils.getDefaultQueryChunkSize());
    }


    public static List<CombinedSearchResult> convertAllToJalo(List<ItemExpressionTranslator> translators, List<String> expressions, int queryChunkSize)
    {
        CombinedSearchResult[] tmp = new CombinedSearchResult[translators.size()];
        List<ItemExpressionTranslator> translators2query = null;
        List<Map<String, Object>> values2query = null;
        List<String> expressions2query = null;
        List<Integer> resultPosition = null;
        JaloSession jaloSession = null;
        int index = 0;
        for(ItemExpressionTranslator trans : translators)
        {
            String vExpr = expressions.get(index);
            boolean exprEmpty = trans.isEmpty(vExpr);
            if(!exprEmpty && trans.documentIDQualifier != null)
            {
                long pk = trans.documentIdRegistry.lookupID(trans.documentIDQualifier, vExpr);
                if(pk <= 0L)
                {
                    tmp[index] = new CombinedSearchResult(null, true);
                }
                else
                {
                    try
                    {
                        if(jaloSession == null)
                        {
                            jaloSession = JaloSession.getCurrentSession();
                        }
                        Item item = jaloSession.getItem(PK.fromLong(pk));
                        if(item == null)
                        {
                            tmp[index] = new CombinedSearchResult(null, true);
                        }
                        else
                        {
                            tmp[index] = new CombinedSearchResult(item, false);
                        }
                    }
                    catch(JaloItemNotFoundException ex)
                    {
                        LOG.warn("Error while resolving item from document ID registry. Value was " + vExpr + ", resolved pk was " + pk + ", error message was: " + ex
                                        .getMessage());
                        tmp[index] = new CombinedSearchResult(null, true);
                    }
                }
            }
            else
            {
                Map<String, Object> params = exprEmpty ? trans.getFullDefaultValueMap() : trans.mergeDefaultValues(
                                CSVUtils.splitAndUnescape(vExpr, new char[] {trans.customDelimiter}, false));
                if(exprEmpty && params == null)
                {
                    tmp[index] = new CombinedSearchResult(null, false, true);
                }
                else
                {
                    if(translators2query == null)
                    {
                        translators2query = new ArrayList<>(translators.size());
                        values2query = new ArrayList<>(translators.size());
                        expressions2query = new ArrayList<>(translators.size());
                        resultPosition = new ArrayList<>(translators.size());
                    }
                    translators2query.add(trans);
                    values2query.add(params);
                    expressions2query.add(vExpr);
                    resultPosition.add(Integer.valueOf(index));
                }
            }
            index++;
        }
        if(translators2query != null)
        {
            List<Object> queryRes = search(translators2query, values2query, expressions2query, queryChunkSize);
            int size = translators2query.size();
            for(int j = 0; j < size; j++)
            {
                int pos = ((Integer)resultPosition.get(j)).intValue();
                Object object = queryRes.get(j);
                tmp[pos] = new CombinedSearchResult(object, (object == null));
            }
        }
        return Arrays.asList(tmp);
    }


    protected static List<Object> search(List<ItemExpressionTranslator> translators2query, List<Map<String, Object>> values2query, List<String> expressions2query, int queryChunkSize)
    {
        if(queryChunkSize > 0)
        {
            return searchInChunks(translators2query, values2query, expressions2query, queryChunkSize);
        }
        return searchCombined(translators2query, values2query, expressions2query);
    }


    protected static List<Object> searchInChunks(List<ItemExpressionTranslator> translators2query, List<Map<String, Object>> values2query, List<String> expressions2query, int chunkSize)
    {
        List<List<ItemExpressionTranslator>> translators2queryChunks = Lists.partition(translators2query, chunkSize);
        List<List<Map<String, Object>>> values2queryChunks = Lists.partition(values2query, chunkSize);
        List<List<String>> expressions2queryChunks = Lists.partition(expressions2query, chunkSize);
        List<Object> results = new ArrayList();
        for(int i = 0; i < translators2queryChunks.size(); i++)
        {
            results.addAll(
                            searchCombined(translators2queryChunks.get(i), values2queryChunks.get(i), expressions2queryChunks.get(i)));
        }
        return results;
    }


    protected static List<Object> searchCombined(List<ItemExpressionTranslator> translators, List<Map<String, Object>> valueMaps, List<String> valueExpressions)
    {
        Map<String, Object> mergedValues = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        int size = translators.size();
        FlexibleSearch flexibleSearch = null;
        if(size == 1)
        {
            ItemExpressionTranslator trans = translators.get(0);
            flexibleSearch = trans.getFlexibleSearch();
            stringBuilder.append(trans.getQuery().getSelect()).append(" AS a, ").append("0").append(" AS b ");
            stringBuilder.append(trans.getQuery().getQueryBody().replaceAll("\\?v", "?t0v"));
            mergeValues(mergedValues, valueMaps.get(0), 0);
        }
        else
        {
            stringBuilder.append("SELECT x.a, x.b FROM (");
            for(int i = 0; i < size; i++)
            {
                ItemExpressionTranslator trans = translators.get(i);
                if(i > 0)
                {
                    stringBuilder.append(" UNION ALL ");
                }
                else
                {
                    flexibleSearch = trans.getFlexibleSearch();
                }
                stringBuilder.append("{{").append(trans.getQuery().getSelect()).append(" AS a, ").append(i).append(" AS b ");
                stringBuilder.append(trans.getQuery().getQueryBody().replaceAll("\\?v", "?t" + i + "v"));
                stringBuilder.append("}}");
                mergeValues(mergedValues, valueMaps.get(i), i);
            }
            stringBuilder.append(") x");
        }
        StandardSearchResult<List> standardSearchResult = (StandardSearchResult<List>)flexibleSearch.search(
                        inserIsNullIfNecessary(stringBuilder
                                        .toString(), mergedValues), mergedValues,
                        Arrays.asList((Class<?>[][])new Class[] {PK.class, Integer.class}, ), true, true, 0, -1);
        Object[] tmp = new Object[translators.size()];
        if(standardSearchResult.getCount() > 0)
        {
            List<PK> pksToFetch = null;
            List<Object[]> pksToFetchInfo = null;
            for(List<PK> row : (Iterable<List<PK>>)standardSearchResult.getResult())
            {
                PK pk = row.get(0);
                int position = ((Integer)row.get(1)).intValue();
                ItemExpressionTranslator trans = translators.get(position);
                if(trans.islinkSourceOrTarget)
                {
                    Object prev = tmp[position];
                    tmp[position] = pk;
                    if(prev != null)
                    {
                        throw new JaloInvalidParameterException("more than one item found for '" + (String)valueExpressions.get(position) + "' using query '" + trans
                                        .getQuery()
                                        .getQueryBody() + "' with values " + valueMaps
                                        .get(position) + " - got at least " + prev + " and " + pk, 0);
                    }
                    continue;
                }
                if(pksToFetch == null)
                {
                    pksToFetch = new ArrayList<>(translators.size());
                    pksToFetchInfo = new ArrayList(translators.size());
                }
                pksToFetch.add(pk);
                pksToFetchInfo.add(new Object[] {Integer.valueOf(position), trans});
            }
            if(pksToFetch != null)
            {
                Collection<? extends Item> fetchedItems = JaloSession.getCurrentSession().getItems(null, pksToFetch, true, true);
                int index = 0;
                for(Item item : fetchedItems)
                {
                    if(item != null)
                    {
                        int position = ((Integer)((Object[])pksToFetchInfo.get(index))[0]).intValue();
                        Object prev = tmp[position];
                        tmp[position] = item;
                        if(prev != null)
                        {
                            throw new JaloInvalidParameterException("more than one item found for '" + (String)valueExpressions
                                            .get(position) + "' using query '" + (
                                            (ItemExpressionTranslator)((Object[])pksToFetchInfo.get(index))[1]).getQuery().getQueryBody() + "' with values " + valueMaps
                                            .get(position) + " - got at least " + prev + " and " + item, 0);
                        }
                    }
                    index++;
                }
            }
        }
        return Arrays.asList(tmp);
    }


    protected Object convertToJalo(String valueExpr, Item forItem)
    {
        if(this.documentIDQualifier != null)
        {
            long pk = this.documentIdRegistry.lookupID(this.documentIDQualifier, valueExpr);
            if(pk <= 0L)
            {
                setError();
                return null;
            }
            try
            {
                Item item = JaloSession.getCurrentSession().getItem(PK.fromLong(pk));
                if(item == null)
                {
                    setError();
                    return null;
                }
                return item;
            }
            catch(JaloItemNotFoundException e)
            {
                LOG.warn("Error while resolving item from document ID registry. Value was " + valueExpr + ", resolved pk was " + pk + ", error message was: " + e
                                .getMessage());
                setError();
                return null;
            }
        }
        return searchItem(mergeDefaultValues(CSVUtils.splitAndUnescape(valueExpr, new char[] {this.customDelimiter}, false)), valueExpr);
    }
}
