package com.mx.app.view.content;

import com.mx.app.component.FileGridLayout;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.logic.FileLogic;
import com.mx.app.utils.Constantes;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;



@SuppressWarnings("serial")
public final class ContentView extends VerticalLayout implements View {
    
    private final File origenPath;
    private HorizontalLayout toolBar;
    private Component directoryContent;
    
    private final DirectoryLogic viewLogicDirectory = new DirectoryLogic(this); 
    private final FileLogic viewLogicFile = new FileLogic(this);
    
    private final Boolean selected = true;

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

    }


    private Component buildToolbar(File directory) {
        toolBar = new HorizontalLayout();
        toolBar.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        toolBar.setSpacing(true);
        toolBar.addStyleName("toolbar");
        Responsive.makeResponsive(toolBar);

        Label title = new Label("Content");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        toolBar.addComponent(title);

        return toolBar;
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

    
    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
