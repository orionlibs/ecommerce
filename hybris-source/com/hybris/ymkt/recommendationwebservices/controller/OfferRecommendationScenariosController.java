/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.ymkt.recommendationwebservices.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.ymkt.recommendationwebservices.facades.OfferRecommendationPopulatorFacade;
import de.hybris.platform.cmsfacades.data.OptionData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "offerRecommendationScenarios")
public class OfferRecommendationScenariosController
{
    @Resource(name = "offerRecoPopulatorFacade")
    protected OfferRecommendationPopulatorFacade offerRecoPopulatorFacade;


    /**
     * Retrieve dropdown values via oData and return content
     *
     * @param sourceType:
     *           Dropdown to fill
     * @return JSON containing option data list
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws IOException
     */
    @RequestMapping(value = "/data/offer/{sourceField}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(nickname = "offerRecoPopulator", value = "Returns values to populate given dropdown field", produces = MediaType.APPLICATION_JSON)
    public String populateDropdown(
                    @ApiParam(value = "Dropdown field to fill. Determines the data to be returned", required = true) @PathVariable String sourceField)
                    throws IOException
    {
        final Map<String, List<OptionData>> map = new HashMap<>();
        //construct JSON for dropdowns
        map.put("options", this.offerRecoPopulatorFacade.populateDropDown(sourceField));
        return new ObjectMapper().writeValueAsString(map);
    }


    public void setOfferRecoPopulatorFacade(final OfferRecommendationPopulatorFacade offerRecoPopulatorFacade)
    {
        this.offerRecoPopulatorFacade = offerRecoPopulatorFacade;
    }
}
