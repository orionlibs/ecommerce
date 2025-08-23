package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.util.Utilities;

class TableField extends ParsedText
{
    private Table table;
    private ParsedType type;
    private final ParsedType expectedType;
    private String fieldName;
    private PK customLanguagePK;
    private static final boolean convertedToOracleStyle = false;
    private boolean core;
    private boolean localized;
    private boolean optional;


    public String toString()
    {
        return "TableField(name='" + this.fieldName + "',langPK='" + this.customLanguagePK + "',type=" + (
                        (this.type != null) ? this.type.getCode() : "null") + ")";
    }


    TableField(FieldExpression expr, String fieldText)
    {
        super((ParsedText)expr, fieldText);
        this.expectedType = null;
    }


    TableField(FieldExpression expr, String fieldText, ParsedType expectedType)
    {
        super((ParsedText)expr, fieldText);
        this.expectedType = expectedType;
    }


    TableField(ParsedType type, FieldExpression expr, String fieldName)
    {
        super((ParsedText)expr, "dummy text");
        this.type = type;
        this.fieldName = fieldName;
        this.customLanguagePK = null;
        this.core = false;
        this.localized = false;
        this.optional = false;
        this.expectedType = null;
    }


    FieldExpression getFieldExpression()
    {
        return (FieldExpression)getEnclosingText();
    }


    boolean hasTable()
    {
        return (this.table != null);
    }


    Table getTable()
    {
        if(!hasTable())
        {
            throw new IllegalStateException("field has no table (yet) - need second pass");
        }
        return this.table;
    }


    ParsedType getType()
    {
        if(isFirstPass())
        {
            throw new IllegalStateException("field has no type yet - need first pass");
        }
        return this.type;
    }


    protected PK extractCustomLanguagePK(String languageClause) throws FlexibleSearchException
    {
        PK langPk;
        if(languageClause == null)
        {
            langPk = null;
        }
        else if(languageClause.equalsIgnoreCase("any"))
        {
            langPk = PK.NULL_PK;
        }
        else
        {
            langPk = getType().getFSTypeCacheProvider().getLanguagePkFromIsocode(languageClause);
        }
        return langPk;
    }


    protected void translate() throws FlexibleSearchException
    {
        if(isFirstPass())
        {
            FlexibleSearchTools.TableFieldDescriptor desc = FlexibleSearchTools.splitField(getSource());
            this.fieldName = desc.getAttributeQualifier();
            setUpType(desc);
            setUpOptionsAndCustomLanguage(desc);
            if(isNormalField())
            {
                setUpNormalTable();
            }
        }
        else if(isSecondPass())
        {
            setUpSpecialTable();
        }
    }


    private boolean isFirstPass()
    {
        return (this.type == null);
    }


    private boolean isSecondPass()
    {
        return (this.table == null);
    }


    private void setUpType(FlexibleSearchTools.TableFieldDescriptor desc)
    {
        if(this.expectedType != null)
        {
            this.type = this.expectedType;
        }
        else if(desc.getTypeQualifier() == null)
        {
            this.type = getFieldExpression().getDefaultType();
        }
        else
        {
            this
                            .type = (desc.getTypeQualifier() instanceof String) ? getFieldExpression().getType((String)desc.getTypeQualifier()) : getFieldExpression().getType(((Integer)desc.getTypeQualifier()).intValue());
        }
    }


    private void setUpOptionsAndCustomLanguage(FlexibleSearchTools.TableFieldDescriptor desc)
    {
        this.customLanguagePK = extractCustomLanguagePK(desc.getLanguage());
        String options = desc.getOptions();
        if(options != null)
        {
            String asUpper = options.toUpperCase();
            this.core = (asUpper.indexOf('C') > -1);
            this.localized = (asUpper.indexOf('L') > -1 || this.customLanguagePK != null);
            this.optional = (asUpper.indexOf('O') > -1);
        }
    }


    private boolean isNormalField()
    {
        return !FlexibleSearchTools.isSpecialField(this.fieldName);
    }


    private void setUpNormalTable()
    {
        this.table = this.type.getTableForField(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ").append(this.table.getTableAlias()).append(".").append(createColumnName(this.fieldName)).append(" ");
        setBuffer(stringBuilder);
    }


    private void setUpSpecialTable() throws FlexibleSearchException
    {
        this.table = getType().getTableForSpecialField();
        String resolveResult = resolveSpecialFieldName(this.fieldName, this.table, getSource());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ").append(resolveResult).append(" ");
        setBuffer(stringBuilder);
    }


    void appendOracleOuterJoinSign()
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("table field " + this + " is not yet translated");
        }
        if(!getFieldExpression().isTranslated())
        {
            throw new IllegalStateException("field expression " + getFieldExpression() + " of table field " + this + " is not yet translated");
        }
        getFieldExpression().insertIntoTranslated(getInsertPosition() + getTranslated().length(), "(+)");
    }


    void markLocalized()
    {
        this.localized = true;
    }


    void markOptional()
    {
        this.optional = true;
    }


    static String resolveSpecialFieldName(String fieldName, Table specialFieldTable, String source) throws FlexibleSearchException
    {
        if(fieldName == null || specialFieldTable == null)
        {
            throw new IllegalArgumentException("fieldName = " + fieldName + ", specialFieldTable = " + specialFieldTable);
        }
        if(FlexibleSearchTools.PK_PROP.equals(fieldName))
        {
            return specialFieldTable.getTableAlias() + "." + specialFieldTable.getTableAlias();
        }
        if(FlexibleSearchTools.TYPE_PROP.equals(fieldName))
        {
            return specialFieldTable.getTableAlias() + "." + specialFieldTable.getTableAlias();
        }
        throw new FlexibleSearchException(null, "unknown special field name '" + fieldName + "' string was " + source, 0);
    }


    private String createColumnName(String field)
    {
        if(isSecondPass() || isFirstPass())
        {
            throw new IllegalStateException("table or type was not set (table =" + this.table + ", type=" + this.type + ")");
        }
        int propertyType = getType().getTypePersistenceData().getPropertyTypeForName(field);
        switch(propertyType)
        {
            case 0:
                return getType().getTypePersistenceData().getUnlocalizedPropertyColumnName(field);
            case 1:
                return getType().getTypePersistenceData().getLocalizedPropertyColumnName(field);
            case 2:
                return getType().getTypePersistenceData().getCorePropertyColumnName(field);
            case 3:
                return "VALUESTRING1";
        }
        throw new JaloInternalException(null, "illegal field type " + propertyType, 0);
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        throw new IllegalStateException("table fields doesnt have nested texts");
    }


    String getName()
    {
        return this.fieldName;
    }


    boolean isOptional()
    {
        return this.optional;
    }


    PK getCustomLanguagePK()
    {
        return this.customLanguagePK;
    }


    boolean isCore()
    {
        return this.core;
    }


    boolean isLocalized()
    {
        return this.localized;
    }


    boolean ignoreLanguage()
    {
        return FlexibleSearchTools.ignoreLanguage(getCustomLanguagePK());
    }


    public int hashCode()
    {
        int ret = getName().hashCode();
        if(hasTable())
        {
            ret += getTable().hashCode();
        }
        if(getCustomLanguagePK() != null)
        {
            ret += getCustomLanguagePK().hashCode();
        }
        return ret;
    }


    public boolean equals(Object object)
    {
        if(super.equals(object))
        {
            return true;
        }
        if(object == null)
        {
            return false;
        }
        try
        {
            TableField other = (TableField)object;
            if(getName().equals(other.getName()) &&
                            Utilities.equals(getCustomLanguagePK(), other.getCustomLanguagePK()))
            {
                if(Utilities.equals(hasTable() ? getTable() : null, other.hasTable() ? other.getTable() : null))
                    ;
            }
            return false;
        }
        catch(ClassCastException e)
        {
            return false;
        }
    }
}
