package com.sfu;

import com.sfu.Entities.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KeyInput {

    private final ArrayList<String> keyInputs = new ArrayList<>();
    Set<String> validInputs = new HashSet<>(Arrays.asList("WD", "WA", "SD", "SA", "DW", "AW", "DS", "AS"));
    Set<String> correctOrder = new HashSet<>(Arrays.asList("WD", "WA", "SD", "SA"));

    //functions for what to do on key press
    public class wAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (!keyInputs.contains("W")){
                keyInputs.add("W");
            }
        }
    }

    public class aAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (!keyInputs.contains("A")){
                keyInputs.add("A");
            }
        }
    }

    public class sAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (!keyInputs.contains("S")){
                keyInputs.add("S");
            }
        }
    }

    public class dAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (!keyInputs.contains("D")){
                keyInputs.add("D");
            }
        }
    }

    public class W_Release_Action extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            keyInputs.remove("W");
        }
    }

    public class A_Release_Action extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            keyInputs.remove("A");
        }
    }

    public class S_Release_Action extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            keyInputs.remove("S");
        }
    }

    public class D_Release_Action extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            keyInputs.remove("D");
        }
    }

    public KeyInput(Game game) {
        setupKeys(game);
    }

    public void setupKeys(Game game){

        Action wPressed = new KeyInput.wAction();
        Action aPressed = new KeyInput.aAction();
        Action sPressed = new KeyInput.sAction();
        Action dPressed = new KeyInput.dAction();
        Action wReleased = new KeyInput.W_Release_Action();
        Action aReleased = new KeyInput.A_Release_Action();
        Action sReleased = new KeyInput.S_Release_Action();
        Action dReleased = new KeyInput.D_Release_Action();

        // maps keystroke to an Action variable which then calls its function
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "w pressed");
        game.getActionMap().put("w pressed", wPressed);
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "a pressed");
        game.getActionMap().put("a pressed", aPressed);
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "s pressed");
        game.getActionMap().put("s pressed", sPressed);
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "d pressed");
        game.getActionMap().put("d pressed", dPressed);

        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(87,0,
                true), "released W");
        game.getActionMap().put("released W", wReleased);
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(65,0,
                true), "released A");
        game.getActionMap().put("released A", aReleased);
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(83,0,
                true), "released S");
        game.getActionMap().put("released S", sReleased);
        game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(68,0,
                true), "released D");
        game.getActionMap().put("released D", dReleased);

        game.setFocusable(true);
    }

    /**
     * Finds the latest valid keyboard inputs and converts it into a string
     *
     * @return A string that is either empty, or related to a directional input
     */
    public String determineMovingDirection(){

        if (keyInputs.isEmpty()){
            return "";
        }

        if (keyInputs.size() == 1){
            return keyInputs.get(0);
        }

        String lastInput = keyInputs.get(keyInputs.size()-1);
        String secondLastInput = keyInputs.get(keyInputs.size()-2);

        String diagonalMove = lastInput + secondLastInput;

        if (this.validInputs.contains(diagonalMove)) {
            if(this.correctOrder.contains(diagonalMove)){
                return diagonalMove;
            }
            return secondLastInput + lastInput;
        }
        else {
            return lastInput;
        }
    }

    public boolean isEmpty(){
        return keyInputs.isEmpty();
    }

    public Action getWAction() {
        return new wAction();
    }

    public Action getAAction() {
        return new aAction();
    }

    public Action getSAction() {
        return new sAction();
    }

    public Action getDAction() {
        return new dAction();
    }
}
