package de.hybris.platform.solrserver.strategies.impl;

import de.hybris.platform.solrserver.SolrServerException;
import de.hybris.platform.solrserver.strategies.SolrServerCommand;
import de.hybris.platform.solrserver.strategies.SolrServerCommandExecutor;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class DefaultSolrServerCommandExecutor implements SolrServerCommandExecutor
{
    private Map<String, SolrServerCommand> internalCommands;


    public Map<String, SolrServerCommand> getInternalCommands()
    {
        return this.internalCommands;
    }


    public void setInternalCommands(Map<String, SolrServerCommand> internalCommands)
    {
        this.internalCommands = internalCommands;
    }


    public void executeCommand(String command, Map<String, String> configuration) throws SolrServerException
    {
        if(command == null || command.isEmpty() || command.contains("."))
        {
            throw new SolrServerException(MessageFormat.format("Invalid command name for command ''{0}''", new Object[] {command}));
        }
        SolrServerCommand internalCommand = (this.internalCommands != null) ? this.internalCommands.get(command) : null;
        if(internalCommand != null)
        {
            internalCommand.execute(configuration);
        }
        else
        {
            executeExternalCommand(command, configuration);
        }
    }


    protected void executeExternalCommand(String command, Map<String, String> configuration) throws SolrServerException
    {
        try
        {
            String solrServerPath = configuration.get("SOLR_SERVER_PATH");
            Path libsPath = Paths.get(solrServerPath, new String[] {"contrib", "hybris", "controller-libs"});
            File[] libs = libsPath.toFile().listFiles(file -> file.getName().toLowerCase(Locale.ROOT).endsWith(".jar"));
            URL[] urls = new URL[libs.length];
            for(int index = 0; index < libs.length; index++)
            {
                urls[index] = libs[index].toURI().toURL();
            }
            URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            try
            {
                String commandClassName = buildExternalCommandClassName(command);
                Class<?> commandClass = classLoader.loadClass(commandClassName);
                Function<Map<String, String>, Integer> solrCommand = commandClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                solrCommand.apply(configuration);
                classLoader.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    classLoader.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | java.lang.reflect.InvocationTargetException | java.io.IOException e)
        {
            throw new SolrServerException(e);
        }
    }


    protected String buildExternalCommandClassName(String command)
    {
        return "de.hybris.platform.solr.controller.commands." + command.substring(0, 1).toUpperCase(Locale.ROOT) + command
                        .substring(1) + "Command";
    }
}
