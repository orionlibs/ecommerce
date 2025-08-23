package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSTypeRestrictionDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCMSTypeRestrictionDao extends AbstractCMSItemDao implements CMSTypeRestrictionDao
{
    public List<CMSComponentTypeModel> getTypeRestrictionsForPageTemplate(PageTemplateModel pageTemplate)
    {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT tbl.pk FROM ({{");
        queryBuilder.append("SELECT {compType.pk} FROM ");
        queryBuilder.append("{ContentSlotName AS csName ");
        queryBuilder.append("   JOIN ValidComponentTypesForContentSlots as validTypes ON {csName.pk}={validTypes.source} ");
        queryBuilder.append("   JOIN CMSComponentType AS compType ON {validTypes.target}={compType.pk} ");
        queryBuilder.append("}");
        queryBuilder.append("WHERE {csName.template}=?template");
        queryBuilder.append("}} UNION {{");
        queryBuilder.append("SELECT {compType.pk} FROM ");
        queryBuilder.append("{ContentSlotName AS csName ");
        queryBuilder.append("   JOIN ComponentTypeGroup AS ctGroup ON {csName.compTypeGroup}={ctGroup.pk} ");
        queryBuilder.append("   JOIN ComponentTypeGroups2ComponentType AS groupToType ON {ctGroup.pk} = {groupToType.source} ");
        queryBuilder.append("   JOIN CMSComponentType AS compType ON {groupToType.target}={compType.pk} ");
        queryBuilder.append("} ");
        queryBuilder.append("WHERE {csName.template}=?template");
        queryBuilder.append("}}) tbl");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("template", pageTemplate);
        SearchResult<CMSComponentTypeModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }
}
