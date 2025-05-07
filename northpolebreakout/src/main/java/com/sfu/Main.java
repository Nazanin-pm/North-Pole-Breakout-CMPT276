package com.sfu;

import javax.swing.*;

import com.sfu.Menus.Menu;

public class Main {
    public static void main(String[] args) {
        // Window settings
        JFrame window = new JFrame("North Pole Breakout");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        
        
        // Create and set the menu
        Menu menu;
        menu = Menu.createMenu();
        window.add(menu);
        window.pack();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);

    }
}
