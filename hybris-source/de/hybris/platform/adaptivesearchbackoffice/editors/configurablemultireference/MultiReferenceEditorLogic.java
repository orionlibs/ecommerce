package de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference;

import com.hybris.cockpitng.components.Editor;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorLogic;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRenderer;
import java.util.Collection;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListModel;

public interface MultiReferenceEditorLogic<D extends de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData, V> extends EditorLogic<Collection<V>>
{
    String getContext();


    boolean isSortable();


    Collection<String> getColumns();


    Collection<String> getEditableColumns();


    DataHandler<D, V> getDataHandler();


    ListModel<D> getListModel();


    EditorRenderer getItemMasterRenderer();


    EditorRenderer getItemDetailRenderer();


    boolean isOpen(Component paramComponent);


    void setOpen(Component paramComponent, boolean paramBoolean);


    Editor findEditor(Component paramComponent);


    Component findEditorItem(Component paramComponent);


    void updateValue(Collection<V> paramCollection);


    void updateAttributeValue(D paramD, String paramString, Object paramObject);


    void triggerCreateReference();


    void triggerUpdateReference(D paramD);
}
