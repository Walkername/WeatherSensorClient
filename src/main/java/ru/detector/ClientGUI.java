package ru.detector;

import ru.detector.dto.MeasurementDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ClientGUI {

    private WeatherSensorClient weatherSensorClient;

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
        client.buildGui();
    }

    private void buildGui() {
        weatherSensorClient = new WeatherSensorClient();

        JFrame mainFrame = new JFrame("Weather Sensor Client");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setBounds(50, 50, 300, 300);
        mainFrame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // RESPONSE AREA
        JTextArea responseArea = new JTextArea();
        responseArea.setEditable(false);
        responseArea.setFocusable(false);
        responseArea.setRows(10);
        JScrollPane scrollPane = new JScrollPane(responseArea);
        scrollPane.setBounds(0, 0, 100, 100);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // CREATE SENSOR
        Box sensorBox = new Box(BoxLayout.Y_AXIS);
        sensorBox.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel sensorLabel = new JLabel("Sensor Name:");
        JTextField sensorNameField = new JTextField();

        JButton createSensorButton = new JButton("Create Sensor");
        createSensorButton.addActionListener(evt -> {
            String response = weatherSensorClient.createSensor(sensorNameField.getText());
            responseArea.setText(response);
        });

        sensorBox.add(sensorLabel);
        sensorBox.add(sensorNameField);
        sensorBox.add(createSensorButton);

        // GENERATE RANDOM MEASUREMENTS
        Box generateBox = new Box(BoxLayout.Y_AXIS);
        generateBox.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel sensorLabel1 = new JLabel("Sensor Name:");
        JTextField sensorNameField1 = new JTextField();

        JLabel numberLabel = new JLabel("Number of measurements:");
        JTextField measurementsNumberField = new JTextField();
        JButton generateMeasurementsButton = new JButton("Generate Random Measurements");
        generateMeasurementsButton.addActionListener(evt -> {
            String response = weatherSensorClient.addMeasurements(
                    sensorNameField1.getText(),
                    Integer.parseInt(measurementsNumberField.getText())
            );
            responseArea.setText(response);
        });

        generateBox.add(sensorLabel1);
        generateBox.add(sensorNameField1);
        generateBox.add(numberLabel);
        generateBox.add(measurementsNumberField);
        generateBox.add(generateMeasurementsButton);

        // GET MEASUREMENTS
        Box getMeasurementsBox = new Box(BoxLayout.Y_AXIS);
        getMeasurementsBox.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton getMeasurementsButton = new JButton("Get Measurements");
        getMeasurementsButton.addActionListener(evt -> {
            List<MeasurementDTO> measurementDTOList = weatherSensorClient.getMeasurements(false);
            StringBuilder measurements = new StringBuilder();
            for (MeasurementDTO measurement : measurementDTOList) {
                measurements.append(measurement.toString());
                measurements.append("\n");
            }
            responseArea.setText(measurements.toString());
        });

        getMeasurementsBox.add(getMeasurementsButton);

        // GET RAINY DAYS COUNT
        JButton getRainyDaysCountButton = new JButton("Get Rainy Days Count");
        getRainyDaysCountButton.addActionListener(evt -> {
            Long response = weatherSensorClient.getRainyDaysCount();
            responseArea.setText(response.toString());
        });

        getMeasurementsBox.add(getRainyDaysCountButton);

        // MAIN PANEL LAYOUT
        mainPanel.add(BorderLayout.WEST, sensorBox);
        mainPanel.add(BorderLayout.CENTER, generateBox);
        mainPanel.add(BorderLayout.EAST, getMeasurementsBox);
        mainPanel.add(BorderLayout.SOUTH, scrollPane);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
