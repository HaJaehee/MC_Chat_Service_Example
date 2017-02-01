/* -------------------------------------------------------- */
/** 
File name : SC2.java
	Service Consumer of a chatting service.
Author : Jaehee Ha (jaehee.ha@kaist.ac.kr)
Creation Date : 2016-12-31
Version : 0.2.00
Rev. history : 2017-02-01
	Added header field features.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)
*/
/* -------------------------------------------------------- */

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.kaist.mms_client.*;

public class SC2 {
	public static void main(String args[]) throws Exception{
		String myMRN;
		//myMRN = args[0];
		myMRN = "urn:mrn:imo:imo-no:1000007";
		
		MMSConfiguration.MMSURL="127.0.0.1:8088";
		MMSConfiguration.CMURL="127.0.0.1";

		//Service Consumer cannot be HTTP server and should poll from MMS. 
		MMSClientHandler ph = new MMSClientHandler(myMRN);
		int pollInterval = 1000;
		ph.startPolling("urn:mrn:smart-navi:device:mms1",pollInterval);
		//Request Callback from the request message
		ph.setReqCallBack(new MMSClientHandler.ReqCallBack() {
			@Override
			public String callbackMethod(Map<String,List<String>> headerField, String messages) {
				try {
					for (String message : messages.split("\n")){
						JSONParser Jpar = new JSONParser();
						JSONObject Jobj = (JSONObject) Jpar.parse(message);
						String srcMRN = (String) Jobj.get("srcMRN");
						String msg = (String) Jobj.get("msg");
						
						System.out.println("srcMRN: "+srcMRN);
						System.out.println("msg: "+msg);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "OK";
			}
		});
		
		//Service Consumer which can only send message
		MMSClientHandler mh = new MMSClientHandler(myMRN);
		
		String dstMRN = "urn:mrn:imo:imo-no:0100006";
		while (true){
			String msg = new Scanner(System.in).nextLine();
			JSONObject Jobj = new JSONObject();
			Jobj.put("srcMRN", myMRN);
			Jobj.put("dstMRN", dstMRN);
			Jobj.put("msg", msg);
			String a = mh.sendPostMsg("urn:mrn:smart-navi:device:chat-server-kaist", Jobj.toString());
		}
	}
}
