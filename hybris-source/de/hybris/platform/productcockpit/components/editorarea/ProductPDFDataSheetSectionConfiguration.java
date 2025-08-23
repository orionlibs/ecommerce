package de.hybris.platform.productcockpit.components.editorarea;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.productcockpit.services.product.ProductService;
import java.util.Collections;
import java.util.List;
import org.zkoss.spring.SpringUtil;

public class ProductPDFDataSheetSectionConfiguration extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    private static final String PDF_ICON_IMAGE = "/productcockpit/images/docs_PDF.gif";
    TypedObject object;
    ObjectValueContainer objectValues;
    private ProductService productCockpitProductService = null;
    private TypeService typeService = null;


    public ProductService getProductCockpitProductService()
    {
        if(this.productCockpitProductService == null)
        {
            this.productCockpitProductService = (ProductService)SpringUtil.getBean("productCockpitProductService");
        }
        return this.productCockpitProductService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    public void allInitialized(EditorConfiguration config, ObjectType type, TypedObject object)
    {
    }


    public List<EditorSectionConfiguration> getAdditionalSections()
    {
        return Collections.EMPTY_LIST;
    }


    public SectionRenderer getCustomRenderer()
    {
        return (SectionRenderer)new Object(this);
    }


    public void initialize(EditorConfiguration config, ObjectType type, TypedObject object)
    {
    }


    public void loadValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
        this.object = object;
        this.objectValues = objectValues;
    }


    public void saveValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }
}
