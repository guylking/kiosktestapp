package com.fne.kiosk.kiosk.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class WorkflowObjectMapper extends ObjectMapper {

    public WorkflowObjectMapper() {
        this.registerModule( new JavaTimeModule());
    }

    public WorkflowObjectMapper(JsonFactory jf ) {
        super(jf);
        this.registerModule(new JavaTimeModule());
    }

    public WorkflowObjectMapper( ObjectMapper src ) {
        super(src);
        this.registerModule( new JavaTimeModule() );
    }

    public WorkflowObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext ctx ) {
        super(jf, sp, ctx);
        this.registerModule( new JavaTimeModule());
    }
}
