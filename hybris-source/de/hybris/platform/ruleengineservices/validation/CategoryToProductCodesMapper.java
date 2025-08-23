package de.hybris.platform.ruleengineservices.validation;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Set;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

public class CategoryToProductCodesMapper implements Function<CategoryModel, Set<String>>
{
    private ProductService productService;
    private SessionService sessionService;
    private CatalogVersionService catalogVersionService;
    private CommonI18NService commonI18NService;
    private I18NService i18NService;


    public Set<String> apply(CategoryModel category)
    {
        Preconditions.checkArgument((category != null), "Category is required to perform this operation, null given");
        return (Set<String>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, category));
    }


    protected ProductService getProductService()
    {
        return this.productService;
    }


    @Required
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
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


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
