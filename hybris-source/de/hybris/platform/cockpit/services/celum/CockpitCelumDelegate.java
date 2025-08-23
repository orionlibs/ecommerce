package de.hybris.platform.cockpit.services.celum;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;
import org.zkoss.zk.ui.Component;

public interface CockpitCelumDelegate
{
    boolean isCelumAvailable();


    String getCelumLink(ItemModel paramItemModel, String paramString, Map<String, String> paramMap) throws CelumNotAvailableException;


    String getCelumLink(String paramString, Map<String, String> paramMap) throws CelumNotAvailableException;


    void createSynchPopup(Component paramComponent, TypedObject paramTypedObject, EditorListener paramEditorListener) throws CelumNotAvailableException, IllegalArgumentException;


    String getMediaUrl(TypedObject paramTypedObject) throws CelumNotAvailableException, IllegalArgumentException;


    String getLocalMediaUrl(TypedObject paramTypedObject) throws CelumNotAvailableException, IllegalArgumentException;


    boolean isSynchronized(TypedObject paramTypedObject) throws CelumNotAvailableException, IllegalArgumentException;


    void synchronize(TypedObject paramTypedObject) throws CelumNotAvailableException, IllegalArgumentException;


    void unSynchronize(TypedObject paramTypedObject) throws CelumNotAvailableException, IllegalArgumentException;


    boolean isCelumAssetValid(TypedObject paramTypedObject) throws CelumNotAvailableException;


    String getOriginalMediaFormat() throws CelumNotAvailableException;


    Integer getCelumAssetId(ItemModel paramItemModel);
}
