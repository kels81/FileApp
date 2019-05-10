/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.utils;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Eduardo
 */
public class Components {


    public Button createButtonPrimary(String caption) {
        Button btn = new Button(caption);
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        btn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        //btn.setEnabled(false);
        return btn;
    }
    
    public Button createButtonIconTiny() {
        Button btn = new Button();
        btn.addStyleName(ValoTheme.BUTTON_TINY);
        btn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        //btn.setEnabled(false);
        return btn;
    }
    
    public Button createButtonNormal(String caption) {
        Button btn = new Button(caption);
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        //btn.addStyleName("mybutton");
        //btn.setEnabled(false);
        return btn;
    }

    public Button createButtonPath(String caption) {
        Button btn = new Button(caption);
        btn.setStyleName("btnPath");
        return btn;
    }
    
    public MenuBar createMenuButtonPath() {
        MenuBar menubtn = new MenuBar();
        menubtn.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        return menubtn;
    }
    
    public Label createLabelHeader(String caption) {
        Label lbl = new Label(caption);
        lbl.setSizeUndefined();
        lbl.addStyleName(ValoTheme.LABEL_H1);
        lbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        return lbl;
    }
    

}
