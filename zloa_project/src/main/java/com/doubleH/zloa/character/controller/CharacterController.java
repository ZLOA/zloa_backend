package com.doubleH.zloa.character.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.doubleH.zloa.character.model.vo.Character;

@Controller
public class CharacterController {

	
	@PostMapping("/processData")
	public ResponseEntity<String> processData(@RequestBody Character requestData) {
        // YourDataClass: 클라이언트가 전송하는 JSON 데이터에 대한 모델 클래스

        // 데이터 처리 로직 수행
        String processedData = processDataLogic(requestData);

        // 처리된 데이터를 JSON 형태로 클라이언트에게 반환
        return ResponseEntity.ok(processedData);
    }

    private String processDataLogic(Character requestData) {
        // 데이터 처리 로직 구현
        // 예시: 간단히 받은 문자열을 대문자로 변환
        return requestData.getData().toUpperCase();
    }
}
