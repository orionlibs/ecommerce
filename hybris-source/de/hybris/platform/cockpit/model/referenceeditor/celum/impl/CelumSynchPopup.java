package de.hybris.platform.cockpit.model.referenceeditor.celum.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.celum.CockpitCelumDelegate;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class CelumSynchPopup extends Window
{
    private transient HtmlBasedComponent localPreviewComponent = null;
    private transient HtmlBasedComponent celumPreviewComponent = null;
    private final CockpitCelumDelegate delegate;
    private final TypedObject celumAsset;
    private final EditorListener editorListener;
    private boolean doSynch = false;


    public CelumSynchPopup(CockpitCelumDelegate delegate, TypedObject celumAsset, EditorListener listener) throws CelumNotAvailableException, IllegalArgumentException
    {
        this.delegate = delegate;
        this.celumAsset = celumAsset;
        this.editorListener = listener;
        String title = Labels.getLabel("editor.celumeditor.synch.popup.title");
        title = StringUtils.isBlank(title) ? "" : title;
        setSclass("celum_synch_popup");
        setTitle(title);
        setBorder("none");
        setClosable(true);
        appendChild((Component)createPopupContentContainer());
    }


    private HtmlBasedComponent createPopupContentContainer() throws IllegalArgumentException, CelumNotAvailableException
    {
        Div div1 = new Div();
        div1.setHeight("100%");
        Vbox mainVbox = new Vbox();
        mainVbox.setParent((Component)div1);
        mainVbox.setHeight("100%");
        mainVbox.setHeights("none, 30px");
        mainVbox.appendChild((Component)createCelumPopupContent());
        Div btnContainer = new Div();
        btnContainer.setParent((Component)mainVbox);
        btnContainer.setHeight("30px");
        btnContainer.setSclass("celum_synch_popup_btn_container");
        Button closeBtn = new Button(Labels.getLabel("general.close"));
        closeBtn.setParent((Component)btnContainer);
        closeBtn.setSclass("btnblue");
        UITools.addBusyListener((Component)closeBtn, "onClick", (EventListener)new Object(this), null, null);
        Button synchBtn = new Button(Labels.getLabel("general.synchronize"));
        btnContainer.insertBefore((Component)synchBtn, (Component)closeBtn);
        synchBtn.addEventListener("onClick", (EventListener)new Object(this, synchBtn, closeBtn));
        return (HtmlBasedComponent)div1;
    }


    private HtmlBasedComponent createCelumPopupContent() throws IllegalArgumentException, CelumNotAvailableException
    {
        Div popupContent = new Div();
        Hbox hbox = new Hbox();
        hbox.setParent((Component)popupContent);
        if(this.celumAsset != null)
        {
            popupContent.setHeight("100%");
            popupContent.setSclass("celum_synch_popup_content");
            this.localPreviewComponent = createPreviewComponent(Labels.getLabel("editor.celumeditor.synch.popup.preview.local"), this.delegate
                            .getLocalMediaUrl(this.celumAsset));
            hbox.appendChild((Component)this.localPreviewComponent);
            this.celumPreviewComponent = createPreviewComponent(Labels.getLabel("editor.celumeditor.synch.popup.preview.current"), this.delegate
                            .getMediaUrl(this.celumAsset));
            hbox.appendChild((Component)this.celumPreviewComponent);
        }
        return (HtmlBasedComponent)popupContent;
    }


    private HtmlBasedComponent createPreviewComponent(String text, String url)
    {
        Div prevComp = new Div();
        prevComp.setSclass("celumPreviewComponent");
        Vbox vbox = new Vbox();
        vbox.setParent((Component)prevComp);
        vbox.setHeight("100%");
        vbox.setWidth("100%");
        vbox.setHeights("20px, none");
        Label label = new Label(text);
        label.setParent((Component)vbox);
        label.setSclass("celum_synch_popup_prev_label");
        Image prevImg = new Image(url);
        prevImg.setParent((Component)vbox);
        prevImg.setSclass("celum_synch_popup_prev_img");
        return (HtmlBasedComponent)prevComp;
    }
}
