package de.hybris.platform.ticket.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.media.MediaPermissionService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.service.TicketAttachmentsService;
import de.hybris.platform.ticket.service.UnsupportedAttachmentException;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketAttachmentsService implements TicketAttachmentsService
{
    private MediaService mediaService;
    private MediaPermissionService mediaPermissionService;
    private ModelService modelService;
    private CatalogVersionService catalogVersionService;
    private String catalogVersionName;
    private String catalogId;
    private String folderName;
    private String commonCsAgentUserGroup;
    private UserService userService;
    private String allowedUploadedFormats;


    public MediaModel createAttachment(String name, String contentType, byte[] data, UserModel customer)
    {
        checkFileExtension(name);
        MediaModel mediaModel = new MediaModel();
        mediaModel.setCode(UUID.randomUUID().toString());
        mediaModel.setCatalogVersion(getCatalogVersionService().getCatalogVersion(getCatalogId(), getCatalogVersionName()));
        mediaModel.setMime(contentType);
        mediaModel.setRealFileName(name);
        mediaModel.setFolder(getMediaService().getFolder(getFolderName()));
        getModelService().save(mediaModel);
        getMediaService().setDataForMedia(mediaModel, data);
        getMediaPermissionService().grantReadPermission(mediaModel, (PrincipalModel)
                        getUserService().getUserGroupForUID(getCommonCsAgentUserGroup()));
        getMediaPermissionService().grantReadPermission(mediaModel, (PrincipalModel)customer);
        return mediaModel;
    }


    protected void checkFileExtension(String name)
    {
        if(!FilenameUtils.isExtension(name.toLowerCase(),
                        getAllowedUploadedFormats().replaceAll("\\s", "").toLowerCase().split(",")))
        {
            throw new UnsupportedAttachmentException(
                            String.format("File %s has unsupported extension. Only [%s] allowed.", new Object[] {name, getAllowedUploadedFormats()}));
        }
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected MediaPermissionService getMediaPermissionService()
    {
        return this.mediaPermissionService;
    }


    @Required
    public void setMediaPermissionService(MediaPermissionService mediaPermissionService)
    {
        this.mediaPermissionService = mediaPermissionService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected String getCatalogId()
    {
        return this.catalogId;
    }


    @Required
    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    protected String getCatalogVersionName()
    {
        return this.catalogVersionName;
    }


    @Required
    public void setCatalogVersionName(String catalogVersionName)
    {
        this.catalogVersionName = catalogVersionName;
    }


    protected String getCommonCsAgentUserGroup()
    {
        return this.commonCsAgentUserGroup;
    }


    @Required
    public void setCommonCsAgentUserGroup(String commonCsAgentUserGroup)
    {
        this.commonCsAgentUserGroup = commonCsAgentUserGroup;
    }


    protected String getFolderName()
    {
        return this.folderName;
    }


    @Required
    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected String getAllowedUploadedFormats()
    {
        return this.allowedUploadedFormats;
    }


    @Required
    public void setAllowedUploadedFormats(String allowedUploadedFormats)
    {
        this.allowedUploadedFormats = allowedUploadedFormats;
    }
}
