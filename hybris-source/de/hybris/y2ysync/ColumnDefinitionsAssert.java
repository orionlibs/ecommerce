package de.hybris.y2ysync;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import java.util.Collection;
import java.util.Optional;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ColumnDefinitionsAssert extends AbstractAssert<ColumnDefinitionsAssert, Collection<Y2YColumnDefinitionModel>>
{
    protected ColumnDefinitionsAssert(Collection<Y2YColumnDefinitionModel> actual)
    {
        super(actual, ColumnDefinitionsAssert.class);
    }


    public static ColumnDefinitionsAssert assertThat(Collection<Y2YColumnDefinitionModel> actual)
    {
        return new ColumnDefinitionsAssert(actual);
    }


    public ColumnDefinitionsAssert hasSize(int size)
    {
        Assertions.assertThat((Iterable)this.actual).isNotNull().hasSize(size);
        return this;
    }


    public ColumnDefinitionAssert containsDefintionFor(AttributeDescriptorModel descriptor)
    {
        Optional<Y2YColumnDefinitionModel> firstTry = ((Collection<Y2YColumnDefinitionModel>)this.actual).stream().filter(ad -> ad.getAttributeDescriptor().equals(descriptor)).findFirst();
        Assertions.assertThat(firstTry.isPresent()).isTrue();
        return new ColumnDefinitionAssert(firstTry.get());
    }
}
