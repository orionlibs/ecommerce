package de.hybris.platform.configurablebundlebackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;
import java.util.StringJoiner;
import org.apache.commons.collections.CollectionUtils;

public abstract class AbstractBundleRuleLabelProvider<T extends AbstractBundleRuleModel> implements LabelProvider<T>
{
    public String getDescription(T abstractBundleRuleModel)
    {
        return getLabel(abstractBundleRuleModel);
    }


    public String getIconPath(T abstractBundleRuleModel)
    {
        return null;
    }


    protected String getProductNames(Collection<ProductModel> associatedProducts)
    {
        if(CollectionUtils.isNotEmpty(associatedProducts))
        {
            StringJoiner joiner = new StringJoiner(",");
            associatedProducts.forEach(i -> {
                if(i.getName() != null)
                {
                    joiner.add(i.getName());
                }
                else
                {
                    joiner.add("[" + i.getCode() + "]");
                }
            });
            return joiner.toString();
        }
        return "";
    }
}
