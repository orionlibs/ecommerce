/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.media;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.Optional;
import org.zkoss.util.media.Media;

public class MediaUtil
{
    private MediaService mediaService;
    private ModelService modelService;
    private SearchRestrictionService searchRestrictionService;
    private SessionService sessionService;
    private UserService userService;
    private CatalogVersionService catalogVersionService;
    private String mediaCatalogId = BackofficeMediaConstants.DEFAULT_MEDIA_CATALOG_ID;
    private String mediaCatalogVersion = BackofficeMediaConstants.DEFAULT_MEDIA_CATALOG_VERSION;


    public Optional<MediaModel> getMedia(final String code)
    {
        try
        {
            return Optional.of(findMediaByCode(code));
        }
        catch(final UnknownIdentifierException | AmbiguousIdentifierException exc)
        {
            return Optional.empty();
        }
    }


    public MediaModel findMediaByCode(final String mediaCode)
    {
        return getSessionService().executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                try
                {
                    getSearchRestrictionService().disableSearchRestrictions();
                    final CatalogVersionModel backofficeMediaCatalog = findMediaCatalogVersion();
                    return getMediaService().getMedia(backofficeMediaCatalog, mediaCode);
                }
                finally
                {
                    getSearchRestrictionService().enableSearchRestrictions();
                }
            }
        }, getUserService().getAdminUser());
    }


    public CatalogVersionModel findMediaCatalogVersion()
    {
        return getSessionService().executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                return getCatalogVersionService().getCatalogVersion(getMediaCatalogId(), getMediaCatalogVersion());
            }
        }, getUserService().getAdminUser());
    }


    protected MediaFolderModel getMediaFolder(final String code)
    {
        return getSessionService().executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                try
                {
                    getSearchRestrictionService().disableSearchRestrictions();
                    return getMediaService().getFolder(code);
                }
                finally
                {
                    getSearchRestrictionService().enableSearchRestrictions();
                }
            }
        }, getUserService().getAdminUser());
    }


    public void deleteMedia(final MediaModel mediaModel)
    {
        getModelService().remove(mediaModel);
    }


    public void updateMedia(final MediaModel mediaModel, final Media media)
    {
        try
        {
            Transaction.current().execute(new TransactionBody()
            {
                public Object execute()
                {
                    mediaModel.setRealFileName(media.getName());
                    mediaModel.setMime(media.getContentType());
                    getModelService().save(mediaModel);
                    getMediaService().setDataForMedia(mediaModel, media.getByteData());
                    return null;
                }
            });
        }
        catch(final Exception ex)
        {
            throw Transaction.toException(ex, RuntimeException.class);
        }
    }


    public MediaModel createMedia(final String mediaCode, final String folder, final String mediaType, final Media media)
    {
        try
        {
            return (MediaModel)Transaction.current().execute(new TransactionBody()
            {
                public Object execute()
                {
                    final var catalogVersion = findMediaCatalogVersion();
                    final var folderModel = getMediaFolder(folder);
                    final MediaModel mediaModel = getModelService().create(mediaType);
                    mediaModel.setCode(mediaCode);
                    mediaModel.setFolder(folderModel);
                    mediaModel.setCatalogVersion(catalogVersion);
                    updateMedia(mediaModel, media);
                    return mediaModel;
                }
            });
        }
        catch(final Exception ex)
        {
            throw Transaction.toException(ex, RuntimeException.class);
        }
    }


    public MediaModel createMedia(final String mediaCode, final String folder, final String mediaType,
                    final String mimeType, final String fileName)
    {
        try
        {
            return (MediaModel)Transaction.current().execute(new TransactionBody()
            {
                public Object execute()
                {
                    final var catalogVersion = findMediaCatalogVersion();
                    final var folderModel = getMediaFolder(folder);
                    final MediaModel mediaModel = getModelService().create(mediaType);
                    mediaModel.setCode(mediaCode);
                    mediaModel.setFolder(folderModel);
                    mediaModel.setCatalogVersion(catalogVersion);
                    mediaModel.setRealFileName(fileName);
                    mediaModel.setMime(mimeType);
                    getModelService().save(mediaModel);
                    return mediaModel;
                }
            });
        }
        catch(final Exception ex)
        {
            throw Transaction.toException(ex, RuntimeException.class);
        }
    }


    public MediaService getMediaService()
    {
        return mediaService;
    }


    public void setMediaService(final MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    public SearchRestrictionService getSearchRestrictionService()
    {
        return searchRestrictionService;
    }


    public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    public SessionService getSessionService()
    {
        return sessionService;
    }


    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public UserService getUserService()
    {
        return userService;
    }


    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return catalogVersionService;
    }


    public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public String getMediaCatalogId()
    {
        return mediaCatalogId;
    }


    public void setMediaCatalogId(final String mediaCatalogId)
    {
        this.mediaCatalogId = mediaCatalogId;
    }


    public String getMediaCatalogVersion()
    {
        return mediaCatalogVersion;
    }


    public void setMediaCatalogVersion(final String mediaCatalogVersion)
    {
        this.mediaCatalogVersion = mediaCatalogVersion;
    }
}
