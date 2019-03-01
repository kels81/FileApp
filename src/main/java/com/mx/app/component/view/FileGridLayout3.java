/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.view;

import com.mx.app.component.BoxFrame;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.logic.FileLogic;
import com.mx.app.utils.Components;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.util.List;

/**
 *
 * @author Edrd
 */
public class FileGridLayout3 extends CssLayout {
//public class FileGridLayout extends Panel implements LayoutClickListener {

    private HorizontalLayout boxFrame;
    private HorizontalLayout mosaicoLayout;
    private ThemeResource iconResource;
    private Image icon;
    private VerticalLayout fileDetails;
    private File file;
    private CssLayout mainPanel;

    private Label lblName;
    private Label lblNumberOfElementsAndFileSize;

    private final Button downloadInvisibleButton = new Button();
    private final Components component = new Components();

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;
    

    public FileGridLayout3(FileLogic mosaicoFileLogic, DirectoryLogic mosaicoDirectoryLogic, File file) {
        this.viewLogicFile = mosaicoFileLogic;
        this.viewLogicDirectory = mosaicoDirectoryLogic;

        addStyleName("gridView");
        setSizeFull();
        Responsive.makeResponsive(this);

        File currentDir = new File(file.getAbsolutePath());
        List<File> files = (List<File>) component.directoryContents(currentDir);

        for (File file_ : files) {
            this.file = file_;
            BoxFrame boxframe = new BoxFrame(file_, viewLogicFile, viewLogicDirectory, downloadInvisibleButton);
            addComponent(boxframe);
        }
        
//        System.out.println("width-->" + Page.getCurrent().getBrowserWindowWidth());
//        System.out.println("height-->" + Page.getCurrent().getBrowserWindowHeight());

        //BUTTON PARA PODER DESCARGAR ARCHIVOS POR MEDIO DEL CONTEXT MENU
        downloadInvisibleButton.setId("DownloadButtonId");
        downloadInvisibleButton.addStyleName("InvisibleButton");
        addComponent(downloadInvisibleButton);
    }

}
