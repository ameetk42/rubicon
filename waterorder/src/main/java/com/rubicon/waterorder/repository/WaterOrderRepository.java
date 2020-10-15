package com.rubicon.waterorder.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rubicon.waterorder.entity.WaterOrder;

@Repository
public class WaterOrderRepository {

	//different lists to maintain orders
	List<WaterOrder> listOrders = new ArrayList<>();
	List<WaterOrder> requestedOrders = new ArrayList<>();
	List<WaterOrder> inProgressOrders = new ArrayList<>();
	List<WaterOrder> deliveredOrders = new ArrayList<>();
	List<WaterOrder> cancelledOrders = new ArrayList<>();
	
	
	//create new water order
	public void addWaterOrder(WaterOrder waterOrder) {
		listOrders.add(waterOrder);
		requestedOrders.add(waterOrder);
		
	}//addWaterOrder
	
	/*
	 * Mode numbers define the different states of the order process
	 * for example 2 represents In progress water orders
	*/

	//this changes the list of water orders when changing modes
	public void changeList(WaterOrder wOrder, int mode) {
		
		if(mode==2) {
			inProgressOrders.add(wOrder);
			modifyAllOrdersList(wOrder);//This changes the status in all orders history list
			requestedOrders.remove(wOrder);
		}//if order is being delivered
		
		if(mode==3) {
			deliveredOrders.add(wOrder);
			modifyAllOrdersList(wOrder);
			inProgressOrders.remove(wOrder);
		}//if order is delivered
		
		if(mode==4) {
			cancelledOrders.add(wOrder);
			modifyAllOrdersList(wOrder);
			requestedOrders.remove(wOrder);
		}//if order is cancelled
		
	}//changeList
	
	//This method modifies the status of water order in all orders list
	public boolean modifyAllOrdersList(WaterOrder wOrder) {
		
		for (int loop=0;loop<listOrders.size();loop++) {
			
			//perform checks on id, date and time
			if(listOrders.get(loop).getFarmId() == wOrder.getFarmId() &&
					listOrders.get(loop).getStartDate().equals(wOrder.getStartDate()) &&
					listOrders.get(loop).getStartTime().equals(wOrder.getStartTime())
					)
			{
				listOrders.get(loop).setOrderStatus(wOrder.getOrderStatus());
				return true;
			}//if
		}//for
		return false;
	}//modifyAllOrdersList
	
	
	//return all history of water orders
	public List<WaterOrder> getAllWaterOrders(){
		return listOrders;
	}//getAllWaterOrders
	
	//return all waters orders which are requested but not yet performed
	public List<WaterOrder> getAllRequestedWaterOrders(){
		return requestedOrders;
	}//getAllWaterOrders
	
	//return all waters orders which are in progress
	public List<WaterOrder> getAllInProgressWaterOrders(){
		return inProgressOrders;
	}//getAllWaterOrders
	
}
