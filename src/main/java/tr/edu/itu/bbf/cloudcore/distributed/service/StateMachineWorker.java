package tr.edu.itu.bbf.cloudcore.distributed.service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.kryo.UUIDSerializer;
import org.springframework.stereotype.Service;
import tr.edu.itu.bbf.cloudcore.distributed.entity.Events;
import tr.edu.itu.bbf.cloudcore.distributed.entity.States;
import tr.edu.itu.bbf.cloudcore.distributed.ipc.CkptMessage;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    }

    public String startCommunication() throws UnknownHostException {
        logger.info("********* StateMachineWorker::startCommunication()");
        String ipAddr = InetAddress.getLocalHost().getHostAddress();
        String hostname = System.getenv("HOSTNAME");
        logger.info("********* Ip Addr of sender = {}",ipAddr);
        logger.info("********* Hostname of sender  = {}",hostname);
        CkptMessage msg = new CkptMessage();
        msg.setHostname(hostname);
        msg.setIpAddr(ipAddr);
        String reply = (String) rabbitTemplate.convertSendAndReceive(CKPT_EXCHANGE_SMOC1,"rpc",msg);
        logger.info("********* Response from receiver = {}",reply);
        return reply;
    }

    @SuppressWarnings("unchecked")
    public StateMachineContext<States, Events> deserializeStateMachineContext(String reply) throws UnsupportedEncodingException {
        if (reply == null || reply.length() == 0) {
            logger.info("_____ REPLY is NULL _____");
            return null;
        }
        logger.info("_____ REPLY is not NULL _____");
        Kryo kryo = kryoThreadLocal.get();
        /*
        Base64.Decoder decoder = Base64.getDecoder();
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(reply));

         */
        ByteArrayInputStream in = new ByteArrayInputStream(reply.getBytes());
        logger.info("ByteArrayInputStream = {} ",in);
        Input input = new Input(in);
        logger.info("Input = {}",input);
        Object o = kryo.readObject(input,StateMachineContext.class);
        logger.info("Object is = {}",o);
        return kryo.readObject(input, StateMachineContext.class);
    }
}
