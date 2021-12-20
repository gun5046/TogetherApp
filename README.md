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
  Together의 파일 공유 기능이란, 업데이트 버전과 업데이트 내용에 대한 Comment을 저장하고 이전 버전의 내용을 살펴보고 되돌릴 수 있습니다.
  팀원이 수정중인 내용을 확인할 수도 있습니다.
  

### Together 부가 서비스
  
- 팀 구성 : 협업을 진행하는 팀을 생성하고, 프로젝트에 대한 태그나 부가설명으로 팀원을 모집할 수 있습니다.
- 팀 스케줄 작성 : 미팅, 회의 등 주요 일정을 관리합니다.
- 채팅 기능 : 팀 별 채팅 방을 만들고 팀원들끼리 소통할 수 있습니다.
- 개인 시간표 작성 : 개인별 스케줄을 작성해 일정을 확인할 수 있습니다.
- 팀 찾기 : 원하는 프로젝트, 스터디 팀 등을 찾고 지원할 수 있습니다.



### 개발 단계

```
  1. UI 디자인 설계
  2. Data Modeling
  3. Flow 구성
  4. 개발 코드 작성
  5. 에러 디텍팅
```

## Data Modeling

![together_db](https://user-images.githubusercontent.com/48385816/146754847-fdf41440-a5b5-4d10-9e18-9a34d3a29888.png)

