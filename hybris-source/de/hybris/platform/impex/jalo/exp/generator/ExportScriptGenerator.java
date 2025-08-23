package de.hybris.platform.impex.jalo.exp.generator;

import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import java.io.IOException;
import java.util.Locale;
import org.apache.log4j.Logger;

public class ExportScriptGenerator extends AbstractScriptGenerator
{
    private static final Logger log = Logger.getLogger(ExportScriptGenerator.class.getName());


    protected void writeScript() throws IOException
    {
        writeComment(" -------------------------------------------------------");
        writeComment("# used 'header validation mode' during script generation was: " +
                        ImpExManager.getImportStrictMode().getCode());
        Locale thisLocale = JaloSession.getCurrentSession().getSessionContext().getLanguage().getLocale();
        writeBeanShell("impex.setLocale( new Locale( \"" + thisLocale.getLanguage() + "\" , \"" + thisLocale.getCountry() + "\" ) )");
        writeComment(" -------------------------------------------------------");
        for(ComposedType type : getTypes())
        {
            if(log.isDebugEnabled())
            {
                log.debug("generating script statements for type " + type.getCode());
            }
            getScriptWriter().writeSrcLine("");
            writeComment("---- Extension: " + type.getExtensionName() + " ---- Type: " + type.getCode() + " ----");
            writeTargetFileStatement(type, ".csv");
            writeHeader(type);
            writeExportStatement(type, false);
        }
    }


    protected void writeTargetFileStatement(ComposedType type, String fileType) throws IOException
    {
        if(TypeManager.getInstance().getComposedType(UserRight.class).isAssignableFrom((Type)type))
        {
            writeBeanShell("impex.setTargetFile( \"" + type.getCode() + fileType + "\" , true, 0, 0 )");
        }
        else
        {
            writeBeanShell("impex.setTargetFile( \"" + type.getCode() + fileType + "\" )");
        }
    }


    protected void writeExportStatement(ComposedType type, boolean inclSubtypes) throws IOException
    {
        writeBeanShell("impex.exportItems( \"" + type.getCode() + "\" , " + inclSubtypes + " )");
    }
}
