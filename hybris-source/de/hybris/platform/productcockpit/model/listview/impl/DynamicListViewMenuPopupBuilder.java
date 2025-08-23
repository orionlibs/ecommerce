package de.hybris.platform.productcockpit.model.listview.impl;

import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.cockpit.model.listview.ColumnGroup;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultListViewMenuPopupBuilder;
import de.hybris.platform.cockpit.model.meta.impl.ClassificationClassPath;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.productcockpit.services.catalog.ClassificationSystemService;
import java.util.List;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class DynamicListViewMenuPopupBuilder extends DefaultListViewMenuPopupBuilder
{
    private static final String LISTVIEW_COLUMN_MENU_CLASSIFICATION_SYSTEM = "listview.column.menu.classification.system";
    private ClassificationSystemService classificationSystemService = null;


    public void buildClassificationSystemElement(UIListView listView, Menupopup menuPopup, int colIndex)
    {
        Menupopup clsfSystemMenuPopup = createNewMenuPopup(Labels.getLabel("listview.column.menu.classification.system"), menuPopup);
        clsfSystemMenuPopup.addEventListener("onOpen", (EventListener)new Object(this, listView, clsfSystemMenuPopup, colIndex));
    }


    public ClassificationSystemService getClassificationSystemService()
    {
        if(this.classificationSystemService == null)
        {
            this.classificationSystemService = (ClassificationSystemService)SpringUtil.getBean("classificationSystemService");
        }
        return this.classificationSystemService;
    }


    private int buildClassificationSystemsLevel(UIListView listView, Menupopup clsfSystemMenuPopup, int colIndex)
    {
        int popupWidth = 0;
        clsfSystemMenuPopup.getChildren().clear();
        List<ClassificationSystemModel> classificationSystems = getClassificationSystemService().getClassificationSystems();
        for(ClassificationSystemModel classificationSystemModel : classificationSystems)
        {
            String name = classificationSystemModel.getName();
            if(name != null)
            {
                popupWidth = Math.max(popupWidth, name.length());
                Menupopup catalogVersionsPopup = createNewMenuPopup(name, clsfSystemMenuPopup);
                catalogVersionsPopup.addEventListener("onOpen", (EventListener)new Object(this, listView, classificationSystemModel, catalogVersionsPopup, colIndex));
            }
        }
        return popupWidth;
    }


    private int buildCategoriesLevel(UIListView listView, ClassificationSystemModel classificationSystemModel, Menupopup menuPopup, int colIndex)
    {
        int popupWidth = 0;
        menuPopup.getChildren().clear();
        List<ClassificationSystemVersionModel> classificationSystemVersions = getClassificationSystemService().getClassificationSystemVersions(classificationSystemModel);
        for(ClassificationSystemVersionModel classificationSystemVersionModel : classificationSystemVersions)
        {
            buildRootCategoriesLevel(listView, classificationSystemVersionModel, menuPopup, colIndex);
        }
        return 0;
    }


    public void addAllColumnLogic(UIListView listView, Menupopup allColumnsMenuPopup, int colIndex)
    {
        allColumnsMenuPopup.getChildren().clear();
        ColumnGroup group = listView.getModel().getColumnComponentModel().getRootColumnGroup();
        int size = buildHiddenColumnMenupopup(listView, group, allColumnsMenuPopup, colIndex);
        buildClassificationSystemElement(listView, allColumnsMenuPopup, colIndex);
        allColumnsMenuPopup.setWidth("" + size + 2 + "em");
        allColumnsMenuPopup.setStyle("min-width:" + size + 2 + "em");
    }


    private int buildRootCategoriesLevel(UIListView listView, ClassificationSystemVersionModel classificationSystemVersionModel, Menupopup menuPopup, int colIndex)
    {
        menuPopup.getChildren().clear();
        List<ClassificationClassModel> classificationRootCategories = getClassificationSystemService().getClassificationRootCategories(classificationSystemVersionModel);
        return createClassificationMenuEntries(listView, menuPopup, colIndex, classificationRootCategories);
    }


    private int buildSubcategoriesLevel(UIListView listView, ClassificationClassModel classificationRootCategory, Menupopup menuPopup, int colIndex)
    {
        menuPopup.getChildren().clear();
        List<ClassificationClassModel> classificationSubcategories = getClassificationSystemService().getClassificationSubcategories(classificationRootCategory);
        return createClassificationMenuEntries(listView, menuPopup, colIndex, classificationSubcategories);
    }


    private int createClassificationMenuEntries(UIListView listView, Menupopup menuPopup, int colIndex, List<ClassificationClassModel> classificationCategories)
    {
        int popupWidth = 0;
        for(ClassificationClassModel category : classificationCategories)
        {
            String name = category.getName();
            if(name != null)
            {
                popupWidth = Math.max(popupWidth, name.length());
            }
            Menupopup clsfCategoryPopup = createNewMenuPopup(name, menuPopup);
            clsfCategoryPopup.setWidth("" + popupWidth + 2 + "em");
            clsfCategoryPopup.setStyle("min-width:" + popupWidth + 2 + "em");
            clsfCategoryPopup.addEventListener("onOpen", (EventListener)new Object(this, category, listView, clsfCategoryPopup, colIndex));
        }
        return popupWidth;
    }


    private void createClassificationLeaves(UIListView listView, int colIndex, ClassificationClassModel subcategory, Menupopup clsfCategoryPopup, List<ClassificationAttributeModel> classificationAttributes)
    {
        for(ClassificationAttributeModel classificationAttributeModel : classificationAttributes)
        {
            String attrName = classificationAttributeModel.getName();
            Menuitem attribute = new Menuitem(attrName);
            clsfCategoryPopup.appendChild((Component)attribute);
            attribute.addEventListener("onClick", (EventListener)new Object(this, subcategory, classificationAttributeModel, listView, colIndex));
        }
    }


    private String getClassificationPath(ClassificationClassModel classModel, ClassificationAttributeModel attrModel)
    {
        return
                        ClassificationClassPath.getClassCode((ClassificationClass)UISessionUtils.getCurrentSession().getModelService().getSource(classModel)) + "." + ClassificationClassPath.getClassCode((ClassificationClass)UISessionUtils.getCurrentSession().getModelService().getSource(classModel));
    }
}
