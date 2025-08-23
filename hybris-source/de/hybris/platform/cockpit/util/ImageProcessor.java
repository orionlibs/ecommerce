package de.hybris.platform.cockpit.util;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.FileLoadDescriptor;
import javax.media.jai.operator.FileStoreDescriptor;
import javax.media.jai.operator.ScaleDescriptor;
import javax.media.jai.operator.ShearDescriptor;
import javax.media.jai.util.ImagingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageProcessor
{
    static
    {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }

    private static final Logger LOG = LoggerFactory.getLogger(ImageProcessor.class);
    private final ImagingListener silentImagingListener = (ImagingListener)new MyImagingListener();
    private int maxX = 100;
    private int maxY = 100;
    private static final float shear = -0.4F;
    private static final float X_SLIDE = 10.0F;
    private static final float Y_SLIDE = 20.0F;


    public File scaleImage(File source, File target, int maxWidth, int maxHeight, boolean fillSpace)
    {
        JAI.getDefaultInstance().setImagingListener(this.silentImagingListener);
        File result = target;
        if(maxWidth == 0 && maxHeight == 0)
        {
            result = source;
            return result;
        }
        if(!target.getName().contains("."))
        {
            target = new File(target.getPath() + "/" + target.getPath());
        }
        target.getParentFile().mkdirs();
        boolean createTarget = true;
        Rectangle bounds = null;
        if(target.exists())
        {
            RenderedOp opLoadTarget = null;
            try
            {
                opLoadTarget = JAI.create("fileload", target.toString());
            }
            catch(Exception e)
            {
                LOG.error("An error occurred while scaling thumbnail (Reason: " + e.getMessage() + ")");
            }
            bounds = opLoadTarget.getBounds();
            createTarget = !isInBounds(maxWidth, maxHeight, bounds.width, bounds.height);
        }
        if(createTarget)
        {
            RenderedOp opLoadSource = JAI.create("fileload", source.toString());
            bounds = opLoadSource.getBounds();
            RenderedOp opTarget = opLoadSource;
            if(!isInBounds(maxWidth, maxHeight, (opLoadSource.getBounds()).width, (opLoadSource.getBounds()).height))
            {
                float scale = 0.0F;
                if(maxWidth > 0)
                {
                    scale = maxWidth / bounds.width;
                    float _height = bounds.height * scale;
                    if(maxHeight > 0 && _height > maxHeight)
                    {
                        scale = scale * maxHeight / _height;
                    }
                }
                else if(maxHeight > 0)
                {
                    scale = maxHeight / bounds.height;
                }
                else
                {
                    throw new IllegalArgumentException("Invalid scale properties (0,0)");
                }
                ParameterBlock rParam2 = new ParameterBlock();
                rParam2.addSource(opLoadSource);
                rParam2.add(scale);
                rParam2.add(scale);
                rParam2.add(0.0F);
                rParam2.add(0.0F);
                rParam2.add(new InterpolationBicubic(5));
                RenderedOp opScaled = JAI.create("scale", rParam2);
                opTarget = opScaled;
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("COPY&SCALE: " + source.toString() + " (" + bounds.width + "x" + bounds.height + " ) to " + (int)(bounds.width * scale) + "x" + (int)(bounds.height * scale) + "; full processing");
                }
            }
            else
            {
                LOG.debug("NOP: " + source.toString() + " (" + bounds.width + "x" + bounds.height + "); no scaling needed");
                createTarget = false;
                result = source;
            }
            if(createTarget)
            {
                ParameterBlock rParam3 = new ParameterBlock();
                rParam3.addSource(opTarget);
                rParam3.add(target.toString());
                rParam3.add("JPEG");
                JAI.create("filestore", rParam3);
            }
        }
        else
        {
            LOG
                            .debug("SKIP: " + target.toString() + " (" + bounds.width + "x" + bounds.height + "); already copied and well scaled");
        }
        return result;
    }


    public void processImageFiles(List<File> sourceFiles, File targetFile, int maxWidth, int maxHeight)
    {
        this.maxX = (int)((maxWidth - 10.0F * (1 - sourceFiles.size())) / Math.abs(-0.4F));
        this.maxY = maxHeight;
        List<ProcessResult> thumbs = new ArrayList<>();
        for(File file : sourceFiles)
        {
            ProcessResult thumb = processImageFile(file);
            thumbs.add(thumb);
            Rectangle rect = thumb.roi.getBounds();
            if(Math.abs(rect.y) + rect.height > this.maxY)
            {
                this.maxY = Math.abs(rect.y) + rect.height;
            }
        }
        SampleModel sampleModel = ((ProcessResult)thumbs.get(0)).thumbnail.getSampleModel();
        ColorModel colorModel = ((ProcessResult)thumbs.get(0)).thumbnail.getColorModel();
        TiledImage targetImage = new TiledImage(0, 0, maxWidth, maxHeight, 0, 0, sampleModel, colorModel);
        float x_trans = 0.0F;
        ParameterBlockJAI parameterBlock = new ParameterBlockJAI("translate");
        for(ProcessResult thumb : thumbs)
        {
            parameterBlock.setParameter("xTrans", x_trans);
            parameterBlock.setParameter("yTrans", 0.0F);
            parameterBlock.setSource(thumb.thumbnail, 0);
            RenderedOp renderedOp = JAI.create("translate", (ParameterBlock)parameterBlock);
            ROI roi = thumb.roi.performImageOp("translate", (ParameterBlock)parameterBlock, 0, null);
            targetImage.setData(renderedOp.getData(), roi);
            x_trans += 10.0F;
        }
        FileStoreDescriptor.create((RenderedImage)targetImage, targetFile.toString(), "JPEG", null, null, null);
    }


    private ProcessResult processImageFile(File file)
    {
        RenderedOp renderedOp = FileLoadDescriptor.create(file.toString(), null, null, null);
        double scaling = getScaleFactor((PlanarImage)renderedOp, this.maxX, this.maxY);
        renderedOp = ScaleDescriptor.create((RenderedImage)renderedOp, Float.valueOf((float)scaling), Float.valueOf((float)scaling), Float.valueOf(0.0F),
                        Float.valueOf(0.0F), null, null);
        ROIShape rOIShape = new ROIShape(renderedOp.getBounds());
        ParameterBlockJAI parameterBlock1 = new ParameterBlockJAI("shear");
        parameterBlock1.setParameter("shear", -0.4F);
        parameterBlock1.setParameter("shearDir", ShearDescriptor.SHEAR_VERTICAL);
        parameterBlock1.setParameter("yTrans", 20.0F);
        ROI rOI = rOIShape.performImageOp("shear", (ParameterBlock)parameterBlock1, 0, null);
        Rectangle rect = rOI.getBounds();
        TiledImage thumbnail = new TiledImage(0, 0, rect.width, rect.height + 10, 0, 0, renderedOp.getSampleModel(), renderedOp.getColorModel());
        thumbnail.setData(renderedOp.getData());
        parameterBlock1.setSource(thumbnail, 0);
        parameterBlock1.setParameter("interpolation", Interpolation.getInstance(2));
        renderedOp = JAI.create("shear", (ParameterBlock)parameterBlock1);
        ProcessResult result = new ProcessResult();
        result.roi = rOI;
        result.thumbnail = renderedOp;
        return result;
    }


    private double getScaleFactor(PlanarImage image, int maxWidth, int maxHeight)
    {
        Rectangle bounds = image.getBounds();
        float scale = 0.0F;
        if(maxWidth > 0)
        {
            scale = maxWidth / bounds.width;
            float _height = bounds.height * scale;
            if(maxHeight > 0 && _height > maxHeight)
            {
                scale = scale * maxHeight / _height;
            }
        }
        else if(maxHeight > 0)
        {
            scale = maxHeight / bounds.height;
        }
        else
        {
            throw new IllegalArgumentException("Invalid scale properties (0,0)");
        }
        return scale;
    }


    private boolean isInBounds(int maxWidth, int maxHeight, int width, int height)
    {
        boolean result = true;
        result = ((width == maxWidth && maxHeight >= 0 && height <= maxHeight) || (height == maxHeight && maxWidth >= 0 && width <= maxWidth));
        return result;
    }
}
