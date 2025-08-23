package de.hybris.platform.cockpit.model.referenceeditor.collection.model.media;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleMediaReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.services.media.MediaInfo;
import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;

public class DefaultMediaCollectionEditorModel extends DefaultCollectionEditorModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMediaCollectionEditorModel.class);
    private static final String LEFT_PARENTHESIS = "( ";
    private static final String RIGHT_PARENTHESIS = " )";
    private static final String EXTRA_PARAM_PREVENT_CACHING = "cimgnr";
    private MediaInfoService mediaInfoService;
    private ModelService modelService;


    public DefaultMediaCollectionEditorModel(ObjectType rootType)
    {
        super(rootType);
    }


    public String getDownloadUrlCachingFree(Object object)
    {
        String regularDownloadUrl;
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
                if(!UITools.isUrlAbsolute(regularDownloadUrl))
                {
                    regularDownloadUrl = ".." + regularDownloadUrl;
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
            else
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
                try
                {
                    MediaModel media = (MediaModel)item;
                    downloadUrl = media.getDownloadURL();
                }
                catch(Exception e)
                {
                    LOG.error("Could not retrieve media URL", e);
                }
            }
        }
        return downloadUrl;
    }


    protected MediaInfoService getMediaInfoService()
    {
        if(this.mediaInfoService == null)
        {
            this.mediaInfoService = (MediaInfoService)SpringUtil.getBean("mediaInfoService");
        }
        return this.mediaInfoService;
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    protected void initializeReferenceSelectorModel(ObjectType rootType)
    {
        DefaultSimpleMediaReferenceSelectorModel internalModel = new DefaultSimpleMediaReferenceSelectorModel(rootType);
        internalModel.setFallbackIconEnabled(false);
        internalModel.setRootSearchType(getRootSearchType());
        this.referenceSelectorModel = (DefaultSimpleReferenceSelectorModel)internalModel;
    }


    public String getMnemonic(Object object)
    {
        String label = "";
        if(object instanceof TypedObject)
        {
            Object item = ((TypedObject)object).getObject();
            if(item instanceof de.hybris.platform.core.model.ItemModel)
            {
                CatalogVersion catalogVersion = CatalogManager.getInstance().getCatalogVersion((Item)
                                getModelService().getSource(item));
                if(catalogVersion != null)
                {
                    CatalogVersionModel catalogVersionModel = (CatalogVersionModel)getModelService().get(catalogVersion);
                    if(catalogVersionModel != null)
                    {
                        label = catalogVersionModel.getMnemonic();
                    }
                }
            }
        }
        else
        {
            LOG.warn("Can not get mnemonic for item since it is not a TypedObject.");
            if(object != null)
            {
                label = object.toString();
            }
        }
        return StringUtils.isNotBlank(label) ? ("( " + label + " )") : "";
    }
}
