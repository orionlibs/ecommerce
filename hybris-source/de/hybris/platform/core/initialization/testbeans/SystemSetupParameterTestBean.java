package de.hybris.platform.core.initialization.testbeans;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import java.util.ArrayList;
import java.util.List;

@SystemSetup(extension = "SystemSetupParameterTestExtension")
public class SystemSetupParameterTestBean
{
    public static final String SYSTEM_SETUP_PARAMETER_TEST_EXTENSION = "SystemSetupParameterTestExtension";
    public static final String TEST_PARAMETER_KEY = "key";
    public static final String TEST_PARAMETER_VALUE1 = "something";
    public static final String TEST_PARAMETER_VALUE2 = "default!!!";
    public static final String TEST_PARAMETER_VALUE3 = "else";


    @SystemSetup(type = SystemSetup.Type.ALL, process = SystemSetup.Process.ALL)
    public void parameterTest(SystemSetupContext context) throws Exception
    {
        throw new Exception(context.getParameter("key"));
    }


    @SystemSetupParameterMethod(extension = "SystemSetupParameterTestExtension")
    public List<SystemSetupParameter> parameterMethod()
    {
        List<SystemSetupParameter> parameters = new ArrayList<>();
        SystemSetupParameter parameter = new SystemSetupParameter("key");
        parameter.addValue("something");
        parameter.addValue("default!!!", true);
        parameter.addValue("else", true);
        parameters.add(parameter);
        return parameters;
    }
}
