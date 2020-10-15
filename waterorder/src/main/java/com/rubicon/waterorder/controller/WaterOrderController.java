package com.rubicon.waterorder.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rubicon.waterorder.entity.WaterOrder;
import com.rubicon.waterorder.service.WaterOrderService;

@RestController
public class WaterOrderController {

	@Autowired
	WaterOrderService waterOrderService;

	//to create on order
	@PostMapping("/createOrder")
	public WaterOrder createNewOrder(@RequestBody WaterOrder waterOrder) {
		return waterOrderService.saveWaterOrder(waterOrder);
	}// createNewOrder

	//to get history of all the orders
	@PostMapping("/getAllOrders")
	public List<WaterOrder> retrieveAllOrders() {
		return waterOrderService.getAllWaterOrders();
	}// retrieveAllOrders

	//to cancel an order
	@PostMapping("/cancelOrder")
	public WaterOrder cancelWaterOrder(@RequestBody WaterOrder waterOrder) {
		 return waterOrderService.cancelWaterOrder(waterOrder);
	}// cancelWaterOrder

}//class WaterOrderController
