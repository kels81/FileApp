package com.mx.app.view.favourites;

import com.mx.app.component.view.*;
import com.mx.app.data.Item;
import com.mx.app.logic.*;
import com.mx.app.utils.Components;
import com.mx.app.utils.Constantes;
import com.vaadin.navigator.*;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class Favourites extends VerticalLayout implements View {

    private final Item origenPath;
    private HorizontalLayout viewBar;

    private Component directoryContent;
    private Button btnListView;
    private Button btnGridView;

    private final Components component = new Components();
    private final DirectoryLogic viewLogicDirectory = new DirectoryLogic(this);
    private final FileLogic viewLogicFile = new FileLogic(this);

    private Boolean selected = true;
    private Boolean isVisible = true;

    public Favourites() {
        this.origenPath = new Item(Constantes.FAVOURITES);

        setSizeFull();
        addStyleName("content");
        setMargin(false);
        setSpacing(false);

        addComponent(buildHeader(origenPath));

        directoryContent = selectView(selected, origenPath);
        addComponent(directoryContent);
        setExpandRatio(directoryContent, 1);

        Responsive.makeResponsive(this);

    }

    private Component buildHeader(Item directory) {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label title = component.createLabelHeader("Eliminados");
        Component typesViews = buildViewsButtons(directory);

        header.addComponents(title, typesViews);
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

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
