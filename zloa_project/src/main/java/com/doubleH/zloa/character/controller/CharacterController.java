package com.doubleH.zloa.character.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
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
    
    @GetMapping("/characters/profile")
    public ResponseEntity<String> getCharacterProfileAll(@RequestParam("characterName") String characterName) {
    	//System.out.println(characterName);
    	//String i = lostArkClient.getCharacterProfile(characterName, null);

    	getRealPower();
       
    	// 성공!
        return ResponseEntity.ok("ok");
    }
    
    public int getRealPower(){
    	
    	//Dps * 무공 * 카드 * 보석 * 팔찌 * 엘릭서 -> 내 정보 기반
    	// dps는 나중에 db에 넣어서 db에서 가져오도록 하자 ㅇㅇ
    	String characterName = "조아얌";
    	
    	JSONObject characterInfo = new JSONObject(lostArkClient.getCharacterProfile(characterName, null));
    	
    	// 무기 공격력
    	JSONArray statsArray = characterInfo.getJSONObject("ArmoryProfile").getJSONArray("Stats");
    	String attack = "";
    	for (int i = 0; i < statsArray.length(); i++) {
    	    JSONObject statObject = statsArray.getJSONObject(i);
    	    if(statObject.getString("Type").equalsIgnoreCase("공격력")) {
    	    	attack = statObject.getString("Value");
    	    	System.out.println("공격력" + attack);
    	    }
    	}
    	
    	// 직각 뽑아내기
    	JSONArray engravingArray = characterInfo.getJSONObject("ArmoryEngraving").getJSONArray("Effects");
    	String classEngraving = "";
    	
    	for (int i = 0; i < engravingArray.length(); i++) {
    		// 각인 하나씩 보내서 뽑아오기
    		// "false"로 넘어오면 해당각인 x
    		JSONObject effectsObject = engravingArray.getJSONObject(i);
    		String temp = getClassEngravings(effectsObject.getString("Name"));
    		if(!temp.equalsIgnoreCase("false")) {
    			classEngraving = temp;
    		}
    	}
    	
    	// eDps배율 뽑기
    	double eDpsScope = 1.0000;
    	if(!classEngraving.equals("")) {
    		eDpsScope = getClassEDPS(classEngraving);
    		System.out.println(eDpsScope);
    	} else {
    		System.out.println("서포터임");
    	}
    	
    	
    	
    	JSONArray cardArray = characterInfo.getJSONObject("ArmoryCard").getJSONArray("Effects");
    	double cardScope = 1.0000;
    	for(int i = 0; i < cardArray.length(); i++) {
    	
    		JSONObject effectObj = cardArray.getJSONObject(i);
    		JSONArray itemsArray = effectObj.getJSONArray("Items");
    		
    		for(int j = 0; j < itemsArray.length(); j++) {
    			JSONObject itemObj = itemsArray.getJSONObject(j);
    			String itemName = itemObj.getString("Name");
    			
    			System.out.println(itemName);
    			
    			if(j == itemsArray.length()-1) {	
    				if(itemName.contains("세상을 구하는 빛") || itemName.contains("카제로스의 군단장")) {
    					if(itemName.contains("30각")) {
    						System.out.println("스카우터 최고^^");
    					} else if(itemName.contains("18각")) {
    						cardScope = 0.9200;
    					} else {
    						cardScope = 0.8500;
    					}
    				} else if(itemName.contains("알고 보면")) {
    					if(itemName.contains("30각")) {
    						cardScope = 0.9700;
    					} else if(itemName.contains("18각")) {
    						cardScope = 0.9300;
    					} else if(itemName.contains("12각")) {
    						cardScope = 0.8900;
    					} else {
    						cardScope = 0.8500;
    					}
    				} else {
    					cardScope = 0.8500;
    				}
    			}
    		}
    	}
    	
    	JSONArray gemArray = characterInfo.getJSONObject("ArmoryGem").getJSONArray("Gems");
    	// 1레벨 보석의 개수
    	double total = 0;
    	
    	for(int i = 0; i < gemArray.length(); i++) {
    		int gemLevel = gemArray.getJSONObject(i).getInt("Level");
    		
    		double temp = Math.pow(3, gemLevel-1);
    	    total += temp; 
    	
    	}
    	System.out.println(total/gemArray.length());
    	
    	total = (Math.round(((Math.log(total/gemArray.length()) / Math.log(3)) +1 ) * 100)) / 100;

    	System.out.println(total);
    	
    	
    	// 이제 다 뽑은 총 곱 연산 해서 리턴
    	
    	
    	
    	return 12;
    }
    
    // 직업각인 선택
    public String getClassEngravings(String engraving) {
        String cleanEngraving = engraving.replaceAll(" Lv\\. \\d+", "");

        switch(cleanEngraving) {
            case "전투 태세" :
                return "전투 태세";
            case "고독한 기사" :
                return "고독한 기사";
            case "분노의 망치" :
                return "분노의 망치";
            case "중력 수련" :
                return "중력 수련";
            case "광전사의 비기" :
                return "광전사의 비기";
            case "광기" :
                return "광기";
            case "충격 단련" :
                return "충격 단련";
            case "극의: 체술" :
                return "극의: 체술";
            case "초심" :
                return "초심";
            case "오의 강화" :
                return "오의 강화";
            case "역천지체" :
                return "역천지체";
            case "세맥타통" :
                return "세맥타통";
            case "절정" :
                return "절정";
            case "절제" :
                return "절제";
            case "강화 무기" :
                return "강화 무기";
            case "핸드 거너" :
                return "핸드 거너";
            case "화력 강화" :
                return "화력 강화";
            case "포격 강화" :
                return "포격 강화";
            case "죽음의 습격" :
                return "죽음의 습격";
            case "두 번째 동료" :
                return "두 번째 동료";
            case "진화의 유산" :
                return "진화의 유산";
            case "아르데타인의 기술" :
                return "아르데타인의 기술";
            case "상급 소환사" :
                return "상급 소환사";
            case "넘치는 교감" :
                return "넘치는 교감";
            case "점화" :
                return "점화";
            case "환류" :
                return "환류";
            case "황후의 은총" :
                return "황후의 은총";
            case "황제의 칙령" :
                return "황제의 칙령";
            case "멈출 수 없는 충동" :
                return "멈출 수 없는 충동";
            case "완벽한 억제" :
                return "완벽한 억제";
            case "갈증" :
                return "갈증";
            case "달의 소리" :
                return "달의 소리";
            case "잔재된 기운" :
                return "잔재된 기운";
            case "버스트" :
                return "버스트";
            case "만월의 집행자" :
                return "만월의 집행자";
            case "그믐의 경계" :
                return "그믐의 경계";
            case "질풍노도" :
                return "질풍노도";
            case "이슬비" :
                return "이슬비";
            case "권왕파천무" :
                return "권왕파천무";
            case "수라의 길" :
                return "수라의 길";
            case "오의난무" :
                return "오의난무";
            case "일격필살" :
                return "일격필살";
            case "처단자" :
                return "처단자";
            case "포식자" :
                return "포식자";
            case "피스메이커" :
                return "피스메이커";
            case "사냥의 시간" :
                return "사냥의 시간";
            default :
                return "false";
        }
    }
    
    // 직업의 eDps배율
    public double getClassEDPS(String className) {
    	
    	double scope = 1.0000;

    	// 점화소서 255.46
        switch(className) {
            case "전투 태세" :
            	scope = 1.0000;
                return scope;
            case "고독한 기사" :
            	scope = 1.5669;
                return scope;
            case "분노의 망치" :
            	scope = 1.5021;
                return scope;
            // 데이터 없음
            case "중력 수련" :
            	scope = 1.3000;
                return scope;
             // 데이터 없음
            case "광전사의 비기" :
            	scope = 1.3000;
                return scope;
            case "광기" :
            	scope = 1.7270;
                return scope;
            case "충격 단련" :
            	scope = 1.4294;
                return scope;
            case "극의: 체술" :
            	scope = 1.1165;
                return scope;
            case "초심" :
            	scope = 1.4453;
                return scope;
            // 데이터 없음
            case "오의 강화" :
            	scope = 1.3000;
                return scope;
            // 데이터 없음
            case "역천지체" :
            	scope = 1.3000;
                return scope;
             // 데이터 없음
            case "세맥타통" :
            	scope = 1.3000;
                return scope;
            case "절정" :
            	scope = 1.9321;
                return scope;
            case "절제" :
            	scope = 1.3780;
                return scope;
            case "강화 무기" :
            	scope = 2.2549;
                return scope;
            case "핸드 거너" :
            	scope = 1.3304;
                return scope;
            case "화력 강화" :
            	scope = 1.4369;
                return scope;
            case "포격 강화" :
            	scope = 1.2810;
                return scope;
            // 데이터 없음
            case "죽음의 습격" :
            	scope = 1.3000;
                return scope;
            // 데이터 없음
            case "두 번째 동료" :
            	scope = 1.3000;
                return scope;
            case "진화의 유산" :
            	scope = 1.0818;
                return scope;
            case "아르데타인의 기술" :
            	scope = 1.7154;
                return scope;
            case "상급 소환사" :
            	scope = 2.0867;
                return scope;
            case "넘치는 교감" :
            	scope = 1.1175;
                return scope;
            case "점화" :
            	scope = 2.5546;
                return scope;
            // 데이터 없음
            case "환류" :
            	scope = 1.3000;
                return scope;
            // 데이터 없음
            case "황후의 은총" :
            	scope = 1.3000;
                return scope;
            // 데이터 없음
            case "황제의 칙령" :
            	scope = 1.3000;
                return scope;
            case "멈출 수 없는 충동" :
            	scope = 1.0606;
                return scope;
            case "완벽한 억제" :
            	scope = 1.3538;
                return scope;
            case "갈증" :
            	scope = 1.3177;
                return scope;
            case "달의 소리" :
            	scope = 1.5997;
                return scope;
            case "잔재된 기운" :
            	scope = 1.3633;
                return scope;
            case "버스트" :
            	scope = 1.5348;
                return scope;
            // 데이터 없음
            case "만월의 집행자" :
            	scope = 2.8000;
                return scope;
            // 데이터 없음
            case "그믐의 경계" :
            	scope = 2.3000;
                return scope;
            // 데이터 없음
            case "질풍노도" :
            	scope = 1.3000;
                return scope;
            // 데이터 없음
            case "이슬비" :
            	scope = 1.3000;
                return scope;
            // 데이터 없음
            case "권왕파천무" :
            	scope = 3.0000;
                return scope;
            // 데이터 없음
            case "수라의 길" :
            	scope = 3.5000;
                return scope;
            case "오의난무" :
            	scope = 1.8157;
                return scope;
            case "일격필살" :
            	scope = 1.3077;
                return scope;
            case "처단자" :
            	scope = 2.5385;
                return scope;
            case "포식자" :
            	scope = 1.5512;
                return scope;
            case "피스메이커" :
            	scope = 1.8461;
                return scope;
            case "사냥의 시간" :
            	scope = 1.6002;
                return scope;
            default :
                return scope;
        }
    }
    
    
    
    
    // https://developer-lostark.game.onstove.com/characters/{characterName}/siblings
}