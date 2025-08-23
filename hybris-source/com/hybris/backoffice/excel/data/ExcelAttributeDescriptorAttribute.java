package com.hybris.backoffice.excel.data;

import de.hybris.platform.core.enums.RelationEndCardinalityEnum;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.BooleanUtils;

public class ExcelAttributeDescriptorAttribute implements ExcelAttribute
{
    private final AttributeDescriptorModel attributeDescriptorModel;
    private final String isoCode;


    public ExcelAttributeDescriptorAttribute(@Nonnull AttributeDescriptorModel attributeDescriptorModel, @Nullable String isoCode)
    {
        this.attributeDescriptorModel = attributeDescriptorModel;
        this.isoCode = isoCode;
    }


    public ExcelAttributeDescriptorAttribute(@Nonnull AttributeDescriptorModel attributeDescriptorModel)
    {
        this.attributeDescriptorModel = attributeDescriptorModel;
        this.isoCode = null;
    }


    public String getName()
    {
        return this.attributeDescriptorModel.getName();
    }


    public boolean isLocalized()
    {
        return BooleanUtils.isTrue(this.attributeDescriptorModel.getLocalized());
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public String getQualifier()
    {
        return this.attributeDescriptorModel.getQualifier();
    }


    public boolean isMandatory()
    {
        return BooleanUtils.isFalse(this.attributeDescriptorModel.getOptional());
    }


    public String getType()
    {
        String type = (this.attributeDescriptorModel instanceof RelationDescriptorModel) ? getTypeOfRelationElement() : getTypeOfNonRelationElement();
        return isLocalized() ? type.substring("localized:".length()) : type;
    }


    private String getTypeOfRelationElement()
    {
        RelationDescriptorModel relationDescriptorModel = (RelationDescriptorModel)this.attributeDescriptorModel;
        if(BooleanUtils.isTrue(relationDescriptorModel.getIsSource()))
        {
            return relationDescriptorModel.getRelationType().getTargetType().getCode();
        }
        return relationDescriptorModel.getRelationType().getSourceType().getCode();
    }


    private String getTypeOfNonRelationElement()
    {
        if(this.attributeDescriptorModel.getAttributeType() instanceof CollectionTypeModel)
        {
            return getTypeOfCollectionElement();
        }
        return this.attributeDescriptorModel.getAttributeType().getCode();
    }


    private String getTypeOfCollectionElement()
    {
        return ((CollectionTypeModel)this.attributeDescriptorModel.getAttributeType()).getElementType().getCode();
    }


    public boolean isMultiValue()
    {
        return (isManyRelation() || this.attributeDescriptorModel.getAttributeType() instanceof CollectionTypeModel ||
                        BooleanUtils.isTrue(this.attributeDescriptorModel.getPartOf()));
    }


    private boolean isManyRelation()
    {
        if(this.attributeDescriptorModel instanceof RelationDescriptorModel)
        {
            RelationDescriptorModel relationDescriptorModel = (RelationDescriptorModel)this.attributeDescriptorModel;
            boolean isSource = relationDescriptorModel.getIsSource().booleanValue();
            return isSource ? ((relationDescriptorModel.getRelationType().getTargetTypeCardinality() == RelationEndCardinalityEnum.MANY)) : (
                            (relationDescriptorModel.getRelationType().getSourceTypeCardinality() == RelationEndCardinalityEnum.MANY));
        }
        return false;
    }


    public AttributeDescriptorModel getAttributeDescriptorModel()
    {
        return this.attributeDescriptorModel;
    }
}
