package com.sfu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sfu.Menus.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;


class MenuTest {

    private Menu menu;

    @BeforeEach
    void setUp() {

         menu = Menu.createMenu();
        //doNothing().when(menu).startGame(anyString());
        
    }

    @Test
    void testMenuSingleton() {
        // Ensure only one instance of Menu is created
        Menu anotherMenu = (Menu.createMenu());
        assertSame(menu, anotherMenu, "Menu should follow the Singleton pattern");
    }

    @Test
    void testPreferredSize() {
        // Verify that the preferred size matches screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        assertEquals(screenSize, menu.getPreferredSize(), "Menu preferred size should match screen size");
    }

    @Test
    void testBackgroundImageLoading() {
        assertNotNull(menu.getBackground(), "Background image should not be null");
    }


    @Test
    void testPlayButtonActionWithoutDifficulty() {


        JButton playButton = menu.getPlayButton();
        assertNotNull(playButton, "'Play' button should exist in the menu");

        // Simulate clicking the "Play" button without setting a difficulty
        playButton.doClick();

        assertEquals("Medium", menu.getDifficulty());
    }

    @Test
    void testSetDifficulty() {
        // Simulate selecting a difficulty
        menu.actionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, "Difficulty"));

        // Simulate setting a difficulty level
        menu.setDifficulty("Easy");
       assertEquals("Easy", menu.getDifficulty());

       menu.setDifficulty("Nothing");
       assertEquals("Medium", menu.getDifficulty());
    }


    // Utility method to find a JButton by its text
    private JButton findButtonByText(String text) {
        for (Component component : menu.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText() != null && button.getText().equals(text)) {
                    return button;
                }
            }
        }
        return null;
    }

    // Utility method to access private fields for testing
    private Object getPrivateField(Object object, String fieldName) {
        try {
            var field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            fail("Failed to access private field: " + fieldName);
            return null;
        }
    }

    // Utility method to set private fields for testing
    private void setPrivateField(Object object, String fieldName, Object value) {
        try {
            var field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            fail("Failed to set private field: " + fieldName);
        }
    }

    @Test
    void testPlay(){


        JButton playButton = menu.getPlayButton();

        menu.setDifficulty("Easy");

        ActionEvent playEvent = new ActionEvent(playButton, ActionEvent.ACTION_PERFORMED, "Play");

        menu.actionPerformed(playEvent);

        assertFalse(playButton.isEnabled());

    }

    @Test
    void testDifficultyButton(){


    
    }

    

}
