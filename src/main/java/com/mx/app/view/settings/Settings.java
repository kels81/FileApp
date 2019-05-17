package com.mx.app.view.settings;

import com.mx.app.component.view.CountForm;
import com.mx.app.component.view.NotificationForm;
import com.mx.app.utils.Components;
import com.vaadin.navigator.*;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class Settings extends VerticalLayout implements View {
    
    public Settings() {
        setSizeFull();
        addStyleName("transactions");
        setMargin(false);
        setSpacing(false);

        addComponent(buildToolbar());
        addComponentsAndExpand(buildTabs());

    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label title = Components.createLabelHeader("Configuración");
        header.addComponent(title);

        return header;
    }

    private Component buildTabs() {

        TabSheet tabsSettings = new TabSheet();
        tabsSettings.setSizeFull();
        tabsSettings.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
//        tabsSettings.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        tabsSettings.addTab(new CountForm(), "Cuenta");
        tabsSettings.addTab(new NotificationForm(), "Notificaciones");
        return tabsSettings;
    }
    

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
