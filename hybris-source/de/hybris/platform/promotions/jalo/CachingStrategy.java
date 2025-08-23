package de.hybris.platform.promotions.jalo;

import java.util.List;

public interface CachingStrategy
{
    void put(String paramString, List<PromotionResult> paramList);


    List<PromotionResult> get(String paramString);


    void remove(String paramString);
}
