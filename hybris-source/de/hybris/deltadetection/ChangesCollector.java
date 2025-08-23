package de.hybris.deltadetection;

public interface ChangesCollector
{
    boolean collect(ItemChangeDTO paramItemChangeDTO);


    void finish();
}
