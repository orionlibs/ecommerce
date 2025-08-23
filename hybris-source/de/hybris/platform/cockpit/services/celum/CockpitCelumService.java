package de.hybris.platform.cockpit.services.celum;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;

public interface CockpitCelumService
{
    public static final String REQUEST_EVENT_PREFIX = "cel";
    public static final String CELUM_CONTAINER_KEY = "cont";


    String getDirectCelumLink(ItemModel paramItemModel, String paramString, Map<String, String> paramMap);


    String getDirectCelumLink(String paramString, Map<String, String> paramMap);


    String getCelumUrl(TypedObject paramTypedObject) throws IllegalArgumentException;


    String getLocalUrl(TypedObject paramTypedObject) throws IllegalArgumentException;


    void synchronize(TypedObject paramTypedObject);


    void unSynchronize(TypedObject paramTypedObject);


    boolean isSynchronized(TypedObject paramTypedObject) throws IllegalArgumentException;


    boolean isAssetValid(TypedObject paramTypedObject);


    Boolean isCelumAlive();


    String getOriginalMediaFormat();


    Integer getCelumAssetId(ItemModel paramItemModel);
}
