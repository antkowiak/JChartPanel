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
	// Serializable Class ID
	protected static final long serialVersionUID = 1L;

	// Default values for parameters
	public static final Color DEFAULT_CHART_BACKGROUND_COLOR = Color.BLACK;
	public static final Color DEFAULT_SERIES_COLOR = Color.WHITE;
	public static final Color DEFAULT_VERTICAL_GUIDE_COLOR = Color.GREEN;
	public static final Color DEFAULT_HORIZONTAL_GUIDE_COLOR = Color.GREEN;
	public static final Color DEFAULT_TIP_COLOR = Color.GREEN;

	public static final Point DEFAULT_TIP_POSITION = new Point(0, 20);
	public static final Point DEFAULT_KEY_POSITION = new Point(0, 45);
	public static final int DEFAULT_KEY_Y_DELTA = 25;

	public static final Font DEFAULT_TIP_FONT = new Font("Arial", Font.BOLD, 20);
	public static final Font DEFAULT_KEY_FONT = new Font("Arial", Font.BOLD, 20);

	// Counter for chart series struct indices
	protected int chartSeriesIndexCounter = 0;

	protected class ChartSeriesStruct
	{
		protected int seriesIndex;
		protected JChartPanelData seriesData;
		protected String seriesName;
		protected Color seriesColor;
		protected boolean visible;

		protected ChartSeriesStruct(JChartPanelData seriesData, String seriesName, Color seriesColor)
		{
			this.seriesIndex = chartSeriesIndexCounter++;
			this.seriesData = seriesData;
			this.seriesName = seriesName;
			this.seriesColor = seriesColor;
			this.visible = true;
		}
	}

	protected List<ChartSeriesStruct> seriesData = new ArrayList<ChartSeriesStruct>();
	protected List<String> tips;
	protected JFrame parentFrame;

	protected Color chartBackgroundColor = DEFAULT_CHART_BACKGROUND_COLOR;
	protected Color tipColor = DEFAULT_TIP_COLOR;
	protected Color verticalGuideColor = DEFAULT_VERTICAL_GUIDE_COLOR;
	protected Color horizontalGuideColor = DEFAULT_HORIZONTAL_GUIDE_COLOR;
	protected Font tipFont = DEFAULT_TIP_FONT;
	protected Font keyFont = DEFAULT_KEY_FONT;
	protected Point tipPosition = DEFAULT_TIP_POSITION;
	protected Point lastMousePosition = new Point();
	protected Point keyPosition = DEFAULT_KEY_POSITION;

	protected boolean showTips = true;
	protected boolean showVerticalGuide = true;
	protected boolean showHorizontalGuide = true;
	protected boolean showKey = true;

	protected int keyYDelta = DEFAULT_KEY_Y_DELTA;

	public JChartPanel()
	{
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
	}

	public void setParentFrame(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}

	public int addSeries(List<Double> series)
	{
		return addSeries(series, "", DEFAULT_SERIES_COLOR);
	}

	public int addSeries(List<Double> series, String seriesName)
	{
		return addSeries(series, seriesName, DEFAULT_SERIES_COLOR);
	}

	public int addSeries(List<Double> series, Color color)
	{
		return addSeries(series, "", color);
	}

	public int addSeries(List<Double> series, String seriesName, Color color)
	{
		if (series != null && color != null)
		{
			ChartSeriesStruct css = new ChartSeriesStruct(new JChartPanelData(series), seriesName, color);
			seriesData.add(css);
			return css.seriesIndex;
		}

		return -1;
	}

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

	public void showKey(boolean showKey)
	{
		this.showKey = showKey;
	}

	public void setKeyPosition(Point keyPosition)
	{
		if (null != keyPosition)
			this.keyPosition = keyPosition;
	}

	public void setKeyFont(Font keyFont)
	{
		if (null != keyFont)
			this.keyFont = keyFont;
	}

	public void setKeyYDelta(int keyYDelta)
	{
		this.keyYDelta = keyYDelta;
	}

	public void setTipColor(Color tipColor)
	{
		if (null != tipColor)
			this.tipColor = tipColor;
	}

	public void setTipFont(Font tipFont)
	{
		if (null != tipFont)
			this.tipFont = tipFont;
	}

	public void setTipPosition(Point tipPosition)
	{
		if (null != tipPosition)
			this.tipPosition = tipPosition;
	}

	public void showTips(boolean showTips)
	{
		this.showTips = showTips;
	}

	public void showVerticalGuide(boolean showVerticalGuide)
	{
		this.showVerticalGuide = showVerticalGuide;
	}

	public void setVerticalGuideColor(Color verticalGuideColor)
	{
		if (null != verticalGuideColor)
			this.verticalGuideColor = verticalGuideColor;
	}

	public void showHorizontalGuide(boolean showHorizontalGuide)
	{
		this.showHorizontalGuide = showHorizontalGuide;
	}

	public void setHorizontalGuideColor(Color horizontalGuideColor)
	{
		if (null != horizontalGuideColor)
			this.horizontalGuideColor = horizontalGuideColor;
	}

	public void setChartBackgroundColor(Color bgColor)
	{
		if (bgColor != null)
			chartBackgroundColor = bgColor;
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.setColor(chartBackgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (ChartSeriesStruct css : seriesData)
		{
			css.seriesData.setDimension(getSize());
		}

		for (ChartSeriesStruct css : seriesData)
		{
			if (css.visible)
			{
				g.setColor(css.seriesColor);

				List<Point> chartPoints = css.seriesData.getPoints();

				for (int i = 1 ; i < chartPoints.size() ; ++i)
				{
					g.drawLine(chartPoints.get(i - 1).x, chartPoints.get(i - 1).y, chartPoints.get(i).x,
							chartPoints.get(i).y);
				}
			}
		}

		if (showTips)
		{
			g.setColor(tipColor);
			g.setFont(tipFont);
			g.drawString(getTip(lastMousePosition), tipPosition.x, tipPosition.y);
		}

		if (showVerticalGuide)
		{
			g.setColor(verticalGuideColor);
			g.drawLine(lastMousePosition.x, 0, lastMousePosition.x, getHeight());
		}

		if (showHorizontalGuide)
		{
			g.setColor(horizontalGuideColor);
			g.drawLine(0, lastMousePosition.y, getWidth(), lastMousePosition.y);
		}

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

	public void setTips(List<String> tips)
	{
		this.tips = tips;
	}

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

	@Override
	public void mouseDragged(MouseEvent e)
	{
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		lastMousePosition = e.getPoint();
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if (lastMousePosition.x > 0)
			{
				lastMousePosition.x--;
				repaint();
			}
		}
		else if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (lastMousePosition.x < getWidth() - 1)
			{
				lastMousePosition.x++;
				repaint();
			}
		}
		if (ke.getKeyCode() == KeyEvent.VK_UP)
		{
			if (lastMousePosition.y > 0)
			{
				lastMousePosition.y--;
				repaint();
			}
		}
		else if (ke.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (lastMousePosition.y < getHeight() - 1)
			{
				lastMousePosition.y++;
				repaint();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent ke)
	{
	}

	@Override
	public void keyTyped(KeyEvent ke)
	{
		if (ke.getKeyChar() == 'q')
		{
			if (parentFrame != null)
			{
				parentFrame.dispose();
			}
		}
		else if (ke.getKeyChar() == 'v')
		{
			showVerticalGuide = !showVerticalGuide;
			repaint();
		}
		else if (ke.getKeyChar() == 'h')
		{
			showHorizontalGuide = !showHorizontalGuide;
			repaint();
		}
		else if (ke.getKeyChar() == 't')
		{
			showTips = !showTips;
			repaint();
		}
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
		else if (ke.getKeyChar() == 'k')
		{
			showKey = !showKey;
			repaint();
		}
		else if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9')
		{
			int index = ke.getKeyChar() - '0';
			toggleSeriesVisible(index);
			repaint();
		}
	}

}
