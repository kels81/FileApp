package com.mx.app.view;

import com.mx.app.view.content.ContentView;
import com.mx.app.view.deleted.Bin;
import com.mx.app.view.favourites.Favourites;
import com.mx.app.view.settings.Settings;
import com.vaadin.navigator.View;
import com.vaadin.server.*;

public enum AppViewType {
    ALL_FILES("archivos", ContentView.class, FontAwesome.CLOUD, false),
    FAVOURITES("favoritos", Favourites.class, FontAwesome.STAR, true),
    BIN("eliminados", Bin.class, FontAwesome.TRASH, true),
    SETTINGS("configuracion", Settings.class, FontAwesome.GEAR, false);

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
