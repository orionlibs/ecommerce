package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.warehousing.util.builder.CountryModelBuilder;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class Countries extends AbstractItems<CountryModel>
{
    public static final String ISOCODE_CANADA = "CA";
    public static final String ISOCODE_UNITED_STATES = "US";
    public static final String ISOCODE_FRANCE = "FR";
    private CountryDao countryDao;


    public CountryModel Canada()
    {
        return (CountryModel)getFromCollectionOrSaveAndReturn(() -> getCountryDao().findCountriesByCode("CA"), () -> CountryModelBuilder.aModel().withIsoCode("CA").withName("Canada", Locale.ENGLISH).withActive(Boolean.TRUE).build());
    }


    public CountryModel UnitedStates()
    {
        return (CountryModel)getFromCollectionOrSaveAndReturn(() -> getCountryDao().findCountriesByCode("US"), () -> CountryModelBuilder.aModel().withIsoCode("US").withName("United States", Locale.ENGLISH).withActive(Boolean.TRUE).build());
    }


    public CountryModel France()
    {
        return (CountryModel)getFromCollectionOrSaveAndReturn(() -> getCountryDao().findCountriesByCode("FR"), () -> CountryModelBuilder.aModel().withIsoCode("FR").withName("France", Locale.ENGLISH).withActive(Boolean.TRUE).build());
    }


    public CountryDao getCountryDao()
    {
        return this.countryDao;
    }


    @Required
    public void setCountryDao(CountryDao countryDao)
    {
        this.countryDao = countryDao;
    }
}
