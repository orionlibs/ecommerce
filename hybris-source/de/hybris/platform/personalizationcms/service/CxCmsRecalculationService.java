package de.hybris.platform.personalizationcms.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.service.impl.DefaultCxRecalculationService;
import de.hybris.platform.personalizationservices.trigger.dao.CxSegmentTriggerDao;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CxCmsRecalculationService extends DefaultCxRecalculationService
{
    private static final Logger LOG = LoggerFactory.getLogger(CxCmsRecalculationService.class);
    private CMSPreviewService cmsPreviewService;
    private SessionService sessionService;
    private CxSegmentTriggerDao cxSegmentTriggerDao;
    private CxCatalogService cxCatalogService;


    protected void calculateAndLoadInSession(UserModel user)
    {
        PreviewDataModel previewData = getPreviewDataModel();
        if(previewData != null)
        {
            List<CatalogVersionModel> catalogVersions = findCxCatalogVersions(previewData.getCatalogVersions());
            if(CollectionUtils.isEmpty(catalogVersions))
            {
                LOG.debug("There is no catalog versions with cx objects in preview data : {}", previewData.getCode());
                return;
            }
            if(CollectionUtils.isNotEmpty(previewData.getVariations()))
            {
                calculateForVariations(user, catalogVersions, previewData.getVariations());
            }
            else if(CollectionUtils.isNotEmpty(previewData.getSegments()))
            {
                calculateForSegments(user, catalogVersions, previewData.getSegments());
            }
            else
            {
                LOG.debug("clearing now for {}", user.getUid());
                catalogVersions.forEach(cv -> getCxService().clearPersonalizationInSession(user, cv));
            }
        }
        else
        {
            super.calculateAndLoadInSession(user);
        }
    }


    protected List<CatalogVersionModel> findCxCatalogVersions(Collection<CatalogVersionModel> catalogVersions)
    {
        if(CollectionUtils.isEmpty(catalogVersions))
        {
            return Collections.emptyList();
        }
        Objects.requireNonNull(this.cxCatalogService);
        return (List<CatalogVersionModel>)catalogVersions.stream().filter(this.cxCatalogService::isPersonalizationInCatalog).collect(Collectors.toList());
    }


    protected void calculateForVariations(UserModel user, List<CatalogVersionModel> catalogVersions, Collection<CxVariationModel> variations)
    {
        LOG.debug("recalculating now for {} with variations", user.getUid());
        Map<CatalogVersionModel, List<CxVariationModel>> variationsPerCatalogMap = (Map<CatalogVersionModel, List<CxVariationModel>>)variations.stream().collect(Collectors.groupingBy(CxVariationModel::getCatalogVersion));
        Comparator<CxVariationModel> variationComparator = (a, b) -> {
            int c = a.getCustomization().getRank().compareTo(b.getCustomization().getRank());
            return (c == 0) ? a.getRank().compareTo(b.getRank()) : c;
        };
        for(CatalogVersionModel cv : catalogVersions)
        {
            List<CxVariationModel> catalogVariations = variationsPerCatalogMap.getOrDefault(cv, Collections.emptyList());
            if(catalogVariations.isEmpty())
            {
                getCxService().clearPersonalizationInSession(user, cv);
                continue;
            }
            List<CxVariationModel> list = new ArrayList<>(catalogVariations);
            list.sort(variationComparator);
            getCxService().calculateAndLoadPersonalizationInSession(user, cv, list);
        }
    }


    protected void calculateForSegments(UserModel user, List<CatalogVersionModel> catalogVersions, Collection<CxSegmentModel> segments)
    {
        catalogVersions.forEach(cv -> getCxService().calculateAndLoadPersonalizationInSession(user, cv, this.cxSegmentTriggerDao.findApplicableVariations(segments, cv)));
    }


    protected PreviewDataModel getPreviewDataModel()
    {
        String previewTicketId = getPreviewTicketId();
        if(StringUtils.isNotBlank(previewTicketId))
        {
            CMSPreviewTicketModel previewTicket = getCmsPreviewService().getPreviewTicket(previewTicketId);
            if(previewTicket != null)
            {
                return previewTicket.getPreviewData();
            }
        }
        return null;
    }


    protected String getPreviewTicketId()
    {
        return (String)this.sessionService.getAttribute("cmsTicketId");
    }


    protected CMSPreviewService getCmsPreviewService()
    {
        return this.cmsPreviewService;
    }


    @Required
    public void setCmsPreviewService(CMSPreviewService cmsPreviewService)
    {
        this.cmsPreviewService = cmsPreviewService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected CxSegmentTriggerDao getCxSegmentTriggerDao()
    {
        return this.cxSegmentTriggerDao;
    }


    @Required
    public void setCxSegmentTriggerDao(CxSegmentTriggerDao cxSegmentTriggerDao)
    {
        this.cxSegmentTriggerDao = cxSegmentTriggerDao;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }


    @Required
    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }
}
