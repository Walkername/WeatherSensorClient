package ru.detector;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.detector.dto.MeasurementDTO;
import ru.detector.dto.MeasurementsResponse;

import java.util.*;

public class WeatherSensorClient {
    public static void main(String[] args) {
        WeatherSensorClient client = new WeatherSensorClient();
        //client.createSensor("sensor_user1");
        //client.addMeasurements("sensor_user1", 100);
        //client.getMeasurements();
        client.getRainyDaysCount();
    }

    public String createSensor(String sensorName) {
        String url = "http://localhost:8080/sensors/registration";
        Map<String, Object> jsonToSend = new HashMap<>();
        jsonToSend.put("name", sensorName);
        return sendToAPI(url, jsonToSend);
    }



    public String addMeasurements(String sensorName, int number) {
        String url = "http://localhost:8080/measurements/add";
        Random random = new Random();

        double minTemperature = -70.0;
        double maxTemperature = 80.0;

        StringBuilder responses = new StringBuilder();

        for (int i = 0; i < number; i++) {
            Map<String, Object> jsonToSend = new HashMap<>();
            jsonToSend.put("sensor", Map.of("name", sensorName));

            double temperature = random.nextDouble(maxTemperature);
            boolean raining = random.nextBoolean();

            jsonToSend.put("value", temperature);
            jsonToSend.put("raining", raining);

            responses.append(sendToAPI(url, jsonToSend));
            responses.append("\n");
        }

        return responses.toString();
    }

    public Long getRainyDaysCount() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/measurements/rainyDaysCount";

        return restTemplate.getForObject(url, Long.class);
    }

    public List<MeasurementDTO> getMeasurements(boolean print) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/measurements";
        MeasurementsResponse response = restTemplate.getForObject(url, MeasurementsResponse.class);

        List<MeasurementDTO> measurements;
        if (response == null || response.getMeasurements() == null) {
            measurements = Collections.emptyList();
        } else {
            measurements = response.getMeasurements();
        }

        if (print) {
            for (MeasurementDTO measurement : measurements) {
                System.out.println(measurement);
            }
        }

        return measurements;
    }

    private String sendToAPI(String url, Map<String, Object> jsonToSend) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonToSend, headers);


        try {
            return restTemplate.postForObject(url, request, String.class);
            //System.out.println("Request was successfully sent");
        } catch (HttpClientErrorException e) {
            return e.getMessage();
//            System.out.println("Error to send");
//            System.out.println(e.getMessage());
        }

        //return "";
    }
}