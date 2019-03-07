/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.view;

import com.mx.app.component.ButtonContextMenu;
import com.mx.app.component.ItemProperty;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.logic.FileLogic;
import com.mx.app.utils.Components;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.List;

/**
 *
 * @author Edrd
 */
public class FileGridLayout extends CssLayout {

    private HorizontalLayout boxFrame;
    private HorizontalLayout mosaicoLayout;
    private VerticalLayout fileDetails;
    private File file;
    private CssLayout mainPanel;

    private Label lblNumberOfElementsAndFileSize;
    
    private ButtonContextMenu btnContextMenu;
    private  Component boxContent;
    private ItemProperty itemProperty;
    

    private final Button downloadInvisibleButton = new Button();
    private final Components component = new Components();

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

    public FileGridLayout(FileLogic mosaicoFileLogic, DirectoryLogic mosaicoDirectoryLogic, File file) {
        this.viewLogicFile = mosaicoFileLogic;
        this.viewLogicDirectory = mosaicoDirectoryLogic;

        addStyleName("gridView");
        setSizeFull();
        Responsive.makeResponsive(this);

        File currentDir = new File(file.getAbsolutePath());
        List<File> files = (List<File>) component.directoryContents(currentDir);

        for (File file_ : files) {
            this.file = file_;
            addComponent(buildMosaico(file_));
        }
        
        //BUTTON PARA PODER DESCARGAR ARCHIVOS POR MEDIO DEL ButtonContextMenu
        downloadInvisibleButton.setId("DownloadButtonId");
        downloadInvisibleButton.addStyleName("InvisibleButton");
        addComponent(downloadInvisibleButton);
    }

    private Component buildMosaico(File file) {
        mainPanel = new CssLayout();
        mainPanel.addStyleName("mainPanel");
        mainPanel.addComponent(buildBoxFrame(file));

        return mainPanel;
    }

    private HorizontalLayout buildBoxFrame(File file) {
        boxFrame = new HorizontalLayout();
        boxFrame.setSizeFull();
        boxFrame.addStyleName("frame");
        boxFrame.setMargin(true);
        boxFrame.setSpacing(true);
        boxFrame.addStyleName(ValoTheme.LAYOUT_CARD);
        boxFrame.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.isDoubleClick()) {
                    if (file.isDirectory()) {
                        viewLogicFile.cleanAndDisplay(file);
                    } else if (file.isFile()) {
                        Notification.show("Ver archivo: " + file.getName());
//                        Window w = new ViewerWindow(file_);
//                        UI.getCurrent().addWindow(w);
//                        w.focus();
                    }
                }
            }
        });
        
        boxContent = buildBoxContent(); 
        btnContextMenu = component.createButtonContextMenu(downloadInvisibleButton, file, viewLogicFile, viewLogicDirectory);
        
        boxFrame.addComponents(boxContent, btnContextMenu);
        boxFrame.setExpandRatio(boxContent, 1.0f);
        boxFrame.setComponentAlignment(btnContextMenu, Alignment.TOP_RIGHT);

        return boxFrame;
    }
    
    private HorizontalLayout buildBoxContent() {
        itemProperty = new ItemProperty(file);
        Component icon = itemProperty.buildIcon(true);
        Component details = buildFileDetails();
        
        mosaicoLayout = new HorizontalLayout();
        mosaicoLayout.setSizeFull();
        mosaicoLayout.setMargin(false);
        mosaicoLayout.setSpacing(false);
        mosaicoLayout.setDescription(file.getName());
        mosaicoLayout.addComponents(icon, details);
        mosaicoLayout.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
        mosaicoLayout.setExpandRatio(details, 1.0f);

        return mosaicoLayout;
    }

    private VerticalLayout buildFileDetails() {
        Component fileName = getFileName();
        Component numberOfElements = createLabelNumberOfElementsAndFileSize();

        fileDetails = new VerticalLayout();
        fileDetails.setMargin(false);
        fileDetails.setSpacing(false);
        fileDetails.addComponents(fileName, numberOfElements);
        fileDetails.setComponentAlignment(fileName, Alignment.BOTTOM_LEFT);
        fileDetails.setComponentAlignment(numberOfElements, Alignment.BOTTOM_LEFT);
        return fileDetails;
    }
    
    public Label getFileName() {
        Label lblName = new Label(file.getName());
        lblName.setSizeFull();
        lblName.addStyleName("labelName");
        lblName.addStyleName("noselect");
        return lblName;
    }

    private Label createLabelNumberOfElementsAndFileSize() {
        lblNumberOfElementsAndFileSize = new Label(itemProperty.getNumberOfElementsAndFileSize());
        lblNumberOfElementsAndFileSize.addStyleName("labelInfo");
        lblNumberOfElementsAndFileSize.addStyleName("noselect");
        lblNumberOfElementsAndFileSize.addStyleName(ValoTheme.LABEL_TINY);

        return lblNumberOfElementsAndFileSize;
    }
    
}
