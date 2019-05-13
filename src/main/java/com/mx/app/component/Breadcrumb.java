/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.mx.app.data.Item;
import com.mx.app.event.AppCleanAndDisplay;
import com.mx.app.utils.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.*;

/**
 *
 * @author ecortesh
 */
public final class Breadcrumb extends HorizontalLayout {

    private Button btnPath;
    private MenuBar btnDirectories;
    private Label lblArrow;
    private final AppCleanAndDisplay appCleanDisplay;

    private final Components component = new Components();

    public Breadcrumb(Item itemDir, AppCleanAndDisplay cleanDisplay) {
        this.appCleanDisplay = cleanDisplay;

        List<Item> listDirectories = getListDirectories(itemDir);

        if (listDirectories.size() > 3) {
            createShortWay(listDirectories);
        } else {
            createNormalWay(listDirectories);
        }

    }

    private void createNormalWay(List<Item> listDirectories) {
        int size = listDirectories.size();
        int count = 1;
        for (Item directory : listDirectories) {
            btnPath = createButtonPath(directory, size, count);
            addComponent(btnPath);

            if (count != size) {
                lblArrow = component.createLabelArrow();

                addComponent(lblArrow);
                setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);
            }
            count++;
        }
    }

    private void createShortWay(List<Item> listDirectories) {
        int lstSize = listDirectories.size();
        btnDirectories = createMenuButton(listDirectories);
        addComponent(btnDirectories);

        lblArrow = component.createLabelArrow();
        addComponent(lblArrow);
        setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);

        btnPath = createButtonPath(listDirectories.get(lstSize - 1), lstSize, lstSize);
        addComponent(btnPath);
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

    public Button createButtonPath(Item itemDir, int listDirectoriesSize, int count) {
        Button btn = component.createButtonPath(itemDir.getName());
        btn.setEnabled((count != listDirectoriesSize));     //PARA QUE NO TENGA CLICK  EL BUTTON
        btn.addStyleName((count == listDirectoriesSize ? "labelColored" : ""));
        btn.addClickListener((event) -> showContentDirectory(itemDir));

        return btn;
    }

    //private MenuBar createMenuButton(Item itemDir) {
    private MenuBar createMenuButton(List<Item> listDirectories) {
        MenuBar menuBtn = component.createMenuButtonPath();
        MenuBar.MenuItem dropdown = menuBtn.addItem("", FontAwesome.FOLDER_O, null);

        listDirectories = listDirectories.subList(0, (listDirectories.size() - 1));

        for (Item directory : listDirectories) {
            dropdown.addItem(directory.getName(), (event) -> showContentDirectory(directory));
        }

        return menuBtn;
    }

    private void showContentDirectory(Item directory) {
        appCleanDisplay.showContentDirectory(directory);
    }
}
