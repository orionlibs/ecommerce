package de.hybris.platform.testframework.assertions.assertj;

import de.hybris.platform.servicelayer.internal.converter.util.ModelUtils;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;

public class ModelStateAssert extends AbstractAssert<ModelStateAssert, AbstractItemModel>
{
    private String propertyName;


    private ModelStateAssert(AbstractItemModel actual)
    {
        super(actual, ModelStateAssert.class);
    }


    static ModelStateAssert assertThat(AbstractItemModel model)
    {
        return new ModelStateAssert(model);
    }


    private static <T> T getLoadedValue(AbstractItemModel model, String attribute)
    {
        try
        {
            return (T)ModelContextUtils.getItemModelContext(model).getOriginalValue(attribute);
        }
        catch(IllegalStateException e)
        {
            return null;
        }
    }


    public ModelStateAssert forProperty(String propertyName)
    {
        this.propertyName = propertyName;
        return this;
    }


    public ModelStateAssert hasLoadedValueEqualTo(Object expectedValue)
    {
        Object loadedValue = getLoadedValue((AbstractItemModel)this.actual, this.propertyName);
        Assertions.assertThat(loadedValue).isEqualTo(expectedValue);
        return this;
    }


    public ModelStateAssert hasFieldValueEqualTo(Object expectedValue)
    {
        Object fieldValue = ModelUtils.getFieldValue(this.actual, this.propertyName);
        ((AbstractObjectAssert)Assertions.assertThat(fieldValue).isNotNull()).isEqualTo(expectedValue);
        return this;
    }


    public ModelStateAssert hasFieldWithNullValue()
    {
        Object fieldValue = ModelUtils.getFieldValue(this.actual, this.propertyName);
        Assertions.assertThat(fieldValue).isNull();
        return this;
    }


    public ModelStateAssert hasSetter(String setterName)
    {
        Assertions.assertThat(ModelUtils.existsMethod(((AbstractItemModel)this.actual).getClass(), "set" + capitalizeFirstLetter(setterName))).isTrue();
        return this;
    }


    public ModelStateAssert hasGetter(String getterName)
    {
        boolean checkResult = (ModelUtils.existsMethod(((AbstractItemModel)this.actual).getClass(), "get" + capitalizeFirstLetter(getterName)) || ModelUtils.existsMethod(((AbstractItemModel)this.actual).getClass(), "is" + capitalizeFirstLetter(getterName)));
        Assertions.assertThat(checkResult).isTrue();
        return this;
    }


    public ModelStateAssert hasField(String fieldName)
    {
        Assertions.assertThat(ModelUtils.existsField(((AbstractItemModel)this.actual).getClass(), fieldName)).isTrue();
        return this;
    }


    private String capitalizeFirstLetter(String input)
    {
        char[] stringArray = input.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return new String(stringArray);
    }
}
