/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.ItemProperty;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author ecortesh
 */
public class BoxFrame extends CssLayout {

    private HorizontalLayout boxFrame;
    private HorizontalLayout mosaicoLayout;
    private VerticalLayout fileDetails;
    private Label lblNumberOfElementsAndFileSize;

    private final Item file;
    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;
    private final Button downloadInvisibleButton;

    public BoxFrame(Item file, FileLogic mosaicoFileLogic, DirectoryLogic mosaicoDirectoryLogic, Button downloadInvisibleButton) {
        super();
        this.file = file;
        this.viewLogicFile = mosaicoFileLogic;
        this.viewLogicDirectory = mosaicoDirectoryLogic;
        this.downloadInvisibleButton = downloadInvisibleButton;

        addStyleName("mainPanel");
        addComponent(buildBoxFrame());
    }

    private HorizontalLayout buildBoxFrame() {
        boxFrame = new HorizontalLayout();
        boxFrame.setSizeFull();
        boxFrame.addStyleName("frame");
        boxFrame.setMargin(true);
        boxFrame.setSpacing(true);
        boxFrame.addStyleName(ValoTheme.LAYOUT_CARD);
        boxFrame.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            if (event.isDoubleClick()) {
                if (file.isDirectory()) {
                    viewLogicDirectory.cleanAndDisplay(file);
                } else if (file.isFile()) {
                    Notification.show("Ver archivo: " + file.getName());
//                        Window w = new ViewerWindow(file_);
//                        UI.getCurrent().addWindow(w);
//                        w.focus();
                }
            }
        });

        Component boxContent = buildBoxContent();
        ButtonContextMenu btnContextMenu = new ButtonContextMenu(downloadInvisibleButton, file, viewLogicFile, viewLogicDirectory);

        boxFrame.addComponents(boxContent, btnContextMenu);
        boxFrame.setExpandRatio(boxContent, 1.0f);
        boxFrame.setComponentAlignment(btnContextMenu, Alignment.TOP_RIGHT);
        return boxFrame;
    }

    private HorizontalLayout buildBoxContent() {
        Component icon = new ItemProperty(file).buildIcon(true);
        Component details = buildFileDetails();

        mosaicoLayout = new HorizontalLayout();
        mosaicoLayout.setSizeFull();
        mosaicoLayout.setMargin(false);
        mosaicoLayout.setSpacing(false);
        //mosaicoLayout.setDescription(file.getName());
        mosaicoLayout.addComponents(icon, details);
        mosaicoLayout.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
        mosaicoLayout.setExpandRatio(details, 1.0f);

        return mosaicoLayout;
    }

    private VerticalLayout buildFileDetails() {
        Component fileName = new ItemProperty(file).getFileName();
        Component numberOfElements = new ItemProperty(file).getLabelNumberOfElementsAndFileSize();

        fileDetails = new VerticalLayout();
        fileDetails.setMargin(false);
        fileDetails.setSpacing(false);
        fileDetails.addComponents(fileName, numberOfElements);
        fileDetails.setComponentAlignment(fileName, Alignment.BOTTOM_LEFT);
        fileDetails.setComponentAlignment(numberOfElements, Alignment.BOTTOM_LEFT);

        return fileDetails;
    }

}
