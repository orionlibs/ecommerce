package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2005", forRemoval = true)
public class ExcelCatalogVersionValidator implements ExcelValidator
{
    protected static final String CATALOGS_KEY = "Catalogs";
    protected static final String VERSIONS_KEY = "Versions";
    protected static final String CATALOG_VERSIONS_KEY = "CatalogVersions";
    protected static final String CATALOG_VERSIONS_FORMAT_KEY = "%s:%s";
    protected static final String VALIDATION_CATALOG_EMPTY = "excel.import.validation.catalog.empty";
    protected static final String VALIDATION_CATALOG_VERSION_EMPTY = "excel.import.validation.catalogversion.empty";
    protected static final String VALIDATION_CATALOG_DOESNT_EXIST = "excel.import.validation.catalog.doesntexists";
    protected static final String VALIDATION_CATALOG_VERSION_DOESNT_EXIST = "excel.import.validation.catalogversion.doesntexists";
    protected static final String VALIDATION_CATALOG_VERSION_DOESNT_MATCH = "excel.import.validation.catalogversion.doesntmatch";
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private TypeService typeService;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> ctx)
    {
        populateContextIfNeeded(ctx);
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for(Map<String, String> parameters : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            validateSingleReference(ctx, validationMessages, parameters);
        }
        return new ExcelValidationResult(validationMessages);
    }


    protected void populateContextIfNeeded(Map<String, Object> ctx)
    {
        if(!ctx.containsKey("Catalogs") || !ctx.containsKey("Versions") || !ctx.containsKey("CatalogVersions"))
        {
            populateContext(ctx);
        }
    }


    protected void validateSingleReference(Map<String, Object> ctx, List<ValidationMessage> validationMessages, Map<String, String> parameters)
    {
        validateCatalog(ctx, validationMessages, parameters);
        validateCatalogVersion(ctx, validationMessages, parameters);
        if(catalogExists(ctx, parameters) && catalogVersionExists(ctx, parameters) && !catalogVersionMatch(ctx, parameters))
        {
            validationMessages.add(new ValidationMessage("excel.import.validation.catalogversion.doesntmatch", new Serializable[] {parameters
                            .get("version"), parameters.get("catalog")}));
        }
    }


    protected void validateCatalogVersion(Map<String, Object> ctx, List<ValidationMessage> validationMessages, Map<String, String> parameters)
    {
        if(parameters.get("version") == null)
        {
            validationMessages.add(new ValidationMessage("excel.import.validation.catalogversion.empty"));
        }
        else if(!catalogVersionExists(ctx, parameters))
        {
            validationMessages
                            .add(new ValidationMessage("excel.import.validation.catalogversion.doesntexists", new Serializable[] {parameters.get("version")}));
        }
    }


    protected void validateCatalog(Map<String, Object> ctx, List<ValidationMessage> validationMessages, Map<String, String> parameters)
    {
        if(parameters.get("catalog") == null)
        {
            validationMessages.add(new ValidationMessage("excel.import.validation.catalog.empty"));
        }
        else if(!catalogExists(ctx, parameters))
        {
            validationMessages
                            .add(new ValidationMessage("excel.import.validation.catalog.doesntexists", new Serializable[] {parameters.get("catalog")}));
        }
    }


    protected boolean catalogExists(Map<String, Object> ctx, Map<String, String> parameters)
    {
        return (parameters.get("catalog") != null && ((Set)ctx
                        .get("Catalogs")).contains(parameters.get("catalog")));
    }


    protected boolean catalogVersionExists(Map<String, Object> ctx, Map<String, String> parameters)
    {
        return (parameters.get("version") != null && ((Set)ctx
                        .get("Versions")).contains(parameters.get("version")));
    }


    protected boolean catalogVersionMatch(Map<String, Object> ctx, Map<String, String> parameters)
    {
        return ((Set)ctx.get("CatalogVersions")).contains(String.format("%s:%s", new Object[] {parameters
                        .get("catalog"), parameters.get("version")}));
    }


    protected void populateContext(Map<String, Object> ctx)
    {
        UserModel currentUser = getUserService().getCurrentUser();
        Collection<CatalogVersionModel> allWritableCatalogVersions = getUserService().isAdmin(currentUser) ? getCatalogVersionService().getAllCatalogVersions() : getCatalogVersionService().getAllWritableCatalogVersions((PrincipalModel)currentUser);
        Set<String> catalogs = (Set<String>)allWritableCatalogVersions.stream().map(catalogVersion -> catalogVersion.getCatalog().getId()).collect(Collectors.toSet());
        ctx.put("Catalogs", catalogs);
        Set<String> versions = (Set<String>)allWritableCatalogVersions.stream().map(CatalogVersionModel::getVersion).collect(Collectors.toSet());
        ctx.put("Versions", versions);
        Set<String> catalogVersions = (Set<String>)allWritableCatalogVersions.stream().map(catalogVersionModel -> String.format("%s:%s", new Object[] {catalogVersionModel.getCatalog().getId(), catalogVersionModel.getVersion()})).collect(Collectors.toSet());
        ctx.put("CatalogVersions", catalogVersions);
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        Map<String, String> singleValueParameters = importParameters.getSingleValueParameters();
        return (importParameters.isCellValueNotBlank() && singleValueParameters.containsKey("catalog") && singleValueParameters
                        .containsKey("version"));
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
