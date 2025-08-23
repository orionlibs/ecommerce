package de.hybris.platform.platformbackoffice.handlers;

import com.hybris.cockpitng.config.jaxb.wizard.PrepareType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.FlowPrepareHandler;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class ProductWizardCatalogVersionPopulator implements FlowPrepareHandler
{
    private static final String PROPERTY_CONTEXT = "ctx";
    private static final String PROPERTY_ACTION_CONTEXT = "actionContext";
    private static final String PROPERTY_CATALOG_VERSION = "catalogVersion";
    private static final String PROPERTY_NEW_PRODUCT = "newProduct";
    private CatalogVersionService catalogVersionService;
    private UserService userService;


    public void prepareFlow(PrepareType prepare, WidgetInstanceManager widgetInstanceManager)
    {
        WidgetModel widgetModel = widgetInstanceManager.getModel();
        Map<String, Object> ctx = (Map<String, Object>)widgetModel.getValue("ctx", Map.class);
        if(ctx != null)
        {
            CatalogVersionModel catalogVersionFromActionContext = getCatalogVersionFromActionContext(ctx);
            CatalogVersionModel catalogVersion = (catalogVersionFromActionContext != null) ? catalogVersionFromActionContext : getCatalogVersionFromMap(ctx);
            populateProductWithCatalogVersion(widgetModel, catalogVersion);
        }
    }


    protected CatalogVersionModel getCatalogVersionFromActionContext(Map<String, Object> ctx)
    {
        Object actionContext = ctx.get("actionContext");
        if(actionContext instanceof Map)
        {
            return getCatalogVersionFromMap((Map<String, Object>)actionContext);
        }
        return null;
    }


    protected CatalogVersionModel getCatalogVersionFromMap(Map<String, Object> ctx)
    {
        Object catalogVersionObject = ctx.get("catalogVersion");
        if(catalogVersionObject instanceof CatalogVersionModel)
        {
            return (CatalogVersionModel)catalogVersionObject;
        }
        return null;
    }


    protected void populateProductWithCatalogVersion(WidgetModel widgetModel, CatalogVersionModel catalogVersion)
    {
        ProductModel newProduct = (ProductModel)widgetModel.getValue("newProduct", ProductModel.class);
        if(newProduct != null && catalogVersion != null)
        {
            UserModel currentUser = getUserService().getCurrentUser();
            Collection<CatalogVersionModel> catalogVersionModels = getAllWritableCatalogVersions(currentUser);
            if(catalogVersionModels.contains(catalogVersion))
            {
                newProduct.setCatalogVersion(catalogVersion);
            }
        }
    }


    protected Collection<CatalogVersionModel> getAllWritableCatalogVersions(UserModel currentUser)
    {
        return getUserService().isAdmin(currentUser) ? getCatalogVersionService().getAllCatalogVersions() :
                        getCatalogVersionService().getAllWritableCatalogVersions((PrincipalModel)currentUser);
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


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
