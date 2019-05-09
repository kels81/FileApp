/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.mx.app.data.Item;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.utils.Components;
import com.mx.app.utils.Constantes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.*;

/**
 *
 * @author ecortesh
 */
public class Breadcrumb extends HorizontalLayout {

    private Button btnPath;
    private Label lblArrow;

    private final Components component = new Components();
    
    private final DirectoryLogic viewLogicDirectory;

    public Breadcrumb(DirectoryLogic breadcrumbDirectoryLogic, Item itemDir) {
        this.viewLogicDirectory = breadcrumbDirectoryLogic;
        
        List<Item> listDirectories = getListDirectories(itemDir);
        int i = 1;
        for (Item directory : listDirectories) {
            btnPath = component.createButtonPath(directory.getName());
            btnPath.setEnabled((i != listDirectories.size()));
            btnPath.addStyleName((i == listDirectories.size() ? "labelColored" : ""));
            btnPath.addClickListener(event -> {
//                System.out.println("evnt: " + event.getComponent().getCaption());
                viewLogicDirectory.cleanAndDisplay(directory);
            });
            addComponent(btnPath);
            if (i != listDirectories.size()) {
                lblArrow = new Label(FontAwesome.ANGLE_RIGHT.getHtml(), ContentMode.HTML);
                lblArrow.addStyleName(ValoTheme.LABEL_COLORED);
                addComponent(lblArrow);
                setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);
            }
            i++;
        }

    }

    private List<Item> getListDirectories(Item directory) {
        List<Item> listDirectories = new ArrayList<>();
        String[] arrayDirectories = directory.getPath().split(Constantes.SEPARADOR);
        int idxArchivos = Arrays.asList(arrayDirectories).indexOf(Constantes.ROOT_DIRECTORY);
        String[] newArrayDirectories = Arrays.copyOfRange(arrayDirectories, idxArchivos, arrayDirectories.length);
        StringBuilder newPath = new StringBuilder();
        int i = 1;
        for (String dirName : newArrayDirectories) {
//            int fin = directory.getPath().indexOf(dirName);
//            listDirectories.add(new File(directory.getPath().substring(0, fin + dirName.length())));
            newPath.append(dirName);
            if (i != newArrayDirectories.length) {
                newPath.append("\\");
            }
            listDirectories.add(new Item(Constantes.PATH_BASE + newPath.toString()));
            i++;
        }
        System.out.println("newPath = " + newPath.toString());
        return listDirectories;
    }

}
