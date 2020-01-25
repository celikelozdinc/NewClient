package tr.edu.itu.bbf.cloudcore.distributed.persist;

/** DTO = Data Transfer Object */

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection="Checkpoints")
public class CheckpointDbObject {

    @Id
    public String timestamp;
    @Field("context")
    private String context;
    @Field("uuid")
    private UUID uuid;
    @Field("event")
    private String processedEvent;
    @Field("source")
    private String sourceState;
    @Field("target")
    private String targetState;
    @Field("number")
    private Integer eventNumber;


    @PersistenceConstructor
    public CheckpointDbObject(String timestamp, UUID uuid, String sourceState, String processedEvent, String targetState, String context,Integer eventNumber) {
        this.timestamp = timestamp;
        this.uuid = uuid;
        this.sourceState = sourceState;
        this.processedEvent=processedEvent;
        this.targetState = targetState;
        this.context = context;
        this.eventNumber = eventNumber;
    }

    @PersistenceConstructor
    public CheckpointDbObject(String context){
        this.context = context;
    }

    public String getProcessedEvent(){return this.processedEvent;}
    public UUID getUuid(){return this.uuid;}
    public String getSourceState(){return this.sourceState;}
    public String getTargetState(){return this.targetState;}
    public String getContext(){return this.context;}
    public Integer getEventNumber(){return this.eventNumber;}



}