package com.dist.message.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import com.dist.message.server.model.TextMessage;

@RestController("chat")
public class ChatAction {

	private static final Logger LOG = LoggerFactory.getLogger(ChatAction.class);
	/**
	 * SimpMessagingTemplate是SimpMessageSendingOperations的实现类
	 */
	@Autowired
	private SimpMessageSendingOperations simpMessageSendingOperations;
	 
    @MessageMapping("/hello")
    @SendTo("/topic/say")
    public TextMessage chat(String message) throws Exception {
        long time = System.currentTimeMillis();
        LOG.info(time+":"+message);
        return new TextMessage(time,message);
    }
    
    /**
     * 测试对指定用户发送消息方法
     * @return
     */
 /*   @RequestMapping(path = "/send", method = RequestMethod.GET)
    public Greeting send() {
        simpMessageSendingOperations.convertAndSendToUser("1", "/message", new Greeting("I am a msg from SubscribeMapping('/macro')."));
        return new Greeting("I am a msg from SubscribeMapping('/macro').");
    }*/
    
    /*@MessageMapping("/chat/{topic}")
    @SendTo("/topic/messages")
    public OutputMessage send(
            @DestinationVariable("topic") String topic, Message message)
            throws Exception {
        return new OutputMessage(message.getFrom(), message.getText(), topic);
    }*/
}
