package de.hybris.platform.commons.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.log4j.Logger;

public abstract class Formatter extends GeneratedFormatter
{
    private static final Logger log = Logger.getLogger(Formatter.class.getName());


    @ForceJALO(reason = "abstract method implementation")
    public String getScript(SessionContext ctx)
    {
        if(!hasData())
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader r = null;
        try
        {
            r = new BufferedReader(new InputStreamReader(getDataFromStream()));
            boolean first = false;
            String s;
            for(s = r.readLine(); s != null; s = r.readLine())
            {
                if(!first)
                {
                    sb.append("\n");
                }
                else
                {
                    first = false;
                }
                sb.append(s);
            }
            s = sb.toString();
            return s;
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            if(r != null)
            {
                try
                {
                    r.close();
                }
                catch(IOException iOException)
                {
                }
            }
        }
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setScript(SessionContext ctx, String txt)
    {
        if(txt == null)
        {
            try
            {
                removeData(false);
            }
            catch(JaloBusinessException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        else
        {
            try
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(65536);
                OutputStreamWriter wr = new OutputStreamWriter(bos);
                wr.write(txt);
                wr.flush();
                setData(new DataInputStream(new ByteArrayInputStream(bos.toByteArray())), "script.txt", "text");
            }
            catch(IOException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }
}
