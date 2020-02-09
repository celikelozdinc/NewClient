package tr.edu.itu.bbf.cloudcore.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tr.edu.itu.bbf.cloudcore.distributed.persist.CheckpointRepository;
import tr.edu.itu.bbf.cloudcore.distributed.service.StateMachineWorker;


import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.Scanner;

@SpringBootApplication
@ImportResource({"classpath*:channel-config.xml"})
@PropertySource(value={"classpath:application.properties"})
@ComponentScan(basePackages = {"tr.edu.itu.bbf.cloudcore.distributed"})
@EnableMongoRepositories(basePackageClasses= CheckpointRepository.class)
public class Application implements CommandLineRunner {

    @Autowired
    public StateMachineWorker worker;

    static final Logger logger = LoggerFactory.getLogger(Application.class);


    @Override
    public void run(String... args){

        String hostname = System.getenv("HOSTNAME");
        if (hostname.equals("smoc4")){
            logger.info("Will read CKPTs from other smocs...");
            /* Read CKPT information from other smocs */
            long startTime = System.currentTimeMillis();
            try {
                worker.startCommunication();
                worker.prepareCkpts();
                worker.applyCkpts();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            float delta =((float) (endTime - startTime)/1000);
            logger.warn("Applied all CKPTs in {} seconds",delta);
            logger.warn("PID@HOSTNAME is {}",ManagementFactory.getRuntimeMXBean().getName());
        }
        else {
            InputStream stream = System.in;
            Scanner scanner = new Scanner(stream);

            while (true) {
                System.out.println("Waiting events to be processed...");
                String event = scanner.next();
            }
        }



    }



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
