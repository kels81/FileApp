/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.Components;
import com.vaadin.server.*;
import com.vaadin.ui.*;

/**
 *
 * @author ecortesh
 */
public class ViewsButton extends HorizontalLayout {

    private final Button btnListView;
    private final Button btnGridView;

    private final Components component = new Components();
    
    private Boolean selected = true;
    private Boolean isVisible = true;
    
    private final DirectoryLogic viewLogicDirectory;

    public ViewsButton(DirectoryLogic viewsDirectoryLogic, Item directory) {
        this.viewLogicDirectory = viewsDirectoryLogic;
        
        addStyleName("viewbar");
        setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        
        HorizontalLayout viewsButtons =new HorizontalLayout();
        
        btnListView = component.createButtonIconTiny();
        btnListView.setIcon(FontAwesome.BARS);
        //btnListView.addStyleName(setStyle(selected));
        //btnListView.setEnabled(selected);
        btnListView.setDescription("Vista Lista");
        btnListView.setVisible(isVisible);
        btnListView.addClickListener((Button.ClickEvent event) -> {
            selected = false;
            isVisible = false;
            viewLogicDirectory.cleanAndDisplay(directory);
        });

        btnGridView = component.createButtonIconTiny();
        btnGridView.setIcon(FontAwesome.TH_LARGE);
        //btnGridView.addStyleName(setStyle(!selected));
        //btnGridView.setEnabled(!selected);
        btnGridView.setDescription("Vista Grid");
        btnGridView.setVisible(!isVisible);
        btnGridView.addClickListener((Button.ClickEvent event) -> {
            selected = true;
            isVisible = true;
            viewLogicDirectory.cleanAndDisplay(directory);
        });
        
        viewsButtons.addComponents(btnListView, btnGridView);
        
        addComponents(viewsButtons);
        setComponentAlignment(viewsButtons, Alignment.MIDDLE_RIGHT);
    }
    
//    private String setStyle(Boolean selected) {
//        return selected ? "borderButton" : "noBorderButton";
//    }

}
