package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;

public class ProductCatalogVersionDifference extends GeneratedProductCatalogVersionDifference
{
    public void accept(boolean deleteDiff)
    {
        Product sourceProduct = getSourceProduct();
        Product targetProduct = getTargetProduct();
        if(targetProduct != null)
        {
            EnumerationValue newApprovalStatus;
            CatalogManager catalogManager = CatalogManager.getInstance();
            if(sourceProduct != null)
            {
                newApprovalStatus = catalogManager.getApprovalStatus(sourceProduct);
            }
            else
            {
                newApprovalStatus = getSession().getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.ARTICLEAPPROVALSTATUS, GeneratedCatalogConstants.Enumerations.ArticleApprovalStatus.APPROVED);
            }
            catalogManager.setApprovalStatus(targetProduct, newApprovalStatus);
        }
        if(deleteDiff)
        {
            try
            {
                remove();
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    public void accept()
    {
        accept(false);
    }


    public void dontAccept()
    {
        Product targetProduct = getTargetProduct();
        if(targetProduct != null)
        {
            EnumerationValue unapproved = getSession().getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.ARTICLEAPPROVALSTATUS, GeneratedCatalogConstants.Enumerations.ArticleApprovalStatus.UNAPPROVED);
            CatalogManager catalogManager = CatalogManager.getInstance();
            catalogManager.setApprovalStatus(targetProduct, unapproved);
        }
    }


    @ForceJALO(reason = "abstract method implementation")
    public EnumerationValue getSourceProductApprovalStatus(SessionContext ctx)
    {
        if(getSourceProduct() != null)
        {
            return getCatalogManager().getApprovalStatus(getSourceProduct());
        }
        return null;
    }


    @ForceJALO(reason = "abstract method implementation")
    public EnumerationValue getTargetProductApprovalStatus(SessionContext ctx)
    {
        if(getTargetProduct() != null)
        {
            return getCatalogManager().getApprovalStatus(getTargetProduct());
        }
        return null;
    }


    private CatalogManager getCatalogManager()
    {
        return CatalogManager.getInstance();
    }
}
