package de.hybris.platform.cockpit.components.editorarea.renderer;

import de.hybris.platform.cockpit.components.sectionpanel.TooltipRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

public class CommentTooltipRenderer implements TooltipRenderer
{
    private final String DEFAULT_IMG_ATTACHMENT = "/cockpit/images/icon_attachement.png";
    private final String IMG_USER_DUMMY_SMALL = "/cockpit/images/user_dummy_klein.jpg";
    private String dummyImgPath;
    private TypeService typeService;
    private ValueService valueService;


    public Popup renderItemTooltip(ItemModel item)
    {
        AbstractCommentModel abstractComment = null;
        if(item instanceof AbstractCommentModel)
        {
            abstractComment = (AbstractCommentModel)item;
        }
        else
        {
            throw new IllegalArgumentException("Item shoul be of type : AbstractComment");
        }
        Popup popup = new Popup();
        popup.setSclass("commentPopup");
        Div leftCol = new Div();
        leftCol.setSclass("leftCol");
        Object profilePicture = TypeTools.getPropertyValue(getValueService(), getTypeService().wrapItem(abstractComment
                        .getAuthor()), getTypeService().getPropertyDescriptor("Principal.profilePicture"));
        String profilePictureUrl = "/cockpit/images/user_dummy_klein.jpg";
        if(profilePicture != null && !ValueHandler.NOT_READABLE_VALUE.equals(profilePicture))
        {
            profilePictureUrl = UITools.getAdjustedUrl(((MediaModel)((TypedObject)profilePicture).getObject()).getURL());
        }
        String author = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(
                        UISessionUtils.getCurrentSession().getTypeService().wrapItem(abstractComment.getAuthor()));
        Image userImage = new Image(profilePictureUrl);
        userImage.setHeight("50px");
        userImage.setTooltip(author);
        leftCol.appendChild((Component)userImage);
        Div rightCol = new Div();
        rightCol.setSclass("rightCol");
        Div authorDiv = new Div();
        authorDiv.setSclass("author");
        Label authorLabel = new Label(author);
        authorDiv.appendChild((Component)authorLabel);
        rightCol.appendChild((Component)authorDiv);
        Div creationDiv = new Div();
        creationDiv.setSclass("creationTime");
        Label creationLabel = new Label();
        Object creationDate = TypeTools.getPropertyValue(getValueService(), getTypeService().wrapItem(abstractComment),
                        getTypeService().getPropertyDescriptor("Item.modifiedtime"));
        if(ValueHandler.NOT_READABLE_VALUE.equals(creationDate))
        {
            creationLabel.setValue(creationDate.toString());
        }
        else
        {
            creationLabel.setValue(UITools.getLocalDateTime((Date)creationDate));
        }
        creationDiv.appendChild((Component)creationLabel);
        rightCol.appendChild((Component)creationDiv);
        Object subject = TypeTools.getPropertyValue(getValueService(), getTypeService().wrapItem(abstractComment),
                        getTypeService().getPropertyDescriptor("AbstractComment.subject"));
        if(subject != null && !ValueHandler.NOT_READABLE_VALUE.equals(subject))
        {
            Div subjectDiv = new Div();
            subjectDiv.setSclass("subject");
            Label subjectLabel = new Label();
            subjectLabel.setValue((String)subject);
            subjectDiv.appendChild((Component)subjectLabel);
            rightCol.appendChild((Component)subjectDiv);
        }
        String text = TypeTools.getPropertyValueAsString(getValueService(), getTypeService().wrapItem(abstractComment),
                        getTypeService().getPropertyDescriptor("AbstractComment.text"));
        Div commentTextDiv = new Div();
        commentTextDiv.setSclass("commentText");
        Label commentTextLabel = new Label(text);
        commentTextDiv.appendChild((Component)commentTextLabel);
        rightCol.appendChild((Component)commentTextDiv);
        Hbox hbox = new Hbox();
        hbox.appendChild((Component)leftCol);
        hbox.appendChild((Component)rightCol);
        popup.appendChild((Component)hbox);
        return popup;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }


    public String getDummyImgPath()
    {
        return (this.dummyImgPath == null) ? "/cockpit/images/icon_attachement.png" : this.dummyImgPath;
    }


    public void setDummyImgPath(String dummyImgPath)
    {
        this.dummyImgPath = dummyImgPath;
    }
}
