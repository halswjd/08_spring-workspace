package com.kh.opendata.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APIController {

	private static final String serviceKey = "CNT5g0YAUeyWWwY5CusH4minY9m99Yl5rjLjVL%2BOGphq26uWuC%2F8Wb5c%2FLwHxX0XeVuI9RUdD%2FuBM1lXDFyM7w%3D%3D"; 
	
	
	/*
	@ResponseBody
	@RequestMapping(value="air.do", produces="application/json; charset=UTF-8")
	public String airPollution(String location) throws IOException {
		
		String url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		url += "?serviceKey=" + serviceKey;
//		url += "&sidoName=" + location; ex)서울 -> 인코딩 필요!
		url += "&sidoName=" + URLEncoder.encode(location, "UTF-8");
		url += "&returnType=json";
		url += "&numOfRows=50";
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		BufferedReader br = new BufferedReader( new InputStreamReader( urlConnection.getInputStream() ));
		
		String responseText = "";
		String line;
		
		while((line=br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
//		System.out.println(responseText);
		
		return responseText;
	}
	*/
	
	
	
	// api가 xml로만 제공될때
	@ResponseBody							// ▼ 응답하는 데이터가 xml이라는걸 명시
	@RequestMapping(value="air.do", produces="text/xml; charset=UTF-8")
	public String airPollution(String location) throws IOException {
		
		String url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		url += "?serviceKey=" + serviceKey;
//		url += "&sidoName=" + location; ex)서울 -> 인코딩 필요!
		url += "&sidoName=" + URLEncoder.encode(location, "UTF-8");
		url += "&returnType=xml";
		url += "&numOfRows=50";
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		BufferedReader br = new BufferedReader( new InputStreamReader( urlConnection.getInputStream() ));
		
		String responseText = "";
		String line;
		
		while((line=br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
//		System.out.println(responseText);
		
		return responseText;
	}
	
	@ResponseBody
	@RequestMapping(value="disaster.do", produces="text/xml; charset=UTF-8")
	public String disasterShelter() throws IOException {
		
		String url = "https://apis.data.go.kr/1741000/TsunamiShelter3/getTsunamiShelter1List";
		url += "?serviceKey=" + serviceKey;
		url += "&numOfRows=500";
		url += "&type=xml";
		
//		System.out.println(url);
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String responseText = "";
		String line;
		
		while((line=br.readLine()) != null) {
			responseText += line; 
		}
		
		br.close();
		urlConnection.disconnect();
		
		return responseText;
	}
	
	@ResponseBody
	@RequestMapping(value="disasterFacility.do", produces="text/xml; charset=UTF-8")
	public String disasterFacilityList() throws IOException {
		
		String url = "https://apis.data.go.kr/6480000/gyeongnamdisasterfacility/gyeongnamdisasterfacilitylist";
		url += "?serviceKey=" + serviceKey;
		url += "&returnType=xml";
		url += "&numOfRows=10";
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String responseText = "";
		String line;
		
		while((line=br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
		return responseText;
	}
	
	@ResponseBody
	@RequestMapping(value="tw.do", produces="application/json; charset=UTF-8")
	public String twSearch(String tw, String date) throws IOException {
		
		String url = "https://apis.data.go.kr/1360000/BeachInfoservice/getTwBuoyBeach";
		url += "?serviceKey=" + serviceKey;
		url += "&beach_num=" + tw;
		url += "&dataType=JSON";
		url += "&searchTime=" + date.replaceAll("-", "") + "1600";
		
		//System.out.println(url);
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String responseText = "";
		String line;
		
		while((line=br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
		return responseText;
		
		
	}
	
}
