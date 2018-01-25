import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Vector;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class Parser
{
  public Parser(File emgFolder)
    throws Exception
  {
    File[] arrayOfFile;
    int j = (arrayOfFile = emgFolder.listFiles()).length;
    for (int i = 0; i < j; i++)
    {
      File file = arrayOfFile[i];
      if ((!file.isDirectory()) && (file.getName().endsWith(".csv")))
      {
        System.out.println("Datei beginn");
        createPictures(file);
        System.out.println("Datei ende");
      }
    }
  }
  
  private void createPictures(File file)
    throws Exception
  {
    FileReader fr = new FileReader(file);
    BufferedReader br = new BufferedReader(fr);
    Vector<Integer> values = new Vector();
    
    String s = br.readLine();
    while ((s = br.readLine()) != null)
    {
      int val = 0;
      String[] tmp = s.split(",");
      for (int i = 1; i < tmp.length; i++) {
        val += Math.abs(Integer.parseInt(tmp[i]));
      }
      values.add(Integer.valueOf(val));
    }
    createLineCharts(values, file);
  }
  
  private void createLineCharts(Vector<Integer> values, File file)
    throws Exception
  {
    for (int j = 1; j < 7; j++)
    {
      NumberAxis xAxis = new NumberAxis();
      NumberAxis yAxis = new NumberAxis();
      xAxis.setLabel("Zeit");
      yAxis.setLabel("EMG Mittelwerte");
      LineChart<Number, Number> lineChart = new LineChart(xAxis, yAxis);
      lineChart.setTitle("Auswertung EMG Werte");
      
      XYChart.Series series = new XYChart.Series();
      series.setName("EMG Werte");
      for (int i = 0; i < values.size(); i++)
      {
        int insert = getTrendValue(i, values, j);
        if (insert != -1) {
          series.getData().add(new XYChart.Data(Integer.valueOf(i), new Integer(insert)));
        }
      }
      lineChart.getData().add(series);
      lineChart.autosize();
      File parentFile = new File(
        file.getParentFile().getAbsolutePath() + "\\" + file.getName().replace(".csv", "") + "\\");
      if (!parentFile.exists()) {
        parentFile.mkdirs();
      }
      File imgFile = new File(parentFile.getAbsolutePath() + "\\LineChart_" + j + ".png");
      WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imgFile);
    }
  }
  
  private int getTrendValue(int start, Vector<Integer> values, int j)
  {
    int trendWidth = 0;
    switch (j)
    {
    case 1: 
      trendWidth = 20;
      break;
    case 2: 
      trendWidth = 50;
      break;
    case 3: 
      trendWidth = 140;
      break;
    case 4: 
      trendWidth = 220;
      break;
    case 5: 
      trendWidth = 350;
      break;
    case 6: 
      trendWidth = 450;
      break;
    default: 
      trendWidth = 100;
    }
    if (start >= values.size() - trendWidth) {
      return -1;
    }
    int trend = 0;
    for (int i = start; i < start + trendWidth; i++) {
      trend += ((Integer)values.get(i)).intValue();
    }
    trend /= trendWidth;
    return trend;
  }
}