package com.ryanantkowiak.jchartpanel;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used internally by JChartPanel to manage the data for a series.
 * 
 * @author antko
 *
 */
public class JChartPanelData
{
	protected static final Dimension DEFAULT_DIMENSION = new Dimension(1000, 800);

	public List<Double> series;
	public List<Point> chartPoints;

	public int seriesSize;
	public double minValue;
	public double maxValue;
	public double valueSpan;
	public int pixelWidth;
	public int pixelHeight;
	public double pixelsPerValue;

	@SuppressWarnings("unused")
	private JChartPanelData()
	{
	}

	public JChartPanelData(List<Double> series)
	{
		this.series = series;
		setDimension(DEFAULT_DIMENSION);
	}

	public void setDimension(Dimension dim)
	{
		if (dim != null && (dim.width != pixelWidth || dim.height != pixelHeight))
		{
			pixelWidth = dim.width;
			pixelHeight = dim.height;

			recalculate();
		}
	}

	public List<Point> getPoints()
	{
		return chartPoints;
	}

	public void recalculate()
	{
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
