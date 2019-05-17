/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.view;

import com.mx.app.component.BoxFrame;
import com.mx.app.data.Item;
import com.mx.app.event.AppCleanAndDisplay;
import com.mx.app.logic.*;
import com.mx.app.utils.Components;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import java.util.List;

/**
 *
 * @author Edrd
 */
public class FileMosaicoLayout extends CssLayout {
//public class FileGridLayout extends Panel implements LayoutClickListener {

//    private Item file;

    private final Button downloadInvisibleButton = new Button();
//    private final Components component = new Components();

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;
//    private final AppCleanAndDisplay appCleanDisplay;
    

    public FileMosaicoLayout(FileLogic mosaicoFileLogic, DirectoryLogic mosaicoDirectoryLogic, Item file) {
        this.viewLogicFile = mosaicoFileLogic;
        this.viewLogicDirectory = mosaicoDirectoryLogic;
//        this.appCleanDisplay = cleanDisplay;

        addStyleName("gridView");
        setSizeFull();
        Responsive.makeResponsive(this);

        Item currentDir = new Item(file.getPath());
//        List<Item> files = (List<Item>) component.directoryContents(currentDir);
        List<Item> files = currentDir.getContentDirectory(currentDir);

        for (Item item : files) {
//            this.file = file_;
            BoxFrame boxframe = new BoxFrame(item, viewLogicFile, viewLogicDirectory, downloadInvisibleButton);
            addComponent(boxframe);
        }
        
//        System.out.println("width-->" + Page.getCurrent().getBrowserWindowWidth());
//        System.out.println("height-->" + Page.getCurrent().getBrowserWindowHeight());

        //BUTTON PARA PODER DESCARGAR ARCHIVOS POR MEDIO DEL CONTEXT MENU
        downloadInvisibleButton.setId("DownloadButtonId");
        downloadInvisibleButton.addStyleName("InvisibleButton");
        addComponent(downloadInvisibleButton);
    }
    
//     private void showContentDirectory(Item directory) {
//        appCleanDisplay.showContentDirectory(directory);
//    }


}
