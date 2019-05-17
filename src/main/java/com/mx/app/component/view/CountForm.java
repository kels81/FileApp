/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.view;

import com.mx.app.utils.Components;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author ecortesh
 */
public class CountForm extends VerticalLayout {
    
    FormLayout form = new FormLayout();
    
    public CountForm() {
        addStyleName("form-settings");
        setMargin(false);
        setSpacing(false);
        
         form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
         
        addComponent(getSection1());
        addComponent(getSection2());
        addComponent(getSection3());
                 
        Responsive.makeResponsive(this);
    }
    
   private FormLayout getSection1() {
       
       Label section = Components.createLabelSection("OPCIONES GENERALES");
        
        TextField txtPrincipal = Components.createTextSmall("Página principal", "Archivos");
        TextField txtPagina = Components.createTextSmall("Archivos y carpetas por página", "20");
        TextField txtZona = Components.createTextSmall("Zona Horaria", "GMT-07:00 Los Angeles PDT");
        TextField txtIdioma  = Components.createTextSmall("Idioma", "Español (Latinoamérica)");
        
        form.addComponents(section, txtPrincipal, txtPagina, txtZona, txtIdioma);
        
        return form;
   }
   
   private FormLayout getSection2() {
       
       Label section = Components.createLabelSection("AUTENTICACION");
        
        TextField txtCorreoElectronico = Components.createTextSmall("Dirección de correo electronico", "ecortes@otac.mx");
        TextField txtContraseña= Components.createTextSmall("Contraseña actual", "----------");
        
        form.addComponents(section, txtCorreoElectronico, txtContraseña);
        
        return form;
   }
   
   private FormLayout getSection3() {
       
       Label section = Components.createLabelSection("DETALLES DE LA CUENTA");
        
        TextField txtTipoCuenta = Components.createTextSmall("Tipo de cuenta", "Desarrollador");
        TextField txtIdCuenta= Components.createTextSmall("Id de la cuenta", "405573315");
        TextField txtAlmacenamiento= Components.createTextSmall("Almacenamiento utilizado", "275.4 MB de 10.0 GB");
        TextField txtTamañoMaximo= Components.createTextSmall("Tamaño máximo de archivo", "2.0 GB");
        
        form.addComponents(section, txtTipoCuenta, txtIdCuenta, txtAlmacenamiento, txtTamañoMaximo);
        
        return form;
   }

   
}
