package de.hybris.platform.hac.data.samplequery;

public interface SampleQuery
{
    String getQueryDescription();


    String getAdditionalDescription();


    String getQuery();


    boolean isCompatibleWitCurrentDb();


    boolean isFlexibleSearch();
}
