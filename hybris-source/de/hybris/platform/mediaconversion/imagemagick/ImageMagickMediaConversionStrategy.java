package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.conversion.ConvertedMediaCreationStrategy;
import de.hybris.platform.mediaconversion.conversion.MediaConversionException;
import de.hybris.platform.mediaconversion.conversion.MediaConversionStrategy;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.media.MediaIOException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.NoDataAvailableException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringTokenizer;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ImageMagickMediaConversionStrategy extends BasicImageMagickStrategy implements MediaConversionStrategy
{
    private static final Logger LOG = Logger.getLogger(ImageMagickMediaConversionStrategy.class);
    public static final String CONVERSION_INPUT_TOKEN = "{input}";
    public static final String CONVERSION_OUTPUT_TOKEN = "{output}";
    public static final String CONVERSION_ADDON_TOKEN = "{addOn#X}";
    private static final String CONVERSION_ADDON_TOKEN_SEARCHPATTERN = "{addOn#";
    private static final Pattern CONVERSION_SPLIT_PATTERN = Pattern.compile(" ");
    private static final Pattern FILEEXTENSION_PATTERN = Pattern.compile("\\.([a-z]+)(?:[?;].*)?$", 2);
    private ImageMagickService imageMagickService;
    private MimeMappingStrategy mimeMappingStrategy;
    private ConvertedMediaCreationStrategy convertedMediaCreationStrategy;
    private MediaService mediaService;


    public MediaModel convert(MediaConversionService mediaConversionService, MediaModel input, ConversionMediaFormatModel format) throws MediaConversionException
    {
        ServicesUtil.validateParameterNotNull(mediaConversionService, "MediaConversionService must not be null.");
        ServicesUtil.validateParameterNotNull(input, "Input media model must not be null.");
        ServicesUtil.validateParameterNotNull(format, "Conversion media format model must not be null.");
        try
        {
            String fileExt = targetFileExtension(format, input);
            File tmpFile = File.createTempFile("tmp_", "." + fileExt, getTmpDir());
            try
            {
                convert(mediaConversionService, retrieveFile(input), tmpFile, format);
                return create(input, format, tmpFile);
            }
            finally
            {
                if(!tmpFile.delete())
                {
                    LOG.warn("Failed to delete temporary file '" + tmpFile + "'.");
                }
            }
        }
        catch(IOException e)
        {
            throw new MediaConversionException("Failed to run imagemagick.", e);
        }
    }


    private MediaModel create(MediaModel input, ConversionMediaFormatModel format, File tmpFile)
    {
        MediaModel mediaModel = null;
        try
        {
            InputStream inputStream = new FileInputStream(tmpFile);
            try
            {
                mediaModel = getConvertedMediaCreationStrategy().createOrUpdate(input, format, inputStream);
                inputStream.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    inputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(FileNotFoundException e)
        {
            throw new MediaIOException("The specified file '" + tmpFile + "' could not be found.", e);
        }
        catch(IOException e)
        {
            LOG.warn("Failed to close input stream on '" + tmpFile.getAbsolutePath() + "'.", e);
        }
        return mediaModel;
    }


    protected void convert(MediaConversionService mediaConversionService, File input, File target, ConversionMediaFormatModel format) throws IOException
    {
        List<String> command = buildCommand(mediaConversionService, input, target, format);
        getImageMagickService().convert(command);
    }


    protected String targetFileExtension(ConversionMediaFormatModel format, MediaModel input) throws IOException, MediaConversionException
    {
        String ret = targetFileExtension(format);
        if(ret == null && input.getMime() != null)
        {
            ret = getMimeMappingStrategy().fileExtensionForMimeType(input.getMime());
        }
        if(ret == null)
        {
            ret = extractFileExtension(input.getURL());
        }
        if(ret == null)
        {
            throw new MediaConversionException("Target file extension could neither be computed from input's mime '" + input
                            .getMime() + "' nor url '" + input.getURL() + "'.");
        }
        return ret;
    }


    static String extractFileExtension(String url)
    {
        if(url == null)
        {
            return null;
        }
        Matcher matcher = FILEEXTENSION_PATTERN.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }


    private String targetFileExtension(ConversionMediaFormatModel format) throws IOException, MediaConversionException
    {
        String mime = format.getMimeType();
        if(mime == null)
        {
            return null;
        }
        String ret = getMimeMappingStrategy().fileExtensionForMimeType(mime);
        if(ret == null)
        {
            throw new MediaConversionException("No file extension could be found for mime type '" + mime + "'.");
        }
        return ret;
    }


    protected List<String> buildCommand(MediaConversionService mediaConversionService, File input, File output, ConversionMediaFormatModel format) throws IOException
    {
        return buildCommand(mediaConversionService, input.getAbsolutePath(), output.getAbsolutePath(), format);
    }


    protected List<String> buildCommand(MediaConversionService mediaConversionService, String input, String output, ConversionMediaFormatModel format) throws IOException
    {
        boolean addInput = true;
        boolean addOutput = true;
        List<String> ret = new LinkedList<>();
        if(format.getConversion() != null && !format.getConversion().isEmpty())
        {
            StringTokenizer st = new StringTokenizer(format.getConversion());
            StringMatcher sm = StringMatcherFactory.INSTANCE.quoteMatcher();
            st.setQuoteMatcher(sm);
            for(String part : st.getTokenArray())
            {
                if(part != null && !part.isEmpty())
                {
                    String partInput = part.replace("{input}", input);
                    addInput &= part.equals(partInput);
                    String partOutput = partInput.replace("{output}", output);
                    addOutput &= partOutput.equals(partInput);
                    ret.add(replaceAddOns(mediaConversionService, partOutput, format));
                }
            }
        }
        if(addOutput)
        {
            ret.add(output);
        }
        if(addInput)
        {
            ret.add(0, input);
        }
        return ret;
    }


    protected String replaceAddOns(MediaConversionService mediaConversionService, String commandPart, ConversionMediaFormatModel format) throws IOException
    {
        if(format.getMediaAddOns() == null || format.getMediaAddOns().isEmpty() ||
                        !commandPart.contains("{addOn#"))
        {
            return commandPart;
        }
        String ret = commandPart;
        int index = 0;
        Iterator<MediaModel> iterator = format.getMediaAddOns().iterator();
        while(iterator.hasNext() && ret.contains("{addOn#"))
        {
            index++;
            String replace = "{addOn#X}".replace("X",
                            Integer.toString(index));
            MediaModel media = iterator.next();
            if(media == null)
            {
                LOG.warn("MediaAddOn contains null entry.");
                continue;
            }
            if(ret.contains(replace))
            {
                File addOnFile = retrieveFile(media);
                ret = ret.replace(replace, addOnFile.getAbsolutePath());
            }
        }
        return ret;
    }


    public File retrieveFile(MediaModel media) throws IOException
    {
        NoDataAvailableException noDataAvailableException;
        Exception cause = null;
        try
        {
            Collection<File> files = getMediaService().getFiles(media);
            for(File f : files)
            {
                if(f.isFile() && f.canRead())
                {
                    return f;
                }
            }
        }
        catch(NoDataAvailableException e)
        {
            noDataAvailableException = e;
        }
        throw new IOException("Cannot access media '" + media + "'. Data is not locally available.", noDataAvailableException);
    }


    public MimeMappingStrategy getMimeMappingStrategy()
    {
        return this.mimeMappingStrategy;
    }


    @Required
    public void setMimeMappingStrategy(MimeMappingStrategy mimeMappingStrategy)
    {
        this.mimeMappingStrategy = mimeMappingStrategy;
    }


    public ConvertedMediaCreationStrategy getConvertedMediaCreationStrategy()
    {
        return this.convertedMediaCreationStrategy;
    }


    @Required
    public void setConvertedMediaCreationStrategy(ConvertedMediaCreationStrategy service)
    {
        this.convertedMediaCreationStrategy = service;
    }


    public ImageMagickService getImageMagickService()
    {
        return this.imageMagickService;
    }


    @Required
    public void setImageMagickService(ImageMagickService magickService)
    {
        this.imageMagickService = magickService;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
