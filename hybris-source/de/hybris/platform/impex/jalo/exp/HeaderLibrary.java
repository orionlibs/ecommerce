package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.type.ComposedType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class HeaderLibrary extends GeneratedHeaderLibrary
{
    private static final Logger log = Logger.getLogger(HeaderLibrary.class.getName());
    private Map<String, String> library = null;


    protected String getHeader(ComposedType ct, String fieldseparator)
    {
        if(this.library == null)
        {
            this.library = new HashMap<>();
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getDataFromStreamSure()));
                String line;
                while((line = reader.readLine()) != null)
                {
                    int ix = line.indexOf(fieldseparator);
                    if(ix != -1)
                    {
                        String tc = line.substring(0, ix).trim().toLowerCase();
                        int _ix = tc.indexOf(ImpExConstants.Syntax.Mode.INSERT);
                        if(_ix == -1)
                        {
                            _ix = tc.indexOf(ImpExConstants.Syntax.Mode.INSERT_UPDATE + " ");
                        }
                        if(_ix == -1)
                        {
                            _ix = tc.indexOf(ImpExConstants.Syntax.Mode.INSERT_UPDATE + " ");
                        }
                        if(_ix == -1)
                        {
                            _ix = tc.indexOf(ImpExConstants.Syntax.Mode.REMOVE + " ");
                        }
                        if(_ix == -1)
                        {
                            _ix = tc.indexOf(ImpExConstants.Syntax.Mode.UPDATE + " ");
                        }
                        if(_ix != -1)
                        {
                            _ix = tc.indexOf(" ");
                            tc = tc.substring(_ix).trim();
                        }
                        String attribs = line.substring(ix + 1, line.length());
                        this.library.put(tc.trim().toLowerCase(), attribs);
                    }
                }
            }
            catch(JaloBusinessException e)
            {
                log.error("Error while reading from media with code " + getCode() + ": " + e.getMessage(), (Throwable)e);
            }
            catch(IOException e)
            {
                log.error("Error while reading from media with code " + getCode() + ": " + e.getMessage(), e);
            }
        }
        return this.library.get(ct.getCode().toLowerCase());
    }


    public String getHeader(ComposedType ct, String fieldseparator, boolean useSuperType)
    {
        String header = getHeader(ct, fieldseparator);
        if(header == null)
        {
            List<ComposedType> supertypes = ct.getAllSuperTypes();
            for(ComposedType supertype : supertypes)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("checking supertype: " + supertype.getCode());
                }
                header = getHeader(supertype, fieldseparator);
                if(header != null)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug("matching supertype '" + supertype.getCode() + "' found!");
                    }
                    return header;
                }
            }
        }
        return header;
    }
}
