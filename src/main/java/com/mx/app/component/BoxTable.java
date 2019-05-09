/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.google.common.eventbus.Subscribe;
import com.mx.app.data.Item;
import com.mx.app.event.AppEvent;
import com.mx.app.event.AppEventBus;
import com.mx.app.logic.*;
import com.mx.app.utils.Components;
import com.mx.app.utils.ItemProperty;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import java.util.*;

/**
 *
 * @author ecortesh
 */
public class BoxTable extends Grid<Item> implements View{

    private final Item file;
    private final Button downloadInvisibleButton;

    private final Components component = new Components();
    
    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

    private final String COL_TAMANIO = "tamanio";
    private final String COL_MODIFICADO = "modificado";

    private static final Set<Column<Item, ?>> COLLAPSIBLE_COLUMNS = new LinkedHashSet<>();

    private final String[] DEFAULT_COLLAPSIBLE = {COL_TAMANIO, COL_MODIFICADO};

    public BoxTable(Item file, FileLogic mosaicoFileLogic, DirectoryLogic mosaicoDirectoryLogic, Button downloadInvisibleButton) {
        super();
        this.file = file;
        this.viewLogicFile = mosaicoFileLogic;
        this.viewLogicDirectory = mosaicoDirectoryLogic;
        this.downloadInvisibleButton = downloadInvisibleButton;

        setDataProvider(crearContenedor(file));
        setSizeFull();
        addColumns();
        setRowHeight(44);
        setColumnReorderingAllowed(false);
         addItemClickListener((event) -> itemClicked(event));
         
         browserResized();
            
    }
    
    private void itemClicked(ItemClick<Item> event) {
         Item itemId = event.getItem();
        if (itemId != null) {
//                Item file_ = new Item(itemId);
                if (event.getMouseEventDetails().isDoubleClick()) {
                    if (itemId.isDirectory()) {
                        viewLogicFile.cleanAndDisplay(itemId);
                    } else if (itemId.isFile()) {
                        Notification.show("Ver archivo: " + itemId.getName());
//                        Window w = new ViewerWindow(file);;
//                        UI.getCurrent().addWindow(w);
//                        w.focus();
                    }
                }
            }
    }
    
     private ListDataProvider<Item> crearContenedor(Item directory) {
//        ListDataProvider<Item> dataProvider = DataProvider.ofCollection(component.directoryContents(directory));
        ListDataProvider<Item> dataProvider = DataProvider.ofCollection(directory.getContentDirectory(directory));

//        dataProvider.setSortOrder(File::getName,
//                SortDirection.ASCENDING);
//
        return dataProvider;
    }

    private void addColumns() {

        COLLAPSIBLE_COLUMNS
                .add(addColumn(file -> new ItemProperty(file).buildIcon(false), new ComponentRenderer())
                        .setCaption(""));
        COLLAPSIBLE_COLUMNS
                .add(addColumn(file -> file.getName())
                        .setCaption("Nombre")
                        .setExpandRatio(6));
//                .add(addColumn(file_ -> new Label(file_.getName()), new ComponentRenderer()).setCaption("Nombre"));     // OTRA OPCION
//                .add(addColumn(File::getName).setCaption("Nombre"));                                                                                    // OTRA OPCION
        COLLAPSIBLE_COLUMNS
                //                .add(addColumn(file_ -> itemProperty.getNumberOfElementsAndFileSize()).setCaption("Tamaño"));   // OTRA OPCION
                .add(addColumn(file -> new ItemProperty(file).getNumberOfElementsAndFileSize())
                        .setCaption("Tamaño")
                        .setId(COL_TAMANIO)
                        .setExpandRatio(2));
        COLLAPSIBLE_COLUMNS
                .add(addColumn(file -> new ItemProperty(file).getAtributos())
                        .setCaption("Modificado")
                        .setId(COL_MODIFICADO)
                        .setExpandRatio(2)
                        .setStyleGenerator(item -> "v-align-right"));
        COLLAPSIBLE_COLUMNS // BUTTONCONTEXTMENU
                .add(addColumn(file -> new ButtonContextMenu(downloadInvisibleButton, file, viewLogicFile, viewLogicDirectory), new ComponentRenderer())
                        .setCaption("")
                        .setStyleGenerator(item -> "v-align-center"));

    }

//    private boolean defaultColumnsVisible() {
//        boolean result = true;
//        for (Column<File, ?> column : COLLAPSIBLE_COLUMNS) {
//            if (column.isHidden() == Page.getCurrent()
//                    .getBrowserWindowWidth() < 800) {
//                result = false;
//            }
//        }
//        return result;
//    }
    @Subscribe
    public void browserResized(final AppEvent.BrowserResizeEvent event) {
        browserResized();
    }

    private void browserResized() {
        //Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        System.out.println("Entra a browserResized: " + Page.getCurrent().getBrowserWindowWidth());
        List<String> lstColapsibleColumns = Arrays.asList(DEFAULT_COLLAPSIBLE);
//        if (defaultColumnsVisible()) {
        for (Column<Item, ?> column : COLLAPSIBLE_COLUMNS) {
            if (lstColapsibleColumns.contains(column.getId())) {
                column.setHidden(Page.getCurrent().getBrowserWindowWidth() < 800);
            }
        }
//        }
    }

    
}
