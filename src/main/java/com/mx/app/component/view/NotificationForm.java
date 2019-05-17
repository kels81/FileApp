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
public class NotificationForm extends VerticalLayout {

    FormLayout form = new FormLayout();

    public NotificationForm() {
        addStyleName("form-settings");
        setMargin(false);
        setSpacing(false);

        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        addComponent(getSection1());

        Responsive.makeResponsive(this);
    }

    private FormLayout getSection1() {

        Label section = Components.createLabelSection("ALERTAS");

        TextField txtPregunta = Components.createTextSmall("Enviarme un correo electronico cuando:", "");
        CheckBox chb_1 = Components.createCheckBoxSmall("Me estoy quedando sin espacio", true);
        CheckBox chb_2 = Components.createCheckBoxSmall("Elimino una gran cantidad de archivos", true);
        CheckBox chb_3 = Components.createCheckBoxSmall("Se vincula un nuevo dispositivo", true);
        CheckBox chb_4 = Components.createCheckBoxSmall("Se vincula una nueva aplicación", true);

        form.addComponents(section, txtPregunta, chb_1, chb_2, chb_3, chb_4);

        return form;
    }

}
