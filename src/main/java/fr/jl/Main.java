/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl;

import fr.jl.views.JfClu;
import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.*;

/**
 *
 */
public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.out.println("Failed to initialize LaF");
        }
        JFrame frame = new JfClu();
        frame.setTitle("Clu");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(1024,768);
        frame.setLocationRelativeTo(null);

    }
}