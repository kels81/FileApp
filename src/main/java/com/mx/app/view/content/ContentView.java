package com.mx.app.view.content;

import com.mx.app.component.Breadcrumb;
import com.mx.app.component.view.*;
import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import java.io.File;

@SuppressWarnings("serial")
public final class ContentView extends VerticalLayout implements View {

    private final Item origenPath;
//    private HorizontalLayout rootPath;
    private HorizontalLayout header;
    private HorizontalLayout viewBar;
//    private HorizontalLayout viewButtons;

    private Component directoryContent;
//    private Button btnPath;
//    private Label lblArrow;
    private Button btnListView;
    private Button btnGridView;

    private final Components component = new Components();
    private final DirectoryLogic viewLogicDirectory = new DirectoryLogic(this);
    private final FileLogic viewLogicFile = new FileLogic(this);

    private Boolean selected = true;
    private Boolean isVisible = true;

    public ContentView() {
        this.origenPath = new Item(Constantes.ROOT_PATH);

        setSizeFull();
        addStyleName("content");
        setMargin(false);
        setSpacing(false);

        addComponent(buildHeader(origenPath));

        directoryContent = selectView(selected, origenPath);
        addComponent(directoryContent);
        setExpandRatio(directoryContent, 1);

        Responsive.makeResponsive(this);
//        Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
//        Page.getCurrent().getStyles().add(".v-horizontallayout {border: 1px solid green;} .v-horizontallayout .v-slot {border: 1px solid violet;}");
    }

    private Component buildHeader(Item directory) {
        header = new HorizontalLayout();
        header.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
//        toolBar.addStyleName("toolbar");
        header.addStyleName("viewheader");

//        Component path = buildPath(directory);
        Component typesViews = buildViewsButtons(directory);
        Breadcrumb pathDir = new Breadcrumb(viewLogicDirectory, directory);
//        ViewsButton typesViews = new ViewsButton(directory);

        header.addComponents(pathDir, typesViews);
        header.setExpandRatio(typesViews, 1.0f);

        return header;
    }

//    private Component buildPath(File fileDirectory) {
//        rootPath = new HorizontalLayout();
//
//        List<File> listDirectories = getListDirectories(fileDirectory);
//        int i = 1;
//        for (File directory : listDirectories) {
//            btnPath = component.createButtonPath(directory.getName());
//            btnPath.setEnabled((i != listDirectories.size())); 
//            btnPath.addStyleName((i == listDirectories.size() ? "labelColored" : ""));
//            btnPath.addClickListener(event -> {
//                //System.out.println("evnt: "+event.getComponent().getCaption());
//                cleanAndDisplay(directory);
//            });
//            rootPath.addComponent(btnPath);
//            if (i != listDirectories.size()) {
//                lblArrow = new Label(FontAwesome.ANGLE_RIGHT.getHtml(), ContentMode.HTML);
//                lblArrow.addStyleName(ValoTheme.LABEL_COLORED);
//                rootPath.addComponent(lblArrow);
//                rootPath.setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);
//            }
//            i++;
//        }
//        return rootPath;
//    }

//    private Component buildViewsBar(File directory) {
//        viewBar = new HorizontalLayout();
//        viewBar.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
//        viewBar.addStyleName("viewbar");
//
////        Component viewsButtons = buildViewsButtons(directory);
//        ViewsButton viewsButtons = new ViewsButton(directory);
//
//        viewBar.addComponent(viewsButtons);
//        viewBar.setComponentAlignment(viewsButtons, Alignment.MIDDLE_RIGHT);
//
//        return viewBar;
//    }

    private Component buildViewsButtons(Item directory) {
        viewBar = new HorizontalLayout();
        viewBar.addStyleName("viewbar");
        viewBar.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        
        HorizontalLayout viewsButtons = new HorizontalLayout();

        btnListView = component.createButtonIconTiny();
        btnListView.setIcon(FontAwesome.BARS);
        //btnListView.addStyleName(setStyle(selected));
        //btnListView.setEnabled(selected);
        btnListView.setDescription("Vista Lista");
        btnListView.setVisible(isVisible);
        btnListView.addClickListener((Button.ClickEvent event) -> {
            selected = false;
            isVisible = false;
            cleanAndDisplay(directory);
        });

        btnGridView = component.createButtonIconTiny();
        btnGridView.setIcon(FontAwesome.TH_LARGE);
        //btnGridView.addStyleName(setStyle(!selected));
        //btnGridView.setEnabled(!selected);
        btnGridView.setDescription("Vista Grid");
        btnGridView.setVisible(!isVisible);
        btnGridView.addClickListener((Button.ClickEvent event) -> {
            selected = true;
            isVisible = true;
            cleanAndDisplay(directory);
        });

        viewsButtons.addComponents(btnListView, btnGridView);
        
        viewBar.addComponent(viewsButtons);
        viewBar.setComponentAlignment(viewsButtons, Alignment.MIDDLE_RIGHT);

        return viewBar;
    }

//    private List<File> getListDirectories(File directory) {
//        List<File> listDirectories = new ArrayList<>();
//        String[] arrayDirectories = directory.getPath().split(Constantes.SEPARADOR);
//        int idxArchivos = Arrays.asList(arrayDirectories).indexOf(Constantes.ROOT_DIRECTORY);
//        String[] newArrayDirectories = Arrays.copyOfRange(arrayDirectories, idxArchivos, arrayDirectories.length);
//        StringBuilder newPath = new StringBuilder();
//        int i = 1;
//        for (String dirName : newArrayDirectories) {
////            int fin = directory.getPath().indexOf(dirName);
////            listDirectories.add(new File(directory.getPath().substring(0, fin + dirName.length())));
//            newPath.append(dirName);
//            if (i != newArrayDirectories.length) {
//                newPath.append("\\");
//            }
//            listDirectories.add(new File(Constantes.PATH_BASE + newPath.toString()));
//            i++;
//        }
//        System.out.println("newPath = " + newPath.toString());
//        return listDirectories;
//    }

    private Component selectView(Boolean selected, Item pathDirectory) {
        Component viewSelected;
        if (selected) {
            viewSelected = new FileMosaicoLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
        } else {
            viewSelected = new FileListLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
        }

        return viewSelected;
    }

    public void cleanAndDisplay(Item directory) {
        removeAllComponents();
        addComponent(buildHeader(directory));
        directoryContent = selectView(selected, directory);
        addComponent(directoryContent);
        setExpandRatio(directoryContent, 1);
    }

//    private String setStyle(Boolean selected) {
//        return selected ? "borderButton" : "noBorderButton";
//    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
