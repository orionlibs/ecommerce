package de.hybris.datasupplier.services.impl;

import de.hybris.datasupplier.exceptions.DSSenderException;
import de.hybris.datasupplier.services.DSSenderService;
import de.hybris.datasupplier.utils.CmdExecutor;
import de.hybris.platform.util.Config;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultSLDRegSenderService implements DSSenderService
{
    private static final Logger LOG = Logger.getLogger(DefaultSLDRegSenderService.class);
    public static final String SLD_REG_EXE_PATH = "datasupplier.sldreg.exe.cmd";
    public static final String SLD_REG_CONFIG_PATH = "datasupplier.sldreg.config.path";
    public static final String SLD_SEND_SUCCESSFUL_MSG = "datasupplier.sldreg.msg.success";


    public boolean send(String payload) throws DSSenderException
    {
        String filePath = null;
        try
        {
            filePath = createTempFileWithPayload(payload);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Payload has been saved to: " + filePath);
            }
            String sldregCommand = Config.getParameter("datasupplier.sldreg.exe.cmd");
            if(StringUtils.isEmpty(sldregCommand))
            {
                throw new DSSenderException("Sld command parameter datasupplier.sldreg.exe.cmd hasn't been found");
            }
            CmdExecutor cmd = new CmdExecutor(Config.getString("datasupplier.sldreg.msg.success", "Data send status: true"), new String[] {sldregCommand, "-connectfile", getAndCheckThePath("datasupplier.sldreg.config.path"), "-file", filePath});
            boolean succeed = cmd.execute();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Sending payload with sldreg has " + (succeed ? "succeed" : "failed"));
            }
            return succeed;
        }
        finally
        {
            removePayloadFile(filePath);
        }
    }


    protected void removePayloadFile(String path)
    {
        try
        {
            if(!StringUtils.isEmpty(path))
            {
                File tempFile = new File(path);
                if(tempFile.isFile())
                {
                    tempFile.delete();
                }
            }
        }
        catch(Exception e)
        {
            LOG.debug(e);
        }
    }


    protected String createTempFileWithPayload(String payload) throws DSSenderException
    {
        try
        {
            File temp = File.createTempFile("sld_data" + (new Date()).getTime(), ".xml");
            FileWriter fileWriter = new FileWriter(temp);
            try
            {
                fileWriter.write(payload);
                fileWriter.flush();
                fileWriter.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    fileWriter.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            return temp.getAbsolutePath();
        }
        catch(IOException e)
        {
            LOG.debug(e);
            throw new DSSenderException("Cannot generate tmp file with payload");
        }
    }


    protected String getAndCheckThePath(String paramKey) throws DSSenderException
    {
        String sldRegPath = Config.getParameter(paramKey);
        if(sldRegPath == null)
        {
            throw new DSSenderException(paramKey + " hasn't been specified. Please add proper configuration to your local.properties");
        }
        if(!(new File(sldRegPath)).exists())
        {
            throw new DSSenderException(paramKey + " hasn't been found in the following path: " + paramKey);
        }
        return sldRegPath;
    }
}
