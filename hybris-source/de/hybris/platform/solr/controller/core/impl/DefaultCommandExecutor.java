package de.hybris.platform.solr.controller.core.impl;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.SolrControllerRuntimeException;
import de.hybris.platform.solr.controller.core.CommandBuilder;
import de.hybris.platform.solr.controller.core.CommandExecutor;
import de.hybris.platform.solr.controller.core.CommandOption;
import de.hybris.platform.solr.controller.core.CommandResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public class DefaultCommandExecutor implements CommandExecutor
{
    public CommandResult execute(Collection<CommandBuilder> builders, CommandOption... options)
    {
        ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        processBuilder.redirectErrorStream(true);
        try
        {
            for(CommandBuilder commandBuilder : builders)
            {
                commandBuilder.build(processBuilder);
            }
        }
        catch(SolrControllerException e)
        {
            throw new SolrControllerRuntimeException(e);
        }
        try
        {
            OutputStream outputStream = new ByteArrayOutputStream();
            try
            {
                CommandOptions commandOptions = CommandOptions.parse(options);
                if(!commandOptions.collectOutput)
                {
                    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                }
                Process process = processBuilder.start();
                if(commandOptions.collectOutput)
                {
                    Thread streamCopyThread = new Thread((Runnable)new StreamCopyRunnable(process.getInputStream(), outputStream));
                    streamCopyThread.start();
                    streamCopyThread.join();
                }
                process.waitFor();
                outputStream.flush();
                String output = outputStream.toString();
                DefaultCommandResult commandResult = new DefaultCommandResult();
                commandResult.setOutput(output);
                commandResult.setExitValue(process.exitValue());
                DefaultCommandResult defaultCommandResult1 = commandResult;
                outputStream.close();
                return (CommandResult)defaultCommandResult1;
            }
            catch(Throwable throwable)
            {
                try
                {
                    outputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new SolrControllerRuntimeException("Error while executing command", e);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new SolrControllerRuntimeException("Error while executing command", e);
        }
    }
}
