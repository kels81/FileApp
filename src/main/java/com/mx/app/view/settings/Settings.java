package com.mx.app.view.settings;

import com.mx.app.utils.Components;
import com.vaadin.navigator.*;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class Settings extends VerticalLayout implements View {
    
    private final Components component = new Components();

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

        Label title = new Label("Configuración");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        return header;
    }

    private Component buildTabs() {

        TabSheet tabsSettings = new TabSheet();
        tabsSettings.setSizeFull();
        tabsSettings.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabsSettings.addTab(tabCuenta());
        tabsSettings.addTab(tabNotificaciones());

        return tabsSettings;
    }
    
    private Component tabCuenta() {
//        Panel panel = new Panel("Opciones generales");
//        panel.setSizeFull();
//        
        VerticalLayout vlCuenta = new VerticalLayout();
        vlCuenta.setCaption("Opciones generales");
        
        TextField txtPrincipal = component.createTextSmall("Página principal", "Archivos");
        TextField txtPagina = component.createTextSmall("Archivos y carpetas por página", "20");
        TextField txtZona = component.createTextSmall("Zona Horaria", "GMT-07:00 Los Angeles PDT");
        TextField txtIdioma  = component.createTextSmall("Idioma", "Español (Latinoamérica)");               
        
        vlCuenta.addComponents(txtPrincipal, txtPagina, txtZona, txtIdioma);
        
//        panel.setContent(vlCuenta);
        
        return vlCuenta;
    }
    
    private Component tabNotificaciones() {
        VerticalLayout vlNotificaciones = new VerticalLayout();
        vlNotificaciones.setCaption("Notificaciones");
        
        TextField txtPrincipal = component.createTextSmall("Página principal", "Archivos");
        TextField txtPagina = component.createTextSmall("Archivos y carpetas por página", "20");
        TextField txtZona = component.createTextSmall("Zona Horaria", "GMT-07:00 Los Angeles PDT");
        TextField txtIdioma  = component.createTextSmall("Idioma", "Español (Latinoamérica)");               
        
        vlNotificaciones.addComponents(txtPrincipal, txtPagina, txtZona, txtIdioma);
        
        return vlNotificaciones;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
