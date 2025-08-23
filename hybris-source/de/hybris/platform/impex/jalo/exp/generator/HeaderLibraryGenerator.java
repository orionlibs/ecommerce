package de.hybris.platform.impex.jalo.exp.generator;

import de.hybris.platform.jalo.type.ComposedType;
import java.io.IOException;
import org.apache.log4j.Logger;

public class HeaderLibraryGenerator extends AbstractScriptGenerator
{
    private static final Logger log = Logger.getLogger(HeaderLibraryGenerator.class.getName());


    public void writeScript() throws IOException
    {
        writeComment(" -------------------------------------------------------");
        writeComment(" Generated header library");
        writeComment(" -------------------------------------------------------");
        getScriptWriter().writeSrcLine("");
        for(ComposedType type : getTypes())
        {
            if(log.isDebugEnabled())
            {
                log.debug("generating script statements for type " + type.getCode());
            }
            writeHeader(type);
        }
    }


    protected String generateFirstHeaderColumn(ComposedType type, boolean hasUniqueColumns)
    {
        return type.getCode();
    }
}
