package de.hybris.bootstrap.typesystem;

public class YRelation extends YComposedType
{
    private boolean localized = false;
    private YRelationEnd src;
    private YRelationEnd tgt;


    public YRelation(YNamespace container, String code)
    {
        this(container, code, "Link", "de.hybris.platform.jalo.link.Link");
    }


    public YRelation(YNamespace container, String code, String superTypeCode, String jaloClassName)
    {
        super(container, code, superTypeCode, jaloClassName);
    }


    protected boolean allowMetaTypeInheritanceFrom(YComposedType superType)
    {
        return superType instanceof YRelation;
    }


    protected String getDefaultMetaTypeCode()
    {
        return "RelationMetaType";
    }


    public void validate()
    {
        super.validate();
        if(getSourceEnd() == null)
        {
            throw new IllegalStateException("invalid relation type " + this + " due to missing src end");
        }
        getSourceEnd().validate();
        if(getTargetEnd() == null)
        {
            throw new IllegalStateException("invalid relation type " + this + " due to missing tgt end");
        }
        getTargetEnd().validate();
        if(getSourceEnd().getCardinality() == YRelationEnd.Cardinality.ONE && getTargetEnd().getCardinality() == YRelationEnd.Cardinality.ONE)
        {
            throw new IllegalStateException("invalid relation type " + this + " since 1-1 relations are not supported");
        }
        if(isLocalized() && isOneToMany())
        {
            throw new IllegalStateException("invalid relation type " + this + " since 1-n relations cannot be localized");
        }
        if(getNamespace().getTypeSystem().isBuildMode())
        {
            if(getDeploymentName() != null && isOneToMany())
            {
                throw new IllegalStateException("invalid relation type " + this + " since 1-n relations cannot have a deployment");
            }
        }
        if(!isOneToMany() && (getSourceEnd().hasConditionQuery().booleanValue() || getTargetEnd().hasConditionQuery().booleanValue()))
        {
            throw new IllegalStateException("n-n relations cannot have condition query at either end");
        }
        if(isOneToMany() && getSourceEnd().hasConditionQuery().booleanValue() && getTargetEnd().hasConditionQuery().booleanValue())
        {
            throw new IllegalStateException("1-n relations cannot have condition query at both ends");
        }
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public void setLocalized(boolean localized)
    {
        this.localized = localized;
    }


    public boolean isOneToMany()
    {
        return (getSourceEnd().getCardinality() == YRelationEnd.Cardinality.ONE || getTargetEnd().getCardinality() == YRelationEnd.Cardinality.ONE);
    }


    public YAttributeDescriptor getOrderingAttribute()
    {
        if(getSourceEnd().getCardinality() == YRelationEnd.Cardinality.ONE)
        {
            return getSourceEnd().getMappedOrderingAttribute();
        }
        if(getTargetEnd().getCardinality() == YRelationEnd.Cardinality.ONE)
        {
            return getTargetEnd().getMappedOrderingAttribute();
        }
        return null;
    }


    public YAttributeDescriptor getLocalizationAttribute()
    {
        if(getSourceEnd().getCardinality() == YRelationEnd.Cardinality.ONE)
        {
            return getSourceEnd().getMappedLocalizationAttribute();
        }
        if(getTargetEnd().getCardinality() == YRelationEnd.Cardinality.ONE)
        {
            return getTargetEnd().getMappedLocalizationAttribute();
        }
        return null;
    }


    public YRelationEnd getSourceEnd()
    {
        return this.src;
    }


    public YRelationEnd getTargetEnd()
    {
        return this.tgt;
    }


    public void setSourceEnd(YRelationEnd src)
    {
        this.src = src;
    }


    public void setTargetEnd(YRelationEnd tgt)
    {
        this.tgt = tgt;
    }
}
