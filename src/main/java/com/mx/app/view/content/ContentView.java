package com.mx.app.view.content;

import com.mx.app.component.*;
import com.mx.app.component.view.*;
import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.*;
import com.vaadin.ui.*;

@SuppressWarnings("serial")
public final class ContentView extends VerticalLayout implements View {

    private final Item origenPath;
    private HorizontalLayout header;
    private HorizontalLayout viewBar;

    private Component directoryContent;
    private Button btnListView;
    private Button btnGridView;

    private final Components component = new Components();
    private final DirectoryLogic viewLogicDirectory = new DirectoryLogic(this);
    private final FileLogic viewLogicFile = new FileLogic(this);

    private Boolean selected = true;
    private Boolean isVisible = true;

    public ContentView() {
        this.origenPath = new Item(Constantes.ALL_FILES);

        setSizeFull();
        addStyleName("content");
        setMargin(false);
        setSpacing(false);

        cleanAndDisplay(origenPath);

        Responsive.makeResponsive(this);
//        Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
//        Page.getCurrent().getStyles().add(".v-horizontallayout {border: 1px solid green;} .v-horizontallayout .v-slot {border: 1px solid violet;}");
    }

    private Component buildHeader(Item directory) {
        header = new HorizontalLayout();
        header.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
//        toolBar.addStyleName("toolbar");
        header.addStyleName("viewheader");

        Breadcrumb pathDir = new Breadcrumb(directory, this::cleanAndDisplay);
        Component typesViews = buildViewsButtons(directory);
//        ViewsButton typesViews = new ViewsButton(directory);

        header.addComponents(pathDir, typesViews);
        header.setExpandRatio(typesViews, 1.0f);

        return header;
    }

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

    private Component selectView(Boolean selected, Item pathDirectory) {
        Component viewSelected;
//        if (!pathDirectory.isEmpty()) {
        if (selected) {
            viewSelected = new FileMosaicoLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
        } else {
            viewSelected = new FileListLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
        }
//        } else {
//            viewSelected = component.createLabelEmptyDirectory();
//        }

        return viewSelected;
    }

    public void cleanAndDisplay(Item directory) {
        removeAllComponents();
        addComponent(buildHeader(directory));
        if (directory.isEmpty()) {
            Label message = component.createLabelEmptyDirectory();
            addComponent(message);
            setComponentAlignment(message, Alignment.MIDDLE_CENTER);
            setExpandRatio(message, 1);
        } else {
            directoryContent = selectView(selected, directory);
            addComponentsAndExpand(directoryContent);
        }
    }

//    private String setStyle(Boolean selected) {
//        return selected ? "borderButton" : "noBorderButton";
//    }
    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
