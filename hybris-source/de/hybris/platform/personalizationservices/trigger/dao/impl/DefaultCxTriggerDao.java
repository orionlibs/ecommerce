package de.hybris.platform.personalizationservices.trigger.dao.impl;

import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.trigger.dao.CxTriggerDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultCxTriggerDao extends AbstractCxDao<CxAbstractTriggerModel> implements CxTriggerDao
{
    public DefaultCxTriggerDao()
    {
        super("CxAbstractTrigger");
    }


    public Optional<CxAbstractTriggerModel> findTriggerByCode(String code, CxVariationModel variation)
    {
        ServicesUtil.validateParameterNotNull(code, "Variation code must not be null");
        ServicesUtil.validateParameterNotNull(variation, "Variation must not be null");
        ServicesUtil.validateParameterNotNull(variation.getCatalogVersion(), "variation must have a not null catalogVersion");
        String query = "SELECT {pk} FROM {CxAbstractTrigger} WHERE {catalogVersion} = ?catalogVersion AND {variation} = ?variation AND {code} = ?code ";
        Map<String, Object> params = new HashMap<>();
        params.put("variation", variation);
        params.put("catalogVersion", variation.getCatalogVersion());
        params.put("code", code);
        return querySingle("SELECT {pk} FROM {CxAbstractTrigger} WHERE {catalogVersion} = ?catalogVersion AND {variation} = ?variation AND {code} = ?code ", params);
    }


    public Collection<CxAbstractTriggerModel> findTriggers(CxVariationModel variation)
    {
        ServicesUtil.validateParameterNotNull(variation, "Variation must not be null");
        ServicesUtil.validateParameterNotNull(variation.getCatalogVersion(), "variation must have a not null catalogVersion");
        String query = "SELECT {pk} FROM {CxAbstractTrigger} WHERE {catalogVersion} = ?catalogVersion AND {variation} = ?variation ";
        Map<String, Object> params = new HashMap<>();
        params.put("variation", variation);
        params.put("catalogVersion", variation.getCatalogVersion());
        return queryList("SELECT {pk} FROM {CxAbstractTrigger} WHERE {catalogVersion} = ?catalogVersion AND {variation} = ?variation ", params);
    }
}
