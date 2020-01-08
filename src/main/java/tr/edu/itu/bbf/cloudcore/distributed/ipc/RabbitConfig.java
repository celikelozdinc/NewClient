package tr.edu.itu.bbf.cloudcore.distributed.ipc;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${CKPT_EXCHANGE_SMOC1}")
    private String CKPT_EXCHANGE_SMOC1;

    @Value("${CKPT_EXCHANGE_SMOC2}")
    private String CKPT_EXCHANGE_SMOC2;

    @Value("${CKPT_EXCHANGE_SMOC3}")
    private String CKPT_EXCHANGE_SMOC3;

    @Bean
    DirectExchange smoc1_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC1); }

    @Bean
    DirectExchange smoc2_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC1); }

    @Bean
    DirectExchange smoc3_Ckpt_Exchange() { return new DirectExchange(CKPT_EXCHANGE_SMOC1); }




}
