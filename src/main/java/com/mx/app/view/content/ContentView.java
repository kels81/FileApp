package com.mx.app.view.content;

import com.mx.app.component.view.FileGridLayout;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.logic.FileLogic;
import com.mx.app.utils.Components;
import com.mx.app.utils.Constantes;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public final class ContentView extends VerticalLayout implements View {

    private final File origenPath;
    private HorizontalLayout rootPath;
    private HorizontalLayout toolBar;
    private HorizontalLayout viewBar;
    private HorizontalLayout viewButtons;

    private Component directoryContent;
    private Button btnFolder;
    private Label lblArrow;
    private Button btnListView;
    private Button btnGridView;

    private final Components component = new Components();
    private final DirectoryLogic viewLogicDirectory = new DirectoryLogic(this);
    private final FileLogic viewLogicFile = new FileLogic(this);

    private Boolean selected = true;

    public ContentView() {
        this.origenPath = new File(Constantes.ROOT_PATH);

        setSizeFull();
        addStyleName("content");
        setMargin(false);
        setSpacing(false);

        addComponent(buildToolbar(origenPath));

        directoryContent = selectView(selected, origenPath);
        addComponent(directoryContent);
        setExpandRatio(directoryContent, 1);

        Responsive.makeResponsive(this);
//        Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
//        Page.getCurrent().getStyles().add(".v-horizontallayout {border: 1px solid green;} .v-horizontallayout .v-slot {border: 1px solid violet;}");
    }

    private Component buildToolbar(File directory) {
        toolBar = new HorizontalLayout();
        toolBar.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
//        toolBar.addStyleName("toolbar");
        toolBar.addStyleName("viewheader");

        Component path = buildPath(directory);
        Component typesViews = buildViewsBar(directory);
        
        toolBar.addComponents(path, typesViews);
        toolBar.setExpandRatio(typesViews, 1.0f);

        return toolBar;
    }

    private Component buildPath(File fileDirectory) {
        rootPath = new HorizontalLayout();

        List<File> listDirectories = getListDirectories(fileDirectory);
        int i = 1;
        for (File directory : listDirectories) {
            btnFolder = component.createButtonPath(directory.getName());
            btnFolder.setEnabled((i != listDirectories.size()));
            btnFolder.addStyleName((i == listDirectories.size() ? "labelColored" : ""));
            btnFolder.addClickListener(event -> {
                //System.out.println("evnt: "+event.getComponent().getCaption());
                cleanAndDisplay(directory);
            });
            rootPath.addComponent(btnFolder);
            if (i != listDirectories.size()) {
                lblArrow = new Label(FontAwesome.ANGLE_RIGHT.getHtml(), ContentMode.HTML);
                lblArrow.addStyleName(ValoTheme.LABEL_COLORED);
                rootPath.addComponent(lblArrow);
                rootPath.setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);
            }
            i++;
        }
        return rootPath;
    }

    private Component buildViewsBar(File directory) {
        viewBar = new HorizontalLayout();
        viewBar.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        viewBar.addStyleName("viewbar");

        Component viewsButtons = buildViewsButtons(directory);

        viewBar.addComponent(viewsButtons);
        viewBar.setComponentAlignment(viewsButtons, Alignment.MIDDLE_RIGHT);

        return viewBar;
    }

    private Component buildViewsButtons(File directory) {
        viewButtons = new HorizontalLayout();
        //viewButtons.setSpacing(true);

        btnListView = component.createButtonIconTiny();
        //btnListView.setIcon(FontAwesome.TH_LIST);
        btnListView.setIcon(FontAwesome.BARS);
        btnListView.addStyleName(setStyle(selected));
        btnListView.setEnabled(selected);
        btnListView.setDescription("Vista Lista");
        btnListView.addClickListener((Button.ClickEvent event) -> {
            selected = false;
            cleanAndDisplay(directory);
        });

        btnGridView = component.createButtonIconTiny();
        btnGridView.setIcon(FontAwesome.TH_LARGE);
        btnGridView.addStyleName(setStyle(!selected));
        btnGridView.setEnabled(!selected);
        btnGridView.setDescription("Vista Grid");
        btnGridView.addClickListener((Button.ClickEvent event) -> {
            selected = true;
            cleanAndDisplay(directory);
        });
        
        viewButtons.addComponents(btnListView, btnGridView);

        return viewButtons;
    }

    private List<File> getListDirectories(File directory) {
        List<File> listDirectories = new ArrayList<>();
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
            listDirectories.add(new File(Constantes.PATH_BASE + newPath.toString()));
            i++;
        }
        System.out.println("newPath = " + newPath.toString());
        return listDirectories;
    }

    private Component selectView(Boolean selected, File pathDirectory) {
        Component viewSelected = null;
        if (selected) {
            viewSelected = new FileGridLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
//        } else {
//            viewSelected = new FileListLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
        }

        return viewSelected;
    }

    public void cleanAndDisplay(File directory) {
        removeAllComponents();
        addComponent(buildToolbar(directory));
        directoryContent = selectView(selected, directory);
        addComponent(directoryContent);
        setExpandRatio(directoryContent, 1);
    }

    private String setStyle(Boolean selected) {
        return selected ? "borderButton" : "noBorderButton";
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
