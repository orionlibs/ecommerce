package de.hybris.platform.directpersistence.read;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;

public class SLDDataContainerAssert extends AbstractAssert<SLDDataContainerAssert, SLDDataContainer>
{
    private LanguageModel currentLanguage;


    private SLDDataContainerAssert(SLDDataContainer actual)
    {
        super(actual, SLDDataContainerAssert.class);
    }


    public static SLDDataContainerAssert assertThat(SLDDataContainer actual)
    {
        return new SLDDataContainerAssert(actual);
    }


    public <T extends de.hybris.platform.core.model.ItemModel> SLDDataContainerAssert hasEqualMetaDataAs(T model)
    {
        Assertions.assertThat((Comparable)((SLDDataContainer)this.actual).getPk()).isEqualTo(model.getPk());
        ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(model.getClass());
        Assertions.assertThat((Comparable)((SLDDataContainer)this.actual).getTypePk()).isEqualTo(composedType.getPk());
        Assertions.assertThat(((SLDDataContainer)this.actual).getTypeCode()).isEqualTo(composedType.getCode());
        return this;
    }


    public AttributeValueAssert containsAttribute(String attributeName)
    {
        Assertions.assertThat(this.actual).isNotNull();
        SLDDataContainer.AttributeValue attributeValue = ((SLDDataContainer)this.actual).getAttributeValue(attributeName, null);
        ((AbstractObjectAssert)Assertions.assertThat(attributeValue)
                        .overridingErrorMessage("Value of '" + attributeName + "' for model '" + ((SLDDataContainer)this.actual).getTypeCode() + "' is null!", new Object[0]))
                        .isNotNull();
        return new AttributeValueAssert(attributeValue);
    }


    public AttributeValueAssert containsLocalizedAttribute(String attributeName)
    {
        Assertions.assertThat(this.actual).isNotNull();
        if(this.currentLanguage == null)
        {
            CommonI18NService commonI18NService = (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService", CommonI18NService.class);
            this.currentLanguage = commonI18NService.getCurrentLanguage();
        }
        SLDDataContainer.AttributeValue attributeValue = ((SLDDataContainer)this.actual).getAttributeValue(attributeName, this.currentLanguage.getPk());
        Assertions.assertThat(attributeValue).isNotNull();
        return new AttributeValueAssert(attributeValue);
    }


    private TypeService getTypeService()
    {
        return (TypeService)Registry.getApplicationContext().getBean("typeService", TypeService.class);
    }
}
