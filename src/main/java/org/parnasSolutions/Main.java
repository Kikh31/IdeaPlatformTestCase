package org.parnasSolutions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get(args[0]);
        if (!Files.exists(inputPath)) {
            System.err.println("Файл не найден: " + inputPath);
            System.exit(1);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, List<Ticket>> rootMap = objectMapper.readValue(inputPath.toFile(), new TypeReference<>() {});
        List<Ticket> tickets = rootMap.get("tickets").stream()
                .filter(ticket -> ticket.origin.equals("VVO") && ticket.destination.equals("TLV"))
                .toList();
//        for (Ticket ticket : tickets) {
//            System.out.println(ticket.toString());
//        }

        Map<String, Long> minFlyDurations = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");

        for (Ticket ticket : tickets) {
            LocalDateTime departureDateTime = LocalDateTime.parse(ticket.departureDate + " " + ticket.departureTime, formatter);
            LocalDateTime arrivalDateTime = LocalDateTime.parse(ticket.arrivalDate + " " + ticket.arrivalTime, formatter);
            long minutes = Duration.between(departureDateTime, arrivalDateTime).toMinutes();

            if (minFlyDurations.containsKey(ticket.carrier)) {
                long oldValue = minFlyDurations.get(ticket.carrier);
                minFlyDurations.put(ticket.carrier, Math.min(oldValue, minutes));
            } else {
                minFlyDurations.put(ticket.carrier, minutes);
            }
        }
        System.out.println("Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика:");
        for (Map.Entry<String, Long> e : minFlyDurations.entrySet()) {
            long hours = e.getValue() / 60;
            long minutes = e.getValue() % 60;
            System.out.printf("%s: %d ч %d мин%n", e.getKey(), hours, minutes);
        }

        int[] sortedPricesArray = tickets.stream()
                .map(ticket -> ticket.price)
                .mapToInt(Integer::intValue)
                .sorted()
                .toArray();

        Double averagePrice = calculateAvgPrice(sortedPricesArray);
        Double medianPrice = calculateMedianPrice(sortedPricesArray);
        double diffBetweenAvgAndMedianPrice = averagePrice - medianPrice;
        System.out.println("Разницa между средней ценой и медианой для полета между городами Владивосток и Тель-Авив: " + diffBetweenAvgAndMedianPrice);
    }

    private static Double calculateMedianPrice(int[] array) {
        if (array.length % 2 == 0) {
            return (array[array.length / 2 - 1] + array[array.length / 2]) / 2.0;
        } else {
            return (double) array[array.length / 2];
        }
    }

    private static Double calculateAvgPrice(int[] array) {
        return Arrays.stream(array).average().orElse(0.0);
    }
}