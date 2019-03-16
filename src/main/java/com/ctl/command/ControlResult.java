package com.ctl.command;


public enum ControlResult {

   
   SUCCESS,         // 성공
   NOT_PREPARED,    // 이벤트에 대한 핸들러가 정의되어 있지 않음.
   EXECUTION_FAIL,  // 핸들러 수행 오류. 
   INAVLID,         // 유효하지 않은 요청 포맷(파라미터)
   FORBIDDEN,       // 허용되지 않은 IP로 부터의 요청
   HTTP_ERROR,      // HTTP 오류
   CONNECTION_FAIL, // 접속 실패
   FAIL;            // 일반 오류
}
