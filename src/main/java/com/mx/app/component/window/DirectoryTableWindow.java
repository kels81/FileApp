/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.window;

import com.mx.app.component.Breadcrumb;
import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.*;
import com.vaadin.data.provider.*;
import com.vaadin.server.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author ecortesh
 */
public class DirectoryTableWindow extends Window {

    private final Item origenPath;
    private HorizontalLayout header;
    private final VerticalLayout content;
    private VerticalLayout body;
    private HorizontalLayout footer;
    private Grid<Item> table;
    private final TabSheet detailsWrapper;
    private Label lblFileName;
    private final Item fileTo;
    private Item targetDir;
    private Button btnCancelar;
    private Button btnMover;
    private Button btnCopiar;

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

//    private final String COL_TAMANIO = "tamanio";
//    private final String COL_MODIFICADO = "modificado";
    private static final Set<Column<Item, ?>> COLLAPSIBLE_COLUMNS = new LinkedHashSet<>();

//    private final String[] DEFAULT_COLLAPSIBLE = {COL_TAMANIO, COL_MODIFICADO};
    public DirectoryTableWindow(FileLogic moveCopyFileLogic, DirectoryLogic moveCopyDirectoryLogic, Item file) {
        this.viewLogicFile = moveCopyFileLogic;
        this.viewLogicDirectory = moveCopyDirectoryLogic;

        this.origenPath = new Item(Constantes.ALL_FILES);
        this.fileTo = file;

        Responsive.makeResponsive(this);

        addStyleName("directorywindow");
        setModal(true);
        setResizable(false);
        setClosable(true);
        center();

        setHeight(90.0f, Unit.PERCENTAGE);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        //content.setSpacing(false);

        detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addTab(body(origenPath));

        content.addComponentsAndExpand(detailsWrapper);
        content.addComponent(buildFooter());

        setContent(content);

        //Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
    }

    private VerticalLayout body(Item directory) {
        body = new VerticalLayout();
        //body.setCaption("Mover o Copiar" + "  \""+fileTo.getName()+"\"");
        body.setCaption("Mover o Copiar");
        body.setMargin(new MarginInfo(true, true, false, true));
        body.setSpacing(true);
        body.addComponent(buildFileName());
        body.addComponent(new Label("Selecciona folder destino:"));
        body.addComponent(buildHeader(directory));

        table = buildTable(directory);
        body.addComponentsAndExpand(table);

        return body;
    }

    private Label buildFileName() {
        lblFileName = new Label("\"" + fileTo.getName() + "\"");
        lblFileName.addStyleName(ValoTheme.LABEL_COLORED);
        lblFileName.addStyleName(ValoTheme.LABEL_BOLD);
        return lblFileName;
    }

    private Component buildHeader(Item directory) {
        header = new HorizontalLayout();
        header.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        Breadcrumb pathDir = new Breadcrumb(directory, this::showContentDirectory);
        header.addComponents(pathDir);

        return header;
    }

    private Grid<Item> buildTable(Item directory) {
        table = new Grid<>();
        table.setDataProvider(crearContenedor(directory));
        //table.setSelectionMode(SelectionMode.MULTI);
        table.setSizeFull();
        addColumns();
        table.setRowHeight(44);
        table.setColumnReorderingAllowed(false);
        table.setHeaderVisible(false);
        table.addItemClickListener((event) -> itemClicked(event));

        return table;
    }

    private void itemClicked(ItemClick<Item> event) {
        Item itemId = event.getItem();
        if (itemId != null) {
            targetDir = itemId;
            btnCopiar.setEnabled(true);
            btnMover.setEnabled(true);

            if (event.getMouseEventDetails().isDoubleClick()) {
                showContentDirectory(itemId);
            }
        }
    }

    private ListDataProvider<Item> crearContenedor(Item directory) {
        ListDataProvider<Item> dataProvider = DataProvider.ofCollection(directory.getListDirectories(directory));
        return dataProvider;
    }

    private void addColumns() {

        COLLAPSIBLE_COLUMNS
                .add(table.addColumn(file -> new ItemProperty(file).buildIcon(false), new ComponentRenderer())
                        .setResizable(false));
        COLLAPSIBLE_COLUMNS
                .add(table.addColumn(file -> file.getName())
                        .setResizable(false)
                        .setExpandRatio(1));
//        COLLAPSIBLE_COLUMNS
//                .add(table.addColumn(file -> new CheckBox(), new ComponentRenderer())
//                        .setStyleGenerator(item -> "v-align-center"));

    }

//    @Subscribe
//    public void browserResized(final AppEvent.BrowserResizeEvent event) {
//        browserResized();
//    }
//
//    private void browserResized() {
//        //Some columns are collapsed when browser window width gets small
//        // enough to make the table fit better.
//        System.out.println("Entra a browserResized: " + Page.getCurrent().getBrowserWindowWidth());
//        List<String> lstColapsibleColumns = Arrays.asList(DEFAULT_COLLAPSIBLE);
////        if (defaultColumnsVisible()) {
//        for (Grid.Column<Item, ?> column : COLLAPSIBLE_COLUMNS) {
//            if (lstColapsibleColumns.contains(column.getId())) {
//                column.setHidden(Page.getCurrent().getBrowserWindowWidth() < 800);
//            }
//        }
////        }
//    }
    private Component buildFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        btnCancelar = Components.createButtonNormal("Cancelar");
        btnCancelar.addClickListener((Button.ClickEvent event) -> {
            close();
        });

        btnMover = Components.createButtonPrimary("Mover");
        btnMover.setEnabled(false);
        btnMover.addClickListener((Button.ClickEvent event) -> {
            Path source = Paths.get(fileTo.getPath());
            Path target = Paths.get(targetDir.getPath().concat("\\").concat(fileTo.getName()));
//            Path target = Paths.get(tree.asSingleSelect().getValue() + "\\" + fileTo.getName());

            if (fileTo.isDirectory()) {
                viewLogicDirectory.moveDirectory(source, target, fileTo);
            } else {
                //DOCUMENTO
                viewLogicFile.moveFile(source, target, fileTo);
            }

            close();
        });

        btnCopiar = Components.createButtonPrimary("Copiar");
        btnCopiar.setEnabled(false);
        btnCopiar.addClickListener((Button.ClickEvent event) -> {
            Path source = Paths.get(fileTo.getPath());
            Path target = Paths.get(targetDir.getPath().concat("\\").concat(fileTo.getName()));
//            Path target = Paths.get(tree.asSingleSelect().getValue() + "\\" + fileTo.getName());

            if (fileTo.isDirectory()) {
                viewLogicDirectory.copyDirectory(source, target, fileTo);
            } else {
                viewLogicFile.copyFile(source, target, fileTo);
            }
            close();
        });

        footer.addComponents(btnCancelar, btnMover, btnCopiar);
        footer.setExpandRatio(btnCancelar, 1.0f);
        footer.setComponentAlignment(btnCancelar, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(btnMover, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(btnCopiar, Alignment.TOP_RIGHT);
        return footer;
    }
    
    private void showContentDirectory(Item directory) {
        header.removeAllComponents();
        header.addComponent(new Breadcrumb(directory, this::showContentDirectory));
        table.setDataProvider(crearContenedor(directory));
    }

}
