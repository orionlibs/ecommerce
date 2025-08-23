package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import java.util.Collection;
import java.util.List;

public interface ListViewListener
{
    void move(int paramInt1, int paramInt2);


    void remove(Collection<Integer> paramCollection);


    void activate(List<Integer> paramList);


    void changeSelection(int paramInt1, int paramInt2);


    void changeSelection(List<Integer> paramList1, List<Integer> paramList2);


    void markAllAsSelected(List<Integer> paramList1, List<Integer> paramList2);


    void changeCellValue(int paramInt1, int paramInt2, Object paramObject);


    void sortColumn(int paramInt, boolean paramBoolean);


    void moveColumn(int paramInt1, int paramInt2);


    void hideColumn(int paramInt);


    void showColumn(ColumnDescriptor paramColumnDescriptor, Integer paramInteger);


    void requestPaging(int paramInt);


    void openInContextEditor(int paramInt, UIEditor paramUIEditor, PropertyDescriptor paramPropertyDescriptor);


    void multiEdit(int paramInt, List<Integer> paramList, Object paramObject);


    void drop(int paramInt1, int paramInt2, DragAndDropContext paramDragAndDropContext);
}
