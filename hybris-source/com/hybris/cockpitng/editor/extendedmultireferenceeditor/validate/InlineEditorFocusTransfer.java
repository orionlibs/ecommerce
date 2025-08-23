/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate;

import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowStateUtil;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class InlineEditorFocusTransfer<T> implements ValidationFocusTransferHandler
{
    private final EditorState<T> editorState;


    public InlineEditorFocusTransfer(final EditorState<T> editorState)
    {
        this.editorState = editorState;
    }


    protected Listitem getItem(final Component parent, final String path)
    {
        final int rowIndex = EditorState.getRowIndex(path);
        final int viewIndex = RowStateUtil.convertModelIndexToView(((Listbox)parent).getModel(), editorState, rowIndex);
        return ((Listbox)parent).getItemAtIndex(viewIndex);
    }


    protected String getItemPath(final String path)
    {
        return EditorState.getRowPath(path);
    }


    @Override
    public int focusValidationPath(final Component parent, final String path)
    {
        final String propertyName = removeLanguagePostfixFromProperty(getItemPath(path));
        final Listitem row = getItem(parent, path);
        final Optional<Component> cell = row.getChildren().stream()
                        .filter(component -> StringUtils.equals((String)component.getAttribute("qualifier"), propertyName)).findFirst();
        if(cell.isPresent() && StringUtils.isNotBlank(cell.get().getUuid()))
        {
            final String scrollIntoParentContainer = String.format(
                            "			var element=document.getElementById('%s'); "
                                            + "if (element!==undefined) "
                                            + "{"
                                            + "	var rect = element.getBoundingClientRect();"
                                            + "	var elementIsInView ="
                                            + "        	((rect.height > 0 || rect.width > 0) && rect.bottom >= 0 && rect.right >= 0 "
                                            + "			&& rect.top <= (window.innerHeight || document.documentElement.clientHeight) "
                                            + "			&& rect.left <= (window.innerWidth || document.documentElement.clientWidth));"
                                            + "	if(!elementIsInView)"
                                            + "	{"
                                            + "	 	element.scrollIntoView(false);"
                                            + "	}"
                                            + "}", cell.get().getUuid());
            Clients.evalJavaScript(scrollIntoParentContainer);
            Events.postEvent(Events.ON_CLICK, cell.get(), null);
            return TRANSFER_SUCCESS;
        }
        else
        {
            return TRANSFER_ERROR_UNKNOWN_PATH;
        }
    }


    protected String removeLanguagePostfixFromProperty(final String propertyName)
    {
        return propertyName.replaceAll("\\[{1}[a-zA-Z]{2}\\]{1}$", StringUtils.EMPTY);
    }
}
