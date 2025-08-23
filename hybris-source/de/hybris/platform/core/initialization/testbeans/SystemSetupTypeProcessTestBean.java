package de.hybris.platform.core.initialization.testbeans;

import de.hybris.platform.core.initialization.SystemSetup;

@SystemSetup(extension = "SystemSetupTypeBeanTestExtension")
public class SystemSetupTypeProcessTestBean
{
    public static final String SYSTEM_SETUP_TYPE_PROCESS_TEST_EXTENSION = "SystemSetupTypeBeanTestExtension";
    public static final String ESSENTIAL_INIT = "essentialinit";
    public static final String ESSENTIAL_UPDATE = "essentialupdate";
    public static final String PROJECT_INIT = "projectinit";
    public static final String PROJECT_UPDATE = "projectupdate";


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.INIT)
    public void essentialInit() throws Exception
    {
        throw new Exception("essentialinit");
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void essentialUpdate() throws Exception
    {
        throw new Exception("essentialupdate");
    }


    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.INIT)
    public void projectInit() throws Exception
    {
        throw new Exception("projectinit");
    }


    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.UPDATE)
    public void projectUpdate() throws Exception
    {
        throw new Exception("projectupdate");
    }
}
