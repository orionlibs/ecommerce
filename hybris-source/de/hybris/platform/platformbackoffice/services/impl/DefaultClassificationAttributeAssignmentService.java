package de.hybris.platform.platformbackoffice.services.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.platformbackoffice.dao.ClassificationAttributeAssignmentDAO;
import de.hybris.platform.platformbackoffice.services.ClassificationAttributeAssignmentService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultClassificationAttributeAssignmentService implements ClassificationAttributeAssignmentService
{
    protected static final Pattern PATTERN = Pattern.compile("(?<catalogId>[^/]+)/(?<systemVersion>[^/]+)/(?<classificationClassCode>[^.]+)\\.(?<classificationAttributeCode>.+)");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultClassificationAttributeAssignmentService.class);
    private ClassificationAttributeAssignmentDAO classificationAttributeAssignmentDAO;


    public ClassAttributeAssignmentModel findClassAttributeAssignment(String classificationAttributeQualifier)
    {
        Matcher matcher = PATTERN.matcher(classificationAttributeQualifier);
        if(matcher.find())
        {
            String catalogId = matcher.group("catalogId");
            String systemVersion = matcher.group("systemVersion");
            String classificationClassCode = matcher.group("classificationClassCode");
            String classificationAttributeCode = matcher.group("classificationAttributeCode");
            if(StringUtils.isNotBlank(catalogId) && StringUtils.isNotBlank(systemVersion) &&
                            StringUtils.isNotBlank(classificationClassCode) && StringUtils.isNotBlank(classificationAttributeCode))
            {
                return findClassAttributeAssignment(catalogId, systemVersion, classificationClassCode, classificationAttributeCode);
            }
        }
        LOG.warn("'{}' is not correct classification attribute qualifier. Expected pattern : {catalogId}/{systemVersion}/{classificationClassCode}.{classificationAttributeCode}", classificationAttributeQualifier);
        return null;
    }


    public ClassAttributeAssignmentModel findClassAttributeAssignment(String catalogId, String systemVersionId, String classificationClassCode, String attributeCode)
    {
        return this.classificationAttributeAssignmentDAO.getClassificationAttributeAssignment(catalogId, systemVersionId, classificationClassCode, attributeCode);
    }


    @Required
    public void setClassificationAttributeAssignmentDAO(ClassificationAttributeAssignmentDAO classificationAttributeAssignmentDAO)
    {
        this.classificationAttributeAssignmentDAO = classificationAttributeAssignmentDAO;
    }
}
