package de.hybris.platform.directpersistence.read;

import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.util.ItemPropertyValue;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class AttributeValueAssert extends AbstractAssert<AttributeValueAssert, SLDDataContainer.AttributeValue>
{
    public AttributeValueAssert(SLDDataContainer.AttributeValue actual)
    {
        super(actual, AttributeValueAssert.class);
    }


    public static AttributeValueAssert assertThat(SLDDataContainer.AttributeValue actual)
    {
        return new AttributeValueAssert(actual);
    }


    public AttributeValueAssert withValueEqualTo(Object value)
    {
        Assertions.assertThat(((SLDDataContainer.AttributeValue)this.actual).getValue()).isEqualTo(value);
        return this;
    }


    public <T extends de.hybris.platform.core.model.ItemModel> AttributeValueAssert withReferenceValueEqualTo(T referenceValue)
    {
        Assertions.assertThat(((SLDDataContainer.AttributeValue)this.actual).getValue()).isInstanceOf(ItemPropertyValue.class);
        Assertions.assertThat((Comparable)((ItemPropertyValue)((SLDDataContainer.AttributeValue)this.actual).getValue()).getPK()).isEqualTo(referenceValue.getPk());
        return this;
    }
}
