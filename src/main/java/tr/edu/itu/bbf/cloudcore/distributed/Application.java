package tr.edu.itu.bbf.cloudcore.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tr.edu.itu.bbf.cloudcore.distributed.persist.CheckpointRepository;
import tr.edu.itu.bbf.cloudcore.distributed.service.StateMachineWorker;


import java.net.UnknownHostException;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses= CheckpointRepository.class)
@ComponentScan(basePackages = {"tr.edu.itu.bbf.cloudcore.distributed"})
@PropertySource(value={"classpath:application.properties"})
public class Application implements CommandLineRunner {

    @Autowired
    public StateMachineWorker worker;

    static final Logger logger = LoggerFactory.getLogger(Application.class);


    @Override
    public void run(String... args){

        /* Read CKPT information from other smocs */

        long startTime = System.currentTimeMillis();
        try {
            worker.startCommunication();
            worker.prepareCkpts();
            worker.applyCkpts();
            //StateMachineContext<States,Events> context = worker.deserializeStateMachineContext(reply);
            //StateMachineContext<States,Events> context = worker.deserializeStateMachineContext(reply.getBytes());
            //logger.info("********* Deserialize context = {}",context.getState().toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        logger.warn("Applying all CKPTs took " + (endTime - startTime) + " milliseconds");

    }



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
