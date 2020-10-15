package com.rubicon.waterorder.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.rubicon.waterorder.entity.WaterOrder;
import com.rubicon.waterorder.exception.ApiRequestException;
import com.rubicon.waterorder.repository.WaterOrderRepository;

@Service
public class WaterOrderService {

	@Autowired
	WaterOrderRepository waterOrderRepository;

	// enum for different status of the water order
	enum Status {
		// String argument
		REQUESTED("Order has been placed but not yet delivered."), INPROGRESS("Order is being delivered right now."),
		DELIVERED("Order has been delivered."), CANCELLED("Order was cancelled before delivery.");

		// declaring private variable for getting values
		private String value;

		// getter method
		public String getValues() {
			return this.value;
		}

		// enum constructor
		private Status(String value) {
			this.value = value;
		}
	}

	
	
	@Scheduled(fixedDelay = 1000 * 5)
	public void performOperation() {

		// for all new requested water orders, it will start the water order on date and
		// time requested

		List<WaterOrder> requestWatOrders = getAllRequestedWaterOrders();
		List<WaterOrder> inProgressWatOrders = getAllInProgessWaterOrders();

		// it will check all the water orders requested and perform operation
		for (int loop = 0; loop < requestWatOrders.size(); loop++) {

			//checks if the date matches
			if (LocalDate.parse(requestWatOrders.get(loop).getStartDate(), DateTimeFormatter.ofPattern("d/MM/yyyy"))
					.equals(LocalDate.now())) {

				//checks if the time matches after date matched
				if (LocalTime.parse(requestWatOrders.get(loop).getStartTime())
						.compareTo(LocalTime.now().truncatedTo(ChronoUnit.MINUTES)) == 0) {

					String currfarmId = requestWatOrders.get(loop).getFarmId();
					changeOrderStatus(requestWatOrders.get(loop), 2);
					System.out.println(
							"Water delivery to  farm " + currfarmId + " started " + " at " + LocalDateTime.now());
				} // if the time matches as well
			} // if date matches
		} // for loop iteration on active orders

		
		// for all the in progress water orders
		for (int loop = 0; loop < inProgressWatOrders.size(); loop++) {

			//check when to end the water delivery i-e date, time and duration
						if (LocalTime.parse(inProgressWatOrders.get(loop).getStartTime())
					.plusHours(inProgressWatOrders.get(loop).getDuration())
					.compareTo(LocalTime.now().truncatedTo(ChronoUnit.MINUTES)) == 0) {

				String currfarmId = inProgressWatOrders.get(loop).getFarmId();
				changeOrderStatus(inProgressWatOrders.get(loop), 3);
				System.out.println("Water delivery to farm " + currfarmId + " stopped " + " at " + LocalDateTime.now());

			} // if the time matches as well
		} // for loop in progress water orders


	}//performOperation
	
	/*
	 * Mode numbers define the different states of the order process
	 * for example 2 represents In progress water orders
	*/

	//changes the status of the water order during different operations
	public WaterOrder changeOrderStatus(WaterOrder watOrder, int mode) {

		// when order is being delivered
		if (mode == 2) {
			watOrder.setOrderStatus(Status.INPROGRESS.value);
			waterOrderRepository.changeList(watOrder, mode);
		}

		// when order is delivered
		if (mode == 3) {
			watOrder.setOrderStatus(Status.DELIVERED.value);
			waterOrderRepository.changeList(watOrder, mode);
			// System.out.println("Order has been delivered.");
		}

		// when order is cancelled
		if (mode == 4) {
			watOrder.setOrderStatus(Status.CANCELLED.value);
			waterOrderRepository.changeList(watOrder, mode);
			// System.out.println("Order has been canclled.");
		}

		return watOrder;
	}// changeOrderStatus

	/*
	 * Validates any given two string inputs for a particular pattern of date and
	 * time
	 */
	public boolean validateDateTime(String date, String time) {
		try {
			LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
			LocalTime.parse(time);

		} catch (Exception e) {
			return false;
		}

		return true;
	}// validateDateTime

	public WaterOrder saveWaterOrder(WaterOrder waterOrder) {
		// check if the fields are empty
		if (waterOrder.getStartDate() != null && waterOrder.getStartTime() != null && waterOrder.getDuration() != 0
				&& (!waterOrder.getFarmId().isBlank() && !waterOrder.getFarmId().isEmpty())) {

			// Checks if the user input is in correct format for date and time
			boolean resultDTValidation = validateDateTime(waterOrder.getStartDate(), waterOrder.getStartTime());

			if (!resultDTValidation) {
				// System.err.println("Exception Occured in Parsing the Date and Time ");
				throw new ApiRequestException("Please Enter Correct Format for Time and Date "
						+ "for example '15/02/2020' for date and '15:45' for time");
		
			} // if date and time not valid

		
			// now check here if the order is not overlapping
			for (WaterOrder order : waterOrderRepository.getAllWaterOrders()) {
				if (order.getFarmId().equals(waterOrder.getFarmId())
						&& order.getStartDate().equals(waterOrder.getStartDate())) {

					//checks if the start time is same but order is cancelled or not
					if (order.getStartTime().equals(waterOrder.getStartTime())) {

						if (!order.getOrderStatus().equals(Status.CANCELLED.value)) {
							throw new ApiRequestException("Order Already exits for the same time");
						}
					} // if both start time equals but one is cancelled
					else {
						LocalTime curOrdTime = LocalTime.parse(waterOrder.getStartTime());
						LocalTime exitstingOrdTime = LocalTime.parse(order.getStartTime());

						//checks if the order is overlapping the with the duration of other water order						
						if (curOrdTime.isBefore(exitstingOrdTime.plusHours(order.getDuration()))) {
							// System.out.println("in different time section");
							throw new ApiRequestException("Please change the time");
						}//if

					} // else

				} // if
			}// for 
	
			//add the water order
			waterOrder.setOrderStatus(Status.REQUESTED.value);
			waterOrderRepository.addWaterOrder(waterOrder);
			System.out.println(
					"New water order for farm " + waterOrder.getFarmId() + " created" + " at " + LocalDateTime.now());
			return waterOrder;
		}
	
		throw new ApiRequestException("Please Enter Correct Values");
		
	}// saveWaterOrder

	public WaterOrder cancelWaterOrder(WaterOrder waterOrder) {

		//performs validation
		if (waterOrder.getStartDate() != null && waterOrder.getStartTime() != null && waterOrder.getDuration() != 0
				&& waterOrder.getFarmId() != null
				&& (!waterOrder.getFarmId().isBlank() && !waterOrder.getFarmId().isEmpty())) {
			// This block checks if the user input is in correct format for date and time
			boolean resultDTValidation = validateDateTime(waterOrder.getStartDate(), waterOrder.getStartTime());
			if (resultDTValidation != false) {

				List<WaterOrder> reqWatOrders = getAllRequestedWaterOrders();

				for (int loop = 0; loop < reqWatOrders.size();)

				{
					//checks if water order is already is being delivered or finished
					if (reqWatOrders.get(loop).getFarmId().equals(waterOrder.getFarmId())
							&& reqWatOrders.get(loop).getStartDate().equals(waterOrder.getStartDate())
							&& reqWatOrders.get(loop).getStartTime().equals(waterOrder.getStartTime())) {
						if (reqWatOrders.get(loop).getOrderStatus().equals(Status.REQUESTED.value)) {

							System.out.println("Water order cancelled for form " + reqWatOrders.get(loop).getFarmId()
									+ " at " + LocalDateTime.now());
							return changeOrderStatus(reqWatOrders.get(loop), 4);
							// reqWatOrders.get(loop).setOrderStatus("Order was cancelled before
							// delivery.");
							// return reqWatOrders.get(loop);
						} // if status also matches
						else {
							throw new ApiRequestException(
									"Can not cancel an order which is already is in process or being delivered.");
						} // if water is in progress or already delivered
					} // if matches
					else {
						throw new ApiRequestException("Can not Find Order");
					}
				}

				throw new ApiRequestException("There are no Water Orders to be cancelled");

			} //
			else {
				// System.err.println("Exception Occured in Parsing the Date and Time ");
				throw new ApiRequestException("Please Enter Correct Format for Time and Date "
						+ "for example '15/02/2020' for date and '15:45' for time");

			} // if date and time not valid

		} // if data entered is not 0 or null
		else {
			throw new ApiRequestException("Please Enter Correct Values");

		} // if any data null or 0

	}// cancelWaterOrder

	public String deleteWaterOrderByName() {
		return "";
	}// deleteWaterOrderByName

	public String deleteWaterOrderById() {
		return "";
	}// deleteWaterOrderById

	public List<WaterOrder> getAllWaterOrders() {
		return waterOrderRepository.getAllWaterOrders();
	}// getAllWaterOrders

	public List<WaterOrder> getAllRequestedWaterOrders() {
		// System.out.println("getting all req wat order service");
		return waterOrderRepository.getAllRequestedWaterOrders();
	}// getAllWaterOrders

	public List<WaterOrder> getAllInProgessWaterOrders() {
		return waterOrderRepository.getAllInProgressWaterOrders();
	}// getAllWaterOrders

	public WaterOrder getWaterOrderById() {
		return null;
	}// getWaterOrderById

	public WaterOrder getWaterOrderByName() {
		return null;
	}// getWaterOrderByName

	public String updateWaterOrder() {
		return "";
	}// updateWaterOrder

}
