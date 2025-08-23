/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.gigya.gigyaservices.service;

import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import de.hybris.platform.gigya.gigyaservices.model.GigyaConfigModel;
import java.util.Map;

/**
 * Service to call gigya api's using gigya's java classes
 */
public interface GigyaService
{
    /**
     * Method to call raw gigya API with the provided gigya configuration
     *
     * @param method
     *           The method name of gigya
     * @param params
     *           The various params
     * @param gigyaConfigModel
     *           The gigya config model
     * @param trys
     *           The number of trys
     * @param tryNum
     *           The current try number
     * @return GSResponse The GS response
     */
    GSResponse callRawGigyaApiWithConfig(String method, Map<String, Object> params, GigyaConfigModel gigyaConfigModel, int trys,
                    int tryNum);


    /**
     * Method to call gigya API with the configuration and gigya object
     *
     * @param method
     *           The method name
     * @param gsObject
     *           The gigya object
     * @param gigyaConfigModel
     *           The gigya configuration model
     * @param trys
     *           The number of trys
     * @param tryNum
     *           The current try number
     * @return GSResponse The gigya response
     */
    GSResponse callRawGigyaApiWithConfigAndObject(String method, GSObject gsObject, GigyaConfigModel gigyaConfigModel, int trys,
                    int tryNum);
}
