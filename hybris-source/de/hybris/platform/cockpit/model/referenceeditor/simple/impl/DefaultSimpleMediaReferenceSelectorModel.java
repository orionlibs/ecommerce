package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.media.MediaInfo;
import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.media.MediaModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;

public class DefaultSimpleMediaReferenceSelectorModel extends DefaultSimpleReferenceSelectorModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSimpleMediaReferenceSelectorModel.class);
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String EXTRA_PARAM_PREVENT_CACHING = "cimgnr";
    private MediaInfoService mediaInfoService;
    private boolean fallbackIconEnabled = true;


    public DefaultSimpleMediaReferenceSelectorModel(ObjectType rootType)
    {
        super(rootType);
    }


    public String getDownloadUrlCachingFree(Object object)
    {
        String regularDownloadUrl = "";
        if(isWebMedia(object).booleanValue())
        {
            regularDownloadUrl = getDownloadUrl(object);
            if(StringUtils.isNotEmpty(regularDownloadUrl))
            {
                if(regularDownloadUrl.contains("?"))
                {
                    regularDownloadUrl = regularDownloadUrl + "&cimgnr=" + regularDownloadUrl;
                }
                else
                {
                    regularDownloadUrl = regularDownloadUrl + "?cimgnr=" + regularDownloadUrl;
                }
            }
        }
        else
        {
            MediaInfo nonWebMediaInfo = getMediaInfoService().getNonWebMediaInfo(getMime(object));
            if(nonWebMediaInfo != null)
            {
                regularDownloadUrl = nonWebMediaInfo.getIcon();
            }
            else if(this.fallbackIconEnabled)
            {
                regularDownloadUrl = getMediaInfoService().getFallbackIcon();
            }
        }
        return regularDownloadUrl;
    }


    protected Boolean isWebMedia(Object object)
    {
        Boolean result = Boolean.FALSE;
        if(object instanceof TypedObject)
        {
            Object item = ((TypedObject)object).getObject();
            if(item instanceof MediaModel)
            {
                result = getMediaInfoService().isWebMedia((MediaModel)item);
            }
        }
        return result;
    }


    protected String getMime(Object object)
    {
        String mime = null;
        if(object instanceof TypedObject)
        {
            Object item = ((TypedObject)object).getObject();
            if(item instanceof MediaModel)
            {
                mime = ((MediaModel)item).getMime();
            }
        }
        return mime;
    }


    protected String getDownloadUrl(Object object)
    {
        String downloadUrl = "";
        if(object instanceof TypedObject)
        {
            Object item = ((TypedObject)object).getObject();
            if(item instanceof MediaModel)
            {
                if(!TypeTools.getModelService().isRemoved(item))
                {
                    try
                    {
                        TypeTools.getModelService().refresh(item);
                        MediaModel media = (MediaModel)item;
                        downloadUrl = media.getDownloadURL();
                    }
                    catch(Exception e)
                    {
                        LOG.error("Could not retrieve media URL", e);
                    }
                }
            }
        }
        return downloadUrl;
    }


    public String getMnemonic(Object object)
    {
        String label = "";
        if(object instanceof TypedObject)
        {
            CatalogVersionModel catalogVersion = UISessionUtils.getCurrentSession().getSystemService().getCatalogVersion((TypedObject)object);
            if(catalogVersion != null)
            {
                label = catalogVersion.getMnemonic();
            }
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can not get mnemonic for item since it is not a TypedObject.");
            }
            if(object != null)
            {
                label = object.toString();
            }
            else
            {
                return "";
            }
        }
        return StringUtils.isBlank(label) ? "" : ("(" + label + ")");
    }


    protected MediaInfoService getMediaInfoService()
    {
        if(this.mediaInfoService == null)
        {
            this.mediaInfoService = (MediaInfoService)SpringUtil.getBean("mediaInfoService");
        }
        return this.mediaInfoService;
    }


    public boolean isFallbackIconEnabled()
    {
        return this.fallbackIconEnabled;
    }


    public void setFallbackIconEnabled(boolean fallbackIconEnabled)
    {
        this.fallbackIconEnabled = fallbackIconEnabled;
    }
}
