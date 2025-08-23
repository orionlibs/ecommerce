package de.hybris.platform.hac.performance.impl;

public class JaloGetPk extends AbstractJaloItemTest
{
    public void executeBlock()
    {
        this.country.getPK();
    }


    public String getTestName()
    {
        return "Jalo item getPk()";
    }
}
