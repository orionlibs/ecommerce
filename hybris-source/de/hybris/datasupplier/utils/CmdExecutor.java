package de.hybris.datasupplier.utils;

import de.hybris.datasupplier.exceptions.DSCommandExecutionException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CmdExecutor
{
    private static final Logger LOG = Logger.getLogger(CmdExecutor.class);
    private static final String CANNOT_RUN = "Cannot run command";
    private final String confirmationMessage;
    private final List<String> commands = new ArrayList<>();


    public CmdExecutor(String confirmationMessage, String... cmds)
    {
        this.confirmationMessage = confirmationMessage;
        for(String tmpCmd : cmds)
        {
            if(!StringUtils.isEmpty(tmpCmd))
            {
                this.commands.add(tmpCmd);
            }
        }
    }


    public boolean execute()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("executing command: " + this.commands);
        }
        try
        {
            ProcessBuilder builder = getProcess(this.commands);
            Process p = builder.start();
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(line);
                }
                sb.append(line);
                sb.append('\n');
            }
            boolean sendStatus = sb.toString().contains(this.confirmationMessage);
            if(!sendStatus && !LOG.isDebugEnabled())
            {
                LOG.error(sb.toString());
            }
            return sb.toString().contains(this.confirmationMessage);
        }
        catch(DSCommandExecutionException | java.io.IOException e)
        {
            LOG.error("Cannot run command", e);
        }
        catch(InterruptedException e)
        {
            LOG.error("Cannot run command", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }


    private static ProcessBuilder getProcess(List<String> commandList) throws DSCommandExecutionException
    {
        if(!commandList.isEmpty() && ((String)commandList.get(0)).length() > 5)
        {
            String tmpCmd = commandList.get(0);
            if(tmpCmd.contains("sldreg"))
            {
                ProcessBuilder builder = new ProcessBuilder(commandList);
                return builder;
            }
            throw new DSCommandExecutionException("First parameter should containt url to sldreg file.");
        }
        throw new DSCommandExecutionException("Invalid command.");
    }
}
