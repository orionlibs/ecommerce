package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FeatureField extends GenericSearchField
{
    private final ClassAttributeAssignment assignment;
    private final boolean isValueSelect;
    private boolean selectRawValue = false;
    private String productAlias;
    private Language expliciteLanguage;
    private final TypedFeature.FeatureType fType;
    private final List<ClassificationAttributeValue> selectable;
    private final boolean enumsAsPosVariable;
    private ClassificationAttributeUnit unit;
    private boolean caseInsensitive = false;


    public FeatureField(ClassAttributeAssignment assignment, String producttypeAlias, Language lang, boolean enumsAsPos, boolean isValueSelect)
    {
        super(FeatureContainer.createUniqueKey(assignment));
        this.assignment = assignment;
        this.unit = assignment.getUnit();
        this.selectable = new ArrayList<>(assignment.getClassificationClass().getAttributeValues(assignment));
        this.fType = TypedFeature.FeatureType.toEnum(assignment.getAttributeType());
        this.productAlias = producttypeAlias;
        this.expliciteLanguage = lang;
        this.enumsAsPosVariable = enumsAsPos;
        this.isValueSelect = isValueSelect;
    }


    public void setSelectRawValue(boolean selectRaw)
    {
        this.selectRawValue = selectRaw;
    }


    public TypedFeature.FeatureType getFeatureType()
    {
        return this.fType;
    }


    public List<ClassificationAttributeValue> getSelectableValues()
    {
        return (this.selectable != null) ? this.selectable : Collections.EMPTY_LIST;
    }


    public void addSelectableValue(int position, ClassificationAttributeValue selValue)
    {
        this.selectable.add(position, selValue);
    }


    public ClassAttributeAssignment getClassAttributeAssignment()
    {
        return this.assignment;
    }


    public ClassificationAttributeUnit getUnit()
    {
        return this.unit;
    }


    public void setUnit(ClassificationAttributeUnit unit)
    {
        if(this.unit != unit && (this.unit == null || !this.unit.equals(unit)))
        {
            if(this.unit != null)
            {
                if(unit == null)
                {
                    throw new IllegalArgumentException("feature " + getClassAttributeAssignment() + " requires a unit");
                }
                if(!this.unit.getConvertibleUnits().contains(unit))
                {
                    throw new IllegalArgumentException("unit " + unit + " is no convertible unit of " + this.unit + " - expected one of " + this.unit
                                    .getConvertibleUnits());
                }
                this.unit = unit;
            }
            else if(unit != null)
            {
                throw new IllegalArgumentException("feature " + getClassAttributeAssignment() + " doesnt specify a unit");
            }
        }
    }


    public void setLanguage(Language lang)
    {
        boolean loc = getClassAttributeAssignment().isLocalizedAsPrimitive();
        if(!loc && lang != null)
        {
            throw new IllegalArgumentException("cannot set language for unlocalized attribute " + getClassAttributeAssignment());
        }
        this.expliciteLanguage = lang;
    }


    public Language getLanguage()
    {
        return this.expliciteLanguage;
    }


    public void setProductTypeAlias(String alias)
    {
        this.productAlias = alias;
    }


    public String getProductTypeAlias()
    {
        return this.productAlias;
    }


    public boolean isCaseInsensitive()
    {
        return this.caseInsensitive;
    }


    public void setCaseInsensitive(boolean insensitive)
    {
        this.caseInsensitive = insensitive;
    }


    protected boolean enumsAsPos()
    {
        return this.enumsAsPosVariable;
    }


    protected void writeValueField(StringBuilder query, Map<String, Object> valueMap)
    {
        if(this.selectRawValue)
        {
            query.append("{feat.").append("rawValue").append("}");
        }
        else
        {
            Collection<ClassificationAttributeUnit> convertibleUnits;
            boolean useToCharForClob = (Config.isOracleUsed() || Config.isHanaUsed());
            switch(null.$SwitchMap$de$hybris$platform$catalog$jalo$classification$util$TypedFeature$FeatureType[getFeatureType().ordinal()])
            {
                case 1:
                    if(enumsAsPos())
                    {
                        query.append("CASE ");
                        int index = 0;
                        for(ClassificationAttributeValue sel : getSelectableValues())
                        {
                            query.append("WHEN ");
                            if(useToCharForClob)
                            {
                                query.append("to_char(");
                            }
                            query.append("{feat.").append("stringValue").append("}");
                            if(useToCharForClob)
                            {
                                query.append(")");
                            }
                            query.append("='")
                                            .append(sel.getPK().toString())
                                            .append("' ");
                            query.append("THEN ").append(index++).append(" ");
                        }
                        query.append("ELSE ").append(index).append(" END ");
                        break;
                    }
                    if(useToCharForClob)
                    {
                        query.append("to_char(");
                    }
                    query.append("{feat.").append("stringValue").append("}");
                    if(useToCharForClob)
                    {
                        query.append(")");
                    }
                    break;
                case 2:
                    if(isCaseInsensitive())
                    {
                        query.append(" LOWER( ");
                    }
                    if(useToCharForClob)
                    {
                        query.append("to_char(");
                    }
                    query.append("{feat.").append("stringValue").append("}");
                    if(useToCharForClob)
                    {
                        query.append(")");
                    }
                    if(isCaseInsensitive())
                    {
                        query.append(" ) ");
                    }
                    break;
                case 3:
                    convertibleUnits = (this.unit != null) ? this.unit.getConvertibleUnits() : null;
                    if(convertibleUnits != null && !convertibleUnits.isEmpty())
                    {
                        query.append("CASE ");
                        double toFactor = this.unit.getConversionFactorAsPrimitive();
                        if(toFactor == 0.0D)
                        {
                            throw new IllegalStateException("target unit " + this.unit + " has zero conversion factor");
                        }
                        String toFactorKey = "unit_" + this.unit.getPK() + "_factor";
                        valueMap.put(toFactorKey, Double.valueOf(toFactor));
                        for(ClassificationAttributeUnit convertble : convertibleUnits)
                        {
                            double fromFactor = convertble.getConversionFactorAsPrimitive();
                            if(fromFactor == 0.0D)
                            {
                                throw new IllegalStateException("source unit " + convertble + " has zero conversion factor");
                            }
                            String fromFactorKey = "unit_" + convertble.getPK() + "_factor";
                            valueMap.put(fromFactorKey, Double.valueOf(fromFactor));
                            query.append("WHEN {feat.").append("unit").append("}=")
                                            .append(JDBCValueMappings.getInstance().pkToSQL(convertble.getPK())).append(" ");
                            query.append("THEN ( {feat.").append("numberValue").append("} * ?").append(toFactorKey)
                                            .append(" ) / ?").append(fromFactorKey).append(" ");
                        }
                        query.append("ELSE {feat.").append("numberValue").append("} END ");
                        break;
                    }
                    query.append("{feat.").append("numberValue").append("}");
                    break;
                case 4:
                    query.append("{feat.").append("numberValue").append("}");
                    break;
                case 5:
                    query.append("{feat.").append("booleanValue").append("}");
                    break;
            }
        }
    }


    protected void writeConditions(StringBuilder query, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        PK assignmentPk = getClassAttributeAssignment().getPK();
        query.append("{feat.").append("classificationAttributeAssignment").append("}= ?cassignment")
                        .append(assignmentPk).append(" ");
        valueMap.put("cassignment" + assignmentPk, assignmentPk);
        query.append(" AND ");
        query.append("{feat.").append("product").append("}=");
        query.append("{").append(getAliasFromTypeMap(typeIndexMap, this.productAlias)).append("." + Item.PK + "}");
        query.append(" AND ");
        if(getClassAttributeAssignment().isLocalizedAsPrimitive())
        {
            query.append("{feat.").append("language").append("}=");
            if(this.expliciteLanguage != null)
            {
                query.append(" ?clang ");
                valueMap.put("clang", this.expliciteLanguage.getPK());
            }
            else
            {
                query.append("?session.language ");
            }
        }
        else
        {
            query.append("{feat.").append("language").append("} IS NULL");
        }
    }


    public void toFlexibleSearch(StringBuilder query, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        if(this.isValueSelect)
        {
            query.append("({{ SELECT ");
            writeValueField(query, valueMap);
            query.append(" FROM {").append(GeneratedCatalogConstants.TC.PRODUCTFEATURE).append(" AS feat} ");
            query.append("WHERE ");
            writeConditions(query, typeIndexMap, valueMap);
            query.append(" }})");
        }
        else
        {
            query.append("SELECT {feat.").append(Item.PK).append("} ");
            query.append("FROM {").append(GeneratedCatalogConstants.TC.PRODUCTFEATURE).append(" AS feat} ");
            query.append("WHERE ");
            writeConditions(query, typeIndexMap, valueMap);
            query.append(" AND ");
            writeValueField(query, valueMap);
        }
    }
}
