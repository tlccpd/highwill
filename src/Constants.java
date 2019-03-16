package kr.ac.gwnu.constants;

public interface Constants {
	String PACKAGE_NAME = "kr.ac.gwnu";
	
	String APIKEY = "846538405931";
	
	String URL_INDEX = "http://m.gwnu.ac.kr";
	
	String URL_PUSH_REGISTER = URL_INDEX+"/registerDeviceToken.push";
	String URL_FACEBOOK = "http://www.facebook.com/pages/%EA%B0%95%EB%A6%89%EC%9B%90%EC%A3%BC%EB%8C%80%ED%95%99%EA%B5%90-Gangneung-Wonju-National-University/184445181617740";
	String URL_CAMPUSMAP = URL_INDEX+"/intro/04/index.do";
	String URL_FOOD = URL_INDEX + "/intro/05/index.do";
	String URL_LOGIN = URL_INDEX + "/auth/login.do";
	String URL_LOGIN_CHECK = URL_INDEX + "/auth/loginCheckJson.do";
	String URL_LOGOUT = URL_INDEX +"/auth/logout.do";
	String PREF_KEY_MEMBER_ID = "memberId";
	String PREF_KEY_MEMBER_PW = "memberPw";
	
	String PREF_KEY_CHECK_LOGIN = "check_login";
	String PREF_KEY_CHECK_PUSH = "check_push";
	
	String KEY_PUSH = "register_result";

	
}