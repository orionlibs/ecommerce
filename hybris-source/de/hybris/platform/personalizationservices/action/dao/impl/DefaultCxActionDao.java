package de.hybris.platform.personalizationservices.action.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.action.dao.CxActionDao;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultCxActionDao extends AbstractCxDao<CxAbstractActionModel> implements CxActionDao
{
    public DefaultCxActionDao()
    {
        super("CxAbstractAction");
    }


    public Optional<CxAbstractActionModel> findActionByCode(String code, CxVariationModel variation)
    {
        ServicesUtil.validateParameterNotNull(code, "Action code must not be null");
        ServicesUtil.validateParameterNotNull(code, "Catalog version must not be null");
        String query = "SELECT {pk} FROM {CxAbstractAction} WHERE {code} = ?code AND {variation} = ?variation AND {catalogVersion} = ?catalogVersion ";
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("variation", variation);
        params.put("catalogVersion", variation.getCatalogVersion());
        return querySingle("SELECT {pk} FROM {CxAbstractAction} WHERE {code} = ?code AND {variation} = ?variation AND {catalogVersion} = ?catalogVersion ", params);
    }


    public List<CxAbstractActionModel> findActions(CxVariationModel variation)
    {
        String query = "SELECT {pk} FROM {CxAbstractAction} WHERE {variation} = ?variation AND {catalogVersion} = ?catalogVersion ORDER BY {variationPOS} ASC";
        Map<String, Object> params = new HashMap<>();
        params.put("variation", variation);
        params.put("catalogVersion", variation.getCatalogVersion());
        return queryList("SELECT {pk} FROM {CxAbstractAction} WHERE {variation} = ?variation AND {catalogVersion} = ?catalogVersion ORDER BY {variationPOS} ASC", params);
    }


    public List<CxAbstractActionModel> findActionsForVariations(Collection<CxVariationModel> variations)
    {
        String query = "SELECT {A.pk} FROM {CxAbstractAction AS A JOIN CxVariation AS V  ON {V.pk} = {A.variation} JOIN CxCustomization AS C ON {C.pk} = {V.customization} } WHERE {A.variation} IN (?variations) AND {A.catalogVersion} IN (?catalogVersions) ORDER BY {C.groupPOS} ASC, {V.customizationPOS} ASC, {A.variationPOS} ASC";
        Set<CatalogVersionModel> catalogVersions = (Set<CatalogVersionModel>)variations.stream().map(CxVariationModel::getCatalogVersion).collect(Collectors.toSet());
        Map<String, Object> params = new HashMap<>();
        params.put("variations", variations);
        params.put("catalogVersions", catalogVersions);
        return queryList(
                        "SELECT {A.pk} FROM {CxAbstractAction AS A JOIN CxVariation AS V  ON {V.pk} = {A.variation} JOIN CxCustomization AS C ON {C.pk} = {V.customization} } WHERE {A.variation} IN (?variations) AND {A.catalogVersion} IN (?catalogVersions) ORDER BY {C.groupPOS} ASC, {V.customizationPOS} ASC, {A.variationPOS} ASC",
                        params);
    }
}
