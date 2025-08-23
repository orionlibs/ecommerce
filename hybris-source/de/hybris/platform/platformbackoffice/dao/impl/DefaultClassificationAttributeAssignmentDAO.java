package de.hybris.platform.platformbackoffice.dao.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.platformbackoffice.dao.ClassificationAttributeAssignmentDAO;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultClassificationAttributeAssignmentDAO implements ClassificationAttributeAssignmentDAO
{
    protected static final String FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT = String.format(
                    "SELECT {caa:%s} FROM {%s as caa JOIN %s as cc ON {cc:%s} = {caa:%s} JOIN %s as ca ON {caa:%s} = {ca:%s} JOIN %s as csv ON {caa:%s} = {csv:%s} JOIN %s as c ON {csv:%s} = {c:%s}} WHERE {cc:%s} = ?%s AND LOWER({ca:%s}) = ?%s AND {csv:%s} = ?%s AND {c:%s} = ?%s", new Object[] {
                                    "pk", "ClassAttributeAssignment", "ClassificationClass", "pk", "classificationClass", "ClassificationAttribute", "classificationAttribute", "pk", "ClassificationSystemVersion", "systemVersion",
                                    "pk", "Catalog", "catalog", "pk", "code", "ClassificationClasscode", "code", "ClassificationAttributecode", "version", "version",
                                    "id", "id"});
    private FlexibleSearchService flexibleSearchService;


    public ClassAttributeAssignmentModel getClassificationAttributeAssignmnent(String catalogId, String systemVersion, String classificationClass, String attribute)
    {
        return getClassificationAttributeAssignment(catalogId, systemVersion, classificationClass, attribute);
    }


    public ClassAttributeAssignmentModel getClassificationAttributeAssignment(String catalogId, String systemVersion, String classificationClass, String attribute)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT);
        query.addQueryParameter("id", catalogId);
        query.addQueryParameter("version", systemVersion);
        query.addQueryParameter("ClassificationClasscode", classificationClass);
        query.addQueryParameter("ClassificationAttributecode", attribute
                        .toLowerCase());
        return (ClassAttributeAssignmentModel)this.flexibleSearchService.searchUnique(query);
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
