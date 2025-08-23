package de.hybris.platform.hac.performance.impl;

public class JaloTenTimesSetNonExistingProperty extends AbstractJaloItemTest
{
    public void executeBlock()
    {
        for(int i = 0; i < 10; i++)
        {
            this.country.setProperty("test", "bla" + i);
        }
    }


    public String getTestName()
    {
        return "Jalo 10 times get non existing property";
    }
}
