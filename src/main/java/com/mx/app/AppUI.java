package com.mx.app;

import com.google.common.eventbus.Subscribe;
import com.mx.app.domain.User;
import com.mx.app.event.AppEvent;
import com.mx.app.event.AppEvent.BrowserResizeEvent;
import com.mx.app.event.AppEvent.UserLoggedOutEvent;
import com.mx.app.event.AppEvent.UserLoginRequestedEvent;
import com.mx.app.event.AppEventBus;
import com.mx.app.view.LoginView;
import com.mx.app.view.MainView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("apptheme")
@Title("File App")
public class AppUI extends UI {

    private final AppEventBus appEventbus = new AppEventBus();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        AppEventBus.register(this);
        Responsive.makeResponsive(this);

        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener((final Page.BrowserWindowResizeEvent event) -> {
            AppEventBus.post(new BrowserResizeEvent());
        });
    }
    
    private void updateContent() {
        User user = (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());
        if (user != null) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

//    private void updateContent() {
//        setContent(new MainView());
//        removeStyleName("loginview");
//    }
    
    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
        User user = new User("Maria", "Castro");
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().setLocation("");
    }

    @Subscribe
    public void closeOpenWindows(final AppEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    public static AppEventBus getAppEventbus() {
        return ((AppUI) getCurrent()).appEventbus;
    }

    @WebServlet(urlPatterns = "/*", name = "AppUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class AppUIServlet extends VaadinServlet {
    }
}
