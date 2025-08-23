/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer.CellContext;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowStateUtil;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.labels.LabelUtils;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.util.UILabelUtil;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Vlayout;

public class InlinePopup<T>
{
    protected static final String ON_CELL_UPDATE_EVENT = "onCellUpdate";
    protected static final String COMPLEX_TYPE_EDIT_POPUP_SCLASS = "ye-complextype-edit-popup";
    protected static final String COMPLEX_TYPE_MAIN_CNT_SCLASS = "ye-complextype-main-cnt";
    protected static final String COMPLEX_TYPE_LABEL_CNT_SCLASS = "ye-complextype-label-cnt";
    protected static final String COMPLEX_TYPE_EDITOR_CNT_SCLASS = "ye-complextype-editor-cnt";
    protected static final String COMPLEX_TYPE_FOOTER_CNT_SCLASS = "ye-complextype-footer-cnt";
    protected static final String COMPLEX_TYPE_OK_BUTTON = "ye-complextype-ok-button y-btn-primary";
    protected static final String COMPLEX_TYPE_CANCEL_BUTTON = "ye-complextype-cancel-button y-btn-secondary";
    private ObjectValueService objectValueService;
    private LabelService labelService;
    private Popup popup;


    public Popup getPopup()
    {
        return popup;
    }


    protected Popup createPopup()
    {
        return new Popup();
    }


    public void createPopup(final Listcell cell, final CellContext<T> cellContext, final Editor editor)
    {
        popup = createPopup();
        YTestTools.modifyYTestId(popup, "inlinePopup");
        popup.addEventListener(Events.ON_OPEN, event -> {
            if(!((OpenEvent)event).isOpen())
            {
                Events.postEvent(ON_CELL_UPDATE_EVENT, cell, cellContext.getRowEntry());
            }
        });
        cell.appendChild(popup);
        UITools.modifySClass(popup, COMPLEX_TYPE_EDIT_POPUP_SCLASS, true);
        final Div popupContainer = new Div();
        UITools.modifySClass(popupContainer, COMPLEX_TYPE_MAIN_CNT_SCLASS, true);
        popup.appendChild(popupContainer);
        final Vlayout vlayout = new Vlayout();
        popupContainer.appendChild(vlayout);
        final Div labelCnt = new Div();
        UITools.modifySClass(labelCnt, COMPLEX_TYPE_LABEL_CNT_SCLASS, true);
        final Label label = new Label(resolveAttributeLabel(cellContext.getColumnConfig(), cellContext.getRowEntryDataType()));
        labelCnt.appendChild(label);
        vlayout.appendChild(labelCnt);
        final Div editorContainer = new Div();
        UITools.modifySClass(editorContainer, COMPLEX_TYPE_EDITOR_CNT_SCLASS, true);
        editorContainer.appendChild(editor);
        vlayout.appendChild(editorContainer);
        final Div footerCnt = new Div();
        UITools.modifySClass(footerCnt, COMPLEX_TYPE_FOOTER_CNT_SCLASS, true);
        final Button cancelBtn = new Button();
        UITools.modifySClass(cancelBtn, COMPLEX_TYPE_CANCEL_BUTTON, true);
        cancelBtn.addEventListener(Events.ON_CLICK, e -> closePopup(cellContext, cell));
        final Button confirmBtn = new Button();
        YTestTools.modifyYTestId(confirmBtn, "inlinePopupConfirmBtn");
        UITools.modifySClass(confirmBtn, COMPLEX_TYPE_OK_BUTTON, true);
        confirmBtn.addEventListener(Events.ON_CLICK, event -> confirm(editor, cellContext, cell));
        footerCnt.appendChild(confirmBtn);
        footerCnt.appendChild(cancelBtn);
        vlayout.appendChild(footerCnt);
        popup.open(cell, "before_center");
        editor.focus();
    }


    public void closePopup(final CellContext<T> cellContext, final Listcell cell)
    {
        Events.postEvent(ON_CELL_UPDATE_EVENT, cell, cellContext.getRowEntry());
        popup.close();
    }


    public void confirm(final Editor editor, final CellContext<T> cellContext, final Listcell cell)
    {
        final String qualifier = cellContext.getColumnConfig().getQualifier();
        final T rowEntry = cellContext.getRowEntry();
        final Object currentValue = editor.getValue();
        final Object storedValue = getObjectValueService().getValue(qualifier, rowEntry);
        if(!Objects.equals(currentValue, storedValue))
        {
            getObjectValueService().setValue(qualifier, rowEntry, currentValue);
            final RowState rowState = RowStateUtil.getRowState(cellContext);
            if(rowState != null)
            {
                rowState.setPropertyModified(qualifier);
            }
        }
        Events.postEvent(ON_CELL_UPDATE_EVENT, cell, rowEntry);
        popup.close();
    }


    protected String resolveAttributeLabel(final ListColumn listColumn, final DataType genericType)
    {
        final String labelKey = listColumn.getLabel();
        if(StringUtils.isNotBlank(labelKey))
        {
            return UILabelUtil.resolveLocalizedLabel(labelKey);
        }
        String label = getLabelService().getObjectLabel(String.format("%s.%s", genericType.getCode(), listColumn.getQualifier()));
        if(StringUtils.isBlank(label))
        {
            label = LabelUtils.getFallbackLabel(listColumn.getQualifier());
        }
        return label;
    }


    protected ObjectValueService getObjectValueService()
    {
        if(objectValueService == null)
        {
            this.objectValueService = SpringUtil.getApplicationContext().getBean("objectValueService", ObjectValueService.class);
        }
        return objectValueService;
    }


    protected LabelService getLabelService()
    {
        if(labelService == null)
        {
            labelService = SpringUtil.getApplicationContext().getBean("labelService", LabelService.class);
        }
        return labelService;
    }
}
