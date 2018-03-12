package com.ryanantkowiak.jchartpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

/**
 * Main class with main function to show how to use the JChartPanel class.
 * 
 * @author antko
 *
 */
public class Main
{
	/**
	 * Main method to show how to use the JChartPanel class.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		// Create a JFrame for the GUI
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Title");
		frame.setSize(new Dimension(800, 600));
		frame.getContentPane().setLayout(new BorderLayout());

		// Create some sample data
		List<Double> series1 = Arrays.asList(5.0, 7.0, 2.0, 1.5, 9.0, 4.0, 7.0, 7.0, 1.0, 3.0);
		List<Double> series2 = Arrays.asList(100.0, 90.0, 110.0, 75.5, 30.0, 35.0, 20.0);

		// Create the JChartPanel and add the sample data series
		JChartPanel chartPanel = new JChartPanel();
		chartPanel.addSeries(series1, "Series 1", Color.RED);
		chartPanel.addSeries(series2, "Series 2", Color.LIGHT_GRAY);	
		
		// Add the chart panel to the frame, and let the chart panel know the frame that
		// owns it.
		frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
		chartPanel.setParentFrame(frame);

		// Set the frame visible
		frame.setVisible(true);
	}
}
