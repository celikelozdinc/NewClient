package tr.edu.itu.bbf.cloudcore.distributed.ipc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    /*
    @Value("${CKPT_EXCHANGE_SMOC1}")
    private String CKPT_EXCHANGE_SMOC1;

    @Value("${CKPT_EXCHANGE_SMOC2}")
    private String CKPT_EXCHANGE_SMOC2;

    @Value("${CKPT_EXCHANGE_SMOC3}")
    private String CKPT_EXCHANGE_SMOC3;

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
    private String CKPT_EXCHANGE_SMOC15;
    */

    /*
    @Value("${EVENT_QUEUE}")
    private String EVENT_QUEUE;

    @Value("${EVENT_EXCHANGE}")
    private String EVENT_EXCHANGE;

    @Value("${QUEUE}")
    private String IPC_QUEUE;

    @Value("${EXCHANGE}")
    private String IPC_EXCHANGE;

     */


    @Value(("${EVENT_EXCHANGE_NEW}"))
    private String EVENT_EXCHANGE_NEW;

    @Value(("${EVENT_QUEUE_NEW}"))
    private String EVENT_QUEUE_NEW;

    @Bean
    Queue eventQueue_New(){ return new Queue(EVENT_QUEUE_NEW, false);}

    @Bean
    DirectExchange eventExchange_New(){return new DirectExchange(EVENT_EXCHANGE_NEW);}

    @Bean
    Binding bindingForEvent_New(Queue eventQueue_New, DirectExchange eventExchange_New){
        return BindingBuilder.bind(eventQueue_New).to(eventExchange_New).with("rpc");
    }

    /*
    @Bean
    Queue eventQueue(){ return new Queue(EVENT_QUEUE, false);}

    @Bean
    DirectExchange eventExchange(){return new DirectExchange(EVENT_EXCHANGE);}

    @Bean
    Binding bindingForEvent(Queue eventQueue, DirectExchange eventExchange){
        return BindingBuilder.bind(eventQueue).to(eventExchange).with("rpc");
    }

    @Bean
    Queue ipcQueue() {
        return new Queue(IPC_QUEUE, false);
    }

    @Bean
    DirectExchange ipcExchange() {
        return new DirectExchange(IPC_EXCHANGE);
    }

    @Bean
    Binding binding(Queue ipcQueue, DirectExchange ipcExchange) {
        return BindingBuilder.bind(ipcQueue).to(ipcExchange).with("rpc");
    }


     */

    /*
    @Bean
    DirectExchange smoc1_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC1); }

    @Bean
    DirectExchange smoc2_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC2); }

    @Bean
    DirectExchange smoc3_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC3); }


    @Bean
    DirectExchange smoc4_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC4); }

    @Bean
    DirectExchange smoc5_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC5); }

    @Bean
    DirectExchange smoc6_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC6); }

    @Bean
    DirectExchange smoc7_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC7); }

    @Bean
    DirectExchange smoc8_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC8); }

    @Bean
    DirectExchange smoc9_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC9); }

    @Bean
    DirectExchange smoc10_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC10); }

    @Bean
    DirectExchange smoc11_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC11); }

    @Bean
    DirectExchange smoc12_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC12); }

    @Bean
    DirectExchange smoc13_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC13); }

    @Bean
    DirectExchange smoc14_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC14); }

    @Bean
    DirectExchange smoc15_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC15); }
    */



}
