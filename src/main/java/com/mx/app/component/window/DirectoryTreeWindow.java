/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.window;

import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.*;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.event.CollapseEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Tree.ItemClick;
import com.vaadin.ui.themes.ValoTheme;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author Edrd
 */
public class DirectoryTreeWindow extends Window {

    private final Item origenPath;
    private final VerticalLayout content;
    private VerticalLayout body;
    private HorizontalLayout footer;
    private Tree<Item> tree;
    private final TabSheet detailsWrapper;
    private Label lblFileName;
    private final Item fileTo;
    private Item targetDir;
    private Button btnCancelar;
    private Button btnMover;
    private Button btnCopiar;
    private int sizeDepth;

    private final Components component = new Components();
    private final TreeData<Item> treeData = new TreeData<>();
    private final List<Integer> depthLst = new ArrayList<>();

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

    public DirectoryTreeWindow(FileLogic moveCopyFileLogic, DirectoryLogic moveCopyDirectoryLogic, Item file) {
        this.viewLogicFile = moveCopyFileLogic;
        this.viewLogicDirectory = moveCopyDirectoryLogic;

        this.origenPath = new Item(Constantes.ROOT_PATH);
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
        detailsWrapper.addComponent(body());

        content.addComponentsAndExpand(detailsWrapper);
        content.addComponent(buildFooter());

        setContent(content);

        //Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
    }

    private VerticalLayout body() {
        body = new VerticalLayout();
        //body.setCaption("Mover o Copiar" + "  \""+fileTo.getName()+"\"");
        body.setCaption("Mover o Copiar");
        body.setMargin(new MarginInfo(true, true, false, true));
        body.setSpacing(true);
        body.addComponent(buildFileName());
        body.addComponent(new Label("Selecciona folder destino:"));
        Component tree = buildTree();
        body.addComponentsAndExpand(tree);

        return body;
    }

    private Label buildFileName() {
        lblFileName = new Label("\"" + fileTo.getName() + "\"");
        lblFileName.addStyleName(ValoTheme.LABEL_COLORED);
        lblFileName.addStyleName(ValoTheme.LABEL_BOLD);
        return lblFileName;
    }

    private Component buildTree() {
        tree = new Tree<>();
        tree.setStyleName("treeApp");       // SE CREA ESTE ESTILO PARA QUE NO MUESTRE EL BORDER DE FOCUS CUANDO SE SELECCIONA EL ITEM
        tree.setSelectionMode(Grid.SelectionMode.NONE);
        tree.setDataProvider(crearContenedor(origenPath));
        tree.setItemCaptionGenerator(Item::getName);
        tree.setItemIconGenerator(Item -> new ThemeResource("img/file_manager/folder_24.png"));
//        tree.setItemIconGenerator(File -> VaadinIcons.FOLDER);
        tree.setRowHeight(35);
        tree.addCollapseListener((event) -> itemCollapsed(event));
        tree.addItemClickListener((event) -> itemClicked(event));
//        tree.addItemClickListener((event) -> {
//            Item itemId = event.getItem();
//            targetDir = itemId;
//            tree.select(itemId);
//            tree.setStyleName("rowSelected");   // SE CREA ESTE ESTILO PARA QUE MUESTRE QUE ESTA SELECCIONADO EL ITEM
//            btnCopiar.setEnabled(true);
//            btnMover.setEnabled(true);
//            //VALIDACION PARA EXPANDIR NODE DESDE EL CAPTION
//            if (event.getMouseEventDetails().isDoubleClick()) {
//                if (tree.isExpanded(itemId)) {
//                    tree.collapse(itemId);
//                } else {
//                    tree.expand(itemId);
//                }
//            }});
        // PARA CUANDO SE QUIERE MOSTRAR CARPETA ROOT Y CARPETAS DENTRO DE ELLA 1ER NIVEL
        tree.expand(tree.getTreeData().getRootItems());

        return tree;
    }

    private void itemCollapsed(CollapseEvent<Item> event) {
        Item itemId = event.getCollapsedItem();
        tree.collapseRecursively(getSelectedItems(itemId), getSizeDepthChildren(itemId));
    }
    
    private void itemClicked(ItemClick<Item> event) {
            Item itemId = event.getItem();
            targetDir = itemId;
            tree.select(itemId);
            tree.setStyleName("rowSelected");   // SE CREA ESTE ESTILO PARA QUE MUESTRE QUE ESTA SELECCIONADO EL ITEM
            btnCopiar.setEnabled(true);
            btnMover.setEnabled(true);
            //VALIDACION PARA EXPANDIR NODE DESDE EL CAPTION
            if (event.getMouseEventDetails().isDoubleClick()) {
                if (tree.isExpanded(itemId)) {
                    tree.collapse(itemId);
                } else {
                    tree.expand(itemId);
                }
            }
    }

    private Component buildFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        btnCancelar = component.createButtonNormal("Cancelar");
        btnCancelar.addClickListener((Button.ClickEvent event) -> {
            close();
        });

        btnMover = component.createButtonPrimary("Mover");
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

        btnCopiar = component.createButtonPrimary("Copiar");
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

    private TreeDataProvider<Item> crearContenedor(Item directory) {
        treeData.addItem(null, directory);
        // add children for the root level items
        createTreeContent(directory);
        //files.forEach(dir -> treeData.addItems(dir, getChildren(dir)));
        return new TreeDataProvider<>(treeData);
    }

    private List<Item> getListSubDirectory(Item directory) {
//        Item[] arrayFiles = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);     //PARA OBTENER UNICAMENTE DIRECTORIOS DE UN DIRECTORIO
//        Arrays.sort(arrayFiles);
//        return Arrays.asList(arrayFiles);
        return directory.getListDirectories(directory);
    }

    private void createTreeContent(Item parentDirectory) {
        for (Item childrenDirectory : getListSubDirectory(parentDirectory)) {
            treeData.addItem(parentDirectory, childrenDirectory);
            createTreeContent(childrenDirectory);
        }
    }

    private List<Item> getSelectedItems(Item itemId) {
        List<Item> selectedItem = new ArrayList<>();
        selectedItem.add(itemId);
        return selectedItem;
    }

    private int getSizeDepthChildren(Item parentItemId) {
        depthLst.clear();
        getChildrens(parentItemId);
        return Collections.max(depthLst);
    }

    private void getChildrens(Item startItemId) {
        sizeDepth = 1;
        for (Item id : getListSubDirectory(startItemId)) {
            getChildrens(id);
            sizeDepth++;
        }
        depthLst.add(sizeDepth);
    }

}
