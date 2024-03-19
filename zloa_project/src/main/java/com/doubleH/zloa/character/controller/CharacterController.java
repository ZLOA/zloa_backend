package com.doubleH.zloa.character.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doubleH.zloa.api.model.service.LostArkApiClient;
import com.doubleH.zloa.character.model.vo.Character;

@RestController
@RequestMapping("/character")
public class CharacterController {
	
	@Autowired
	private final LostArkApiClient lostArkClient;
	
	@Autowired
    public CharacterController(LostArkApiClient lostArkClient) {
		this.lostArkClient = lostArkClient;
    }
	
    @GetMapping("/api/data")
    public ResponseEntity<String> getData(@RequestParam("id") String id) {
        // url을 고정하는 대신 RequestParam으로 파라미터로써 매개변수를 받음
        // ex) location.href="/api/data?id='끄구마'"

        String responseData = id + " 초월했나요?";
        System.out.println(id);

        // 반환할 데이터는 ResponseEntity가 제공하는 ok메소드의 인자에 담으면 되고
        // 이 때, 인자에 담는 데이터의 형태에 따라 제네릭 바꿔주기^^
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/api/data")
    public ResponseEntity<String> getData1(@RequestBody Character ch) {
        // url을 고정하는 대신 RequestParam으로 파라미터로써 매개변수를 받음
        // ex) location.href="/api/data?id='끄구마'"

        String responseData = ch.getData() + " Post?";
        String responseData1 = ch.getData1() + " Post?";
        System.out.println(responseData);
        System.out.println(responseData1);

        // 반환할 데이터는 ResponseEntity가 제공하는 ok메소드의 인자에 담으면 되고
        // 이 때, 인자에 담는 데이터의 형태에 따라 제네릭 바꿔주기^^
        return ResponseEntity.ok(responseData);
    }
    
    @GetMapping("/characters/siblings")
    public ResponseEntity<String> getCharacterSiblings(@RequestParam("characterName") String characterName) {

    	String i = lostArkClient.getCharacterSiblings(characterName);
    	System.out.println(i);
    	
    	// 성공!
        return ResponseEntity.ok(i);
    }
    
    
    
    
    // https://developer-lostark.game.onstove.com/characters/{characterName}/siblings
}