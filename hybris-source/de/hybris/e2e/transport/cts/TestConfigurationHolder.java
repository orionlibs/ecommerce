package de.hybris.e2e.transport.cts;

public class TestConfigurationHolder implements ConfigurationHolder
{
    public String getApplicationType()
    {
        return "HYBRIS";
    }


    public String getSid()
    {
        return "TEST";
    }


    public String getUser()
    {
        return "TestUser";
    }


    public String getPassword()
    {
        return "TestPwd";
    }


    public String getUrl()
    {
        return "sap.fake_url.exportwebservice?wsdl";
    }


    public int getPackageSize()
    {
        return 2;
    }


    public String getWsName()
    {
        return "wWsFakeName";
    }


    public String getWsBindingName()
    {
        return "fakeBindingName";
    }
}
