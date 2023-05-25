package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Stack;

import javax.swing.JFrame;

import gui.authentication.AuthFrame;
import gui.home.DiscoveryPage;
import gui.profile.OthersProfilePage;
import gui.profile.ProfilePage;
import models.User;

/**
 * The Navigator class handles navigation between different frames in the GUI.
 */
public class Navigator {
    public static final String AUTH_FRAME = "AuthFrame";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String DISCOVERY_PAGE = "DiscoveryPage";
    public static final String OTHERS_PROFILE_PAGE = "OthersProfilePage";

    private static Navigator instance;

    private JFrame currentFrame;
    private Stack<String> frameStack;
    private Stack<User> userStack;

    private Navigator() {
        frameStack = new Stack<>();
        userStack = new Stack<>();
    }

    /**
     * Retrieves the singleton instance of the Navigator class.
     *
     * @return the Navigator instance
     */
    public static synchronized Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    /**
     * Navigates to the specified frame without passing a user object.
     *
     * @param frameName the name of the frame to navigate to
     */
    public void navigateTo(String frameName) {
        navigateTo(frameName, null);
    }

    /**
     * Navigates to the specified frame with an optional user object.
     *
     * @param frameName the name of the frame to navigate to
     * @param user      the user object to pass to the frame (can be null)
     */
    public void navigateTo(String frameName, User user) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        if (frameName.equals(AUTH_FRAME)) {
            this.frameStack.clear();
            this.userStack.clear();
        }
        currentFrame = createFrame(frameName, user);
        currentFrame.setVisible(true);
        frameStack.push(frameName);
    }

    /**
     * Navigates back to the previous frame.
     */
    public void navigateBack() {
        if (!frameStack.isEmpty()) {
            frameStack.pop();
        }

        if (currentFrame != null) {
            currentFrame.dispose();
        }

        if (!frameStack.isEmpty()) {
            String previousFrame = frameStack.peek();
            if (previousFrame.equals(OTHERS_PROFILE_PAGE)) {
                userStack.pop();
                User user = userStack.isEmpty() ? null : userStack.peek();
                currentFrame = createFrame(previousFrame, user);
            } else {
                currentFrame = createFrame(previousFrame, null);
            }
            currentFrame.setVisible(true);
        }
    }

    /**
     * Creates and configures a new JFrame based on the specified frame name and user object.
     *
     * @param frameName the name of the frame to create
     * @param user      the user object to pass to the frame (can be null)
     * @return the created JFrame
     */
    private JFrame createFrame(String frameName, User user) {
        JFrame frame = null;
        switch (frameName) {
            case AUTH_FRAME:
                frame = new AuthFrame();
                frame.setMinimumSize(new Dimension(500, 700));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                return frame;
            case PROFILE_PAGE:
                frame = new ProfilePage();
                break;
            case DISCOVERY_PAGE:
                frame = new DiscoveryPage();
                break;
            case OTHERS_PROFILE_PAGE:
                frame = new OthersProfilePage(user);
                userStack.push(user);
                break;
        }

        if (frame != null) {
            Dimension previousSize = null;
            Rectangle previousBounds = null;
            if (currentFrame != null && !(currentFrame instanceof AuthFrame)) {
                previousSize = currentFrame.getSize();
                previousBounds = currentFrame.getBounds();
            }

            if (previousSize != null) {
                frame.setSize(previousSize);
            } else {
                frame.setSize(Application.getInstance().getDimension());
            }

            if (previousBounds != null) {
                frame.setLocation(previousBounds.getLocation());
            } else {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
            }
        }

        return frame;
    }
}
