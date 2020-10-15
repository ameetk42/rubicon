package com.rubicon.waterorder.entity;


public class WaterOrder {

	private String farmId;
	private String startDate;
	private String startTime;
	private int duration;
	private String orderStatus;

	WaterOrder() {

	}// WaterOrder

	public WaterOrder(String farmId, String startDate, String startTime, int duration, String orderStatus) {
		super();
		this.farmId = farmId;
		this.startDate = startDate;
		this.startTime = startTime;
		this.duration = duration;
		this.orderStatus = orderStatus;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
