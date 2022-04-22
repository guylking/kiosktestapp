drop table if exists KIOSK_WORKFLOWROUTES;

create table KIOSK_WORKFLOWROUTES
(
    workflowname varchar(50) primary key,
    workflowroute varchar(max)    
);


insert into KIOSK_WORKFLOWROUTES ( workflowname, workflowroute)
   values ('Credit Card Reader Route',
		'<routes xmlns="http://camel.apache.org/schema/spring"><route id="IDTechCardReader" trace="false"><from uri="jms:queue:dd3b2de0-d774-42fa-881d-f2e4f1eb9492"/><convertBodyTo type="java.lang.String" charset="utf-8"/><to uri="bean:cardMessage"/></route></routes>');
           
commit;