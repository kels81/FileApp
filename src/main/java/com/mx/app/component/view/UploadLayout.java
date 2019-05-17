/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.view;

import com.mx.app.event.*;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;

/**
 *
 * @author ecortesh
 */
public class UploadLayout extends CssLayout {

    private final AppShowSideBar appShowSideBar;

    public UploadLayout(AppShowSideBar cleanDisplay) {
        super();
        this.appShowSideBar = cleanDisplay;

        addStyleName("product-form");
        addStyleName("product-form-wrapper");

        addComponent(body());

        Responsive.makeResponsive(this);
    }

    private Component body() {
        VerticalLayout lay = new VerticalLayout();
        lay.setSizeFull();
//        lay.addStyleName("product-form-wrapper");

        Button button = new Button("Test");
        button.addClickListener((event) -> {
            showSideBar(false);
        });

        lay.addComponent(button);

        return lay;
    }

    private void showSideBar(boolean show) {
        appShowSideBar.showSideBar(show);
    }

}
