# Together-Server

## 협업을 위한 앱 프로젝트, Together의 Server입니다.


```
- seonjaena, bymine과 함께 작업하였습니다.

- Together-APP은 Flutter로 제작됩니다.

- Together-APP과 통신하는 Together-Server는 Spring Boot로 제작됩니다.

- 사용 기술로는 Flutter, Spring Boot, MySQL, CentOS(배포 OS), Naver Cloud Platform, JPA, Tomcat version 9, Java 11등이 있으며, 그 외의 여러 Open Source API들이 사용됩니다.

- Flutter-APP은 협업자인 bymine의 깃허브에 올라가 있습니다.

```

</br>

### Together 제작 동기


-> 학교 강의 조별 과제를 수행하다가 협업에 도움을 주는 서비스를 제공하는 프로그램의 필요성을 느끼고 여러가지 앱들을 살펴보았습니다.
그러던 도중 '우리가 직접 만들면 어떨까'라는 생각이 들어 제작하게 되었습니다.
  

</br>
</br>

### Together 주요 서비스 - 파일 공유 기능


-> 본래 계획한 서비스는 실시간 문서 협업 기능을 목표로 하였으나, 밑에 기술하겠지만 기술 & 비용적인 문제로 파일 공유 기능을 계획하게 되었습니다.
  Together의 파일 공유 기능이란, Realtime Service가 아닌 Lock&Save* 방식과 유사한 방식으로 문서를 수정할 팀원이 수정할 시간을 예약하고, 예약한 시간동안 예약한 팀원은 Read&Write 권한을 얻고 다른 팀원들은 ReadOnly 권한을 갖게 됩니다. 수정완료 후 업로드 시 업데이트 버전과 업데이트 내용에 대한 Comment을 저장하고 이전 버전의 내용을 살펴보고 되돌릴 수 있습니다. 팀원이 수정중인 내용을 확인할 수도 있습니다.

```
  *Lock&Save 방식 : 한 사람이 문서 수정중일 시(Lock상태로 변경) 다른 사람은 그 문서에 접근이 불가합니다. 수정을 완료하고 저장(Save)하면 다른 사람이 수정할 때까지 대기(Free)상태로 변경됩니다. 
```

</br>
</br>

### Together 부가 서비스
  
  
- 팀 구성 : 협업을 진행하는 팀을 생성하고, 프로젝트에 대한 태그나 부가설명으로 팀원을 모집할 수 있습니다.
- 팀 스케줄 작성 : 미팅, 회의 등 주요 일정을 관리합니다.
- 채팅 기능 : 팀 별 채팅 방을 만들고 팀원들끼리 소통할 수 있습니다.
- 개인 시간표 작성 : 개인별 스케줄을 작성해 일정을 확인할 수 있습니다.
- 팀 찾기 : 원하는 프로젝트, 스터디 팀 등을 찾고 지원할 수 있습니다.



### 개발 단계


```
  1. 제공할 서비스 설계
  2. UI 디자인 설계
  3. Data Modeling
  4. Flow 구성
  5. 개발 코드 작성
  6. 에러 디텍팅
```

## Data Modeling

![together_db](https://user-images.githubusercontent.com/48385816/146754847-fdf41440-a5b5-4d10-9e18-9a34d3a29888.png)


```
1. User - user정보에 필요한 Data
2. Project - project정보에 필요한 Data
3. Member - project에 참가하고 있는 user 정보에 대한 테이블
4. File - file정보에 필요한 Data
5. Project_tag - project의 태그 정보. project와 tag_list 테이블 참조.
6. Tag_list - 등록된 tag들의 List.
7. Tag_search - 미등록된 기타 태그들.(자주 이용되는 태그일 경우 tag_list로 이동)
8. Private_schedule - 개인 스케쥴 정보
9. Project_schedule - 팀 스케쥴 정보
10. Chat_message - 채팅 메시지 정보. 이전의 채팅 기록을 남기기 위함.
11. User_validation - 회원가입시 전화번호 인증, 메일 인증시 사용할 Data.
12. Project_invitation - 팀 초대에 대한 기록.
13. File_version - 파일 버전 정보. 업데이트시 Comment 기록.
```

