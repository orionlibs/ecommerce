package de.hybris.platform.configurablebundlebackoffice.widgets.editor.flattypehierarchyreferenceeditor;

import com.hybris.cockpitng.common.model.TypeSelectorTreeModel;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLogic;
import de.hybris.platform.configurablebundlebackoffice.widgets.editor.flattypehierarchyreferenceeditor.model.FlatHierarchyTypeSelectorTreeModel;

public class FlatTypeHierarchyReferenceEditorLayout<T> extends ReferenceEditorLayout<T>
{
    public FlatTypeHierarchyReferenceEditorLayout(ReferenceEditorLogic<T> referenceEditorInterface)
    {
        super(referenceEditorInterface);
    }


    public FlatTypeHierarchyReferenceEditorLayout(ReferenceEditorLogic<T> referenceEditorInterface, Base configuration)
    {
        super(referenceEditorInterface, configuration);
    }


    public TypeSelectorTreeModel createTypeSelectorTreeModel(String typeCode) throws TypeNotFoundException
    {
        return (TypeSelectorTreeModel)new FlatHierarchyTypeSelectorTreeModel(getTypeFacade().load(typeCode), getTypeFacade(), getPermissionFacade());
    }
}
