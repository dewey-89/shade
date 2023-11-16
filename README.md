
![shade-logo](https://github.com/dewey-89/sparta-mini-project/assets/140939516/0feb318a-428c-4ad6-8349-8dcfce071fba)

## SHADE! 개발을 공부하는 사람들의 익명 커뮤니티
팀 프로젝트 고민, 개발자로 잘 성장할 수 있을까에 대한 고민, 면대 면으로 나눌 수 없는 사소한 이야기들을 공유해요!

## URL
https://mini-project-l76lk5tvn-goyka.vercel.app/

## 기술 스택

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/amazon rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">

## 개발 환경

🖱Backend
* IntelliJ
* spring boot 3.1.3
* spring-boot-jpa 
* Spring Security
* Java 17
* mysql
  
🖱CI/CD
* cloudtype
* github Action

## 서비스 아키텍처
![system](https://github.com/dewey-89/sparta-mini-project/assets/140939516/23b6c316-88b0-4940-b76b-32c0b8945fa5)


## API 명세서
https://aquamarine-laugh-515.notion.site/fa59c54bb3d04ac2bcc16c0e579711b0?v=e296ec2150114fbcb80b5b53ed56ee3c&pvs=4

## ERD
<img width="927" alt="스크린샷 2023-09-19 오전 11 16 30" src="https://github.com/dewey-89/sparta-mini-project/assets/140939516/7257e7ec-c1ca-4e29-941b-cbd480ff558f">

<img width="326" alt="스크린샷 2023-09-27 오후 2 30 06" src="https://github.com/dewey-89/sparta-mini-project/assets/140939516/b12216e8-dff3-454f-8e57-16b02f5026e3">




## custom 예외처리 메시지
<img width="1261" alt="스크린샷 2023-09-27 오후 1 32 20" src="https://github.com/dewey-89/sparta-mini-project/assets/140939516/2d8430a1-c6d9-488d-8abb-08a8862e18c8">

## 트러블 슈팅
✨**문제상황:**
프론트엔드와 백엔드 간에 서로 다른 형식의 응답 메시지가 전송되어 프론트엔드에서 응답을 올바르게 처리하지 못하는 문제가 발생했습니다.

**해결방법:**
응답 메시지 형식을 통일시켜 프론트엔드에서 일관된 방식으로 처리할 수 있도록 아래의 단계를 따라 해결하였습니다.

1. 백엔드에서 일관된 응답 메시지 형식을 정의하기 위해 `ApiResponse` 클래스를 생성하였습니다. 이 클래스는 응답의 상태(`status`), 메시지(`message`), 데이터(`data`)를 표현합니다.
2. `ApiResponse` 클래스에는 성공(`successData`, `successMessage`)과 에러(`error`) 응답을 생성하는 정적 메소드를 구현하여, 각각의 경우에 맞는 응답을 생성할 수 있도록 하였습니다. 이를 통해 응답을 좀 더 구체적으로 나타낼 수 있게 되었습니다.
3. 에러 처리를 위해 `CustomException` 클래스를 생성하여 각종 예외 사항에 대한 에러 코드(`ErrrorCode`)를 정의하였습니다. 이렇게 함으로써, 예외 발생 시 해당 예외에 대한 상태 코드와 메시지를 일관되게 반환할 수 있게 되었습니다.
4. 에러 처리를 위한 `GlobalExceptionHandler` 클래스를 구현하였습니다. 이 클래스는 `CustomException`을 처리하고, 해당 예외에 대한 응답을 **`ApiResponse`** 형식으로 생성하여 반환합니다. 이를 통해 예외가 발생하더라도 일관된 응답 메시지가 클라이언트에게 전달됩니다.

이러한 방식으로 응답 메시지 형식을 통일시켜, 프론트엔드와 백엔드 간의 통신에서 발생하는 혼란을 해결하고, 클라이언트 측에서 응답을 올바르게 처리할 수 있게 되었습니다.

---

✨**문제 상황** : 코드 수정 전, 토큰의 유효성을 검사하고 토큰이 유효하지 않을 경우 로그에 "Token Error"를 출력하고 메서드를 종료하는데, 프론트엔드에게는 명확한 오류 응답이 제공되지 않아 프론트엔드는 토큰 오류를 파악하기 어려웠습니다.

**해결 방법** :

1. 토큰이 유효하지 않을 때 클라이언트에게 명확한 에러 메시지를 제공하기 위해 `ObjectMapper`를 사용합니다. `ObjectMapper`는 Java 객체를 JSON 문자열로 변환하고 반대로 JSON 문자열을 Java 객체로 역직렬화하는 데 사용됩니다.
2. `ObjectMapper`를 사용하여 `ApiResponse` 클래스의 error 메소드로 생성된 JSON 응답을 문자열로 변환합니다.
3. 문자열로 변환된 JSON 응답을 프론트엔드에게 전달하기 전에, 응답 객체(`HttpServletResponse`)의 문자 인코딩과 콘텐츠 유형을 설정합니다. 이로써 클라이언트가 JSON 형식의 응답을 올바르게 해석할 수 있게 됩니다.
4. 응답 객체(`HttpServletResponse`)에 상태 코드(`SC_UNAUTHORIZED`)를 설정하여 클라이언트에게 요청이 거부되었음을 알립니다.
5. 마지막으로, 클라이언트에게 전달할 JSON 응답을 응답 객체(`HttpServletResponse`)에 기록하고, 클라이언트로 응답을 보냅니다

프론트엔드는 이 JSON 응답을 받아 토큰이 유효하지 않다는 오류 메시지를 확인할 수 있으며, 이를 통해 프론트엔드는 토큰 오류에 대한 명확한 정보를 받아 문제를 해결할 수 있습니다.

## 백엔드 팀
- 강성원 : https://github.com/dewey-89
- 이재하 : https://github.com/jaeha0183
- 박나원 : https://github.com/Hewllpark

## 프론트엔트 팀 github Repo
https://github.com/Goyka/mini-project-SHADE-FE-
