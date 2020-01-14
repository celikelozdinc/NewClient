package tr.edu.itu.bbf.cloudcore.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tr.edu.itu.bbf.cloudcore.distributed.ipc.Response;
import tr.edu.itu.bbf.cloudcore.distributed.service.StateMachineWorker;


import java.net.UnknownHostException;

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
            worker.startCommunication();
            //StateMachineContext<States,Events> context = worker.deserializeStateMachineContext(reply);
            //StateMachineContext<States,Events> context = worker.deserializeStateMachineContext(reply.getBytes());
            //logger.info("********* Deserialize context = {}",context.getState().toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
