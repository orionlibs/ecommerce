package de.hybris.platform.commons.jalo;

import com.google.common.base.Joiner;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.localization.Localization;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil2;
import eu.medsea.mimeutil.detector.ExtensionMimeDetector;
import eu.medsea.mimeutil.detector.MagicMimeMimeDetector;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.configuration.Configuration;
import org.apache.fop.configuration.ConfigurationException;
import org.apache.fop.configuration.DefaultConfigurationBuilder;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

public class FOPFormatter extends GeneratedFOPFormatter
{
    private static final Logger LOG = Logger.getLogger(FOPFormatter.class);
    private static final String FOP_CONFIG_XML_KEY = "fop.config.xml";
    static final String DEFAULT_FOP_CONFIG_CLASSPATH = "classpath:commons/config/fopconfig.xml";
    private static final String VALIDATION_EXCEPTION_LOCALE_KEY = "type.FOPFormatter.validation.exception";
    private static final String FOP_MEDIA_PREFIX = "FO2PDF";
    private static final String TRANSFORMER_VERSION_KEY = "versionParam";
    private static final String TRANSFORMER_VERSION_VALUE = "2.0";
    private static final String STRICT_VALIDATION_KEY = "strict-validation";


    public Media format(Media media)
    {
        Media result = null;
        try
        {
            result = format(media.getDataFromStream());
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Error during formatting media by FOP", (Throwable)e);
        }
        return result;
    }


    public Media format(InputStream inputStream) throws JaloSystemException
    {
        File tempfile = null;
        try
        {
            tempfile = transform(inputStream, getDataFromStream());
            if(!validate(tempfile))
            {
                throw new TransformerException(Localization.getLocalizedString("type.FOPFormatter.validation.exception", new String[] {FOPFormatter.class
                                .getSimpleName()}));
            }
            Media ret = MediaManager.getInstance().createMedia(generateCode());
            ret.setFile(tempfile);
            return ret;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            FileUtils.deleteQuietly(tempfile);
        }
    }


    File createTempFile() throws IOException
    {
        return File.createTempFile("pdffile_temp", ".pdf");
    }


    String getConfigurationKey()
    {
        return Config.getString("fop.config.xml", "classpath:commons/config/fopconfig.xml");
    }


    private String generateCode()
    {
        return Joiner.on("-").join("FO2PDF", Long.valueOf(System.currentTimeMillis()), new Object[0]);
    }


    private FopFactory getFopFactory() throws SAXException, IOException, ConfigurationException
    {
        Configuration configuration = loadConfig(getConfigurationKey());
        FopFactoryBuilder fopFactoryBuilder = (new FopFactoryBuilder((new File(".")).toURI())).setConfiguration(configuration);
        updateStrictValidationFlag(configuration, fopFactoryBuilder);
        FopFactory fopFactory = fopFactoryBuilder.build();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Using FOP config from configuration [" + getConfigurationKey() + "]");
        }
        return fopFactory;
    }


    private void updateStrictValidationFlag(Configuration configuration, FopFactoryBuilder fopFactoryBuilder)
    {
        Configuration strictValidationCfg = configuration.getChild("strict-validation", false);
        if(strictValidationCfg != null)
        {
            fopFactoryBuilder.setStrictFOValidation(strictValidationCfg
                            .getValueAsBoolean(true));
        }
    }


    File transform(InputStream schemaSourceInputStream, InputStream mediaSourceInputStream)
    {
        try
        {
            FopFactory fopFactory = getFopFactory();
            File temporaryFile = createTempFile();
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            OutputStream out = null;
            try
            {
                Fop fop = fopFactory.newFop("application/pdf", foUserAgent, out = new BufferedOutputStream(new FileOutputStream(temporaryFile)));
                TransformerFactory factory = Utilities.getTransformerFactory();
                Transformer transformer = factory.newTransformer(new StreamSource(mediaSourceInputStream));
                transformer.setParameter("versionParam", "2.0");
                Source src = new StreamSource(schemaSourceInputStream);
                Result res = new SAXResult(fop.getDefaultHandler());
                transformer.transform(src, res);
                return temporaryFile;
            }
            finally
            {
                out.close();
            }
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected Configuration loadConfig(String fopConfigXml) throws ConfigurationException, SAXException, IOException
    {
        if(fopConfigXml.startsWith("classpath:"))
        {
            return (Configuration)(new DefaultConfigurationBuilder()).build(getClass().getClassLoader().getResourceAsStream(fopConfigXml
                            .substring("classpath:".length())));
        }
        return (Configuration)(new DefaultConfigurationBuilder()).buildFromFile(ResourceUtils.getFile(fopConfigXml));
    }


    protected boolean validate(File temporaryFile) throws IllegalArgumentException, IOException
    {
        if(temporaryFile == null)
        {
            throw new IllegalArgumentException("Validated pdf file path shouldn't be null");
        }
        MimeUtil2 mimeUtil = new MimeUtil2();
        mimeUtil.registerMimeDetector(ExtensionMimeDetector.class.getName());
        mimeUtil.registerMimeDetector(MagicMimeMimeDetector.class.getName());
        for(Object recognizedMime : mimeUtil.getMimeTypes(temporaryFile))
        {
            MimeType mime = (MimeType)recognizedMime;
            if("application/pdf".equalsIgnoreCase(mime.toString()))
            {
                return true;
            }
        }
        return false;
    }
}
