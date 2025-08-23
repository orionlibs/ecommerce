package de.hybris.platform.hac.performance.impl;

public class JaloGetNonExistingProperty extends AbstractJaloItemTest
{
    public void executeBlock()
    {
        this.country.getProperty("notExisting");
    }


    public String getTestName()
    {
        return "Jalo item get not existing property";
    }
}
