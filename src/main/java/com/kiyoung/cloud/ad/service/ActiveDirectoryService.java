package com.kiyoung.cloud.ad.service;

import com.kiyoung.cloud.common.dto.BCBResponse;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("ActiveDirectorySvc")
public class ActiveDirectoryService {

    private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

    /**
     * 원래는 리턴이 BcbResponse인데 이걸 쓰면 타입캐스팅 에러가 나서, 예제가 돌게끔 이 예제를 사용해봄.
     * @param exchange
     * @return
     * @throws Exception
     */
    public DCloudResultSet addAdUserRest(Exchange exchange) throws Exception {
        DCloudResultSet resBcb = new DCloudResultSet();
        log.error("addAdUserRest 호출...");
        System.out.println("sysout addAdUserRest...");
        return resBcb;
    }
}
