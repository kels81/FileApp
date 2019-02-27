package com.mx.app.view;

import com.mx.app.view.content.ContentView;
import com.mx.app.view.transactions.TransactionsView;
import com.mx.app.view.schedule.ScheduleView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum AppViewType {
    TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE, true),
    SCHEDULE("schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false),
    CONTENT("archivos", ContentView.class, FontAwesome.CLOUD, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private AppViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static AppViewType getByViewName(final String viewName) {
        AppViewType result = null;
        for (AppViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
