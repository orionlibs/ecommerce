/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.pagetemplates.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cmsfacades.pagetemplates.PageTemplateFacade;
import de.hybris.platform.cmswebservices.data.PageTemplateDTO;
import de.hybris.platform.cmswebservices.data.PageTemplateData;
import de.hybris.platform.cmswebservices.data.PageTemplateListData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to deal with PageTemplate objects
 */
@Controller
@IsAuthorizedCmsManager
@Api(tags = "page templates")
public class PageTemplateController
{
    @Resource
    private PageTemplateFacade pageTemplateFacade;
    @Resource
    private DataMapper dataMapper;


    @RequestMapping(value = API_VERSION + "/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pagetemplates", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Finds page templates by page type.", notes = "Returns a holder of a collection of PageTemplateData filtered on the given data passed as query string.",
                    nickname = "getPageTemplatesByPageType")
    @ApiResponses(
                    {@ApiResponse(code = 200, message = "List of page templates", response = PageTemplateListData.class)})
    @ApiImplicitParams(
                    {@ApiImplicitParam(name = "siteId", value = "The site identifier", required = true, dataType = "string", paramType = "path"),
                                    @ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
                                    @ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path"),
                                    @ApiImplicitParam(name = "pageTypeCode", value = "Item type of a page", required = true, dataType = "string", paramType = "query"),
                                    @ApiImplicitParam(name = "active", value = "When set to TRUE, filter the results for active templates", required = false, dataType = "string", paramType = "query")})
    public PageTemplateListData findPageTemplatesByPageType(@ApiParam(value = "Holder of search filters", required = true)
    @ModelAttribute final PageTemplateDTO pageTemplateInfo)
    {
        final de.hybris.platform.cmsfacades.data.PageTemplateDTO convertedPageTemplateDTO = getDataMapper().map(pageTemplateInfo,
                        de.hybris.platform.cmsfacades.data.PageTemplateDTO.class);
        final List<PageTemplateData> pageTemplates = getDataMapper()
                        .mapAsList(getPageTemplateFacade().findPageTemplates(convertedPageTemplateDTO), PageTemplateData.class, null);
        final PageTemplateListData pageTemplateListData = new PageTemplateListData();
        pageTemplateListData.setTemplates(pageTemplates);
        return pageTemplateListData;
    }


    @RequestMapping(value = API_VERSION + "/pagetemplate", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Finds page templates by page uuid.", notes = "Returns the PageTemplateData filtered on the given data passed as query string.", nickname = "getPageTemplatesByPageUuid")
    @ApiResponses({@ApiResponse(code = 200, message = "Get a page template", response = PageTemplateData.class)})
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "pageUuid", value = "Uuid of a page", required = true, dataType = "string", paramType = "query")})
    public PageTemplateData findPageTemplatesByPageUuid(
                    @ApiParam(value = "page uuid", required = true) @RequestParam(value = "pageUuid") final String pageUuid)
    {
        return getDataMapper().map(getPageTemplateFacade().findPageTemplateByPageUuid(pageUuid), PageTemplateData.class);
    }


    public PageTemplateFacade getPageTemplateFacade()
    {
        return pageTemplateFacade;
    }


    public void setPageTemplateFacade(final PageTemplateFacade pageTemplateFacade)
    {
        this.pageTemplateFacade = pageTemplateFacade;
    }


    protected DataMapper getDataMapper()
    {
        return dataMapper;
    }


    public void setDataMapper(final DataMapper dataMapper)
    {
        this.dataMapper = dataMapper;
    }
}
