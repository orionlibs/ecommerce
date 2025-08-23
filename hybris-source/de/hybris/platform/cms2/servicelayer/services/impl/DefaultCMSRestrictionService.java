package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.RestrictionEvaluationException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSInverseRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import de.hybris.platform.cms2.servicelayer.daos.CMSRestrictionDao;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluatorRegistry;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSRestrictionService extends AbstractCMSService implements CMSRestrictionService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSRestrictionService.class.getName());
    private CMSRestrictionEvaluatorRegistry evaluatorRegistry;
    private CatalogLevelService cmsCatalogLevelService;
    private Comparator<AbstractPageModel> cmsItemCatalogLevelComparator;
    private CMSRestrictionDao cmsRestrictionDao;
    private CMSComponentService cmsComponentService;


    public boolean evaluate(AbstractRestrictionModel restriction, RestrictionData restrictionDataInfo) throws RestrictionEvaluationException
    {
        return evaluateAnyRestriction(restriction, restrictionDataInfo);
    }


    public boolean evaluateCMSComponent(AbstractCMSComponentModel component, RestrictionData data)
    {
        return evaluate(component.getRestrictions(), data, component.isOnlyOneRestrictionMustApply());
    }


    public List<AbstractCMSComponentModel> evaluateCMSComponents(List<AbstractCMSComponentModel> components, RestrictionData data)
    {
        List<AbstractCMSComponentModel> result = new ArrayList<>();
        for(AbstractCMSComponentModel component : components)
        {
            boolean allowed = true;
            if(component.isRestricted())
            {
                LOG.debug("Evaluating restrictions for component [" + component.getName() + "].");
                allowed = evaluate(component.getRestrictions(), data, component.isOnlyOneRestrictionMustApply());
            }
            if(allowed)
            {
                LOG.debug("Adding component [" + component.getName() + "] to allowed components.");
                result.add(component);
            }
        }
        return result;
    }


    public Collection<AbstractPageModel> evaluatePages(Collection<AbstractPageModel> pages, RestrictionData data)
    {
        List<AbstractPageModel> result = new ArrayList<>();
        Collection<AbstractPageModel> defaultPages = getDefaultPages(pages);
        for(AbstractPageModel page : pages)
        {
            if(!defaultPages.contains(page))
            {
                List<AbstractRestrictionModel> restrictions = page.getRestrictions();
                if(CollectionUtils.isNotEmpty(restrictions) && evaluate(restrictions, data, page.isOnlyOneRestrictionMustApply()))
                {
                    LOG.debug("Adding page [" + page.getName() + "] to allowed pages");
                    result.add(page);
                }
            }
        }
        if(result.isEmpty() && CollectionUtils.isNotEmpty(defaultPages))
        {
            result.addAll(defaultPages);
        }
        return (CollectionUtils.isNotEmpty(defaultPages) && defaultPages.size() > 1) ? getMultiCountryRestrictedPages(result) :
                        result;
    }


    public Collection<AbstractRestrictionModel> getOwnRestrictionsForPage(AbstractPageModel pageModel, CatalogVersionModel catalogVersion)
    {
        return getCmsRestrictionDao().findRestrictionsForPage(pageModel, catalogVersion);
    }


    public Collection<AbstractRestrictionModel> getOwnRestrictionsForComponents(Collection<AbstractCMSComponentModel> components, CatalogVersionModel catalogVersion)
    {
        return getCmsRestrictionDao().findRestrictionsForComponents(components, catalogVersion);
    }


    public boolean relatedSharedSlots(AbstractRestrictionModel restrictionModel)
    {
        return CollectionUtils.emptyIfNull(restrictionModel.getComponents())
                        .stream()
                        .anyMatch(componentModel -> getCmsComponentService().inSharedSlots(componentModel));
    }


    protected Collection<AbstractPageModel> getMultiCountryRestrictedPages(Collection<AbstractPageModel> pages)
    {
        Collection<AbstractPageModel> restrictedPages = null;
        int tmplevelNumber = 0;
        List<AbstractPageModel> pagesWithContentCatalog = (List<AbstractPageModel>)getDefaultPages(pages).stream().filter(abstractPageModel -> abstractPageModel.getCatalogVersion().getCatalog() instanceof ContentCatalogModel).collect(Collectors.toList());
        for(AbstractPageModel page : pagesWithContentCatalog)
        {
            ContentCatalogModel contentCatalog = (ContentCatalogModel)page.getCatalogVersion().getCatalog();
            if(!getCmsCatalogLevelService().isTopLevel(contentCatalog))
            {
                boolean isIntermediate = getCmsCatalogLevelService().isIntermediateLevel(contentCatalog);
                boolean isLeaf = getCmsCatalogLevelService().isBottomLevel(contentCatalog);
                int levelNumber = getCmsCatalogLevelService().getCatalogLevel(contentCatalog);
                if(!isIntermediate || levelNumber > tmplevelNumber)
                {
                    tmplevelNumber = levelNumber;
                    restrictedPages = getRestrictedPages(pages, page, page.getCatalogVersion());
                }
                if(!isIntermediate && isLeaf)
                {
                    return restrictedPages;
                }
            }
        }
        return getSortedPagesByCatalogLevel(CollectionUtils.isNotEmpty(restrictedPages) ? restrictedPages : pages);
    }


    protected Collection<AbstractPageModel> getSortedPagesByCatalogLevel(Collection<AbstractPageModel> pages)
    {
        return (Collection<AbstractPageModel>)pages.stream().sorted(getCmsItemCatalogLevelComparator()).collect(Collectors.toList());
    }


    protected Collection<AbstractPageModel> getRestrictedPages(Collection<AbstractPageModel> pages, AbstractPageModel abstractPageModel, CatalogVersionModel catalogVersion)
    {
        List<AbstractPageModel> returnList;
        if(abstractPageModel instanceof ContentPageModel)
        {
            returnList = (List<AbstractPageModel>)pages.stream().filter(restrictionPage -> restrictionPage.getCatalogVersion().equals(catalogVersion))
                            .filter(restrictionPage -> (restrictionPage instanceof ContentPageModel && ((ContentPageModel)restrictionPage).getLabelOrId().equals(((ContentPageModel)abstractPageModel).getLabelOrId()))).collect(Collectors.toList());
        }
        else
        {
            returnList = (List<AbstractPageModel>)pages.stream().filter(restrictionPage -> restrictionPage.getCatalogVersion().equals(catalogVersion)).collect(Collectors.toList());
        }
        if(returnList.isEmpty())
        {
            returnList.add(abstractPageModel);
        }
        return returnList;
    }


    public Collection<String> getCategoryCodesForRestriction(CMSCategoryRestrictionModel restriction)
    {
        Collection<String> result = new ArrayList<>();
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, restriction, result));
        return result;
    }


    public Collection<String> getProductCodesForRestriction(CMSProductRestrictionModel restriction)
    {
        Collection<String> result = new ArrayList<>();
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, restriction, result));
        return result;
    }


    protected CMSRestrictionDao getCmsRestrictionDao()
    {
        return this.cmsRestrictionDao;
    }


    @Required
    public void setCmsRestrictionDao(CMSRestrictionDao cmsRestrictionDao)
    {
        this.cmsRestrictionDao = cmsRestrictionDao;
    }


    public void setEvaluatorRegistry(CMSRestrictionEvaluatorRegistry evaluatorRegistry)
    {
        this.evaluatorRegistry = evaluatorRegistry;
    }


    protected boolean evaluate(List<AbstractRestrictionModel> restrictions, RestrictionData data, boolean oneRestrictionApply)
    {
        Map<String, Boolean> restrictionResults = new HashMap<>();
        boolean allowed = true;
        for(AbstractRestrictionModel restriction : restrictions)
        {
            String restId = restriction.getUid();
            if(restrictionResults.containsKey(restId))
            {
                allowed = ((Boolean)restrictionResults.get(restId)).booleanValue();
            }
            else
            {
                try
                {
                    allowed = evaluate(restriction, data);
                }
                catch(RestrictionEvaluationException ree)
                {
                    LOG.warn("Evaluation of restriction [" + restId + "] threw exception: " + ree.getMessage(), (Throwable)ree);
                    allowed = false;
                }
                restrictionResults.put(restId, Boolean.valueOf(allowed));
            }
            LOG.debug("Result of restriction [" + restriction.getName() + "]: " + allowed);
            if((allowed && oneRestrictionApply) || (!allowed && !oneRestrictionApply))
            {
                break;
            }
        }
        return allowed;
    }


    protected boolean evaluateAnyRestriction(AbstractRestrictionModel restrictionModel, RestrictionData context) throws RestrictionEvaluationException
    {
        boolean invert = false;
        AbstractRestrictionModel restrictionToEvaluate = restrictionModel;
        if(restrictionModel instanceof CMSInverseRestrictionModel)
        {
            invert = true;
            restrictionToEvaluate = ((CMSInverseRestrictionModel)restrictionModel).getOriginalRestriction();
        }
        CMSRestrictionEvaluator restrictionEvaluator = this.evaluatorRegistry.getCMSRestrictionEvaluator(restrictionToEvaluate.getItemtype().toLowerCase());
        if(restrictionEvaluator != null)
        {
            return invert ^ restrictionEvaluator.evaluate(restrictionToEvaluate, context);
        }
        throw new RestrictionEvaluationException("Can not evaluate restriction of type [" + restrictionModel.getTypeCode() + "]");
    }


    protected Collection<AbstractPageModel> getDefaultPages(Collection<AbstractPageModel> pages)
    {
        List<AbstractPageModel> ret = new ArrayList<>();
        for(AbstractPageModel page : pages)
        {
            if(Boolean.TRUE.equals(page.getDefaultPage()))
            {
                ret.add(page);
            }
        }
        return ret;
    }


    protected String createMoreThanOneDefaultPageWarning(Collection<AbstractPageModel> defaultPages)
    {
        StringBuilder warningMessage = new StringBuilder();
        if(defaultPages.isEmpty())
        {
            return warningMessage.toString();
        }
        warningMessage.append("More than one default page defined! (");
        for(AbstractPageModel defaultPage : defaultPages)
        {
            warningMessage.append(' ').append(defaultPage.getName()).append(',');
        }
        warningMessage.replace(warningMessage.length() - 1, warningMessage.length(), "");
        warningMessage.append(" ).");
        warningMessage.append(" First one will be taken as default.");
        return warningMessage.toString();
    }


    protected CatalogLevelService getCmsCatalogLevelService()
    {
        return this.cmsCatalogLevelService;
    }


    @Required
    public void setCmsCatalogLevelService(CatalogLevelService cmsCatalogLevelService)
    {
        this.cmsCatalogLevelService = cmsCatalogLevelService;
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


    protected CMSComponentService getCmsComponentService()
    {
        return this.cmsComponentService;
    }


    @Required
    public void setCmsComponentService(CMSComponentService cmsComponentService)
    {
        this.cmsComponentService = cmsComponentService;
    }
}
