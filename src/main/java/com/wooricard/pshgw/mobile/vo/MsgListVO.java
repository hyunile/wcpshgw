package com.wooricard.pshgw.mobile.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooricard.pshgw.mobile.dto.MobileDTO;
import com.wooricard.pshgw.mobile.dto.MsgDTO;
import com.wooricard.pshgw.utils.Utils;

public class MsgListVO extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(MsgListVO.class);
	private static ObjectMapper mapper = new ObjectMapper();

	public MsgListVO(MobileDTO dto) {
		List<MsgDTO> list = dto.getMsgList();

		//put("TOTAL", String.valueOf(dto.getTotal())); 
		put("COUNT", String.valueOf(list != null ? list.size() : 0));
		
		if(list != null && ! list.isEmpty()) {
			List<HashMap<String, Object>> msgs = new ArrayList<HashMap<String, Object>>();
			
			for(MsgDTO msg : list) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("MSGID", String.valueOf(msg.getMsgId()));
				map.put("TITLE", msg.getTitle());
				map.put("TEXT", msg.getText());
				String payload = msg.getPayload();
				if(! Utils.isEmpty(payload)) {
					if(payload.startsWith("{")) {
						Map<String, Object> subMap = null;
						try {
							subMap = mapper.readValue(payload, new TypeReference<Map<String, Object>>(){});
						} 
						catch (Exception e) {
							subMap = null;
							logger.error(String.format("mapper.readValue() failed. e = %s, m = %m" + e.getClass().getName(), e.getMessage()));
						} 
						
						map.put("PAYLOAD", subMap != null ? subMap : "");
					}
					else {
						map.put("PAYLOAD", payload);
					}
				}
				else {
					map.put("PAYLOAD", "");
				}
				
				map.put("DATE", msg.getRegDate());
				
				msgs.add(map);
			}
			
			put("MSGS", msgs);
		}		
	}
}
