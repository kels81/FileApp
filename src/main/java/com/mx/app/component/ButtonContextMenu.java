/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;


import com.mx.app.component.window.*;
import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.nio.file.*;

/**
 *
 * @author Edrd
 */
public class ButtonContextMenu extends MenuBar {

    public ButtonContextMenu(Button dwnldInvisibleBtn, Item file, FileLogic viewLogicFile, DirectoryLogic viewLogicDirectory) {
        addStyleName(ValoTheme.MENUBAR_SMALL);
        addStyleName("btn-contextmenu");

        MenuBar.MenuItem menu = addItem("", FontAwesome.ELLIPSIS_H, null);
//        //DESCARGAR
        MenuBar.MenuItem descargar = menu.addItem("Descargar", FontAwesome.DOWNLOAD, e -> {
            //Path source = Paths.get(file.getAbsolutePath());
            if (file.isDirectory()) {
                viewLogicDirectory.downloadDirectory(file, dwnldInvisibleBtn);
            } else {
                viewLogicFile.downloadFile(file, dwnldInvisibleBtn);
            }
        });
        //EDITAR
        MenuBar.MenuItem editar = menu.addItem("Editar", FontAwesome.PENCIL, e -> {
            EditWindow editWindow = new EditWindow(viewLogicFile, viewLogicDirectory, file);
            Window w = editWindow;
            UI.getCurrent().addWindow(w);
            w.focus();
        });
        //FAVORITOS
        MenuBar.MenuItem favorito = menu.addItem("Favorito", FontAwesome.STAR, e -> {
            Path source = Paths.get(file.getPath());
            if (file.isDirectory()) {
                viewLogicDirectory.favouriteDirectory(source, file);
            } else {
                viewLogicFile.favouriteFile(source, file);
            }
        });
        //BORRAR
        MenuBar.MenuItem borrar = menu.addItem("Eliminar", FontAwesome.TRASH, e -> {
            ConfirmWindow confirmWindow = new ConfirmWindow(viewLogicFile, viewLogicDirectory, file);
            Window w = confirmWindow;
            UI.getCurrent().addWindow(w);
            w.focus();
        });

        menu.addSeparator();
        //MOVER-COPIAR
        MenuBar.MenuItem moverCopiar = menu.addItem("Mover o Copiar", FontAwesome.COPY, e -> {
            DirectoryTreeWindow directoryTreeWindow = new DirectoryTreeWindow(viewLogicFile, viewLogicDirectory, file);
            DirectoryTableWindow directoryTableWindow = new DirectoryTableWindow(viewLogicFile, viewLogicDirectory, file);
            Window w = directoryTreeWindow;
//            Window w = directoryTableWindow;
            UI.getCurrent().addWindow(w);
            w.focus();
        });
        //ZIP
        MenuBar.MenuItem zip = menu.addItem("Zip", FontAwesome.FOURSQUARE, e -> {
            Path source = Paths.get(file.getPath());
            if (file.isDirectory()) {
                viewLogicDirectory.zipDirectory(source, file);
            } else {
                viewLogicFile.zipFile(source, file);
            }
        });

    }

}
