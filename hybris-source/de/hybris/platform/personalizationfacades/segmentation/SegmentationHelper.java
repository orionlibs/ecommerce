package de.hybris.platform.personalizationfacades.segmentation;

import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;

public interface SegmentationHelper
{
    public static final int SEGMENT_INDEX = 0;
    public static final int CUSTOMER_INDEX = 1;
    public static final int BASESITE_INDEX = 2;
    public static final int PROVIDER_INDEX = 3;
    public static final int NUMBER_OF_CODE_INDEXES = 4;


    String getCustomerSegmentationCode(CxUserToSegmentModel paramCxUserToSegmentModel);


    String getSegmentationCode(String... paramVarArgs);


    String[] splitCustomerSegmentationCode(String paramString);


    String validateSegmentationCode(String paramString);
}
