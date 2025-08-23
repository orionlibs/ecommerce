package de.hybris.platform.cluster.udp;

import de.hybris.platform.cluster.RawMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import org.apache.log4j.Logger;

public class UDPRecorder extends UDPSniffer
{
    private static final Logger LOG = Logger.getLogger(UDPRecorder.class.getName());
    private final FileWriter fileWriter;


    public UDPRecorder(File targetFile)
    {
        try
        {
            this.fileWriter = new FileWriter(targetFile);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    protected void start()
    {
        try
        {
            super.start();
        }
        finally
        {
            if(this.fileWriter != null)
            {
                try
                {
                    this.fileWriter.close();
                }
                catch(Exception e)
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
    }


    protected void showStatus(DatagramPacket datagramPacket, RawMessage message)
    {
        try
        {
            StringBuilder line = new StringBuilder();
            line.append(String.valueOf(System.currentTimeMillis())).append(" ");
            line.append(message).append(" ");
            if(2 == message.getKind() || 1 == message
                            .getKind())
            {
                String text = new String(message.getData());
                line.append(text);
            }
            line.append('\n');
            if(LOG.isDebugEnabled())
            {
                LOG.debug(line.toString());
            }
            this.fileWriter.write(line.toString());
            this.fileWriter.flush();
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
    }


    private static void usage()
    {
        LOG.error("Usage: java UDPRecorder <target_file>");
        System.exit(1);
    }


    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            usage();
        }
        else
        {
            File targetFile = new File(args[0]);
            new UDPRecorder(targetFile);
        }
    }
}
