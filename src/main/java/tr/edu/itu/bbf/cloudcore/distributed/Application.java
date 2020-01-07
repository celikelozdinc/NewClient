package tr.edu.itu.bbf.cloudcore.distributed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tr.edu.itu.bbf.cloudcore.distributed.service.StateMachineWorker;

@SpringBootApplication
@ComponentScan(basePackages = {"tr.edu.itu.bbf.cloudcore.distributed"})
public class Application implements CommandLineRunner {

    @Autowired
    public StateMachineWorker worker;

    @Override
    public void run(String... args){


    }



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
