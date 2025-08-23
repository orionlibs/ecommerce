package de.hybris.platform.productcockpit.services.product.impl;

import bsh.EvalError;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.ValueContainerMap;
import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.commons.jalo.Document;
import de.hybris.platform.commons.jalo.Format;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.productcockpit.services.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.workflow.ScriptEvaluationService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ProductServiceImpl implements ProductService
{
    private static final String PRODUCT2PDF_FORMAT = "product2pdf_format";
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private TypeService typeService;
    private ModelService modelService;
    private VariantsService variantsService;
    private ScriptEvaluationService scriptEvaluationService;
    private EnumerationService enumerationService;


    public Collection<PropertyDescriptor> getVariantAttributes(String typeCode)
    {
        List<PropertyDescriptor> ret = new ArrayList<>();
        Set<String> variantAttributes = getVariantsService().getVariantAttributes(typeCode);
        for(String string : variantAttributes)
        {
            ret.add(getTypeService().getPropertyDescriptor(string));
        }
        return ret;
    }


    public boolean hasVariants(TypedObject item)
    {
        return (isBaseProduct(item) && !getVariants(item).isEmpty());
    }


    public boolean isBaseProduct(TypedObject item)
    {
        Object object = item.getObject();
        return (object instanceof ProductModel && ((ProductModel)object).getVariantType() != null);
    }


    public List<TypedObject> getVariants(TypedObject base)
    {
        if(!isBaseProduct(base))
        {
            throw new IllegalArgumentException("no base product");
        }
        Collection<VariantProductModel> variants = ((ProductModel)base.getObject()).getVariants();
        return getTypeService().wrapItems(variants);
    }


    public String getVariantTypecode(TypedObject base)
    {
        if(!isBaseProduct(base))
        {
            throw new IllegalArgumentException("no base product");
        }
        return ((ProductModel)base.getObject()).getVariantType().getCode();
    }


    public String getApprovalStatusCode(TypedObject item)
    {
        String ret = null;
        Object object = item.getObject();
        if(object instanceof ProductModel && UISessionUtils.getCurrentSession().getSystemService().checkAttributePermissionOn(item
                        .getType().getCode(), "approvalStatus", "read"))
        {
            Product product = (Product)getModelService().getSource(object);
            EnumerationValue approvalStatus = CatalogManager.getInstance().getApprovalStatus(product);
            if(approvalStatus != null)
            {
                ret = approvalStatus.getCode();
            }
        }
        return ret;
    }


    public List<String> getAllApprovalStatusCodes()
    {
        List<String> ret = new ArrayList<>();
        List<EnumerationValue> values = EnumerationManager.getInstance().getEnumerationType("ArticleApprovalStatus").getValues();
        for(EnumerationValue enumerationValue : values)
        {
            ret.add(enumerationValue.getCode());
        }
        return ret;
    }


    public String getApprovalStatusName(String code)
    {
        EnumerationValue approvalStateFromCode = getApprovalStatusFromCode(code);
        if(approvalStateFromCode != null)
        {
            return approvalStateFromCode.getName();
        }
        return null;
    }


    @Deprecated
    protected EnumerationValue getApprovalStatusFromCode(String code)
    {
        EnumerationManager enumerationManager = EnumerationManager.getInstance();
        return enumerationManager.getEnumerationValue(enumerationManager.getEnumerationType("ArticleApprovalStatus"), code);
    }


    protected HybrisEnumValue getApprovalStatusValueFromCode(String code)
    {
        return getEnumerationService().getEnumerationValue("ArticleApprovalStatus", code);
    }


    public void setApprovalStatus(TypedObject item, String code)
    {
        try
        {
            Object object = item.getObject();
            if(object instanceof ProductModel && UISessionUtils.getCurrentSession().getSystemService().checkAttributePermissionOn(item
                            .getType().getCode(), "approvalStatus", "change"))
            {
                ProductModel itemModel = (ProductModel)object;
                ObjectValueContainer itemValueContainer = TypeTools.createValueContainer(item, item
                                                .getType().getPropertyDescriptors(),
                                UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos(), true);
                HybrisEnumValue approvalStatus = getApprovalStatusValueFromCode(code);
                ValueContainerMap valueContainerMap = new ValueContainerMap(itemValueContainer, true, null);
                Map<Object, Object> currentValues = new HashMap<>((Map<?, ?>)valueContainerMap);
                currentValues.put("approvalStatus", approvalStatus);
                if(!currentValues.get("approvalStatus").equals(valueContainerMap.get("approvalStatus")))
                {
                    getScriptEvaluationService().evaluateActivationScripts((ItemModel)itemModel, currentValues, (Map)valueContainerMap, "save");
                }
                getModelService().setAttributeValue(itemModel, "approvalStatus", approvalStatus);
            }
        }
        catch(EvalError e)
        {
            log.error("Could not change approval status of item (Reason: " + e.getMessage() + ").", (Throwable)e);
        }
    }


    public boolean createPdfDatasheetDocument(TypedObject product)
    {
        boolean ret = false;
        if(product == null)
        {
            log.error("in createPdfDatasheetDocument: Product was null.");
        }
        else
        {
            Object object = product.getObject();
            if(object instanceof ProductModel)
            {
                Product j_product = (Product)getModelService().getSource(object);
                Format pdfFormat = getFormat("product2pdf_format", (Item)j_product);
                if(pdfFormat == null)
                {
                    log.error("Format not found, maybe ProductPdfDataSheet extension is not present.");
                }
                else
                {
                    try
                    {
                        pdfFormat.format((Item)j_product);
                        ret = true;
                    }
                    catch(Exception e)
                    {
                        log.error("Could not create PDF, maybe ProductPdfDataSheet extension is not present.", e);
                    }
                }
            }
        }
        return ret;
    }


    protected Format getFormat(String code, Item item)
    {
        Format ret = null;
        Collection<Format> formatsForItem = CommonsManager.getInstance().getFormatsForItem(item);
        for(Format format : formatsForItem)
        {
            if(code.equals(format.getCode()))
            {
                ret = format;
                break;
            }
        }
        return ret;
    }


    public List<TypedObject> getPdfDatasheetDocuments(TypedObject product)
    {
        List<TypedObject> ret = null;
        if(product == null)
        {
            log.error("in getPdfDatasheetDocuments: Product was null.");
        }
        else
        {
            Object object = product.getObject();
            if(object instanceof ProductModel)
            {
                Product j_product = (Product)TypeTools.getModelService().getSource(object);
                Format pdfFormat = getFormat("product2pdf_format", (Item)j_product);
                ret = (pdfFormat == null) ? Collections.EMPTY_LIST : getTypeService().wrapItems(CommonsManager.getInstance().getDocuments((Item)j_product, pdfFormat));
            }
        }
        return (ret == null) ? Collections.EMPTY_LIST : ret;
    }


    public void deletePdfDatasheetDocument(TypedObject document)
    {
        if(document == null)
        {
            log.error("in deletePdfDatasheetDocument: Document was null.");
        }
        else
        {
            Object object = document.getObject();
            if(object instanceof de.hybris.platform.commons.model.DocumentModel)
            {
                Document doc = (Document)getModelService().getSource(object);
                if("product2pdf_format".equals(doc.getFormat().getCode()))
                {
                    try
                    {
                        doc.remove();
                    }
                    catch(ConsistencyCheckException e)
                    {
                        log.error(e.getMessage(), (Throwable)e);
                    }
                }
                else
                {
                    log.error("in deletePdfDatasheetDocument: Wrong document format '" + doc.getFormat().getCode() + "', expected 'product2pdf_format'.");
                }
            }
        }
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public VariantsService getVariantsService()
    {
        return this.variantsService;
    }


    @Required
    public void setVariantsService(VariantsService variantsService)
    {
        this.variantsService = variantsService;
    }


    private ScriptEvaluationService getScriptEvaluationService()
    {
        return this.scriptEvaluationService;
    }


    public void setScriptEvaluationService(ScriptEvaluationService scriptEvaluationService)
    {
        this.scriptEvaluationService = scriptEvaluationService;
    }


    private EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }
}
