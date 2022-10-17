package com.ep.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.ep.bean.EventBean;
import com.ep.controller.EventsController;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EventsHandlerService {

	public static void main(String[] args) {
		// file address
		String filePath = "C:\\Users\\MJohnpaul\\Desktop\\testevents.csv";
		// kafka topic
		String topic = "sampleevents";
		// kafka borker address
		String broker = "127.0.0.1:9092";
		try {
			List<EventBean> events = EventsController.readFileData(filePath);
			EventsController.produce(events, broker, topic);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
