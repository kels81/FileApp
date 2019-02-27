package com.mx.app.view.transactions;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;



@SuppressWarnings("serial")
public final class TransactionsView extends VerticalLayout implements View {

    public TransactionsView() {
        setSizeFull();
        addStyleName("transactions");
        setMargin(false);
        setSpacing(false);

        addComponent(buildToolbar());

    }


    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label title = new Label("Transactions");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        return header;
    }

    
    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
