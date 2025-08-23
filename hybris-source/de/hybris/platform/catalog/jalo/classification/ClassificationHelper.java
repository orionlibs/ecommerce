package de.hybris.platform.catalog.jalo.classification;

public class ClassificationHelper
{
    public static int getLayer(String classificationNumber)
    {
        int layer;
        if("00".equals(classificationNumber.substring(2, 4)))
        {
            layer = 1;
        }
        else if("00".equals(classificationNumber.substring(4, 6)))
        {
            layer = 2;
        }
        else if("00".equals(classificationNumber.substring(4, 6)))
        {
            layer = 2;
        }
        else if("00".equals(classificationNumber.substring(6)))
        {
            layer = 3;
        }
        else
        {
            layer = 4;
        }
        return layer;
    }


    public static String getSuperCategoryCode(String classificationNumber, int layer)
    {
        StringBuilder superClassificationNumber = new StringBuilder(classificationNumber);
        switch(layer)
        {
            case 2:
                superClassificationNumber.replace(2, 4, "00");
                break;
            case 3:
                superClassificationNumber.replace(4, 6, "00");
                break;
            case 4:
                superClassificationNumber.replace(6, 8, "00");
                break;
        }
        return superClassificationNumber.toString();
    }
}
