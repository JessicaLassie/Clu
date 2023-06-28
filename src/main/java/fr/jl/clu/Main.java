/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.clu;

import com.formdev.flatlaf.FlatDarculaLaf;
import fr.jl.clu.views.JfClu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * The main class
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            LOGGER.warn("Failed to initialize LaF");
        }
        JFrame frame = new JfClu();
        frame.setTitle("Clu");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(ClassLoader.getSystemResource("clu_icon_becris_lineal_20.png")).getImage());

    }
}