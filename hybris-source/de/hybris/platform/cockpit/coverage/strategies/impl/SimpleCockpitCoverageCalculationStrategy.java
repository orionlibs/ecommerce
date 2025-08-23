package de.hybris.platform.cockpit.coverage.strategies.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.coverage.CoverageDescriptionStrategy;
import de.hybris.platform.validation.coverage.CoverageInfo;
import de.hybris.platform.validation.coverage.strategies.CoverageCalculationStrategy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Executions;

public class SimpleCockpitCoverageCalculationStrategy implements CoverageCalculationStrategy
{
    private SimpleCoverageCalculationStrategy delegationStrategy;


    public CoverageInfo calculate(ItemModel item)
    {
        this.delegationStrategy.resetAttributes();
        this.delegationStrategy.setAttributeQualifiers(getAllQualifiers(item));
        CoverageInfo coverageInfo = this.delegationStrategy.calculate(item);
        coverageInfo.setCoverageDescriptionStrategy((CoverageDescriptionStrategy)new ShortCoverageDescriptionStrategy(this, coverageInfo.getPropertyInfoMessages()));
        return coverageInfo;
    }


    public void setDelegationStrategy(SimpleCoverageCalculationStrategy delegationStrategy)
    {
        this.delegationStrategy = delegationStrategy;
    }


    protected Set<String> getAllQualifiers(ItemModel item)
    {
        UIConfigurationService uiConfigurationService = UISessionUtils.getCurrentSession().getUiConfigurationService();
        TypedObject typedObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(item);
        ObjectTemplate bestTemplate = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(typedObject);
        String configCode = UITools.getCockpitParameter("default.inspector.configcode", Executions.getCurrent());
        EditorConfiguration componentConfiguration = (EditorConfiguration)uiConfigurationService.getComponentConfiguration(bestTemplate,
                        StringUtils.isBlank(configCode) ? "editorArea" : configCode, EditorConfiguration.class);
        List<PropertyDescriptor> allPropertyDescriptors = componentConfiguration.getAllPropertyDescriptors();
        Set<String> returnSet = new HashSet<>();
        for(PropertyDescriptor pd : allPropertyDescriptors)
        {
            String qualifier = pd.getQualifier();
            if(qualifier.contains("."))
            {
                returnSet.add(qualifier.substring(qualifier.indexOf('.') + 1));
                continue;
            }
            returnSet.add(qualifier);
        }
        return returnSet;
    }
}
