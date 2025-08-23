package com.hybris.backoffice.excel.data;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import org.apache.commons.lang3.BooleanUtils;

public class ExcelClassificationAttribute implements ExcelAttribute
{
    private ClassAttributeAssignmentModel attributeAssignment;
    private String isoCode;
    private String name;


    public void setAttributeAssignment(ClassAttributeAssignmentModel attributeAssignment)
    {
        this.attributeAssignment = attributeAssignment;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public boolean isLocalized()
    {
        return BooleanUtils.isTrue(this.attributeAssignment.getLocalized());
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public String getQualifier()
    {
        return this.attributeAssignment.getClassificationAttribute().getCode();
    }


    public boolean isMandatory()
    {
        return false;
    }


    public String getType()
    {
        return this.attributeAssignment.getAttributeType().getCode();
    }


    public boolean isMultiValue()
    {
        return BooleanUtils.isTrue(this.attributeAssignment.getMultiValued());
    }


    public ClassAttributeAssignmentModel getAttributeAssignment()
    {
        return this.attributeAssignment;
    }
}
