package com.driver.service;

import com.driver.repository.AirportRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AirportService {
    @Autowired
    private AirportRepository airportRepository;
    public void addAirportToDB(Airport airport) {
        airportRepository.addAirport(airport);
    }

    public String getLargeAirport() {
        List<Airport> airportList = airportRepository.getAllAirports();
        int maxTerminal = 0;
        String largest = "";
        for(Airport A : airportList){
            if(A.getNoOfTerminals() > maxTerminal){
                maxTerminal = A.getNoOfTerminals();
                largest = A.getAirportName();
            } else if (A.getNoOfTerminals() == maxTerminal) {
                int res = A.getAirportName().compareTo(largest);
                if(res < 0){
                    largest = A.getAirportName();
                }
            }
        }
        return largest;
    }

    public double getShortestDuration(City fromCity, City toCity) {
        List<Flight> flightList = airportRepository.getAllFlights();
        double shortestDuration = Integer.MAX_VALUE;
        for(Flight f : flightList){
            if(f.getFromCity().equals(fromCity) && f.getToCity().equals(toCity)){
                shortestDuration = Math.min(shortestDuration,f.getDuration());
            }
        }
        if(shortestDuration == Integer.MAX_VALUE) return -1;
        return shortestDuration;
    }

    public void addFlightToDB(Flight flight) {
        airportRepository.addFlight(flight);
    }

    public void addPassengerToDB(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }

    public String bookTicket(Integer flightId, Integer passengerId) {
        Optional<Flight> flight = airportRepository.getFlight(flightId);
        if(flight.isEmpty()) return "FAILURE";
        List<Integer> passengerList = airportRepository.getPassengerListForThisFlight(flightId);
        if(passengerList.size() > flight.get().getMaxCapacity()) return "FAILURE";
        for(int p : passengerList){
            if(p == passengerId) return "FAILURE";
        }
        airportRepository.bookTheTickets(flightId,passengerId);
        return  "Success";
    }

    public String cancelTickets(Integer flightId, Integer passengerId) {
        Optional<Flight> flight = airportRepository.getFlight(flightId);
        if(flight.isEmpty()) return "FAILURE";
        List<Integer> passengerList = airportRepository.getPassengerListForThisFlight(flightId);
        for(int p : passengerList){
            if(p == passengerId){
                airportRepository.cancelTheTickets(flightId,passengerId);
                return "SUCCESS";
            }
        }
        return  "FAILURE";
    }

    public int getFlightTicketPrice(Integer flightId) {
        List<Integer> passengerList = airportRepository.getPassengerListForThisFlight(flightId);
        int noOfTicketsAlreadyBooked = passengerList.size();
        return 3000 + ((noOfTicketsAlreadyBooked + 1 )*50);
    }

    public int getRevenue(Integer flightId) {
        List<Integer> passengerList = airportRepository.getPassengerListForThisFlight(flightId);
        int size = passengerList.size();
        int sum = (size * (size + 1))/2;
        return (size * 3000) + (sum * 50);
    }

    public int getBookingsFromPassengers(Integer passengerId) {
        List<Integer> flightListFromBookings = airportRepository.getFlightListFromBookings();
        int count = 0;
        for(int id : flightListFromBookings){
            List<Integer> passengerList = airportRepository.getPassengerListForThisFlight(id);
            for(int i=0;i<passengerList.size();i++){
                if(passengerList.get(i) == passengerId){
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public int getNumberOfPeople(Date date, String airportName) {
        List<Flight> flightList = airportRepository.getAllFlights();
        Optional<Airport> airport = airportRepository.getAirportByName(airportName);
        int totalPassengers = 0;
        for(Flight f : flightList){
            if(f.getFlightDate().equals(date) && ((f.getFromCity().equals(airport.get().getCity())) || (f.getToCity().equals(airport.get().getCity())))){
                    List<Integer> passengerList = airportRepository.getPassengerListForThisFlight(f.getFlightId());
                    totalPassengers += passengerList.size();
            }
        }
        return totalPassengers;
    }

    public String getAirportName(Integer flightId) {
        Optional<Flight> flight = airportRepository.getFlight(flightId);
        if(flight.isEmpty()) return null;
        City city = flight.get().getFromCity();
        List<Airport> airports = airportRepository.getAllAirports();
        for(Airport a : airports){
            if(a.getCity().equals(city)) return a.getAirportName();
        }
        return null;
    }
}
