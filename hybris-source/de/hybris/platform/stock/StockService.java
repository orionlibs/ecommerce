package de.hybris.platform.stock;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StockService
{
    public static final String BEAN_NAME = "stockService";


    StockLevelStatus getProductStatus(ProductModel paramProductModel, WarehouseModel paramWarehouseModel);


    StockLevelStatus getProductStatus(ProductModel paramProductModel, Collection<WarehouseModel> paramCollection);


    int getTotalStockLevelAmount(ProductModel paramProductModel);


    int getTotalStockLevelAmount(ProductModel paramProductModel, Collection<WarehouseModel> paramCollection);


    int getStockLevelAmount(ProductModel paramProductModel, WarehouseModel paramWarehouseModel);


    void updateActualStockLevel(ProductModel paramProductModel, WarehouseModel paramWarehouseModel, int paramInt, String paramString);


    void reserve(ProductModel paramProductModel, WarehouseModel paramWarehouseModel, int paramInt, String paramString) throws InsufficientStockLevelException;


    void release(ProductModel paramProductModel, WarehouseModel paramWarehouseModel, int paramInt, String paramString);


    void setInStockStatus(ProductModel paramProductModel, Collection<WarehouseModel> paramCollection, InStockStatus paramInStockStatus);


    InStockStatus getInStockStatus(ProductModel paramProductModel, WarehouseModel paramWarehouseModel);


    String getAvailability(ProductModel paramProductModel, WarehouseModel paramWarehouseModel, Date paramDate, LanguageModel paramLanguageModel);


    String getAvailability(ProductModel paramProductModel, List<WarehouseModel> paramList, Date paramDate, LanguageModel paramLanguageModel);


    String getAvailability(ProductModel paramProductModel, WarehouseModel paramWarehouseModel, int paramInt, LanguageModel paramLanguageModel);


    String getAvailability(ProductModel paramProductModel, List<WarehouseModel> paramList, int paramInt, LanguageModel paramLanguageModel);


    WarehouseModel getBestMatchOfQuantity(Map<WarehouseModel, Integer> paramMap);


    WarehouseModel getBestMatchOfAvailability(Map<WarehouseModel, Date> paramMap);


    StockLevelModel getStockLevel(ProductModel paramProductModel, WarehouseModel paramWarehouseModel);


    Collection<StockLevelModel> getAllStockLevels(ProductModel paramProductModel);


    Collection<StockLevelModel> getStockLevels(ProductModel paramProductModel, Collection<WarehouseModel> paramCollection);


    Map<WarehouseModel, Integer> getAvailability(List<WarehouseModel> paramList, ProductModel paramProductModel, Date paramDate);


    Map<WarehouseModel, Date> getAvailability(List<WarehouseModel> paramList, ProductModel paramProductModel, int paramInt);
}
