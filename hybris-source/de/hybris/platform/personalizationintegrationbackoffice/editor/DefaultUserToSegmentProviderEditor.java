package de.hybris.platform.personalizationintegrationbackoffice.editor;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import de.hybris.platform.personalizationintegrationbackoffice.editor.facade.UserSegmentsProviderSearchFacade;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Resource;
import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

public class DefaultUserToSegmentProviderEditor implements CockpitEditorRenderer<String>
{
    @Resource
    private UserSegmentsProviderSearchFacade facade;


    public void render(Component parent, EditorContext<String> context, EditorListener<String> listener)
    {
        Combobox editorView = new Combobox();
        editorView.setValue((String)context.getInitialValue());
        ArrayList<String> options = new ArrayList<>(Collections.singletonList(""));
        options.addAll(this.facade.getProviders());
        ListModelList listModelList = new ListModelList(options);
        editorView.setModel((ListModel)listModelList);
        editorView.setReadonly(true);
        editorView.addEventListener("onChange", event -> handleEvent((Textbox)editorView, event, listener));
        editorView.addEventListener("onOK", event -> handleEvent((Textbox)editorView, event, listener));
        editorView.setParent(parent);
    }


    protected void handleEvent(Textbox editorView, Event event, EditorListener<String> listener)
    {
        String result = (String)editorView.getRawValue();
        listener.onValueChanged(!StringUtils.hasLength(result) ? "" : result);
        if("onOK".equals(event.getName()))
        {
            listener.onEditorEvent("enter_pressed");
        }
    }
}
