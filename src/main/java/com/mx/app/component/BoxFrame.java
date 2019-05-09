/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.FileFormats;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author ecortesh
 */
public class BoxFrame extends CssLayout {

    private HorizontalLayout boxFrame;
    private HorizontalLayout mosaicoLayout;
    private ThemeResource iconResource;
    private Image icon;
    private VerticalLayout fileDetails;
    private Label lblName;
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
        Component icon = buildIcon();
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

    private Image buildIcon() {
        icon = new Image(null, getIconExtension());
        icon.setWidth((file.isDirectory() ? 43.0f : 40.0f), Unit.PIXELS);
        icon.setHeight((file.isDirectory() ? 43.0f : 49.0f), Unit.PIXELS);
        return icon;
    }

    private ThemeResource getIconExtension() {
        String extension = FilenameUtils.getExtension(file.getPath()).toLowerCase();
        if (file.isDirectory()) {
            iconResource = new ThemeResource("img/file_manager/folder_" + (file.list().length == 0 ? "empty" : "full") + ".png");
        } else {
            //documento
            iconResource = findExtension(extension);
        }
        return iconResource;
    }

    private ThemeResource findExtension(String extension) {
        String formato = "desconocido";

        List<String[]> allFileFormats = new ArrayList<>();
        for (FileFormats fileFormats : FileFormats.values()) {
            allFileFormats.add(fileFormats.getArrayFileFormats());
        }

        for (String[] array : allFileFormats) {
            if (ArrayUtils.contains(array, extension)) {
                formato = FileFormats.values()[allFileFormats.indexOf(array)].toString().toLowerCase();
                break;
            }
        }

        return new ThemeResource("img/file_manager/" + formato + ".png");
    }

    private VerticalLayout buildFileDetails() {
        Component fileName = getFileName();
        Component numberOfElements = getNumberOfElementsAndFileSize();

        fileDetails = new VerticalLayout();
        fileDetails.setMargin(false);
        fileDetails.setSpacing(false);
        fileDetails.addComponents(fileName, numberOfElements);
        fileDetails.setComponentAlignment(fileName, Alignment.BOTTOM_LEFT);
        fileDetails.setComponentAlignment(numberOfElements, Alignment.BOTTOM_LEFT);
        
        return fileDetails;
    }

    private Label getFileName() {
        lblName = new Label(file.getName());
        lblName.setSizeFull();
        lblName.addStyleName("labelName");
        lblName.addStyleName("noselect");
        lblName.addStyleName(ValoTheme.LABEL_BOLD);
        
        return lblName;
    }

    private Label getNumberOfElementsAndFileSize() {
        long fileSize = file.length();
        String fileSizeDisplay = FileUtils.byteCountToDisplaySize(fileSize);
        String label = (file.isDirectory()
                ? String.valueOf(file.list().length == 0
                        ? "" : file.list().length) + (file.list().length > 1
                ? " elementos" : file.list().length == 0
                ? "vacío" : " elemento")
                : fileSizeDisplay);

        lblNumberOfElementsAndFileSize = new Label(label);
        lblNumberOfElementsAndFileSize.addStyleName("labelInfo");
        lblNumberOfElementsAndFileSize.addStyleName("noselect");
        lblNumberOfElementsAndFileSize.addStyleName(ValoTheme.LABEL_TINY);
       
        return lblNumberOfElementsAndFileSize;
    }

}
