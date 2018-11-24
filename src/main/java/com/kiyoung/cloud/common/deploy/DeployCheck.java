package com.kiyoung.cloud.common.deploy;
import java.util.Iterator;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * 소스 정상 Deploy 여부를 체크하기 위한 Class
 *
 * @author heokjin
 * @since 2012.10.16
 * @version 1.0
 */
public class DeployCheck implements Processor {

    public void process(Exchange exchange) throws Exception {
//	System.out.println("**************************************");
//	System.getProperties().list(System.out);
//	System.out.println("*************************************");

//	Map<String, Endpoint> endPointMap = exchange.getUnitOfWork().getRouteContext().getCamelContext().getEndpointMap();
//	Iterator<String> keySet = endPointMap.keySet().iterator();
//	StringBuffer sb = new StringBuffer();
//	while (keySet.hasNext()) {
//	    sb.append(endPointMap.get(keySet.next()) + "</br>");
//	}

        // send a html response
//	exchange.getOut().setBody("<html><body>OK!! </br>" + sb.toString() + "</body></html>");
        // Thread.sleep(5000);
        exchange.getOut().setBody("<html><body>OK!!</body></html>");
    }
}