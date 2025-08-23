package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import de.hybris.bootstrap.util.LocaleHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class YRelationEnd extends YDescriptor
{
    private static final Logger LOG = Logger.getLogger(YRelationEnd.class);
    public static final String ITEM_CUSTOM_ORDERING_ATTRIBUTE = "ordering.attribute";
    public static final String DEFAULT_POSITION_ATTRIBUTE_SUFFIX = "POS";
    public static final String CONDITION_QUERY = "condition.query";
    private final String relationCode;
    private YRelation rel;
    private YAttributeDescriptor mapped;
    private YAttributeDescriptor mappedOrderingAttribute;
    private YAttributeDescriptor mappedLocalizationAttribute;
    private final boolean source;
    private boolean ordered;
    private Cardinality card = Cardinality.MANY;
    private YCollectionType.TypeOfCollection collectionType = YCollectionType.TypeOfCollection.COLLECTION;
    private String description;
    private ModelTagListener.ModelData modelData;
    private final boolean navigable;
    private Boolean hasConditionQuery;
    private String conditionQuery;


    public YRelationEnd(YNamespace container, String relationCode, String role, String typeCode, boolean navigable, boolean isSource)
    {
        super(container, navigable ? role : toNonnavigableRole(relationCode, typeCode), typeCode);
        this.relationCode = relationCode;
        this.source = isSource;
        this.navigable = navigable;
    }


    protected String getDefaultMetaTypeCode()
    {
        return "RelationDescriptor";
    }


    public void validate()
    {
        if(getQualifier() == null && this.navigable)
        {
            throw new IllegalStateException("Found descriptor with empty 'qualifier' attribute at " + getLoaderInfo() + ".");
        }
        getType();
        if((isSource() && getRelation().getSourceEnd() != this) || (!isSource() && getRelation().getTargetEnd() != this))
        {
            throw new IllegalStateException("invalid relation end " + this + " since it seems not to be assigned to relation " +
                            getRelation());
        }
        getMappedAttribute();
        if(isUniqueModifier() && Cardinality.MANY.equals(getCardinality()))
        {
            throw new IllegalStateException("The attribute '" + getQualifier() + "' in the relation '" + getRelationCode() + "' is of typ collection and is unique. The modifier unique is not allowed here. See: " + this);
        }
        if(Cardinality.ONE.equals(getCardinality()) && isOrdered())
        {
            LOG.warn("The attribute '" + getQualifier() + "' in the relation '" + getRelationCode() + "' is of ONE cardinality but is set as ordered, this will be ignored. See: " + this);
        }
        if(hasConditionQuery().booleanValue() && !Cardinality.MANY.equals(getCardinality()))
        {
            throw new IllegalStateException("invalid relation end " + this + " due to having conditionQuery set and not having many cardinality");
        }
    }


    private static String toNonnavigableRole(String relationCode, String typeCode)
    {
        return StringUtils.lowerCase("__nonnavigable_" + relationCode + "_" + typeCode, LocaleHelper.getPersistenceLocale());
    }


    public String getRole()
    {
        return getQualifier();
    }


    public Cardinality getCardinality()
    {
        return this.card;
    }


    public void setCardinality(Cardinality card)
    {
        this.card = card;
    }


    public boolean isSource()
    {
        return this.source;
    }


    public String getRelationCode()
    {
        return this.relationCode;
    }


    public YRelation getRelation()
    {
        if(this.rel == null)
        {
            YType tmp = getTypeSystem().getType(getRelationCode());
            if(!(tmp instanceof YRelation))
            {
                throw new IllegalStateException("invalid relation end " + this + " due to missing relation type '" +
                                getRelationCode() + "'");
            }
            this.rel = (YRelation)tmp;
        }
        return this.rel;
    }


    public YComposedType getType()
    {
        return (YComposedType)super.getType();
    }


    public YRelationEnd getOppositeEnd()
    {
        return isSource() ? getRelation().getTargetEnd() : getRelation().getSourceEnd();
    }


    public boolean mappedAttributeDoesntExist()
    {
        return (this.mapped == null && getOppositeEnd().getType().getAttribute(getRole()) == null);
    }


    public YAttributeDescriptor getMappedAttribute()
    {
        if(this.mapped == null)
        {
            this.mapped = getOppositeEnd().getType().getAttribute(getRole());
            if(this.mapped == null && isNavigable())
            {
                throw new IllegalStateException("invalid relation end " + this + " due to missing mapped attribute " +
                                getOppositeEnd().getType().getCode() + "." + getRole());
            }
        }
        return this.mapped;
    }


    private String stripOrderingAttributeParamQuotesIfNeeded(String attribute)
    {
        if(attribute.length() >= 2 && attribute.charAt(0) == '"' && attribute.charAt(attribute.length() - 1) == '"')
        {
            return attribute.substring(1, attribute.length() - 1);
        }
        return attribute;
    }


    public YAttributeDescriptor getMappedOrderingAttribute()
    {
        if(this.mappedOrderingAttribute == null)
        {
            String defaultOrderingAttribute = getDefaultOrderingAttribute();
            String customOrderingAttribute = (String)getCustomProps().get("ordering.attribute");
            if(customOrderingAttribute != null)
            {
                String customOrderAttribute = stripOrderingAttributeParamQuotesIfNeeded(customOrderingAttribute);
                if(!defaultOrderingAttribute.equals(customOrderAttribute))
                {
                    this.mappedOrderingAttribute = getOppositeEnd().getType().getAttribute(customOrderAttribute);
                    return this.mappedOrderingAttribute;
                }
            }
            if(getRelation().isAbstract() && getOppositeEnd().isOrdered())
            {
                this.mappedOrderingAttribute = getOppositeEnd().getType().getAttribute(getDefaultOrderingAttribute());
                if(this.mappedOrderingAttribute == null)
                {
                    throw new IllegalStateException("invalid relation end " + this + " due to missing mapped ordering attribute " +
                                    getOppositeEnd().getType()
                                                    .getCode() + "." + getDefaultOrderingAttribute());
                }
            }
        }
        return this.mappedOrderingAttribute;
    }


    private String getDefaultOrderingAttribute()
    {
        return getRole() + "POS";
    }


    public YAttributeDescriptor getMappedLocalizationAttribute()
    {
        if(this.mappedLocalizationAttribute == null)
        {
            if(getRelation().isAbstract() && getRelation().isLocalized())
            {
                this.mappedLocalizationAttribute = getOppositeEnd().getType().getAttribute(getRole() + "LOC");
                if(this.mappedLocalizationAttribute == null)
                {
                    throw new IllegalStateException("invalid relation end " + this + " due to missing mapped localization attribute " +
                                    getOppositeEnd().getType().getCode() + "." + getRole() + "LOC");
                }
            }
        }
        return this.mappedLocalizationAttribute;
    }


    public Boolean hasConditionQuery()
    {
        if(this.hasConditionQuery == null)
        {
            if(!isSource() && StringUtils.isNotBlank((String)getCustomProps().get("condition.query")))
            {
                this.hasConditionQuery = Boolean.valueOf(true);
            }
            else
            {
                this.hasConditionQuery = Boolean.valueOf(false);
            }
        }
        return this.hasConditionQuery;
    }


    public String getConditionQuery()
    {
        if(hasConditionQuery().booleanValue())
        {
            this.conditionQuery = (String)getCustomProps().get("condition.query");
        }
        return this.conditionQuery;
    }


    public YCollectionType.TypeOfCollection getCollectionType()
    {
        return (this.collectionType != null) ? this.collectionType : YCollectionType.TypeOfCollection.LIST;
    }


    public void setCollectionType(YCollectionType.TypeOfCollection collectionType)
    {
        this.collectionType = collectionType;
    }


    public boolean isOrdered()
    {
        return this.ordered;
    }


    public boolean isNavigable()
    {
        return this.navigable;
    }


    public void setOrdered(boolean ordered)
    {
        this.ordered = ordered;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setModelData(ModelTagListener.ModelData modelData)
    {
        this.modelData = modelData;
    }


    public ModelTagListener.ModelData getModelData()
    {
        return this.modelData;
    }
}
