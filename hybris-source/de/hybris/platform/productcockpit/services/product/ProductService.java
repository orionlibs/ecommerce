package de.hybris.platform.productcockpit.services.product;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public interface ProductService
{
    boolean isBaseProduct(TypedObject paramTypedObject);


    boolean hasVariants(TypedObject paramTypedObject);


    List<TypedObject> getVariants(TypedObject paramTypedObject);


    String getVariantTypecode(TypedObject paramTypedObject);


    String getApprovalStatusCode(TypedObject paramTypedObject);


    void setApprovalStatus(TypedObject paramTypedObject, String paramString);


    String getApprovalStatusName(String paramString);


    List<String> getAllApprovalStatusCodes();


    List<TypedObject> getPdfDatasheetDocuments(TypedObject paramTypedObject);


    boolean createPdfDatasheetDocument(TypedObject paramTypedObject);


    void deletePdfDatasheetDocument(TypedObject paramTypedObject);
}
