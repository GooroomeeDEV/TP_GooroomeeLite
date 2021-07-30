# TP_GooroomeeLite

# GooroomeeLite- Android

개발 기간 : 2021년 6월 1일 ~ 2021년 7월 30일

---

## **[ About ]**

**캠 스터디 구루미의 혼공버전을 지원하는 서비스**

기존의 캠 스터디인 구루미에서 캠 없이 혼자 공부하는것을 기록할수 있는 혼공족을 위한 
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

---
### **[ Feature Implementation ]**

- 스톱워치 페이지 데이터 연동 구현

  - FireBase와 스톱워치 시간 데이터 연동 

- 통계 페이지 구현
    
    - Tap Layout 구현

    — 일간,주간,월간 페이지
      
      - button 클릭 시 주간,월간 날짜 이동 구현
      
      - MpAndroidChart를 이용한 barchart, stackChart, pieChart Custom

    
    
    
    — 월간 페이지
    
    - MpAndroidChart를 이용한 barchart, stackChart, pieChart Custom
    
    - 카메라 가져오기 시 Permission 구현

- 공유 페이지 구현

      — 카메라 기능 구현

      — 화면 회전
      
      — 최신 이미지 가져오기
      
      - 사진에 frame 씌우기
      
- 플레이스토어 연결

---

### **[ Tools ]**

- AndroidStudio

- Figma

- Slack

- Github

- Notion
