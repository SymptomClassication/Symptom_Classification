package com.symptom.histogram;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.model.Chapter;
import com.symptom.model.Histogram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class HistogramPage extends JFrame {
    private List<Histogram> histogramList = new ArrayList<>();
    private List<Chapter> chaptersList = new ArrayList<>();
    private int count = 0;
    private final String GET_CLASSIFIED_DATA_URL = "http://dagere.comiles.eu:8098/trainings/definitions";
    private final String FETCH_CHAPTERS_API_URL = "http://dagere.comiles.eu:8098/spacychapters";
    public HistogramPage() {
        // create the dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chaptersList = retrieveChapters();
        histogramList = getHistogramData();
        for(int i =0; i < chaptersList.size(); i++)
        {
            String selectedChapter = chaptersList.get(i).getName().strip();
            //System.out.println(selectedChapter);
            for(int j = 0; j < histogramList.size(); j++)
            {
                String selectedHistogramChapter = histogramList.get(j).getChapterName().strip();
                //System.out.println(selectedHistogramChapter);
                if(selectedChapter.equals(selectedHistogramChapter))
                {
                    count++;
                }
            }
            if(count > 0)
            {
                dataset.setValue(count, "Value", selectedChapter);
                count = 0;
            }
        }
        // create the chart
        JFreeChart chart = ChartFactory.createBarChart("Chapter occurences", "", "Value", dataset, PlotOrientation.VERTICAL, false, true, false);
        //set the category label positions
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        // display the chart
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
        setContentPane(chartPanel);
        pack();
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    @SuppressWarnings("deprecation")
    public List<Histogram> getHistogramData()
    {
        List<Histogram> histogramList = new ArrayList<>();
        try {
            URL url = new URL(GET_CLASSIFIED_DATA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JsonElement chaptersJson = new JsonParser().parse(response.toString());
                JsonArray chaptersArray = chaptersJson.getAsJsonArray();
                for (JsonElement histogramJson : chaptersArray) {
                    JsonObject histogramObject = histogramJson.getAsJsonObject();
                    int id = histogramObject.get("id").getAsInt();
                    String subchapterName = histogramObject.get("subchapterName").getAsString();
                    String symptom = histogramObject.get("symptom").getAsString();
                    String chapterName = histogramObject.get("chapterName").getAsString();
                    Histogram histogram = new Histogram(id,subchapterName,symptom,chapterName);
                    histogramList.add(histogram);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving histogram list: " + e.getMessage());
        }
        return histogramList;
    } 
    @SuppressWarnings("deprecation")
    public List<Chapter> retrieveChapters() {
        List<Chapter> chapters = new ArrayList<>();
        try {
            URL url = new URL(FETCH_CHAPTERS_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JsonElement chaptersJson = new JsonParser().parse(response.toString());
                JsonArray chaptersArray = chaptersJson.getAsJsonArray();
                for (JsonElement chapterJson : chaptersArray) {
                    JsonObject chapterObject = chapterJson.getAsJsonObject();
                    int id = chapterObject.get("id").getAsInt();
                    //System.out.println(id);
                    String name = chapterObject.get("name").getAsString();
                    Chapter chapter = new Chapter(id, name);
                    chapters.add(chapter);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving chapters: " + e.getMessage());
        }
        return chapters;
    }
}
