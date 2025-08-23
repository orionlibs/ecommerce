package de.hybris.platform.personalizationpromotionsweb.beans;

import de.hybris.platform.personalizationfacades.data.ActionData;

public class CxPromotionTestActionData extends ActionData
{
    private static final long serialVersionUID = 1L;
    private String testField;


    public String getTestField()
    {
        return this.testField;
    }


    public void setTestField(String testField)
    {
        this.testField = testField;
    }
}
