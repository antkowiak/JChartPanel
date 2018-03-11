package com.ryanantkowiak.jchartpanel;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used internally by JChartPanel to manage the data for a series and the
 * calculation of screen placement.
 * 
 * @author antko
 *
 */
public class JChartPanelDataScreenPlacement
{
	/**
	 * Default dimension for a chart panel
	 */
	protected static final Dimension DEFAULT_DIMENSION = new Dimension(1000, 800);

	/**
	 * The raw data points.
	 */
	public List<Double> series;

	/**
	 * The calculated screen positions of each point on the chart panel.
	 */
	public List<Point> chartPoints;

	/**
	 * The number of data points in the series data.
	 */
	public int seriesSize;

	/**
	 * The minimum value in the series data.
	 */
	public double minValue;

	/**
	 * The maximum value in the series data.
	 */
	public double maxValue;

	/**
	 * The size of the range between the minimum and maximum value of the data in
	 * the series.
	 */
	public double valueSpan;

	/**
	 * The height of the chart panel in pixels.
	 */
	public int pixelWidth;

	/**
	 * The width of the chart panel in pixels.
	 */
	public int pixelHeight;

	/**
	 * The calculated amount of pixels per value in the series data.
	 */
	public double pixelsPerValue;

	/**
	 * Constructor for the chart panel data
	 * 
	 * @param series
	 *            - the series data points
	 */
	public JChartPanelDataScreenPlacement(List<Double> series)
	{
		this.series = series;

		seriesSize = series.size();
		minValue = Double.MAX_VALUE;
		maxValue = Double.MIN_VALUE;

		for (int i = 0 ; i < seriesSize ; ++i)
		{
			double v = series.get(i);

			if (v < minValue)
				minValue = v;

			if (v > maxValue)
				maxValue = v;
		}

		valueSpan = maxValue - minValue;

		setDimension(DEFAULT_DIMENSION);
	}

	/**
	 * Sets the screen dimension size of the chart panel.
	 * 
	 * @param dim
	 *            - the dimensions of the size of the chart panel
	 */
	public void setDimension(Dimension dim)
	{
		if (dim != null && (dim.width != pixelWidth || dim.height != pixelHeight))
		{
			pixelWidth = dim.width;
			pixelHeight = dim.height;

			recalculate();
		}
	}

	/**
	 * Returns the list of screen points of the data series on the chart panel, in
	 * pixels.
	 * 
	 * @return - the screen points for the chart panel
	 */
	public List<Point> getPoints()
	{
		return chartPoints;
	}

	/**
	 * Recalculates the screen points of the data series in the chart panel.
	 */
	private void recalculate()
	{
		pixelsPerValue = pixelWidth / (seriesSize - 1.0);

		chartPoints = new ArrayList<Point>();

		for (int i = 0 ; i < seriesSize ; ++i)
		{
			double xCoord = pixelsPerValue * ((double) i);
			double yPctInRange = (series.get(i) - minValue) / (valueSpan);
			double yCoord = pixelHeight - (yPctInRange * pixelHeight);

			chartPoints.add(new Point((int) xCoord, (int) yCoord));
		}
	}

}
