package de.hybris.y2ysync;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ColumnDefinitionAssert extends AbstractAssert<ColumnDefinitionAssert, Y2YColumnDefinitionModel>
{
    protected ColumnDefinitionAssert(Y2YColumnDefinitionModel actual)
    {
        super(actual, ColumnDefinitionAssert.class);
    }


    public static ColumnDefinitionAssert assertThat(Y2YColumnDefinitionModel actual)
    {
        return new ColumnDefinitionAssert(actual);
    }


    public ColumnDefinitionAssert withImpexHeader(String impexHeader)
    {
        Assertions.assertThat(((Y2YColumnDefinitionModel)this.actual).getImpexHeader()).isEqualTo(impexHeader);
        return this;
    }


    public ColumnDefinitionAssert withColumnName(String columnName)
    {
        Assertions.assertThat(((Y2YColumnDefinitionModel)this.actual).getColumnName()).isEqualTo(columnName);
        return this;
    }


    public ColumnDefinitionAssert withLanguage(LanguageModel languageModel)
    {
        Assertions.assertThat(((Y2YColumnDefinitionModel)this.actual).getLanguage()).isNotNull();
        Assertions.assertThat(((Y2YColumnDefinitionModel)this.actual).getLanguage().getIsocode()).isEqualTo(languageModel.getIsocode());
        return this;
    }


    public ColumnDefinitionAssert withoutLanguage()
    {
        Assertions.assertThat(((Y2YColumnDefinitionModel)this.actual).getLanguage()).isNull();
        return this;
    }
}
