package de.hybris.platform.cockpit.widgets.portal.impl;

import de.hybris.platform.cockpit.widgets.portal.ContainerLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultContainerLayout implements ContainerLayout
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultContainerLayout.class);
    private String id;
    private String i3LabelKey;
    private String previewURL;
    private String widths;
    public static final ContainerLayout ONE_COLUMN = new DefaultContainerLayout("One column", null, "100%");
    public static final ContainerLayout TWO_COLUMN_3070 = new DefaultContainerLayout("Two column, 30-70", null, "30%,70%");
    public static final ContainerLayout TWO_COLUMN_5050 = new DefaultContainerLayout("Two column", null, "50%,50%");
    public static final ContainerLayout THREE_COLUMN = new DefaultContainerLayout("Three column", null, "33%,33%,33%");


    public DefaultContainerLayout()
    {
    }


    public DefaultContainerLayout(String id, String previewURL, String widths)
    {
        this.id = id;
        this.previewURL = previewURL;
        this.widths = widths;
    }


    public void setID(String id)
    {
        this.id = id;
    }


    public void setPreviewURL(String previewURL)
    {
        this.previewURL = previewURL;
    }


    public void setWidths(String widths)
    {
        this.widths = widths;
    }


    public String getID()
    {
        return this.id;
    }


    public String getPreviewURL()
    {
        return this.previewURL;
    }


    public String getWidths()
    {
        return this.widths;
    }


    public int getColumns()
    {
        int ret = 1;
        try
        {
            ret = (this.widths.split(",")).length;
        }
        catch(Exception e)
        {
            LOG.warn("Could not get number of columns, using 1.");
        }
        return ret;
    }


    public String getLabelI3Key()
    {
        return getI3LabelKey();
    }


    public void setI3LabelKey(String i3LabelKey)
    {
        this.i3LabelKey = i3LabelKey;
    }


    public String getI3LabelKey()
    {
        return this.i3LabelKey;
    }
}
