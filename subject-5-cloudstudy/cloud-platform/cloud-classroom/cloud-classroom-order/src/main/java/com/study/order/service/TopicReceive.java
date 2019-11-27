package com.study.order.service;

//@Component
public class TopicReceive {

//	@RabbitListener(queues = "topic.payOrderMessage")
	public void process(String context) {
		System.out.println("队列topic.message接收到的消息：" + context);
	}

}
