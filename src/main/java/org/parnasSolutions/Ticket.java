package org.parnasSolutions;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Ticket {
    public String origin;

    @JsonSetter("origin_name")
    public String originName;

    public String destination;

    @JsonSetter("destination_name")
    public String destinationName;

    @JsonSetter("departure_date")
    public String departureDate;

    @JsonSetter("departure_time")
    public String departureTime;

    @JsonSetter("arrival_date")
    public String arrivalDate;

    @JsonSetter("arrival_time")
    public String arrivalTime;

    public String carrier;
    public int stops;
    public int price;

    @Override
    public String toString() {
        return String.format(
                "Ticket[%s (%s) → %s (%s), вылет %s %s, прилёт %s %s, перевозчик: %s, пересадок: %d, цена: %d]",
                origin, originName,
                destination, destinationName,
                departureDate, departureTime,
                arrivalDate, arrivalTime,
                carrier, stops, price);
    }
}
