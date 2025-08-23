package de.hybris.platform.personalizationpromotionsweb.queries;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationpromotionsweb.data.PromotionRuleListWsDTO;
import de.hybris.platform.personalizationpromotionsweb.data.PromotionRuleWsDTO;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.impl.AbstractRestQueryExecutor;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.ruleengineservices.rule.services.SourceRuleInspector;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;

public class CxPromotionsForCatalogQueryExecutor extends AbstractRestQueryExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(CxPromotionsForCatalogQueryExecutor.class);
    private static final String FIELD_REQUIRED = "field.required";
    private static final String DEFAULT_MESSAGE = "Missing required parameter: ";
    private static final String CATALOG = "catalog";
    private static final String CATALOG_VERSION = "version";
    private Converter<PromotionSourceRuleModel, PromotionRuleWsDTO> promotionConverter;
    private RuleService ruleService;
    private SourceRuleInspector sourceRuleInspector;
    private CatalogVersionService catalogVersionService;


    protected void validateInputParams(Map<String, String> params, Errors errors)
    {
        Set<String> keySet = params.keySet();
        List<String> missingVersionCatalogs = (List<String>)keySet.stream().filter(this::isCatalogKey).filter(ck -> hasCatalogVersion(ck, keySet)).collect(Collectors.toList());
        missingVersionCatalogs.forEach(catalogKey -> rejectCatalogKey(catalogKey, errors));
        if(params.get("catalog") != null && params.get("version") != null)
        {
            CatalogVersionModel model = getCatalogVersionModel(params.get("catalog"), params.get("version"));
            if(model == null)
            {
                errors.reject("Parameters 'catalog' and 'version' must have proper values.");
            }
        }
    }


    protected boolean isCatalogKey(String key)
    {
        return (key != null && key.startsWith("catalog"));
    }


    protected String buildCatalogVersionKey(String catalogKey)
    {
        String id = catalogKey.substring("catalog".length());
        return "version" + id;
    }


    protected boolean hasCatalogVersion(String catalogKey, Set<String> keySet)
    {
        return !keySet.contains(buildCatalogVersionKey(catalogKey));
    }


    protected void rejectCatalogKey(String catalogKey, Errors errors)
    {
        String cvk = buildCatalogVersionKey(catalogKey);
        errors.rejectValue(cvk, "field.required", "Missing required parameter: " + cvk);
    }


    protected PromotionRuleListWsDTO executeAfterValidation(Map<String, String> params)
    {
        List<PromotionSourceRuleModel> ruleList = (List<PromotionSourceRuleModel>)buildCatalogVersions(params).stream().filter(Objects::nonNull).map(this::getActiveRules).flatMap(Collection::stream).filter(p -> p instanceof PromotionSourceRuleModel).map(r -> (PromotionSourceRuleModel)r)
                        .filter(this::isCxAwareRule).sorted(this::comparePromotions).collect(Collectors.toList());
        PromotionRuleListWsDTO result = new PromotionRuleListWsDTO();
        result.setPromotions(this.promotionConverter.convertAll(ruleList));
        return result;
    }


    protected boolean isCxAwareRule(SourceRuleModel sourceRule)
    {
        return this.sourceRuleInspector.hasRuleCondition(sourceRule, "cx_aware_promotion");
    }


    protected List<CatalogVersionModel> buildCatalogVersions(Map<String, String> params)
    {
        return (List<CatalogVersionModel>)buildCatalogVersionsDTOs(params).stream()
                        .map(dto -> getCatalogVersionModel(dto.getCatalog(), dto.getVersion()))
                        .collect(Collectors.toList());
    }


    protected List<CatalogVersionWsDTO> buildCatalogVersionsDTOs(Map<String, String> params)
    {
        return (List<CatalogVersionWsDTO>)params.keySet().stream().filter(this::isCatalogKey).map(ck -> {
            String cvk = buildCatalogVersionKey(ck);
            CatalogVersionWsDTO dto = new CatalogVersionWsDTO();
            dto.setCatalog((String)params.get(ck));
            dto.setVersion((String)params.get(cvk));
            return dto;
        }).collect(Collectors.toList());
    }


    protected CatalogVersionModel getCatalogVersionModel(String catalog, String version)
    {
        try
        {
            return this.catalogVersionService.getCatalogVersion(catalog, version);
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Catalog version {}:{} not found", catalog, version);
            return null;
        }
    }


    protected List<AbstractRuleModel> getActiveRules(CatalogVersionModel catalogVersion)
    {
        try
        {
            return this.ruleService.getActiveRulesForCatalogVersionAndRuleType(catalogVersion, RuleType.PROMOTION);
        }
        catch(IllegalStateException e)
        {
            LOG.debug("No valid mapping for catalog version {}:{} ", catalogVersion.getCatalog().getId(), catalogVersion
                            .getVersion());
            return Collections.emptyList();
        }
    }


    protected int comparePromotions(PromotionSourceRuleModel p1, PromotionSourceRuleModel p2)
    {
        String p1Website = (p1.getWebsite() == null) ? null : p1.getWebsite().getIdentifier();
        String p2Website = (p2.getWebsite() == null) ? null : p2.getWebsite().getIdentifier();
        int result = StringUtils.compare(p1Website, p2Website);
        return (result == 0) ? StringUtils.compare(p1.getCode(), p2.getCode()) : result;
    }


    public List<CatalogVersionWsDTO> getCatalogsForWriteAccess(Map<String, String> params)
    {
        return Collections.emptyList();
    }


    public List<CatalogVersionWsDTO> getCatalogsForReadAccess(Map<String, String> params)
    {
        return buildCatalogVersionsDTOs(params);
    }


    protected Converter<PromotionSourceRuleModel, PromotionRuleWsDTO> getPromotionConverter()
    {
        return this.promotionConverter;
    }


    protected RuleService getRuleService()
    {
        return this.ruleService;
    }


    protected SourceRuleInspector getSourceRuleInspector()
    {
        return this.sourceRuleInspector;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    public void setPromotionConverter(Converter<PromotionSourceRuleModel, PromotionRuleWsDTO> promotionConverter)
    {
        this.promotionConverter = promotionConverter;
    }


    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public void setSourceRuleInspector(SourceRuleInspector sourceRuleInspector)
    {
        this.sourceRuleInspector = sourceRuleInspector;
    }
}
