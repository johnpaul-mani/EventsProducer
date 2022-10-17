package com.ep.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import com.ep.bean.EventBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;

public class EventsController {

	private final static ObjectMapper jsonMapper = new ObjectMapper();

	public static List<EventBean> readFileData(String filePath) throws FileNotFoundException, IOException {
		// create an object of file reader with CSV file as a parameter.
		FileReader filereader = new FileReader(filePath);
		// set parsing rules
		CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
		// create csvReader object with parsing rules
		CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(0).withCSVParser(parser).build();
		// map CSV items to event bean object and create list of events
		List<EventBean> events = new CsvToBeanBuilder<EventBean>(csvReader).withType(EventBean.class).build().parse();
		// close the CSV reader
		csvReader.close();
		// close the file reader
		filereader.close();
		return events;
	}

	public static void produce(List<EventBean> events, String brokers, String topicName) throws JsonProcessingException {
		KafkaProducer<byte[], byte[]> producer = new KafkaProducer<byte[], byte[]>(createKafkaProperties(brokers));
		for (EventBean r : events) {
			byte[] data = jsonMapper.writeValueAsBytes(r);
			ProducerRecord<byte[], byte[]> kafkaRecord = new ProducerRecord<byte[], byte[]>(topicName, data);
			producer.send(kafkaRecord);
		}
		producer.close();
	}

	private static Properties createKafkaProperties(String brokers) {
		Properties kafkaProps = new Properties();
		kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
		kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
		return kafkaProps;
	}

}
