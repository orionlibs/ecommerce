package de.hybris.e2e.transport;

import com.sap.document.sap.soap.functions.mc_style.BasicAuthenticator;
import de.hybris.e2e.transport.cts.ConfigurationHolder;
import de.hybris.e2e.transport.cts.CtsService;
import de.hybris.e2e.transport.cts.impl.CtsConfigurationHolder;
import de.hybris.e2e.transport.cts.impl.SimpleCtsClient;
import de.hybris.e2e.transport.cts.impl.SimpleCtsService;
import de.hybris.e2e.transport.utils.ConsolePrinter;
import java.net.Authenticator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AntExecutor
{
    private static final Logger LOGGER = Logger.getLogger(AntExecutor.class.getName());
    private static final int ARGSLENGTH = 2;


    public static void main(String[] args)
    {
        try
        {
            FileHandler handler = new FileHandler("../../log/export.log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            handler.setFormatter(simpleFormatter);
            LOGGER.addHandler(handler);
            if(args != null && args.length == 2)
            {
                uploadFiles(args);
                return;
            }
            ConsolePrinter.println("ERROR:Incorrect arguments");
            LOGGER.log(Level.SEVERE, "Incorrect arguments for ANT execution");
        }
        catch(Exception e)
        {
            ConsolePrinter.println("Cannot create log file. Reason is: " + e.getMessage());
            for(StackTraceElement stackTraceElement : e.getStackTrace())
            {
                ConsolePrinter.println(stackTraceElement.toString());
            }
        }
    }


    private static void uploadFiles(String[] args)
    {
        try
        {
            CtsConfigurationHolder holder = new CtsConfigurationHolder(args[0]);
            Authenticator.setDefault((Authenticator)new BasicAuthenticator((ConfigurationHolder)holder));
            SimpleCtsService simpleCtsService = new SimpleCtsService(holder.getUrl(), holder.getWsName(), holder.getWsBindingName());
            SimpleCtsClient client = new SimpleCtsClient((ConfigurationHolder)holder, (CtsService)simpleCtsService, args[1]);
            client.uploadFile();
            return;
        }
        catch(Exception e)
        {
            ConsolePrinter.println("ERROR - Something went wrong while exporting: " + e.getMessage());
            LOGGER.log(Level.SEVERE, e, () -> "Something went wrong while exporting transport package: " + e.getMessage());
            return;
        }
    }
}
