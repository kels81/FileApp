/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.utils;

import com.mx.app.component.ButtonContextMenu;
import com.mx.app.data.Item;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.logic.FileLogic;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        //btn.addStyleName(ValoTheme.BUTTON_SMALL);
        //btn.setEnabled(false);
        return btn;
    }
    
    public MenuBar createMenuBar() {
        MenuBar menu = new MenuBar();
        menu.addStyleName(ValoTheme.MENUBAR_SMALL);
        menu.addStyleName("primary");
        return menu;
    }
    
//    public List<File> directoryContents(File directory) {
    public List<Item> directoryContents(Item directory) {
        // ARRAY QUE VA A ACONTENER TODOS LOS ARCHIVOS ORDENADOS POR TIPO Y ALFABETICAMENTE
        List<Item> allDocsLst = new ArrayList<>();
//        Item[] files = directory.listFiles();
        List<Item> fileLst = new ArrayList<>();
        List<Item> directoryLst = new ArrayList<>();
        for (Item file : directory.getList()) {
            if (file.isDirectory()) {
                directoryLst.add(file);
                //directoryContents(file);   //para conocer los archivos de las subcarpetas
            } else {
                fileLst.add(file);
            }
        }
        allDocsLst.addAll(directoryLst);
        allDocsLst.addAll(fileLst);

        return allDocsLst;
    }
    
    public List<Item> directoryFolderContents(Item directory) {
        // ARRAY QUE VA A ACONTENER TODOS LOS ARCHIVOS ORDENADOS POR TIPO Y ALFABETICAMENTE
        List<Item> allDocsLst = new ArrayList<>();
//        File[] files = directory.listFiles();
        List<Item> fileLst = new ArrayList<>();
        List<Item> directoryLst = new ArrayList<>();
        for (Item file : directory.getList()) {
            if (file.isDirectory()) {
                directoryLst.add(file);
                //directoryContents(file);   //para conocer los archivos de las subcarpetas
            } else {
                fileLst.add(file);
            }
        }
        allDocsLst.addAll(directoryLst);
        allDocsLst.addAll(fileLst);

        return allDocsLst;
    }
    
     public ButtonContextMenu createButtonContextMenu(Button downloadInvisibleButton, Item file, FileLogic viewLogicFile, DirectoryLogic viewLogicDirectory) {
        return new ButtonContextMenu(downloadInvisibleButton, file, viewLogicFile, viewLogicDirectory);
    }

}
