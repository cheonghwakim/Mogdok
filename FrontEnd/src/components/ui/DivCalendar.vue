<template lang="">
   <div class="study-calendar">
      <!-- 달력 관련 부분 -->
      <div class="calendar-warpper">
         <!-- 최상단 : 년월 이동 버튼 존재 -->
         <div class="top-wrapper">
            <button @click="calendarData(-1)">&lt;</button>
            {{ year }} / {{ month }}
            <button @click="calendarData(1)">&gt;</button>
         </div>
         <!-- 달력 부분이 들어감 -->
         <table class="calendar">
            <!-- 월화수목금토일 -->
            <thead class="head-section">
               <th v-for="day in days" :key="day">{{ day }}</th>
            </thead>

            <!-- 달력부분 -->
            <tbody class="day-section">
               <tr v-for="(week, idx) in dates" :key="idx">
                  <td
                     v-for="(day, jdx) in week"
                     :key="jdx"
                     class="day"
                     :class="[
                        {
                           'day--today': day === today && month === currentMonth && year === currentYear,
                           'day--pre': idx === 0 && day >= lastMonthStart,
                           'day--next': dates.length - 1 === idx && nextMonthStart > day,
                        },
                        `level--${convertLevel(day)}`,
                     ]"
                     @click="openDayDetail(day)"
                  >
                     {{ day }}
                  </td>
               </tr>
            </tbody>
         </table>
         <div class="desc">
            여기엔 짧막한 설명이 들어갑니다.
         </div>
      </div>

      <!-- 달력 디테일 부분 -->
      <transition name="detail-down">
         <div v-if="isOpenDetail" class="detail-wrapper">
            <p class="title">DAY STUDY TIME</p>
            <table class="running-table">
               <tr class="day">
                  <td class="head"><img src="@/assets/img/emoji/day.png" alt="" /></td>
                  <td class="time" v-for="time in dayRunningTable" :key="'day-' + time" :class="{ 'study-hour': selectedDayDetail[time] }">{{ time }}</td>
               </tr>
               <tr class="noon">
                  <td class="head"><img src="@/assets/img/emoji/noon.png" alt="" /></td>
                  <td class="time" v-for="time in noonRunningTable" :key="'noon-' + time" :class="{ 'study-hour': selectedDayDetail[time] }">{{ time }}</td>
               </tr>
               <tr class="night">
                  <td class="head"><img src="@/assets/img/emoji/night.png" alt="" /></td>
                  <td class="time" v-for="time in nightRunningTable" :key="'night-' + time" :class="{ 'study-hour': selectedDayDetail[time] }">{{ time }}</td>
               </tr>
            </table>
         </div>
      </transition>
   </div>
</template>
<script>
import { mapState } from 'vuex';

export default {
   data() {
      return {
         days: ['일', '월', '화', '수', '목', '금', '토'],
         dates: [],

         // 달력 제어용
         currentYear: 0, // 실제 현재 연도
         currentMonth: 0, // 실제 현재 월
         year: 0, // 조회용 연도
         month: 0, // 조회용 월
         today: 0,

         // 달력에서 회색으로 표기하기 위한 변수
         lastMonthStart: 0,
         nextMonthStart: 0,

         // 선택한 날의 상세 러닝타임
         isOpenDetail: false,
         selectedDayDetail: [],
         dayRunningTable: [7, 8, 9, 10, 11, 12, 13, 14],
         noonRunningTable: [15, 16, 17, 18, 19, 20, 21, 22],
         nightRunningTable: [23, 24, 1, 2, 3, 4, 5, 6],
      };
   },
   computed: {
      ...mapState({
         studyCalendarMonth: (state) => state.calendar.studyCalendarMonth, // 보고있는 책상의 ID
         studyCalendarDay: (state) => state.calendar.studyCalendarDay, //  책상의 메모들
         runningTimeCalendar: (state) => state.calendar.runningTimeCalendar, //  책상의 디데이들
      }),
   },
   watch: {
      month: {
         // immediate: true,
         handler(value) {
            console.log('📅 watch');

            const param = {
               userName: 'ssafy',
               month: value,
               year: this.year,
            };

            this.$store.dispatch('GET_CALENDAR', param);
         },
      },
   },
   // beforeCreate() {
   //    console.log('📅 beforeCreate');
   //    console.log(this.days);
   // },
   created() {
      this.initCal();
   },
   methods: {
      // 달력 초기 셋팅
      initCal: function() {
         console.log('📅 initCal');
         const date = new Date();

         this.currentYear = date.getFullYear();
         this.currentMonth = date.getMonth() + 1;
         this.today = date.getDate(); // 오늘 날짜

         // 현재 날짜를 기준으로, 조회용 날짜를 셋팅함
         this.year = this.currentYear;
         this.month = this.currentMonth;

         this.calendarData();
      },

      // 달력 생성
      calendarData: function(arg) {
         // 인자가 있을 경우
         if (arg < 0) {
            // -1이 들어오면 지난 달 달력으로 이동
            this.month -= 1;
         } else if (arg === 1) {
            // 1이 들어오면 다음 달 달력으로 이동
            this.month += 1;
         }

         if (this.month === 0) {
            // 작년 12월
            this.year -= 1;
            this.month = 12;
         } else if (this.month > 12) {
            // 내년 1월
            this.year += 1;
            this.month = 1;
         }

         const [monthFirstDay, monthLastDate, lastMonthLastDate] = this.getFirstDayLastDate(this.year, this.month);

         // getMonthOfDays를 통해 받은 날짜 배열을 dates 배열에 할당
         this.dates = this.getMonthOfDays(monthFirstDay, monthLastDate, lastMonthLastDate);
      },

      // 지난 달의 마지막 날짜 가져오기
      getFirstDayLastDate: function(year, month) {
         const firstDay = new Date(year, month - 1, 1).getDay(); // 이번 달 시작 요일
         const lastDate = new Date(year, month, 0).getDate(); // 이번 달 마지막 날짜
         let lastYear = year;
         let lastMonth = month - 1;
         if (month === 1) {
            lastMonth = 12;
            lastYear -= 1;
         }
         const prevLastDate = new Date(lastYear, lastMonth, 0).getDate(); // 지난 달 마지막 날짜
         return [firstDay, lastDate, prevLastDate];
      },

      // 현재 달의 날짜들을 배열로 반환
      getMonthOfDays: function(monthFirstDay, monthLastDate, prevMonthLastDate) {
         let day = 1;
         let prevDay = prevMonthLastDate - monthFirstDay + 1;
         const dates = [];
         let weekOfDays = [];

         while (day <= monthLastDate) {
            if (day === 1) {
               // 1일이 어느 요일인지에 따라 테이블에 그리기 위한 지난 셀의 날짜들을 구할 필요가 있다.
               for (let j = 0; j < monthFirstDay; j += 1) {
                  if (j === 0) this.lastMonthStart = prevDay; // 지난 달에서 제일 작은 날짜
                  weekOfDays.push(prevDay);
                  prevDay += 1;
               }
            }
            weekOfDays.push(day);
            if (weekOfDays.length === 7) {
               // 일주일 채우면
               dates.push(weekOfDays);
               weekOfDays = []; // 초기화
            }
            day += 1;
         }
         const len = weekOfDays.length;
         if (len > 0 && len < 7) {
            for (let k = 1; k <= 7 - len; k += 1) {
               weekOfDays.push(k);
            }
         }
         if (weekOfDays.length > 0) dates.push(weekOfDays); // 남은 날짜 추가
         this.nextMonthStart = weekOfDays[0]; // 이번 달 마지막 주에서 제일 작은 날짜
         return dates;
      },

      // ===================================================
      // 현재 날짜의 러닝타임을 5단계 스탭으로 변환하여 표시
      convertLevel: function(day) {
         if (!this.runningTimeCalendar) {
            return 0;
         } else {
            var runningTime = this.runningTimeCalendar[day].runningTime;
            if (runningTime == 0) return 0;

            var convertHour = parseInt(runningTime / 3600000);

            // 4단계 기준
            // 1: 0~3시간
            // 2: 3~6시간
            // 3: 6~9시간
            // 4: 9시간 이상

            var step;
            switch (convertHour) {
               case 0:
               case 1:
               case 2:
                  step = 1;
                  break;
               case 3:
               case 4:
               case 5:
                  step = 2;
                  break;
               case 6:
               case 7:
               case 8:
                  step = 3;
                  break;
               default:
                  step = 4;
                  break;
            }

            return step;
         }
      },

      // 현재 클릭한 날짜의 상세 데이터 출력
      openDayDetail: function(day) {
         const selectedDay = this.runningTimeCalendar[day].runningDetail;

         if (selectedDay.length == 0) {
            // 상세 데이터 없는 경우
            this.isOpenDetail = false;
            return;
         }

         this.selectedDayDetail = new Array(24);

         for (let i = 0; i < selectedDay.length; i++) {
            const { startTime, runningTime } = selectedDay[i];

            const _startTime = new Date(startTime);
            const startHour = _startTime.getHours();
            const runningHour = runningTime / (1000 * 60 * 60);
            // array splice 기능 이용해서 해당 시간에 true 삽입
            for (let j = 0; j < runningHour; j++) {
               this.selectedDayDetail.splice(startHour + j, 1, true); // 시작시간부터 공부시간만큼 제거
            }
         }

         this.isOpenDetail = true;
      },

      // TEST용 데이터
      getData: function() {
         // 선택한 달의 전체 공부 달력을 가져옴
         console.log(this.studyCalendarMonth);
         console.log(this.runningTimeCalendar);
      },
   },
};
</script>
<style scoped lang="scss">
* {
   /* border: 1px dashed red; */
}

/* 테이블 day의 높이(이걸 기준으로 전체 캘린더 높이가 결정됨) */
$cell_h: 27px;

.study-calendar {
   width: 240px;
   height: 500px;
   border: 1px dashed red;

   .calendar-warpper {
      width: 100%;
      height: auto;
      padding: 12px;
      padding-bottom: 50px;

      background-color: white;
      border-radius: 20px;
      box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.2);

      border: 1px solid red;

      table.calendar {
         width: 92%;
         margin: 0 auto;
         /* border: 1px solid blue; */

         border-spacing: 0px; // cell 사이 여백을 없앰

         /* 월화수목금토일 */
         thead.head-section {
            color: gray;
            font-size: 8pt;

            th {
               height: 20px;
               line-height: 20px;
            }
            /* 일요일 */
            th:nth-child(1) {
               color: rgb(247, 53, 53);
            }
            /* 토요일 */
            th:nth-child(7) {
               color: rgb(49, 17, 255);
            }
         }
      }
   }

   .detail-wrapper {
      width: 90%;
      height: 120px;

      margin: 0 auto;
      margin-top: 10px;

      background-color: white;
      border-radius: 15px;
      box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.2);

      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .title {
         font-weight: 600;
         letter-spacing: 4px;
         font-size: 5pt;
         margin-bottom: 10px;
      }

      .running-table {
         border-spacing: 2px 2px;
         border-collapse: separate;

         tr.day {
            margin: 10px;
         }

         td {
            font-size: 7pt;
            width: 18px;
            vertical-align: middle;
            text-align: center;

            border: 1px solid rgb(189, 189, 189);
            color: rgb(109, 109, 109);

            &.time {
               border-radius: 3px;
            }

            &.study-hour {
               color: white;
               background-color: rgb(34, 150, 16);
            }

            &.head {
               border: none;
               background-color: transparent;
               width: 26px;
               img {
                  width: 60%;
               }
            }
         }
      }
   }
}

/* 달력 부분의 디자인 */
/* 가독성을 위해 분리 */
tbody.day-section {
   /* background-color: rgb(203, 203, 203); */

   .day {
      text-align: center;
      font-size: 8pt;

      width: 18px;
      height: $cell_h;
      line-height: $cell_h;

      box-shadow: 0 0 0 0.5px rgb(255, 255, 255) inset;
      cursor: pointer;

      color: rgb(157, 157, 157);

      &--today {
         color: rgb(255, 77, 0) !important;
         font-weight: 600;
      }

      /* 이전달, 다음달 */
      &--pre,
      &--next {
         color: rgb(206, 206, 206) !important;
         background-color: rgb(238, 238, 238) !important;
         pointer-events: none;
      }

      /* 레벨 단계 */
      &.level--0 {
         background-color: transparent;
      }
      &.level--1 {
         color: white;
         background-color: #a9ffa9;
      }
      &.level--2 {
         color: white;
         background-color: #5cdf5c;
      }
      &.level--3 {
         color: white;
         background-color: #10a410;
      }
      &.level--4 {
         color: white;
         background-color: #073a07;
      }
   }
}

/* 트랜지션 */
.detail-down-enter-active {
   transition: all 0.5s ease;
}
.detail-down-leave-active {
   transition: all 0.2s ease;
}
.detail-down-enter,
.detail-down-leave-to {
   transform: translateY(-50px);
   opacity: 0;
}
</style>
