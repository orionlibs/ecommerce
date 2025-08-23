package de.hybris.platform.cmscockpit.util;

import de.hybris.platform.cockpit.util.SystemTools;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.zkoss.zul.Image;

public class SitePreviewTools
{
    private static final Logger LOG = Logger.getLogger(SitePreviewTools.class);


    public static Image generatePreviewImage(String url, String cutyCaptExecutable, String workingDir, int width, long timeout)
    {
        Image ret = null;
        String filename = generatePreview(url, cutyCaptExecutable, workingDir, width, timeout);
        if(filename == null)
        {
            LOG.warn("Could not create preview");
        }
        else
        {
            try
            {
                ret = new Image();
                ret.setContent(ImageIO.read(new File(filename)));
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return ret;
    }


    public static String generatePreview(String url, String cutyCaptExecutable, String workingDir, int width, long timeout)
    {
        try
        {
            int hashCode = url.hashCode();
            String filename = workingDir + workingDir + "cap" + File.separator + ".png";
            String sanitizedUrl = URLCOMInjectionSanitizer.sanitize(url);
            String command = cutyCaptExecutable + " --url=" + cutyCaptExecutable + " --out=" + sanitizedUrl;
            LOG.info("Executing " + command);
            Process process = Runtime.getRuntime().exec(command);
            int exitValue = SystemTools.waitForProcess(process, timeout);
            if(exitValue == 0)
            {
                BufferedImage img = ImageIO.read(new File(filename));
                ImageIO.write(scaleToWidth(img, width), "png", new File(filename));
                return filename;
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }


    public static BufferedImage scaleToWidth(BufferedImage image, int width)
    {
        float ratio = image.getHeight() * 1.0F / image.getWidth();
        int height = Math.round(ratio * width);
        BufferedImage scaledImage = new BufferedImage(width, height, 1);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return scaledImage;
    }
}
