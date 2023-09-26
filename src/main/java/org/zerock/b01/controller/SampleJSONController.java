package org.zerock.b01.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SampleJSONController {

    // 브라우저에서 '/helloArr'경로를 호출하면 배열이 그대로 출력.
    // 중요한 점은 서버에서 해당 데이터가 'application/json'이라는 것을 전송했다는 점.
    @GetMapping("/helloArr")
    public String[] helloArr() {
        log.info("helloArr..............");

        return new String[]{"AAA", "BBB", "CCC"};
    }
}
