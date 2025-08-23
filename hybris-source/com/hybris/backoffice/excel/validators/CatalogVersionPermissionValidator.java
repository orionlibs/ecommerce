package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionPermissionValidator extends ExcelGenericReferenceValidator
{
    private static final String CATALOG_VERSION_PARAMETER = "CatalogVersion.version";
    private static final String CATALOG_ID_PARAMETER = "Catalog.id";
    private static final Logger LOG = LoggerFactory.getLogger(CatalogVersionPermissionValidator.class);
    private UserService userService;
    private CatalogVersionService catalogVersionService;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        Map<String, String> parameters = importParameters.getSingleValueParameters();
        try
        {
            CatalogVersionModel catalogVersion = retrieveCatalogVersion(parameters);
            if(catalogVersion != null)
            {
                Objects.requireNonNull(messages);
                validateCatalogVersion(parameters, attributeDescriptor, catalogVersion).ifPresent(messages::add);
            }
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug(e.getMessage(), (Throwable)e);
        }
        return new ExcelValidationResult(messages);
    }


    private CatalogVersionModel retrieveCatalogVersion(Map<String, String> parameters)
    {
        String version = parameters.get("CatalogVersion.version");
        String catalogId = parameters.get("Catalog.id");
        if(StringUtils.isEmpty(version) || StringUtils.isEmpty(catalogId))
        {
            return null;
        }
        return getCatalogVersionService().getCatalogVersion(catalogId, version);
    }


    protected Optional<ValidationMessage> validateCatalogVersion(Map<String, String> parameters, AttributeDescriptorModel attributeDescriptor, CatalogVersionModel catalogVersion)
    {
        UserModel currentUser = getUserService().getCurrentUser();
        Collection<CatalogVersionModel> allWritableCatalogVersions = getAllWritableCatalogVersions(currentUser);
        if(!allWritableCatalogVersions.contains(catalogVersion))
        {
            RequiredAttribute requiredAttribute = getRequiredAttributesFactory().create(attributeDescriptor);
            return Optional.of(prepareValidationMessage(requiredAttribute, parameters));
        }
        return Optional.empty();
    }


    private Collection<CatalogVersionModel> getAllWritableCatalogVersions(UserModel currentUser)
    {
        return getUserService().isAdmin(currentUser) ? getCatalogVersionService().getAllCatalogVersions() :
                        getCatalogVersionService().getAllWritableCatalogVersions((PrincipalModel)currentUser);
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return getTypeService().isAssignableFrom(attributeDescriptor.getAttributeType().getCode(), "CatalogVersion");
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
