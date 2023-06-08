package edu.touro.cs.mcon364;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

class GUIMain {

    public static void main(String[] args) {
        OthelloModel model = new OthelloModel();
        new OthelloGUI(model);
    }
}




