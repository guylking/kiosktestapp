package com.fne.kiosk.kiosk.model.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkflowEngine {

    static Logger log = LogManager.getLogger( WorkflowEngine.class );

//    @Autowired
//    private JpaEmailRepository emailRepository;

    public WorkflowEngine() {

    }

    protected boolean performUpdateBasedOnCodeAntType( ) {
        return true;
    }

    protected boolean perfromNewInsert( ) {

        return true;
    }


}
