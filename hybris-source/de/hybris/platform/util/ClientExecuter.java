package de.hybris.platform.util;

import bsh.ConsoleInterface;
import bsh.EvalError;
import bsh.Interpreter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;

public class ClientExecuter
{
    public static void execute(String toexecute, boolean loadHybris, boolean systemInit) throws EvalError
    {
        RegistrableThread.registerThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).asNotSuspendableOperation().build());
        try
        {
            if(loadHybris)
            {
                Registry.activateStandaloneMode();
                if(systemInit)
                {
                    Registry.activateMasterTenantForInit();
                }
                else
                {
                    Registry.activateMasterTenant();
                }
            }
            Interpreter bsh = new Interpreter((ConsoleInterface)new MyConsoleInterface());
            bsh.setExitOnEOF(true);
            bsh.eval(toexecute.replace("\\", "\\\\"));
        }
        finally
        {
            RegistrableThread.unregisterThread();
        }
    }
}
