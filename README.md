# TP_GooroomeeLite

# GooroomeeLite- Android

개발 기간 : 2021년 6월 1일 ~ 2021년 7월 30일

---

## **[ About ]**

**캠 스터디 구루미의 혼공버전을 지원하는 서비스**

기존의 캠 스터디인 구루미에서 캠 없이 혼자 공부하는것을 기록할 수 있는 혼공족을 위한 
스터디메이트 앱 입니다.

정보 제공 방식은 다음과 같습니다.

- 타이머를 이용한 공부 기록
- 서비스를 이용한 미디어 플레이어 기능
- 프로필 수정, 플레이스토어 연결 기능
- 공유, 카메라, 스티커 기능
- 그래프를 이용한 공부 데이터 정리 기능

---

## **[ Preview ]**

GooroomeeLite PDF 예정

---

대략적인 PDF 이미지 프리뷰

## **[ Develop Environment ]**

- Language : Kotlin
- Android Depolyment Target : Android 6.0(Mashmallow)

---

## **[ Library ]**

1. Server
- Firebasedatabase
- Firebasestorage

 2.   Layout

- [Glide](https://github.com/bumptech/glide)
- [ScaleRatingBar](https://github.com/williamyyu/SimpleRatingBar)
- [gridlayout](https://mvnrepository.com/artifact/androidx.gridlayout/gridlayout/1.0.0-rc01)
- ConstraintLayout
- RelativeLayout
- [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2?hl=ko)

 3. Function

- [TedPermission](https://github.com/ParkSangGwon/TedPermission)
- [ColorPicker](https://github.com/Dhaval2404/ColorPicker)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)


---

## **[ Role ]**

- FirebaseData Management

- Statistic Page

- Share Page

- Sticker Page

---
### **[ Feature Implementation ]**

- 스톱워치 페이지 데이터 연동 구현

  - FireBase와 스톱워치 시간 데이터 연동 
  
- FirebaseData와 연동하여 데이터 관리

   - MVVM패턴으로 ViewModeal를 사용하여 livedata와 dataBinding으로 일간,주간,월간 실시간 연동
  
   - 스티커 페이지에서 공부시간 기록 연동

- 통계 페이지 구현
    
  - Tap Layout 

  - 일간,주간,월간 페이지
    
        - LocalDataTime에서 날짜 분류 작업 
       
        - button 클릭 시 주간,월간 날짜 이동 구현
       
        - MpAndroidChart를 이용한 barchart, stackChart, pieChart Custom

  - 주간 페이지
 
        - MpAndroidChart에서 Limit Line(제한선) 구현


  - PieChart(원 차트)
 
        - RecyclerView Adapter와 PieChart를 연동하여 일간 공부시간 나타내기

- 공유 페이지 구현

      — 카메라 나타내기 구현
      
      - 캡처 버튼 리스너 등록

      — 카메라 전환(앞면/후면)
      
      - 카메라 줌 기능 구현
      
      — 외부 저장소에서 가장 최근의 사진을 가져오기
      
      
- 스티커 페이지 구현

      — 사진에 Sticker 설정

      — 오늘 공부한 시간 프레임 씌어진 사진 공유하기 
     
---

### **[ Tools ]**

- AndroidStudio

- Figma

- Slack

- Github

- Notion
