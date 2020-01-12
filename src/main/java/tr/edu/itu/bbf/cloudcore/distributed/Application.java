package tr.edu.itu.bbf.cloudcore.distributed;

import com.esotericsoftware.kryo.Kryo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.kryo.UUIDSerializer;
import tr.edu.itu.bbf.cloudcore.distributed.entity.Events;
import tr.edu.itu.bbf.cloudcore.distributed.entity.States;
import tr.edu.itu.bbf.cloudcore.distributed.service.StateMachineWorker;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.UUID;
import com.esotericsoftware.kryo.io.Input;

@SpringBootApplication
@ComponentScan(basePackages = {"tr.edu.itu.bbf.cloudcore.distributed"})
public class Application implements CommandLineRunner {

    @Autowired
    public StateMachineWorker worker;

    static final Logger logger = LoggerFactory.getLogger(Application.class);


    @Override
    public void run(String... args){

        /* Read CKPT information from other smocs */

        try {
            String reply = worker.startCommunication();
            logger.info("********* Response from receiver = {}",reply);
            StateMachineContext<States,Events> context = worker.deserializeStateMachineContext(reply);
            logger.info("********* Deserialize context = {}",context.getState().toString());
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
