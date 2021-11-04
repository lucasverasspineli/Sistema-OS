package br.com.sistemaOS.View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class BordaRedonda implements Border {

	private int radius;
	private String cor;
	private Border bord;
	private JButton btnEx;

	BordaRedonda(int radius) {
		this.radius = radius;

	}

	public Insets getBorderInsets(Component c) {
		return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
	}

}
