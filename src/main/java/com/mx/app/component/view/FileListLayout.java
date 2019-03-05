/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.view;

import com.google.common.eventbus.Subscribe;
import com.mx.app.component.ItemProperty;
import com.mx.app.event.AppEvent.BrowserResizeEvent;
import com.mx.app.event.AppEventBus;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.logic.FileLogic;
import com.mx.app.utils.Components;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Edrd
 */
public class FileListLayout extends VerticalLayout implements View {

    private ThemeResource iconResource;
    private Image icon;
    private final Components component = new Components();
    private ItemProperty itemProperty;

//    private IndexedContainer idxCont;
    private Grid<File> table;
    private final Button downloadInvisibleButton = new Button();

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

    private final String COL_FILE = "file";
    private final String COL_ICON = "icon";
    private final String COL_NOMBRE = "nombre";
    private final String COL_TAMANIO = "tamanio";
    private final String COL_MODIFICADO = "modificado";
    private final String COL_CONTEXT_MENU = "contextMenu";

    private static final Set<Column<File, ?>> collapsibleColumns = new LinkedHashSet<>();

    private final Object[] COLUMNS_VISIBLES = {COL_ICON, COL_NOMBRE, COL_TAMANIO, COL_MODIFICADO, COL_CONTEXT_MENU};
    private final Object[] COLUMNS_VISIBLES_MOBILES = {COL_ICON, COL_NOMBRE, COL_CONTEXT_MENU};
    private final String[] COLUMNS_HEADERS = {"", "Nombre", "Tamaño", "Modificado", ""};
    private final String[] DEFAULT_COLLAPSIBLE = {COL_TAMANIO, COL_MODIFICADO};

    public FileListLayout(FileLogic mosaicoFileLogic, DirectoryLogic mosaicoDirectoryLogic, File file) {
        this.viewLogicFile = mosaicoFileLogic;
        this.viewLogicDirectory = mosaicoDirectoryLogic;
        System.out.println("List Layout");

        setSizeFull();
        addStyleName("listView");
        AppEventBus.register(this);   //NECESARIO PARA CONOCER LA ORIENTACION Y RESIZE DEL BROWSER

        Component table = buildTable(file);
        addComponent(table);
        setExpandRatio(table, 1);
        browserResized();
//        System.out.println("width-->" + Page.getCurrent().getBrowserWindowWidth());
//        System.out.println("height-->" + Page.getCurrent().getBrowserWindowHeight());

        //BUTTON PARA PODER DESCARGAR ARCHIVOS POR MEDIO DEL CONTEXT MENU
        downloadInvisibleButton.setId("DownloadButtonId");
        downloadInvisibleButton.addStyleName("InvisibleButton");
        addComponent(downloadInvisibleButton);
    }

    //METODO NECESARIO PARA CONOCER LA ORIENTACION Y RESIZE DEL BROWSER
    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        AppEventBus.unregister(this);
    }

    private Grid<File> buildTable(File file) {
        table = new Grid<>();
        table.setDataProvider(crearContenedor(file));
        table.setSizeFull();
        //table.setSelectionMode(SelectionMode.SINGLE);
        table.setStyleName(ValoTheme.TABLE_COMPACT);
        addColumns();
        table.setBodyRowHeight(44);
        table.setColumnReorderingAllowed(false);
        table.addItemClickListener((event) -> {
            if (event.getItem() != null) {
                File file_ = new File(event.getItem().getAbsolutePath());
                if (event.getMouseEventDetails().isDoubleClick()) {
                    //table.select(selectedItemInTheRow);
                    if (file_.isDirectory()) {
                        viewLogicFile.cleanAndDisplay(file_);
                    } else if (file_.isFile()) {
                        Notification.show("Ver archivo: " + file_.getName());
//                        Window w = new ViewerWindow(file);;
//                        UI.getCurrent().addWindow(w);
//                        w.focus();
                    }
                }
            }
        });

//         HeaderRow groupingHeader = table.prependHeaderRow();
//        groupingHeader.join("icon","nombre").setText("Person");
        return table;
    }

    private ListDataProvider<File> crearContenedor(File directory) {
        List<File> files = component.directoryContents(directory);
        ListDataProvider<File> dataProvider
                = DataProvider.ofCollection(files);

//        dataProvider.setSortOrder(File::getName,
//                SortDirection.ASCENDING);
//
        return dataProvider;
    }

    private void addColumns() {

        collapsibleColumns
                .add(table.addColumn(file -> new ItemProperty(file).buildIcon(false), new ComponentRenderer())
                        .setCaption("Icon")
                        .setHidable(true));
        collapsibleColumns
                //                .add(table.addColumn(file -> new ItemProperty(file).getFileName(), new ComponentRenderer())
                .add(table.addColumn(file -> file.getName())
                        .setCaption("Nombre")
                        .setHidable(true)
                        .setExpandRatio(5));
//                .add(table.addColumn(file_ -> new Label(file_.getName()), new ComponentRenderer()).setCaption("Nombre"));     // OTRA OPCION
//                .add(table.addColumn(File::getName).setCaption("Nombre"));                                                                                    // OTRA OPCION
        collapsibleColumns
                //                .add(table.addColumn(file_ -> itemProperty.getNumberOfElementsAndFileSize()).setCaption("Tamaño"));   // OTRA OPCION
                .add(table.addColumn(file -> new ItemProperty(file).getNumberOfElementsAndFileSize())
                        .setCaption("Tamaño")
                        //                        .setHidable(true)
                        .setExpandRatio(2));
        collapsibleColumns
                .add(table.addColumn(file -> new ItemProperty(file).getAtributos())
                        .setCaption("Modificado")
                        //                        .setHidable(true)
                        .setExpandRatio(2)
                        .setHidable(true)
                        .setStyleGenerator(item -> "v-align-right"));
        collapsibleColumns // BUTTONCONTEXTMENU
                .add(table.addColumn(file -> component.createButtonContextMenu(downloadInvisibleButton, file, viewLogicFile, viewLogicDirectory), new ComponentRenderer())
                        .setCaption("")
                        .setStyleGenerator(item -> "v-align-center"));
//        collapsibleColumns // FILE PATH
//                .add(table.addColumn(file -> file.getAbsolutePath())
//                        .setCaption("")
//                        .setHidden(true));

    }

    @Subscribe
    public void browserResized(final BrowserResizeEvent event) {
        browserResized();
    }

    private boolean defaultColumnsVisible() {
        System.out.println("entra columns visible");
        boolean result = true;
        for (Column<File, ?> column : collapsibleColumns) {
            if (column.isHidden() == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    private void browserResized() {
        System.out.println("entra resize");
        System.out.println("Window_width: " + Page.getCurrent().getBrowserWindowWidth());
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        if (defaultColumnsVisible()) {
            for (Column<File, ?> column : collapsibleColumns) {
                column.setHidden(
                        Page.getCurrent().getBrowserWindowWidth() < 800);
            }
        }
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
    }

}
