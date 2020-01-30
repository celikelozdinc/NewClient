package tr.edu.itu.bbf.cloudcore.distributed.service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.kryo.UUIDSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tr.edu.itu.bbf.cloudcore.distributed.entity.Events;
import tr.edu.itu.bbf.cloudcore.distributed.entity.States;
import tr.edu.itu.bbf.cloudcore.distributed.ipc.CkptMessage;
import tr.edu.itu.bbf.cloudcore.distributed.ipc.Response;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

@Service
public class StateMachineWorker {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${CKPT_EXCHANGE_SMOC1}")
    private String CKPT_EXCHANGE_SMOC1;

    @Value("${CKPT_EXCHANGE_SMOC2}")
    private String CKPT_EXCHANGE_SMOC2;

    @Value("${CKPT_EXCHANGE_SMOC3}")
    private String CKPT_EXCHANGE_SMOC3;
/*
    @Value("${CKPT_EXCHANGE_SMOC4}")
    private String CKPT_EXCHANGE_SMOC4;

    @Value("${CKPT_EXCHANGE_SMOC5}")
    private String CKPT_EXCHANGE_SMOC5;

    @Value("${CKPT_EXCHANGE_SMOC6}")
    private String CKPT_EXCHANGE_SMOC6;

    @Value("${CKPT_EXCHANGE_SMOC7}")
    private String CKPT_EXCHANGE_SMOC7;

    @Value("${CKPT_EXCHANGE_SMOC8}")
    private String CKPT_EXCHANGE_SMOC8;

    @Value("${CKPT_EXCHANGE_SMOC9}")
    private String CKPT_EXCHANGE_SMOC9;

    @Value("${CKPT_EXCHANGE_SMOC10}")
    private String CKPT_EXCHANGE_SMOC10;

    @Value("${CKPT_EXCHANGE_SMOC11}")
    private String CKPT_EXCHANGE_SMOC11;

    @Value("${CKPT_EXCHANGE_SMOC12}")
    private String CKPT_EXCHANGE_SMOC12;

    @Value("${CKPT_EXCHANGE_SMOC13}")
    private String CKPT_EXCHANGE_SMOC13;

    @Value("${CKPT_EXCHANGE_SMOC14}")
    private String CKPT_EXCHANGE_SMOC14;

    @Value("${CKPT_EXCHANGE_SMOC15}")
    private String CKPT_EXCHANGE_SMOC15;*/

    private ArrayList<Response> mixedCkpts;
    private ArrayList<Response> sequentialCktps;

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {
        @NotNull
        @SuppressWarnings("rawtypes")
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer());
            kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
            kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
            return kryo;
        }
    };


    static final Logger logger = LoggerFactory.getLogger(StateMachineWorker.class);

    public StateMachineWorker(){
        logger.info("+++++StateMachineWorker::Constructor+++++");
    }

    @PostConstruct
    public void init() {
        logger.info("+++++StateMachineWorker::PostConstruct+++++");
        stateMachine.start();
        logger.info("SMOC __{}__ is started. From now on, events can be processed.",stateMachine.getUuid().toString());
        mixedCkpts = new ArrayList<Response>();
        sequentialCktps = new ArrayList<Response>();
    }

    public void startCommunication() throws UnknownHostException {
        logger.info("********* StateMachineWorker::startCommunication()");
        String ipAddr = InetAddress.getLocalHost().getHostAddress();
        String hostname = System.getenv("HOSTNAME");
        logger.info("********* Ip Addr of sender = {}",ipAddr);
        logger.info("********* Hostname of sender  = {}",hostname);
        CkptMessage msg = new CkptMessage();
        msg.setHostname(hostname);
        msg.setIpAddr(ipAddr);

        ArrayList<Response> smoc1CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC1,"rpc",msg);
        logger.info("Count of ckpts stored by smoc1 --> {}",smoc1CkptList.size());
        mixedCkpts.addAll(smoc1CkptList);

        ArrayList<Response> smoc2CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC2,"rpc",msg);
        logger.info("Count of ckpts stored by smoc2 --> {}",smoc2CkptList.size());
        mixedCkpts.addAll(smoc2CkptList);

        ArrayList<Response> smoc3CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC3,"rpc",msg);
        logger.info("Count of ckpts stored by smoc3 --> {}",smoc3CkptList.size());
        mixedCkpts.addAll(smoc3CkptList);
/*

        ArrayList<Response> smoc4CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC4,"rpc",msg);
        logger.info("Count of ckpts stored by smoc4 --> {}",smoc4CkptList.size());
        mixedCkpts.addAll(smoc4CkptList);

        ArrayList<Response> smoc5CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC5,"rpc",msg);
        logger.info("Count of ckpts stored by smoc5 --> {}",smoc5CkptList.size());
        mixedCkpts.addAll(smoc5CkptList);

        ArrayList<Response> smoc6CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC6,"rpc",msg);
        logger.info("Count of ckpts stored by smoc6 --> {}",smoc6CkptList.size());
        mixedCkpts.addAll(smoc6CkptList);

        ArrayList<Response> smoc7CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC7,"rpc",msg);
        logger.info("Count of ckpts stored by smoc7 --> {}",smoc7CkptList.size());
        mixedCkpts.addAll(smoc7CkptList);

        ArrayList<Response> smoc8CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC8,"rpc",msg);
        logger.info("Count of ckpts stored by smoc8 --> {}",smoc8CkptList.size());
        mixedCkpts.addAll(smoc8CkptList);

        ArrayList<Response> smoc9CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC9,"rpc",msg);
        logger.info("Count of ckpts stored by smoc9 --> {}",smoc9CkptList.size());
        mixedCkpts.addAll(smoc9CkptList);

        ArrayList<Response> smoc10CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC10,"rpc",msg);
        logger.info("Count of ckpts stored by smoc10 --> {}",smoc10CkptList.size());
        mixedCkpts.addAll(smoc10CkptList);

        ArrayList<Response> smoc11CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC11,"rpc",msg);
        logger.info("Count of ckpts stored by smoc11 --> {}",smoc11CkptList.size());
        mixedCkpts.addAll(smoc11CkptList);

        ArrayList<Response> smoc12CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC12,"rpc",msg);
        logger.info("Count of ckpts stored by smoc12 --> {}",smoc12CkptList.size());
        mixedCkpts.addAll(smoc12CkptList);

        ArrayList<Response> smoc13CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC13,"rpc",msg);
        logger.info("Count of ckpts stored by smoc13 --> {}",smoc13CkptList.size());
        mixedCkpts.addAll(smoc13CkptList);

        ArrayList<Response> smoc14CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC14,"rpc",msg);
        logger.info("Count of ckpts stored by smoc14 --> {}",smoc14CkptList.size());
        mixedCkpts.addAll(smoc14CkptList);

        ArrayList<Response> smoc15CkptList = (ArrayList<Response>) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC15,"rpc",msg);
        logger.info("Count of ckpts stored by smoc15 --> {}",smoc15CkptList.size());
        mixedCkpts.addAll(smoc15CkptList);
*/

        logger.info("Count of ckpts stored by all smocs --> {}",mixedCkpts.size());
    }

    public void prepareCkpts(){

        Integer size = mixedCkpts.size();

        for(int event=1 ; event<=size; event++){
            logger.info("Searching for event {} is started ...",event);
            for(Response response: mixedCkpts){
                if(response.getEventNumber() == event){
                    logger.info("Eventnumber {} is found",event);
                    sequentialCktps.add(response);
                }
            }
            logger.info("Searching for event {} is finished ...",event);
        }
        logger.info("Size of ordered ckpts -> {}",sequentialCktps.size());

        for(Response response:sequentialCktps){
            logger.warn("{}.event: {} --> {} --> {}",response.getEventNumber(),response.getSourceState(),response.getProcessedEvent(),response.getDestinationState());
        }

    }

    public void applyCkpts() throws Exception {
        for(Response response: sequentialCktps){
            String event = response.getProcessedEvent();
            switch(event){
                case "Pay": case "pay": case "PAY":
                    System.out.print("\n\n\n\n\n");
                    sendPayEvent(event,0);
                    System.out.print("\n\n\n\n\n");
                    break;
                case "Receive": case "receive": case "RECEIVE":
                    System.out.print("\n\n\n\n\n");
                    sendReceiveEvent(event,0);
                    System.out.print("\n\n\n\n\n");
                    break;
                case "StartFromScratch": case "startfromscratch": case"STARTFROMSCRATCH":
                    System.out.print("\n\n\n\n\n");
                    sendStartFromScratchEvent(event,0);
                    System.out.print("\n\n\n\n\n");
                    break;
                default:
                    System.out.println("Please send one of the events below.");
                    System.out.println("Pay/Receive/StartFromScratch");
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public StateMachineContext<States, Events> deserializeStateMachineContext(String reply) {
        if (StringUtils.isEmpty(reply)) {
            logger.info("_____ REPLY is NULL _____");
            return null;
        }
        logger.info("_____ REPLY is not NULL _____");
        Kryo kryo = kryoThreadLocal.get();
        //Base64.Decoder decoder = Base64.getMimeDecoder();
        Base64.Decoder decoder = Base64.getDecoder();
        //Base64.Decoder decoder = Base64.getUrlDecoder();
        ByteArrayInputStream in = new ByteArrayInputStream(decoder.decode(reply));
        //ByteArrayInputStream in = new ByteArrayInputStream(decoder.decode(reply.replace("\n","").replace("\r","")));
        //ByteArrayInputStream in = new ByteArrayInputStream(decoder.decode(reply));
        logger.info("ByteArrayInputStream = {} ",in);
        Input input = new Input(in);
        logger.info("Input = {}",input);
        return kryo.readObject(input, StateMachineContext.class);

        /*
        ByteArrayInputStream in = new ByteArrayInputStream(reply);
        logger.info("ByteArrayInputStream = {} ",in);
        Input input = new Input(in);
        logger.info("Input = {}",input);
        Object o = kryo.readObject(input,StateMachineContext.class);
        logger.info("Object is = {}",o);
        return kryo.readObject(input, StateMachineContext.class);

         */
    }

    public void sendPayEvent(@NotNull String event, int timeSleep)throws Exception {
        Message<Events> messagePay = MessageBuilder
                .withPayload(Events.PAY)
                .setHeader("timeSleep", timeSleep)
                .setHeader("machineId", stateMachine.getUuid())
                .setHeader("source", "UNPAID")
                .setHeader("processedEvent", event)
                .setHeader("target", "WAITING_FOR_RECEIVE")
                .build();
        stateMachine.sendEvent(messagePay);
    }

    public void sendReceiveEvent(@NotNull String event, int timeSleep) throws Exception {
        Message<Events> messageReceive = MessageBuilder
                .withPayload(Events.RECEIVE)
                .setHeader("timeSleep", timeSleep)
                .setHeader("machineId", stateMachine.getUuid())
                .setHeader("source", "WAITING_FOR_RECEIVE")
                .setHeader("processedEvent", event)
                .setHeader("target", "DONE")
                .build();
        stateMachine.sendEvent(messageReceive);
    }

    public void sendStartFromScratchEvent(@NotNull String event, int timeSleep) throws Exception {
        Message<Events> messageStartFromScratch = MessageBuilder
                .withPayload(Events.STARTFROMSCRATCH)
                .setHeader("timeSleep", timeSleep)
                .setHeader("machineId", stateMachine.getUuid())
                .setHeader("source", "DONE")
                .setHeader("processedEvent", event)
                .setHeader("target", "UNPAID")
                .build();
        stateMachine.sendEvent(messageStartFromScratch);
    }

}
