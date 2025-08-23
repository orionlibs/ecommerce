package de.hybris.platform.mediaconversion.setup;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.impex.jalo.ImpExManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

@SystemSetup(extension = "mediaconversion")
public class SampleData
{
    public static final String SETUP_SAMPLE_DATA = "sampledata";
    public static final String IMPEX_RESOURCE = "/impex/sample_mediaconversion_formats.impex";
    private static final Logger LOG = Logger.getLogger(SampleData.class);


    @SystemSetup(extension = "mediaconversion", process = SystemSetup.Process.ALL, type = SystemSetup.Type.PROJECT)
    public void createData(SystemSetupContext context)
    {
        if(Boolean.parseBoolean(context.getParameter("mediaconversion_sampledata")) ||
                        Boolean.parseBoolean(context.getParameter("mediaconversiontest_sample")))
        {
            LOG.info("Importing 'mediaconversion' sampledata from '/impex/sample_mediaconversion_formats.impex'.");
            InputStream inputstream = SampleData.class.getResourceAsStream("/impex/sample_mediaconversion_formats.impex");
            if(inputstream == null)
            {
                LOG.error("Impex resource '/impex/sample_mediaconversion_formats.impex' is not available.");
                return;
            }
            try
            {
                ImpExManager impexManager = ImpExManager.getInstance();
                impexManager.importData(inputstream, "UTF-8", ';', '"', true);
            }
            finally
            {
                try
                {
                    inputstream.close();
                }
                catch(IOException e)
                {
                    LOG.warn("Failed to close input stream on '/impex/sample_mediaconversion_formats.impex'.", e);
                }
            }
        }
    }


    @SystemSetupParameterMethod(extension = "mediaconversion")
    public List<SystemSetupParameter> getSystemSetupParameters()
    {
        SystemSetupParameter param = new SystemSetupParameter("sampledata");
        param.setLabel("Create sample data");
        param.addValue(Boolean.TRUE.toString());
        param.addValue(Boolean.FALSE.toString(), true);
        return Collections.singletonList(param);
    }
}
