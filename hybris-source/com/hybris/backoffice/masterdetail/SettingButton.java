/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.masterdetail;

public class SettingButton
{
    private TypesEnum type = TypesEnum.CLOSE;
    private boolean visible = true;
    private boolean disabled = false;


    public enum TypesEnum
    {
        SAVE,
        CLOSE,
        SAVE_AND_CLOSE,
        CANCEL
    }


    public TypesEnum getType()
    {
        return type;
    }


    public void setType(final TypesEnum type)
    {
        this.type = type;
    }


    public boolean isVisible()
    {
        return visible;
    }


    public void setVisible(final boolean visible)
    {
        this.visible = visible;
    }


    public boolean isDisabled()
    {
        return disabled;
    }


    public void setDisabled(final boolean disabled)
    {
        this.disabled = disabled;
    }


    public static class Builder
    {
        private SettingButton button = new SettingButton();


        public Builder setType(final TypesEnum type)
        {
            this.button.setType(type);
            return this;
        }


        public Builder setVisible(final boolean visible)
        {
            this.button.setVisible(visible);
            return this;
        }


        public Builder setDisabled(final boolean disabled)
        {
            this.button.setDisabled(disabled);
            return this;
        }


        public SettingButton build()
        {
            return this.button;
        }
    }
}
