package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewListener;
import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewModel;
import de.hybris.platform.cmscockpit.session.impl.CmsCockpitPerspective;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.MediaUpdateService;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;

public class DefaultMediaReferencePreviewListener implements MediaReferencePreviewListener
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaReferencePreviewListener.class);
    private final MediaReferencePreviewModel model;
    private MediaUpdateService mediaUpdateService;
    private CMSAdminSiteService cmsAdminSiteService;


    public MediaUpdateService getMediaUpdateService()
    {
        if(this.mediaUpdateService == null)
        {
            this.mediaUpdateService = (MediaUpdateService)SpringUtil.getBean("mediaUpdateService");
        }
        return this.mediaUpdateService;
    }


    public CMSAdminSiteService getCmsAdminPerspective()
    {
        if(this.cmsAdminSiteService == null)
        {
            this.cmsAdminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.cmsAdminSiteService;
    }


    public DefaultMediaReferencePreviewListener(MediaReferencePreviewModel model)
    {
        this.model = model;
    }


    public void valueChanged(TypedObject object)
    {
        this.model.setSource(object);
    }


    public void clearImageStream(TypedObject currentObject)
    {
        TypedObject refreshedObject = getMediaUpdateService().removeCurrentImageStream(currentObject);
        this.model.setSource(refreshedObject);
    }


    public void uploadImageStream(TypedObject typedObject, MediaEditorSectionConfiguration.MediaContent mediaData)
    {
        TypedObject refreshedObject = getMediaUpdateService().updateMediaBinaryStream(typedObject, mediaData.getData(), mediaData
                        .getMimeType(), mediaData.getName());
        this.model.setSource(refreshedObject);
    }


    public void editImage(TypedObject object)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().activateItemInEditor(object);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @HybrisDeprecation(sinceVersion = "ages")
    public void createImage(TypedObject typedObject)
    {
        TypedObject source = this.model.getSource();
        ObjectTemplate currentTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(source.getType().getCode());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Media.removable", Boolean.TRUE);
        parameters.put("Media.code", RandomStringUtils.randomAlphanumeric(10));
        TypedObject catalogVersion = UISessionUtils.getCurrentSession().getTypeService().wrapItem(getCmsAdminPerspective().getActiveCatalogVersion());
        parameters.put("Media.catalogVersion", catalogVersion);
        if(UISessionUtils.getCurrentSession().getCurrentPerspective() instanceof CmsCockpitPerspective)
        {
            ((CmsCockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective()).createNewItem(currentTemplate, parameters);
            ((CmsCockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective()).expandEditorArea();
        }
        else
        {
            LOG.warn("Can not create image. Reason: Current perspective not a CmsCockpitPerspective.");
        }
    }
}
