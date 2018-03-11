package com.ryanantkowiak.jchartpanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class definition of JChartPanel. A java component that displays a dynamic
 * chart.
 * 
 * @author antko
 *
 */
public class JChartPanel extends JPanel implements MouseMotionListener, KeyListener
{
	/**
	 * Serializable class ID
	 */
	protected static final long serialVersionUID = 1L;

	/**
	 * Default background color for the chart panel.
	 */
	public static final Color DEFAULT_CHART_BACKGROUND_COLOR = Color.BLACK;

	/**
	 * Default color for the graph and labels of a series of data.
	 */
	public static final Color DEFAULT_SERIES_COLOR = Color.WHITE;

	/**
	 * Default color for the vertical guide.
	 */
	public static final Color DEFAULT_VERTICAL_GUIDE_COLOR = Color.GREEN;

	/**
	 * Default color for the horizontal guide.
	 */
	public static final Color DEFAULT_HORIZONTAL_GUIDE_COLOR = Color.GREEN;

	/**
	 * Default color of the horizontal tips labels.
	 */
	public static final Color DEFAULT_TIP_COLOR = Color.GREEN;

	/**
	 * Default position of the tip labels on the chart panel.
	 */
	public static final Point DEFAULT_TIP_POSITION = new Point(0, 20);

	/**
	 * Default position of the key/legend on the chart panel.
	 */
	public static final Point DEFAULT_KEY_POSITION = new Point(0, 45);

	/**
	 * Default delta (in pixels) in the Y-direction (up and down) between labels on
	 * the key/legend.
	 */
	public static final int DEFAULT_KEY_Y_DELTA = 25;

	/**
	 * Default font used to display the tips.
	 */
	public static final Font DEFAULT_TIP_FONT = new Font("Arial", Font.BOLD, 20);

	/**
	 * Default font used to display the key/legend.
	 */
	public static final Font DEFAULT_KEY_FONT = new Font("Arial", Font.BOLD, 20);

	/**
	 * Counter for chart series struct indices.
	 */
	protected int chartSeriesIndexCounter = 0;

	/**
	 * Class used to track the traits of a series of data that is displayed by a
	 * chart panel. The traits include: the index of the series, the data points,
	 * the name of the series, the color to display the series graph, and a flag to
	 * indicate whether this series is visible in the chart panel.
	 * 
	 * @author antko
	 *
	 */
	protected class ChartSeriesStruct
	{
		/**
		 * Index of the series, used to identify this series and index it.
		 */
		protected int seriesIndex;

		/**
		 * Chart panel data, which includes the raw data point values, in addition to
		 * calculations for pixel placements.
		 */
		protected JChartPanelDataScreenPlacement seriesData;

		/**
		 * The name of the series.
		 */
		protected String seriesName;

		/**
		 * The color to use to display the series data and label.
		 */
		protected Color seriesColor;

		/**
		 * Flag to indicate whether or not this series is visible in the chart panel.
		 */
		protected boolean visible;

		/**
		 * Construct a ChartSeriesStruct object.
		 * 
		 * @param seriesData
		 *            - series data
		 * @param seriesName
		 *            - series name
		 * @param seriesColor
		 *            - color to use when displaying the series
		 */
		protected ChartSeriesStruct(JChartPanelDataScreenPlacement seriesData, String seriesName, Color seriesColor)
		{
			this.seriesIndex = chartSeriesIndexCounter++;
			this.seriesData = seriesData;
			this.seriesName = seriesName;
			this.seriesColor = seriesColor;
			this.visible = true;
		}
	}

	/**
	 * Data structure that maps indices to the actual series data.
	 */
	protected List<ChartSeriesStruct> seriesData = new ArrayList<ChartSeriesStruct>();

	/**
	 * List of tips that are displayed in the chart, as the cursor moves
	 * left-to-right in the chart panel.
	 */
	protected List<String> tips;

	/**
	 * Reference to the parent JFrame that owns this chart. Useful for callbacks on
	 * keyboard events in order to do things such as maximize/restore or close the
	 * window based on a key-press.
	 */
	protected JFrame parentFrame;

	/**
	 * The background color of the chart panel.
	 */
	protected Color chartBackgroundColor = DEFAULT_CHART_BACKGROUND_COLOR;

	/**
	 * The color of the tip text in the chart panel.
	 */
	protected Color tipColor = DEFAULT_TIP_COLOR;

	/**
	 * The color of the vertical guide in the chart panel.
	 */
	protected Color verticalGuideColor = DEFAULT_VERTICAL_GUIDE_COLOR;

	/**
	 * The color of the horizontal guide in the chart panel.
	 */
	protected Color horizontalGuideColor = DEFAULT_HORIZONTAL_GUIDE_COLOR;

	/**
	 * The font used to display the tips in the chart panel.
	 */
	protected Font tipFont = DEFAULT_TIP_FONT;

	/**
	 * The font used to display the key/legend in the chart panel.
	 */
	protected Font keyFont = DEFAULT_KEY_FONT;

	/**
	 * The coordinates of the tips labels in the chart panel.
	 */
	protected Point tipPosition = DEFAULT_TIP_POSITION;

	/**
	 * The most recent mouse/cursor coordinates from a mouse movement within the
	 * chart panel.
	 */
	protected Point lastMousePosition = new Point();

	/**
	 * The coordinates of the key/legend in the chart panel.
	 */
	protected Point keyPosition = DEFAULT_KEY_POSITION;

	/**
	 * Flag to indicate whether the tips text should be displayed in the chart
	 * panel.
	 */
	protected boolean showTips = true;

	/**
	 * Flag to indicate whether the vertical guide should be displayed in the chart
	 * panel.
	 */
	protected boolean showVerticalGuide = true;

	/**
	 * Flag to indicate whether the horizontal guide should be displayed in the
	 * chart panel.
	 */
	protected boolean showHorizontalGuide = true;

	/**
	 * Flag to indicate whether the key/legend should be displayed in the chart
	 * panel.
	 */
	protected boolean showKey = true;

	/**
	 * Delta (in pixels) in the Y-direction (up and down) between labels on the
	 * key/legend.
	 */
	protected int keyYDelta = DEFAULT_KEY_Y_DELTA;

	/**
	 * Construct a JChartPanel.
	 */
	public JChartPanel()
	{
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
	}

	/**
	 * Set the parent JFrame that owns the chart panel.
	 * 
	 * @param parentFrame
	 *            - the JFrame that owns the chart panel
	 */
	public void setParentFrame(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}

	/**
	 * Add a data series to the chart panel.
	 * 
	 * @param series
	 *            - the series data
	 * @return - integer index identifier of the series
	 */
	public int addSeries(List<Double> series)
	{
		return addSeries(series, "", DEFAULT_SERIES_COLOR);
	}

	/**
	 * Add a data series to the chart panel.
	 * 
	 * @param series
	 *            - the series data
	 * @param seriesName
	 *            - the name/label of the series
	 * @return - integer index identifier of the series
	 */
	public int addSeries(List<Double> series, String seriesName)
	{
		return addSeries(series, seriesName, DEFAULT_SERIES_COLOR);
	}

	/**
	 * Add a data series to the chart panel.
	 * 
	 * @param series
	 *            - the series data
	 * @param color
	 *            - the color to use when drawing series data or label name
	 * @return - integer index identifier of the series
	 */
	public int addSeries(List<Double> series, Color color)
	{
		return addSeries(series, "", color);
	}

	/**
	 * Add a data series to the chart panel.
	 * 
	 * @param series
	 *            - the series data
	 * @param seriesName
	 *            - the name/label of the series
	 * @param color
	 *            - the color to use when drawing the series data or label name
	 * @return - integer index identifier of the series
	 */
	public int addSeries(List<Double> series, String seriesName, Color color)
	{
		if (series != null && color != null)
		{
			ChartSeriesStruct css = new ChartSeriesStruct(new JChartPanelDataScreenPlacement(series), seriesName, color);
			seriesData.add(css);
			return css.seriesIndex;
		}

		return -1;
	}

	/**
	 * Remove a data series from the chart panel, identified by the given series
	 * index ID.
	 * 
	 * @param seriesIndex
	 *            - id/index of the series data to remove from the chart panel
	 * @return - true if the series was successfully removed from the chart panel
	 */
	public boolean removeSeries(int seriesIndex)
	{
		for (ChartSeriesStruct css : seriesData)
		{
			if (seriesIndex == css.seriesIndex)
			{
				seriesData.remove(css);
				return true;
			}
		}

		return false;
	}

	/**
	 * Remove all data series data from the chart panel.
	 */
	public void removeAllSeries()
	{
		seriesData.clear();
	}

	/**
	 * Set the visiblity flag of a data series.
	 * 
	 * @param seriesIndex
	 *            - index/id of the data series
	 * @param visible
	 *            - flag to indicate whether the data series should be visible
	 * @return - true if the visibility flag was successfully set
	 */
	public boolean setSeriesVisible(int seriesIndex, boolean visible)
	{
		for (ChartSeriesStruct css : seriesData)
		{
			if (seriesIndex == css.seriesIndex)
			{
				css.visible = visible;
				return true;
			}
		}

		return false;
	}

	/**
	 * Toggle the visibility of series data.
	 * 
	 * @param seriesIndex
	 *            - the index/id of the data series
	 * @return - true if the visibility of the data series was successfully toggled
	 */
	public boolean toggleSeriesVisible(int seriesIndex)
	{
		for (ChartSeriesStruct css : seriesData)
		{
			if (seriesIndex == css.seriesIndex)
			{
				css.visible = !css.visible;
				return true;
			}
		}

		return false;
	}

	/**
	 * Set the color for the display of the series data and the label/name.
	 * 
	 * @param seriesIndex
	 *            - the index/id of the data series
	 * @param color
	 *            - the color to set
	 * @return - true if the color of the data series was successfully set
	 */
	public boolean setSeriesColor(int seriesIndex, Color color)
	{
		if (color != null)
		{
			for (ChartSeriesStruct css : seriesData)
			{
				if (seriesIndex == css.seriesIndex)
				{
					css.seriesColor = color;
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Show/Hide the key/legend of the chart panel.
	 * 
	 * @param showKey
	 *            - flag to indicate if the key/legend should be shown
	 */
	public void showKey(boolean showKey)
	{
		this.showKey = showKey;
	}

	/**
	 * Set the coordinates of the key/legend of the chart panel.
	 * 
	 * @param keyPosition
	 *            - the new coordinates of the key/legend
	 */
	public void setKeyPosition(Point keyPosition)
	{
		if (null != keyPosition)
			this.keyPosition = keyPosition;
	}

	/**
	 * Set the font used to display the key/legend of the chart panel.
	 * 
	 * @param keyFont
	 *            - the new font of of the key/legend
	 */
	public void setKeyFont(Font keyFont)
	{
		if (null != keyFont)
			this.keyFont = keyFont;
	}

	/**
	 * Set the delta (in pixels) in the Y-direction (up and down) between labels on
	 * the key/legend.
	 * 
	 * @param keyYDelta
	 *            - the new delta between labels
	 */
	public void setKeyYDelta(int keyYDelta)
	{
		this.keyYDelta = keyYDelta;
	}

	/**
	 * Set the color of the tip text on the chart panel.
	 * 
	 * @param tipColor
	 *            - the new color to use when displaying the tips
	 */
	public void setTipColor(Color tipColor)
	{
		if (null != tipColor)
			this.tipColor = tipColor;
	}

	/**
	 * Set the font of the tip text on the chart panel.
	 * 
	 * @param tipFont
	 *            - the new font to use when displaying the tips
	 */
	public void setTipFont(Font tipFont)
	{
		if (null != tipFont)
			this.tipFont = tipFont;
	}

	/**
	 * Set the coordinates of the tips text on the chart panel.
	 * 
	 * @param tipPosition
	 *            - the new coordinates of the tips text
	 */
	public void setTipPosition(Point tipPosition)
	{
		if (null != tipPosition)
			this.tipPosition = tipPosition;
	}

	/**
	 * Show/Hide the tips text on the chart panel.
	 * 
	 * @param showTips
	 *            - true if the tips text should be shown on the chart panel
	 */
	public void showTips(boolean showTips)
	{
		this.showTips = showTips;
	}

	/**
	 * Show/Hide the vertical guide on the chart panel.
	 * 
	 * @param showVerticalGuide
	 *            - true if the vertical guide should be shown
	 */
	public void showVerticalGuide(boolean showVerticalGuide)
	{
		this.showVerticalGuide = showVerticalGuide;
	}

	/**
	 * Set the color of the vertical guide on the chart panel.
	 * 
	 * @param verticalGuideColor
	 *            - the new color for the vertical guide
	 */
	public void setVerticalGuideColor(Color verticalGuideColor)
	{
		if (null != verticalGuideColor)
			this.verticalGuideColor = verticalGuideColor;
	}

	/**
	 * Show/Hide the horizontal guide on the chart panel.
	 * 
	 * @param showHorizontalGuide
	 *            - true if the horizontal guide should be shown
	 */
	public void showHorizontalGuide(boolean showHorizontalGuide)
	{
		this.showHorizontalGuide = showHorizontalGuide;
	}

	/**
	 * Set the color of the horizontal guide on the chart panel.
	 * 
	 * @param horizontalGuideColor
	 *            - the new color for the horizontal guide
	 */
	public void setHorizontalGuideColor(Color horizontalGuideColor)
	{
		if (null != horizontalGuideColor)
			this.horizontalGuideColor = horizontalGuideColor;
	}

	/**
	 * Set the background color of the chart panel.
	 * 
	 * @param bgColor
	 *            - the new background color for the chart panel
	 */
	public void setChartBackgroundColor(Color bgColor)
	{
		if (bgColor != null)
			chartBackgroundColor = bgColor;
	}

	/**
	 * Paints the component.
	 * 
	 * @param g
	 *            - the graphics context in which to paint the component
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		// First call super-class paintComponent
		super.paintComponent(g);

		// Set the and draw the background color
		g.setColor(chartBackgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Have each series calculate their screen placement, given the current size of
		// the chart panel window
		for (ChartSeriesStruct css : seriesData)
			css.seriesData.setDimension(getSize());

		// Iterate over all the series data
		for (ChartSeriesStruct css : seriesData)
		{
			// If the data series is visible, draw it
			if (css.visible)
			{
				// Use the appropriate color for each data series
				g.setColor(css.seriesColor);

				// Get the on-screen coordinates of all the data points
				List<Point> chartPoints = css.seriesData.getPoints();

				// Draw line graph of the data points
				for (int i = 1 ; i < chartPoints.size() ; ++i)
					g.drawLine(chartPoints.get(i - 1).x, chartPoints.get(i - 1).y, chartPoints.get(i).x,
							chartPoints.get(i).y);
			}
		}

		// Draw the on-screen tip text, if necessary
		if (showTips)
		{
			g.setColor(tipColor);
			g.setFont(tipFont);
			g.drawString(getTip(lastMousePosition), tipPosition.x, tipPosition.y);
		}

		// Show the vertical guide, if necessary
		if (showVerticalGuide)
		{
			g.setColor(verticalGuideColor);
			g.drawLine(lastMousePosition.x, 0, lastMousePosition.x, getHeight());
		}

		// Show the horizontal guide, if necessary
		if (showHorizontalGuide)
		{
			g.setColor(horizontalGuideColor);
			g.drawLine(0, lastMousePosition.y, getWidth(), lastMousePosition.y);
		}

		// Draw the key/legend, if necessary
		if (showKey)
		{
			Point p = new Point(keyPosition);

			for (ChartSeriesStruct css : seriesData)
			{
				if (css.visible)
				{
					g.setColor(css.seriesColor);
					g.setFont(keyFont);
					g.drawString("" + css.seriesIndex + ": " + css.seriesName, p.x, p.y);
				}

				p.y += keyYDelta;
			}
		}
	}

	/**
	 * Set the tips text for the chart panel.
	 * 
	 * @param tips
	 *            - list of tips text to display as the cursor moves left-to-right
	 *            inside the chart panel
	 */
	public void setTips(List<String> tips)
	{
		this.tips = tips;
	}

	/**
	 * Get the tip text that corresponds to the given point.
	 * 
	 * @param p
	 *            - the coordinate from which to retreive the relevant tip text
	 * @return - the tip text that cooresponds to the given point
	 */
	public String getTip(Point p)
	{
		int xPos = p.x;

		if (tips == null || tips.isEmpty() || xPos < 0 || xPos >= getWidth())
		{
			return "";
		}

		if (tips.size() == 1)
			return tips.get(0);

		double tipsPixelsPerValue = getWidth() / (tips.size() - 1.0);
		int index = (int) ((double) xPos / tipsPixelsPerValue);

		if (index >= 0 && index < tips.size())
		{
			return tips.get(index + 1);
		}

		return "";
	}

	/**
	 * 
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
	}

	/**
	 * 
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		lastMousePosition = e.getPoint();
		repaint();
	}

	/**
	 * Check for keyboard presses.
	 */
	@Override
	public void keyPressed(KeyEvent ke)
	{
		// Left-Arrow - Nudge the vertical guide, one pixel to the left
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if (lastMousePosition.x > 0)
			{
				lastMousePosition.x--;
				repaint();
			}
		}
		// Right-Arrow - Nudge the vertical guide, one pixel to the right
		else if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (lastMousePosition.x < getWidth() - 1)
			{
				lastMousePosition.x++;
				repaint();
			}
		}
		// Up-Arrow - Nudge the horizontal guide, one pixel up
		if (ke.getKeyCode() == KeyEvent.VK_UP)
		{
			if (lastMousePosition.y > 0)
			{
				lastMousePosition.y--;
				repaint();
			}
		}
		// Down-Arrow - Nudge the horizontal guide, one pixel down
		else if (ke.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (lastMousePosition.y < getHeight() - 1)
			{
				lastMousePosition.y++;
				repaint();
			}
		}

	}

	/**
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent ke)
	{
	}

	/**
	 * Check for keys typed
	 */
	@Override
	public void keyTyped(KeyEvent ke)
	{
		// q - Tell the parent JFrame to dispose
		if (ke.getKeyChar() == 'q')
		{
			if (parentFrame != null)
			{
				parentFrame.dispose();
			}
		}
		// v - Toggle display of the vertical guide
		else if (ke.getKeyChar() == 'v')
		{
			showVerticalGuide = !showVerticalGuide;
			repaint();
		}
		// h - Toggle display of the horizontal guide
		else if (ke.getKeyChar() == 'h')
		{
			showHorizontalGuide = !showHorizontalGuide;
			repaint();
		}
		// t - Toggle display of the tips text
		else if (ke.getKeyChar() == 't')
		{
			showTips = !showTips;
			repaint();
		}
		// m - Toggle the display of the mouse cursor over the chart panel
		else if (ke.getKeyChar() == 'm')
		{
			if (getCursor().getName() == "transparent")
			{
				setCursor(Cursor.getDefaultCursor());
			}
			else
			{
				setCursor(getToolkit().createCustomCursor(getToolkit().getImage(""), new Point(), "transparent"));
			}
		}
		// x - Request that the parent Frame maximizes or restores window position
		else if (ke.getKeyChar() == 'x')
		{
			if (null != parentFrame)
			{
				if (parentFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH)
				{
					parentFrame.setExtendedState(JFrame.NORMAL);
				}
				else
				{
					parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
			}
		}
		// k - Toggle display of the key/legend
		else if (ke.getKeyChar() == 'k')
		{
			showKey = !showKey;
			repaint();
		}
		// 0-9 - Toggle the display of the relevant data-series
		else if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9')
		{
			int index = ke.getKeyChar() - '0';
			toggleSeriesVisible(index);
			repaint();
		}
	}

}
