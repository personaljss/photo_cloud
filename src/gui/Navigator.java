package gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JFrame;

import authentication.AuthFrame;
import gui.home.DiscoveryPage;
import gui.profile.ProfilePage;

public class Navigator {
    private static Navigator instance;
    private AuthFrame authFrame;
    private ProfilePage profilePage;
    private DiscoveryPage discoveryPage;
    private Map<String, Stack<JFrame>> navigationStacks;

    private Navigator() {
        // Initialize the navigation stacks
        navigationStacks = new HashMap<>();
        navigationStacks.put("PROFILE", new Stack<>());
        navigationStacks.put("DISCOVERY", new Stack<>());

        // Create the initial AuthFrame
        authFrame = new AuthFrame();
    }

    /**
     * Retrieves the singleton instance of the Navigator class.
     * @return The Navigator instance.
     */
    public static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    /**
     * Navigates to the specified page.
     * @param pageName The name of the page to navigate to.
     */
    /*
    public void navigateTo(String pageName) {
        Stack<JFrame> navigationStack = navigationStacks.get(pageName);

        if (!navigationStack.isEmpty()) {
            // Hide the current page on top of the stack
            JFrame currentFrame = navigationStack.peek();
            currentFrame.setVisible(false);
        }

        // Create or retrieve the requested page
        JFrame page = getPage(pageName);
        navigationStack.push(page);

        // Show the page
        page.setVisible(true);
    }
    */

    /**
     * Navigates back to the previous page.
     * @param pageName The name of the current page.
     */
    public void navigateBack(String pageName) {
        Stack<JFrame> navigationStack = navigationStacks.get(pageName);

        if (!navigationStack.isEmpty()) {
            // Hide the current page on top of the stack
            JFrame currentFrame = navigationStack.pop();
            currentFrame.setVisible(false);
        }

        if (!navigationStack.isEmpty()) {
            // Show the previous page on top of the stack
            JFrame previousFrame = navigationStack.peek();
            previousFrame.setVisible(true);
        }
    }

    /**
     * Creates or retrieves the requested page.
     * @param pageName The name of the page to create or retrieve.
     * @return The JFrame instance of the requested page.
     */
    /*
    private JFrame getPage(String pageName) {
        switch (pageName) {
            case "PROFILE":
                if (profilePage == null) {
                    //profilePage = new ProfilePage();
                    //profilePage.setNavigator(this);
                }
                return profilePage;
            case "DISCOVERY":
                if (discoveryPage == null) {
                    discoveryPage = new DiscoveryPage();
                    //discoveryPage.setNavigator(this);
                }
                return discoveryPage;
            default:
                throw new IllegalArgumentException("Invalid page name: " + pageName);
        }
    }
    */
}

