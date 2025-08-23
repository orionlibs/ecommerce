package de.hybris.platform.cms2.servicelayer.services.impl;

import com.google.common.collect.Iterables;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.common.functions.ThrowableSupplier;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSContentPageService extends AbstractCMSPageService implements CMSContentPageService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSContentPageService.class.getName());
    protected static final String SLASH = "/";
    private Comparator<AbstractPageModel> cmsItemCatalogLevelComparator;
    private CMSSiteService cmsSiteService;
    private CMSCatalogVersionService cmsCatalogVersionService;
    private CMSPreviewService cmsPreviewService;


    public ContentPageModel getDefaultPageForLabel(String label, CatalogVersionModel version) throws CMSItemNotFoundException
    {
        if(version == null)
        {
            throw new IllegalArgumentException("Catalog version must not be null.");
        }
        Collection<ContentPageModel> pages = getCmsPageDao().findDefaultContentPageByLabelAndCatalogVersions(label,
                        Collections.singleton(version));
        ContentPageModel contentPage = null;
        if(pages != null && pages.size() == 1)
        {
            contentPage = pages.iterator().next();
        }
        else
        {
            throw new CMSItemNotFoundException("No default page with label [" + label + "] found for catalog version [" + version
                            .getVersion() + "].");
        }
        return contentPage;
    }


    public ContentPageModel getHomepage()
    {
        return (ContentPageModel)getSessionSearchRestrictionsDisabler().execute(this::getHomepageInternal);
    }


    protected ContentPageModel getHomepageInternal()
    {
        Collection<CatalogVersionModel> homepageCatalogVersions = getContentCatalogVersionsOfHomepage();
        if(Objects.isNull(homepageCatalogVersions) || CollectionUtils.isEmpty(homepageCatalogVersions))
        {
            return null;
        }
        Collection<ContentPageModel> homepages = getCmsPageDao().findHomepagesByPageStatuses(homepageCatalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
        ContentPageModel homepage = homepages.stream().max(getCmsItemCatalogLevelComparator()).orElse(null);
        Collection<AbstractPageModel> result = null;
        if(homepage != null)
        {
            Collection<AbstractPageModel> pages = getCmsPageDao().findPagesByLabelAndPageStatuses(homepage.getLabel(),
                            Arrays.asList(new CatalogVersionModel[] {homepage.getCatalogVersion()}, ), Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
            result = getCmsRestrictionService().evaluatePages(pages, null);
            if(result.size() > 1)
            {
                LOG.info("More than one page found for label [" + homepage.getLabel() + "]. Returning the last entry in the list.");
            }
        }
        if(Objects.isNull(result) || CollectionUtils.isEmpty(result))
        {
            return null;
        }
        return (ContentPageModel)Iterables.getLast(result);
    }


    public ContentPageModel getHomepage(PagePreviewCriteriaData pagePreviewCriteria)
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = this::getHomepage;
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (ContentPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public ContentPageModel getPageForLabelAndPreview(String label, PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = () -> getPageForLabelAndStatuses(label, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (ContentPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    public ContentPageModel getPageForLabelAndStatuses(String label, List<CmsPageStatus> pageStatuses) throws CMSItemNotFoundException
    {
        return getPageForLabel(label, pageStatuses, true);
    }


    protected ContentPageModel getPageForLabel(String label, List<CmsPageStatus> pageStatuses, boolean exactLabelMatch) throws CMSItemNotFoundException
    {
        List<String> labels = new ArrayList<>();
        labels.addAll(findLabelVariations(label, exactLabelMatch));
        ContentPageModel page = getSingleContentPage(labels);
        if(page != null)
        {
            LOG.debug("Only one ContentPage with label [" + label + "] found. Considering this as default.");
            return page;
        }
        Collection<AbstractPageModel> pages = (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByLabelsAndPageStatuses(labels, getContentCatalogVersions(), pageStatuses));
        if(!exactLabelMatch)
        {
            pages = findPagesForBestLabelMatch(pages, labels);
        }
        Collection<AbstractPageModel> result = getCmsRestrictionService().evaluatePages(pages, null);
        if(result.isEmpty())
        {
            throw new CMSItemNotFoundException("No page with label [" + label + "] found.");
        }
        if(result.size() > 1)
        {
            LOG.info("More than one page found for label [" + label + "]. Returning default.");
        }
        return (ContentPageModel)Iterables.getLast(result);
    }


    protected ContentPageModel getSingleContentPage(List<String> labels)
    {
        Collection<AbstractPageModel> pages = (Collection<AbstractPageModel>)getSessionSearchRestrictionsDisabler().execute(() -> getCmsPageDao().findPagesByLabelsAndPageStatuses(labels, getContentCatalogVersions(), Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE})));
        if(pages.size() == 1)
        {
            ContentPageModel page = pages.iterator().next();
            if(page.getRestrictions().isEmpty())
            {
                return page;
            }
        }
        return null;
    }


    protected Collection<CatalogVersionModel> getSessionContentCatalogVersions()
    {
        return (Collection<CatalogVersionModel>)getCatalogVersionService().getSessionCatalogVersions().stream()
                        .filter(catalogVersion -> ContentCatalogModel.class.isAssignableFrom(catalogVersion.getCatalog().getClass()))
                        .collect(Collectors.toList());
    }


    protected Collection<CatalogVersionModel> getContentCatalogVersions()
    {
        Collection<CatalogVersionModel> sessionCatalogVersions = getSessionContentCatalogVersions();
        CatalogVersionModel catalogVersion = getCmsPreviewService().getPreviewContentCatalogVersion();
        if(Objects.nonNull(catalogVersion))
        {
            List<CatalogVersionModel> previewHierarchy = getCmsCatalogVersionService().getFullHierarchyForCatalogVersion(catalogVersion, getCmsSiteService().getCurrentSite());
            return getCmsCatalogVersionService().getIntersectionOfCatalogVersions(sessionCatalogVersions, previewHierarchy);
        }
        return sessionCatalogVersions;
    }


    protected Collection<CatalogVersionModel> getContentCatalogVersionsOfHomepage()
    {
        Collection<CatalogVersionModel> sessionCatalogVersions = getContentCatalogVersions();
        if(!getCmsCatalogVersionService().areCatalogVersionsRelatives(sessionCatalogVersions))
        {
            CatalogVersionModel defaultContentCatalog = getCmsSiteService().getCurrentSiteDefaultContentCatalogActiveVersion();
            if(Objects.nonNull(defaultContentCatalog))
            {
                List<CatalogVersionModel> hierarchyCatalogVersions = getCmsCatalogVersionService().getFullHierarchyForCatalogVersion(defaultContentCatalog, getCmsSiteService().getCurrentSite());
                return getCmsCatalogVersionService()
                                .getIntersectionOfCatalogVersions(hierarchyCatalogVersions, sessionCatalogVersions);
            }
        }
        return sessionCatalogVersions;
    }


    public List<AbstractPageModel> findPagesForBestLabelMatch(Collection<AbstractPageModel> pages, List<String> labels)
    {
        for(String label : labels)
        {
            List<AbstractPageModel> bestMatches = (List<AbstractPageModel>)pages.stream().map(page -> (ContentPageModel)page).filter(page -> page.getLabel().equals(label))
                            .sorted(Comparator.comparing(ContentPageModel::getLabel).reversed().thenComparing(Comparator.comparing(ItemModel::getCreationtime))).collect(Collectors.toList());
            if(!bestMatches.isEmpty())
            {
                return bestMatches;
            }
        }
        return Collections.emptyList();
    }


    public List<String> findLabelVariations(String label, boolean exactLabelMatch)
    {
        List<String> labels = new ArrayList<>();
        labels.add(label);
        if(label.startsWith("/"))
        {
            labels.add(label.substring(1));
        }
        else
        {
            labels.add("/" + label);
        }
        if(exactLabelMatch)
        {
            return labels;
        }
        return findBestMatchLabelVariations(label, labels);
    }


    protected List<String> findBestMatchLabelVariations(String label, List<String> labels)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(label.startsWith("/"))
        {
            stringBuilder.append("/");
        }
        Stream.<String>of(label.split("/"))
                        .filter(section -> !section.isBlank())
                        .forEach(section -> {
                            stringBuilder.append(section);
                            labels.add(stringBuilder.toString());
                            stringBuilder.append("/");
                            if(stringBuilder.toString().length() < label.length())
                            {
                                labels.add(stringBuilder.toString());
                            }
                        });
        if(labels.stream().noneMatch(labelVariation -> labelVariation.equals(label)))
        {
            labels.add(label);
        }
        labels.sort(Comparator.<String, Comparable>comparing(String::length).reversed());
        return (List<String>)labels.stream().distinct().collect(Collectors.toList());
    }


    public ContentPageModel getPageForLabelOrIdAndMatchType(String labelOrId, boolean exactLabelMatch) throws CMSItemNotFoundException
    {
        AbstractPageModel page;
        try
        {
            ContentPageModel contentPageModel = getPageForLabel(labelOrId, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}, ), exactLabelMatch);
        }
        catch(CMSItemNotFoundException e)
        {
            LOG.debug("No ContentPage with label <" + labelOrId + "> found. Trying to get ContentPage by id", (Throwable)e);
            page = getPageForId(labelOrId);
        }
        if(page == null)
        {
            throw new CMSItemNotFoundException("No page with labelOrId [" + labelOrId + "] found.");
        }
        return (ContentPageModel)page;
    }


    public ContentPageModel getPageForLabelOrIdAndMatchType(String labelOrId, PagePreviewCriteriaData pagePreviewCriteria, boolean exactLabelMatch) throws CMSItemNotFoundException
    {
        ThrowableSupplier<AbstractPageModel> defaultSupplier = () -> getPageForLabelOrIdAndMatchType(labelOrId, exactLabelMatch);
        ThrowableSupplier<AbstractPageModel> versionSupplier = () -> getPageForVersionUid(pagePreviewCriteria.getVersionUid());
        return (ContentPageModel)getItemByCriteria(pagePreviewCriteria, defaultSupplier, versionSupplier);
    }


    protected Comparator<AbstractPageModel> getCmsItemCatalogLevelComparator()
    {
        return this.cmsItemCatalogLevelComparator;
    }


    @Required
    public void setCmsItemCatalogLevelComparator(Comparator<AbstractPageModel> cmsItemCatalogLevelComparator)
    {
        this.cmsItemCatalogLevelComparator = cmsItemCatalogLevelComparator;
    }


    @Required
    public void setCmsSiteService(CMSSiteService cmsSiteService)
    {
        this.cmsSiteService = cmsSiteService;
    }


    protected CMSSiteService getCmsSiteService()
    {
        return this.cmsSiteService;
    }


    protected CMSCatalogVersionService getCmsCatalogVersionService()
    {
        return this.cmsCatalogVersionService;
    }


    @Required
    public void setCmsCatalogVersionService(CMSCatalogVersionService cmsCatalogVersionService)
    {
        this.cmsCatalogVersionService = cmsCatalogVersionService;
    }


    public CMSPreviewService getCmsPreviewService()
    {
        return this.cmsPreviewService;
    }


    public void setCmsPreviewService(CMSPreviewService cmsPreviewService)
    {
        this.cmsPreviewService = cmsPreviewService;
    }
}
