/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.ymkt.segmentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.ymkt.segmentation.facades.CampaignRestrictionPopulatorFacade;
import de.hybris.platform.cmsfacades.data.OptionData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "CampaignRestriction")
public class CampaignRestrictionController
{
    @Resource(name = "campaignRestrictionPopulatorFacade")
    protected CampaignRestrictionPopulatorFacade campaignRestrictionPopulatorFacade;


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/data/segmentation/campaign")
    @ApiOperation(nickname = "getCampaign", value = "Returns campaigns to populate downdown field", produces = "application/json")
    public String populateCampaignsDropdown(
                    @RequestParam(required = false) final String mask,
                    @RequestParam(required = true) final String currentPage,
                    @RequestParam(required = true) final String pageSize)
                    throws IOException
    {
        final List<OptionData> campaigns = this.campaignRestrictionPopulatorFacade.getCampaigns(mask, currentPage, pageSize);
        return new ObjectMapper().writeValueAsString(Collections.singletonMap("options", campaigns));
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/data/segmentation/campaign/{campaignId}")
    @ApiOperation(nickname = "getCampaignById", value = "Get the campaign name of given campaign id", produces = "application/json")
    public String setExistingCampaignDropdownValue(
                    @ApiParam(value = "Campaign Id", required = true)
                    @PathVariable Optional<String> campaignId)
                    throws IOException
    {
        if(campaignId.isPresent())
        {
            final OptionData campaignOption = this.campaignRestrictionPopulatorFacade.getCampaignById(campaignId.get());
            return new ObjectMapper().writeValueAsString(campaignOption);
        }
        return "";
    }


    public void setCampaignRestrictionPopulatorFacade(CampaignRestrictionPopulatorFacade campaignRestrictionPopulatorFacade)
    {
        this.campaignRestrictionPopulatorFacade = campaignRestrictionPopulatorFacade;
    }
}