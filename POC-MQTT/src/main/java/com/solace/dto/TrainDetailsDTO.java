package com.solace.dto;

import java.io.Serializable;
import java.util.List;

public class TrainDetailsDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String trainNo;
	private String trainName;
	private String origin;
	private String destination;
	private String deperture;
	private String arrival;
	private String travelTime;
	private String daysOfRun;
	private Days day;
	
	

	

	public TrainDetailsDTO(String trainNo, String trainName, String origin, String destination, String deperture,
			String arrival, String travelTime, String daysOfRun, Days days) {
		super();
		this.trainNo = trainNo;
		this.trainName = trainName;
		this.origin = origin;
		this.destination = destination;
		this.deperture = deperture;
		this.arrival = arrival;
		this.travelTime = travelTime;
		this.daysOfRun = daysOfRun;
		this.day =  days;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDeperture() {
		return deperture;
	}

	public void setDeperture(String deperture) {
		this.deperture = deperture;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getDaysOfRun() {
		return daysOfRun;
	}

	public void setDaysOfRun(String daysOfRun) {
		this.daysOfRun = daysOfRun;
	}

	public Days getDays() {
		return day;
	}

	public void setDays(Days days) {
		this.day = days;
	}
	
	
}
