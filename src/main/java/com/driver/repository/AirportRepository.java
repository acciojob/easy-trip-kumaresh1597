package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {

    private Map<Integer, Flight> FlightData = new HashMap<>();
    private Map<Integer, Passenger> PassengerData = new HashMap<>();
    private Map<String,Airport> AirportData = new HashMap<>();


    private Map<Integer,List<Integer>> BookingTicketsData = new HashMap<>();


    public void addAirport(Airport airport) {
        AirportData.put(airport.getAirportName(),airport);
    }

    public List<Airport> getAllAirports() {
        return new ArrayList<>(AirportData.values());
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(FlightData.values());
    }

    public void addFlight(Flight flight) {
        FlightData.put(flight.getFlightId(),flight);
    }

    public void addPassenger(Passenger passenger) {
        PassengerData.put(passenger.getPassengerId(),passenger);
    }


    public Optional<Flight> getFlight(Integer flightId) {
        for(Map.Entry<Integer,Flight> map : FlightData.entrySet()){
            if(map.getKey() == flightId){
                return Optional.of(map.getValue());
            }
        }
        return Optional.empty();
    }

    public List<Integer> getPassengerListForThisFlight(Integer flightId) {
        for(Map.Entry<Integer,List<Integer>> map : BookingTicketsData.entrySet()){
        if(map.getKey() == flightId){
            return new ArrayList<>(map.getValue());
        }
    }
        return new ArrayList<>();
    }

    public void bookTheTickets(Integer flightId, Integer passengerId) {
        if(BookingTicketsData.containsKey(flightId)){
            List<Integer> bookings = BookingTicketsData.get(flightId);
            bookings.add(passengerId);
            BookingTicketsData.put(flightId,bookings);
        }
        else{
            List<Integer> newBookings = new ArrayList<>();
            newBookings.add(passengerId);
            BookingTicketsData.put(flightId,newBookings);
        }
    }

    public void cancelTheTickets(Integer flightId, Integer passengerId) {
            List<Integer> bookings = BookingTicketsData.get(flightId);
            bookings.remove(passengerId);
            BookingTicketsData.put(flightId,bookings);
    }

    public List<Integer> getFlightListFromBookings() {
        return new ArrayList<>(BookingTicketsData.keySet());
    }

    public Optional<Airport> getAirportByName(String airportName) {
        if(AirportData.containsKey(airportName)) return Optional.of(AirportData.get(airportName));
        return Optional.empty();
    }
}
