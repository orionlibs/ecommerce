package de.hybris.platform.impex.jalo.exp.generator;

import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.ScriptGenerator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public abstract class AbstractScriptGenerator implements ScriptGenerator
{
    private static final Logger LOG = Logger.getLogger(AbstractScriptGenerator.class.getName());
    private final Set<ComposedType> ignoreTypes = new HashSet<>();
    private final Map<ComposedType, Set<String>> ignoreColumns = new HashMap<>();
    private final Map<ComposedType, Map<String, Map<String, String>>> additionalModifiers = new HashMap<>();
    private final Map<ComposedType, Set<String>> additionalColumns = new HashMap<>();
    private final Map<ComposedType, Map<String, String>> replacedColumnExpressions = new LinkedHashMap<>();
    private Set<Language> langs;
    private final List<ScriptModifier> scriptModifiers = new ArrayList<>();
    private boolean useDocumentIdVariable = true;
    private boolean includeSystemTypesVariable = false;
    private Set<ComposedType> types;
    private CSVWriter scriptWriter;


    public String generateScript()
    {
        if(this.langs == null)
        {
            this.langs = C2LManager.getInstance().getAllLanguages();
        }
        if(this.types == null)
        {
            this.types = determineInitialTypes();
        }
        initScriptModifier();
        try
        {
            StringWriter script = new StringWriter();
            this.scriptWriter = new CSVWriter(script);
            writeScript();
            this.scriptWriter.close();
            if(LOG.isDebugEnabled())
            {
                LOG.debug(script.getBuffer().toString());
            }
            return script.getBuffer().toString();
        }
        catch(IOException e)
        {
            LOG.error("Error while writing to CSVWriter: " + e.getMessage());
            return "";
        }
    }


    protected abstract void writeScript() throws IOException;


    protected CSVWriter getScriptWriter()
    {
        return this.scriptWriter;
    }


    public void setTypes(Set<ComposedType> types)
    {
        this.types = types;
    }


    public Set<ComposedType> getTypes()
    {
        return this.types;
    }


    public boolean isUseDocumentID()
    {
        return this.useDocumentIdVariable;
    }


    public void useDocumentID(boolean enable)
    {
        this.useDocumentIdVariable = enable;
    }


    public void setLanguages(Set<Language> langs)
    {
        this.langs = langs;
    }


    public Set<Language> getLanguages()
    {
        return this.langs;
    }


    public boolean isIncludeSystemTypes()
    {
        return this.includeSystemTypesVariable;
    }


    public void includeSystemTypes(boolean enable)
    {
        this.includeSystemTypesVariable = enable;
    }


    public void registerScriptModifier(ScriptModifier script)
    {
        if(script == null)
        {
            LOG.warn("call \"registerScriptModifier( <null> )\" ignored!");
            return;
        }
        this.scriptModifiers.add(script);
    }


    public void unregisterScriptModifier(ScriptModifier script)
    {
        this.scriptModifiers.remove(script);
    }


    protected List<ScriptModifier> getScriptModifiers()
    {
        return this.scriptModifiers;
    }


    public boolean hasRegisteredScriptModifiers()
    {
        return !getScriptModifiers().isEmpty();
    }


    private final void initScriptModifier()
    {
        for(ScriptModifier modifier : this.scriptModifiers)
        {
            modifier.init(this);
        }
    }


    public void addAdditionalModifier(String typeString, String column, String modifier, String value)
    {
        if(typeString == null)
        {
            return;
        }
        ComposedType type = null;
        try
        {
            type = TypeManager.getInstance().getComposedType(typeString);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("Can not find type " + typeString + ", will not add additional modifier " + modifier + " to attribute " + column);
            return;
        }
        Map<String, Map<String, String>> typeMap = this.additionalModifiers.get(type);
        if(typeMap == null)
        {
            typeMap = new HashMap<>();
            this.additionalModifiers.put(type, typeMap);
        }
        Map<String, String> columnMap = typeMap.get(column);
        if(columnMap == null)
        {
            columnMap = new HashMap<>();
            typeMap.put(column, columnMap);
        }
        if(!columnMap.containsKey(modifier) || columnMap.get(modifier) != null)
        {
            columnMap.put(modifier, value);
        }
    }


    protected Map<String, String> getAdditionalModifiers(ComposedType type, String column)
    {
        Map<String, String> ret = null;
        for(Map.Entry<ComposedType, Map<String, Map<String, String>>> typeEntry : this.additionalModifiers.entrySet())
        {
            if(((ComposedType)typeEntry.getKey()).isAssignableFrom((Type)type))
            {
                for(Map.Entry<String, Map<String, String>> columnEntry : (Iterable<Map.Entry<String, Map<String, String>>>)((Map)typeEntry.getValue()).entrySet())
                {
                    if(column.equalsIgnoreCase(columnEntry.getKey()))
                    {
                        if(ret == null)
                        {
                            ret = columnEntry.getValue();
                            continue;
                        }
                        for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)((Map)columnEntry.getValue()).entrySet())
                        {
                            if(!ret.containsKey(entry.getKey()) || ret.get(entry.getKey()) != null)
                            {
                                ret.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
            }
        }
        return (ret == null) ? Collections.EMPTY_MAP : ret;
    }


    public void addSpecialColumn(String type, String columnText)
    {
        addAdditionalColumn(type, "@" + columnText);
    }


    public void addReplacedColumnExpression(String typeString, String column, String expression)
    {
        if(typeString == null)
        {
            return;
        }
        ComposedType type = null;
        try
        {
            type = TypeManager.getInstance().getComposedType(typeString);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("Can not find type " + typeString + ", will not replace expression for attribute " + column);
            return;
        }
        Map<String, String> typeMap = this.replacedColumnExpressions.get(type);
        if(typeMap == null)
        {
            typeMap = new LinkedHashMap<>();
            this.replacedColumnExpressions.put(type, typeMap);
        }
        typeMap.put(column, expression);
    }


    public void addAdditionalColumn(String typeString, String columnText)
    {
        if(typeString == null)
        {
            return;
        }
        ComposedType type = null;
        try
        {
            type = TypeManager.getInstance().getComposedType(typeString);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("Can not find type " + typeString + ", will not add additional column " + columnText);
            return;
        }
        Set<String> typeSet = this.additionalColumns.get(type);
        if(typeSet == null)
        {
            typeSet = new HashSet<>();
            this.additionalColumns.put(type, typeSet);
        }
        typeSet.add(columnText);
    }


    public void addIgnoreType(String typeString)
    {
        if(typeString == null)
        {
            return;
        }
        ComposedType type = null;
        try
        {
            type = TypeManager.getInstance().getComposedType(typeString);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("Can not find type " + typeString + ", will not ignore it");
            return;
        }
        this.ignoreTypes.add(type);
    }


    public void addIgnoreColumn(String typeString, String column)
    {
        if(typeString == null)
        {
            return;
        }
        ComposedType type = null;
        try
        {
            type = TypeManager.getInstance().getComposedType(typeString);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("Can not find type " + typeString + ", will not ignore attribute " + column);
            return;
        }
        Set<String> typeSet = this.ignoreColumns.get(type);
        if(typeSet == null)
        {
            typeSet = new HashSet<>();
            this.ignoreColumns.put(type, typeSet);
        }
        typeSet.add(column);
    }


    protected Set<String> getAdditionalColumns(ComposedType type)
    {
        Set<String> ret = null;
        for(Map.Entry<ComposedType, Set<String>> entry : this.additionalColumns.entrySet())
        {
            if(((ComposedType)entry.getKey()).isAssignableFrom((Type)type))
            {
                if(ret == null)
                {
                    ret = entry.getValue();
                    continue;
                }
                ret.addAll(entry.getValue());
            }
        }
        return (ret == null) ? Collections.EMPTY_SET : ret;
    }


    protected String getReplacedExpression(ComposedType type, String column)
    {
        if(this.replacedColumnExpressions.isEmpty())
        {
            return null;
        }
        Map<String, String> typeMap = null;
        ComposedType curType = type;
        while(typeMap == null && curType != null)
        {
            typeMap = this.replacedColumnExpressions.get(curType);
            curType = curType.getSuperType();
        }
        if(typeMap != null)
        {
            return typeMap.get(column);
        }
        return null;
    }


    protected boolean isIgnoreType(ComposedType type)
    {
        for(ComposedType curType : this.ignoreTypes)
        {
            if(curType.isAssignableFrom((Type)type))
            {
                return true;
            }
        }
        return false;
    }


    protected boolean isIgnoreColumn(ComposedType type, String column)
    {
        for(Map.Entry<ComposedType, Set<String>> entry : this.ignoreColumns.entrySet())
        {
            if(((ComposedType)entry.getKey()).isAssignableFrom((Type)type))
            {
                for(String curColumn : entry.getValue())
                {
                    if(curColumn.equals(column))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    protected String generateColumn(AttributeDescriptor attributeDescriptor, String langModifier)
    {
        Type attType = attributeDescriptor.getRealAttributeType();
        StringBuffer ret = new StringBuffer("");
        String replacement = getReplacedExpression(attributeDescriptor.getEnclosingType(), attributeDescriptor
                        .getQualifier());
        if(replacement == null)
        {
            ret.append(generateColumnDescription(attType, attributeDescriptor.getQualifier(), langModifier));
        }
        else
        {
            ret.append(attributeDescriptor.getQualifier())
                            .append('(')
                            .append(replacement)
                            .append(')');
        }
        ret.append(generateColumnModifiers(attributeDescriptor, langModifier));
        return ret.toString();
    }


    protected String generateColumnDescription(Type rawType, String columnQualifier, String langModifier)
    {
        StringBuffer ret = new StringBuffer(columnQualifier);
        if(rawType instanceof CollectionType)
        {
            return generateColumnDescription(((CollectionType)rawType).getElementType(), columnQualifier, langModifier);
        }
        if(rawType instanceof MapType)
        {
            if(langModifier == null)
            {
                ret.append('(');
                String argTypeString = generateColumnDescription(((MapType)rawType).getArgumentType(), "", null);
                if(argTypeString.length() != 0)
                {
                    ret.append("key").append(argTypeString);
                }
                String retTypeString = generateColumnDescription(((MapType)rawType).getReturnType(), "", null);
                if(retTypeString.length() != 0)
                {
                    if(argTypeString.length() != 0)
                    {
                        ret.append(',');
                    }
                    ret.append("value").append(retTypeString);
                }
                ret.append(')');
            }
            else
            {
                return generateColumnDescription(((MapType)rawType).getReturnType(), columnQualifier, langModifier);
            }
        }
        if(rawType instanceof ComposedType)
        {
            ret.append(generateColumnTypeDescription((ComposedType)rawType));
        }
        return ret.toString();
    }


    protected String generateColumnTypeDescription(ComposedType attType)
    {
        StringBuffer ret = new StringBuffer("");
        ret.append('(');
        boolean foundUnique = false;
        for(AttributeDescriptor ad : attType.getAttributeDescriptorsIncludingPrivate())
        {
            if(ad.isUnique())
            {
                if(foundUnique)
                {
                    ret.append(',');
                }
                foundUnique = true;
                if(ad.isLocalized())
                {
                    LOG.warn("Found unique attribute which is localized!!");
                }
                ret.append(generateColumnDescription(ad.getAttributeType(), ad.getQualifier(), null));
            }
        }
        if(!foundUnique)
        {
            if(this.useDocumentIdVariable)
            {
                ret.append("&").append("Item");
            }
            else
            {
                ret.append(Item.PK);
            }
        }
        ret.append(')');
        return ret.toString();
    }


    protected String generateColumnModifiers(AttributeDescriptor attributeDescriptor, String langModifier)
    {
        Map<String, String> mods = new HashMap<>();
        if(langModifier != null)
        {
            mods.put("lang", langModifier);
        }
        if(attributeDescriptor.isUnique())
        {
            mods.put("unique", "true");
        }
        if(attributeDescriptor.getAttributeType().getCode().equalsIgnoreCase("java.util.Date") || attributeDescriptor
                        .getAttributeType().getCode().equalsIgnoreCase("de.hybris.platform.util.StandardDateRange"))
        {
            mods.put("dateformat", "dd.MM.yyyy hh:mm:ss");
        }
        mods.putAll(getAdditionalModifiers(attributeDescriptor.getEnclosingType(), attributeDescriptor.getQualifier()));
        for(Iterator<Map.Entry<String, String>> iter = mods.entrySet().iterator(); iter.hasNext(); )
        {
            if(((Map.Entry)iter.next()).getValue() == null)
            {
                iter.remove();
            }
        }
        StringBuffer ret = new StringBuffer("");
        if(!mods.isEmpty())
        {
            ret.append('[');
            for(Map.Entry<String, String> entry : mods.entrySet())
            {
                ret.append(entry.getKey()).append('=').append(entry.getValue());
                ret.append(',');
            }
            ret = new StringBuffer(ret.substring(0, ret.length() - 1));
            ret.append(']');
        }
        return ret.toString();
    }


    protected Set<ComposedType> determineInitialTypes()
    {
        Set<ComposedType> allTypes = new LinkedHashSet<>();
        Set<ComposedType> rootTypes = getExportableRootTypes();
        for(ComposedType rootType : rootTypes)
        {
            if(!isIncludeSystemTypes() && ImpExManager.getInstance().isSystemTypeAsPrimitive(rootType))
            {
                continue;
            }
            if(isIgnoreType(rootType))
            {
                continue;
            }
            allTypes.add(rootType);
            Set<ComposedType> subtypes = rootType.getSubTypes();
            while(!subtypes.isEmpty())
            {
                Set<ComposedType> next = new HashSet<>();
                for(ComposedType subType : subtypes)
                {
                    if(!isIncludeSystemTypes() && ImpExManager.getInstance().isSystemTypeAsPrimitive(subType))
                    {
                        continue;
                    }
                    if(isIgnoreType(subType))
                    {
                        continue;
                    }
                    if(!filterTypeCompletely(subType) && !rootTypes.contains(subType))
                    {
                        if(!subType.isAbstract())
                        {
                            allTypes.add(subType);
                        }
                        next.addAll(subType.getSubTypes());
                    }
                }
                subtypes = next;
            }
        }
        return allTypes;
    }


    protected boolean filterTypeCompletely(ComposedType type)
    {
        if(hasRegisteredScriptModifiers())
        {
            for(ScriptModifier cur : getScriptModifiers())
            {
                if(cur.filterTypeCompletely(type))
                {
                    return true;
                }
            }
            return false;
        }
        return ExportUtils.filterTypeCompletely(type);
    }


    protected Set<ComposedType> getExportableRootTypes()
    {
        if(hasRegisteredScriptModifiers())
        {
            Set<ComposedType> ret = new LinkedHashSet<>();
            for(ScriptModifier cur : getScriptModifiers())
            {
                ret.addAll(cur.getExportableRootTypes(this));
            }
            return ret;
        }
        return ExportUtils.getExportableRootTypes(this);
    }


    protected void writeComment(String comment) throws IOException
    {
        this.scriptWriter.write(Collections.singletonMap(Integer.valueOf(0), "# " + comment));
    }


    protected void writeBeanShell(String command) throws IOException
    {
        getScriptWriter().write(Collections.singletonMap(Integer.valueOf(0), "#% " + command + ";"));
    }


    protected void writeHeader(ComposedType type) throws IOException
    {
        if(TypeManager.getInstance().getComposedType(UserRight.class).isAssignableFrom((Type)type))
        {
            getScriptWriter()
                            .writeComment("SPECIAL CASE: Type UserRight will be exported with special logic (without header definition), see https://wiki.hybris.com/x/PIFvAg");
        }
        else
        {
            Collection<AttributeDescriptor> attribs = type.getAttributeDescriptorsIncludingPrivate();
            boolean hasUnique = false;
            Set<String> columns = new TreeSet<>();
            for(AttributeDescriptor ad : attribs)
            {
                if(!isIgnoreColumn(type, ad.getQualifier()) && (!(ad instanceof de.hybris.platform.jalo.type.RelationDescriptor) || ad
                                .isProperty()) &&
                                !ad.getQualifier().equals(Item.PK) && (ad
                                .isInitial() || ad.isWritable()) && (
                                !ad.getQualifier().equals("itemtype") || TypeManager.getInstance().getType("EnumerationValue")
                                                .isAssignableFrom((Type)type)))
                {
                    if(!ad.isOptional())
                    {
                        addAdditionalModifier(type.getCode(), ad.getQualifier(), "allownull", "true");
                    }
                    if(ad.isUnique() ||
                                    getAdditionalModifiers(type, ad.getQualifier()).get("unique") != null)
                    {
                        hasUnique = true;
                    }
                    if(!ad.isWritable())
                    {
                        addAdditionalModifier(type.getCode(), ad.getQualifier(), "forceWrite", "true");
                    }
                    if(ad.isLocalized())
                    {
                        for(Language lang : getLanguages())
                        {
                            columns.add(generateColumn(ad, lang.getIsoCode()));
                        }
                        continue;
                    }
                    columns.add(generateColumn(ad, null));
                }
            }
            columns.addAll(getAdditionalColumns(type));
            Map<Object, Object> line = new HashMap<>();
            int index = 0;
            String firstColumn = generateFirstHeaderColumn(type, hasUnique);
            line.put(Integer.valueOf(index), firstColumn);
            index++;
            if(isUseDocumentID())
            {
                line.put(Integer.valueOf(index), "&Item");
            }
            else
            {
                line.put(Integer.valueOf(index), Item.PK + Item.PK);
            }
            index++;
            for(Iterator<String> iter = columns.iterator(); iter.hasNext(); index++)
            {
                String column = iter.next();
                if(column.length() != 0)
                {
                    line.put(Integer.valueOf(index), column);
                }
            }
            getScriptWriter().write(line);
        }
    }


    protected String generateFirstHeaderColumn(ComposedType type, boolean hasUniqueColumns)
    {
        StringBuffer result = new StringBuffer("");
        if(hasUniqueColumns || !isUseDocumentID())
        {
            result.append(ImpExConstants.Syntax.Mode.INSERT_UPDATE);
        }
        else
        {
            result.append(ImpExConstants.Syntax.Mode.INSERT);
        }
        result.append(' ').append(type.getCode());
        return result.toString();
    }
}
