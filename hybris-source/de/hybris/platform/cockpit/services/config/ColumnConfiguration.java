package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Map;

public interface ColumnConfiguration
{
    DefaultColumnDescriptor getColumnDescriptor();


    UIEditor getCellEditor();


    void setCellEditor(UIEditor paramUIEditor);


    CellRenderer getCellRenderer();


    void setCellRenderer(CellRenderer paramCellRenderer);


    ValueHandler getValueHandler();


    String getName();


    void setName(String paramString);


    boolean isVisible();


    void setVisible(boolean paramBoolean);


    int getPosition();


    void setPosition(int paramInt);


    boolean isSortable();


    boolean isSelectable();


    List<LanguageModel> getLanguages();


    void setLanguages(List<LanguageModel> paramList);


    boolean isLocalized();


    String getEditor();


    void setEditor(String paramString);


    Map<String, String> getParameters();


    String getParameter(String paramString);


    void setParameters(Map<String, String> paramMap);


    void setParameter(String paramString1, String paramString2);


    String getWidth();


    void setWidth(String paramString);
}
