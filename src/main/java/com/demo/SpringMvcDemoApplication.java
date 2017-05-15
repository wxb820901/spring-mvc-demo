package com.demo;

import com.demo.jpa.entity.JsonObject;
import com.demo.jpa.repository.JsonObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.demo.jpa")
public class SpringMvcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcDemoApplication.class, args);
	}

	/*
	* # generate keystore.p12
	* keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
	* # server side setting
	* server.ssl.key-store:          target/classes/keystore.p12
	* server.ssl.key-store-password: Google@123
	* server.ssl.keyStoreType:       PKCS12
	* server.ssl.keyAlias:           tomcat
	* */


	/*
	* JMS
	* */
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }
    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

//	@Bean
//	public BrokerService getBrokerService() throws Exception {//embed activeMQ broker start!!
//		BrokerService broker = new BrokerService();
//		TransportConnector connector = new TransportConnector();
//		broker.setBrokerName("fred");
//		connector.setUri(new URI("tcp://localhost:61616"));
//		broker.addConnector(connector);
//		broker.setPersistent(false);
//		broker.start();
//		return broker;
//	}
//
//	@Bean
//	public ActiveMQConnectionFactory getActiveMQConnectionFactory(){//connect to MQ braoker
//		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
//		factory.setBrokerURL("tcp://localhost:61616");
//		return factory;
//	}
//
//	@Bean
//	public ActiveMQQueue getActiveMQQueue(){//MQ queue
//		ActiveMQQueue queue = new ActiveMQQueue("mmm");
//		return queue;
//	}
//
//	@Bean
//	public JmsTemplate getJmsTemplate(ConnectionFactory cf){
//		JmsTemplate jmsTemplate = new JmsTemplate(cf);
//		return jmsTemplate;
//	}
//
//	@Bean
//	public DefaultMessageListenerContainer getDefaultMessageListenerContainer(){//listen
//		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//		container.setConnectionFactory(getActiveMQConnectionFactory());
//		container.setDestination(getActiveMQQueue());
//		container.setConcurrentConsumers(3);
//		container.setMessageListener(new DemoMessageListener());
//		return container;
//	}


    private static final Logger log = LoggerFactory.getLogger(SpringMvcDemoApplication.class);

    @Bean
    public CommandLineRunner demo(JsonObjectRepository repository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new JsonObject(1, "Jack", "Bauer"));
            repository.save(new JsonObject(2, "Chloe", "O'Brian"));
            repository.save(new JsonObject(3, "Kim", "Bauer"));
            repository.save(new JsonObject(4, "David", "Palmer"));
            repository.save(new JsonObject(5, "Michelle", "Dessler"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (JsonObject customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            JsonObject customer = repository.findOne(1L);
            log.info("Customer found with findOne(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            for (JsonObject bauer : repository.findByName("Bauer")) {
                log.info(bauer.toString());
            }
            log.info("");
        };
    }
//	@Bean
//	public DataSourceTransactionManager getTxManager(){
//		DataSourceTransactionManager txManager = new DataSourceTransactionManager(getDB());
//		return txManager;
//	}

}
