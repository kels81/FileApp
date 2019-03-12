/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.window;

import com.mx.app.logic.DirectoryLogic;
import com.mx.app.logic.FileLogic;
import com.mx.app.utils.Components;
import com.mx.app.utils.Constantes;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeContextClickEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

/**
 *
 * @author Edrd
 */
public class DirectoryTreeFolderWindow extends Window {

    private final File origenPath;
    private final VerticalLayout content;
    private VerticalLayout body;
    private HorizontalLayout footer;
    private Tree<File> tree;
    private final TabSheet detailsWrapper;
    private Label lblFileName;
    private final File fileTo;
    private Button btnCancelar;
    private Button btnMover;
    private Button btnCopiar;
    private int sizeDepth;

    private final Components component = new Components();
    private final TreeData<File> treeData = new TreeData<>();
    private final List<Integer> depthLst = new ArrayList<>();

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

    public DirectoryTreeFolderWindow(FileLogic moveCopyFileLogic, DirectoryLogic moveCopyDirectoryLogic, File file) {
        this.viewLogicFile = moveCopyFileLogic;
        this.viewLogicDirectory = moveCopyDirectoryLogic;

        this.origenPath = new File(Constantes.ROOT_PATH);
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
        tree.setStyleName("treeApp");
        tree.setDataProvider(crearContenedor(origenPath));
        tree.setItemCaptionGenerator(File::getName);
//        tree.setItemIconGenerator(File -> new ThemeResource("img/file_manager/folder_24.png"));
        tree.setItemIconGenerator(File -> VaadinIcons.FOLDER);
        tree.setRowHeight(35);
        tree.addCollapseListener((event) -> {
            File itemId = event.getCollapsedItem();
            tree.collapseRecursively(tree.getSelectedItems(), getSizeDepthChildren(itemId));
        });
//        tree.addExpandListener((event) -> {
//            File itemId = event.getExpandedItem();
//            tree.select(itemId);
//        });
        tree.addItemClickListener((event) -> {
                File itemId = event.getItem();
//                tree.getSelectionModel().select(itemId);
                tree.select(itemId);
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
        });
        // PARA CUANDO SE QUIERE MOSTRAR CARPETA ROOT Y CARPETAS DENTRO DE ELLA 1ER NIVEL
        tree.expand(tree.getTreeData().getRootItems());
        
        return tree;
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
            Path source = Paths.get(fileTo.getAbsolutePath());
            Path target = Paths.get(tree.asSingleSelect().getValue() + "\\" + fileTo.getName());

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
            Path source = Paths.get(fileTo.getAbsolutePath());
            Path target = Paths.get(tree.asSingleSelect().getValue() + "\\" + fileTo.getName());

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

    private TreeDataProvider<File> crearContenedor(File directory) {
        treeData.addItem(null, directory);
        // add children for the root level items
        createTreeContent(directory);
        //files.forEach(dir -> treeData.addItems(dir, getChildren(dir)));
        return new TreeDataProvider<>(treeData);
    }

    private List<File> getListSubDirectory(File directory) {
        File[] arrayFiles = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);     //PARA OBTENER UNICAMENTE DIRECTORIOS DE UN DIRECTORIO
        Arrays.sort(arrayFiles);
        return Arrays.asList(arrayFiles);
    }

    private void createTreeContent(File directory) {
        for (File file : getListSubDirectory(directory)) {
            treeData.addItem(directory, file);
            createTreeContent(file);
        }
    }

    private int getSizeDepthChildren(File startItemId) {
        depthLst.clear();
        getChildrens(startItemId);
        return Collections.max(depthLst);
    }

    private void getChildrens(File startItemId) {
        sizeDepth = 1;
        for (File id : getListSubDirectory(startItemId)) {
            getChildrens(id);
            sizeDepth++;
        }
        depthLst.add(sizeDepth);
    }

}
