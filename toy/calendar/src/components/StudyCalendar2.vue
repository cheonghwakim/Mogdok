<template lang="">
   <div class="study-calendar">
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
                  ]"
               >
                  {{ day }}
               </td>
            </tr>
         </tbody>
      </table>
   </div>
</template>
<script>
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

         // VUEX 이동용
         showDetailStudyTime: false,
         selectedDate: '',
         selectedDateStudyHourList: new Array(24),
         studyTimeCalendar: undefined,

         // 테스트데이터
         sample: [
            {
               studyId: 1,
               studyTime: '2021-04-28T00:00:00.000',
               startTime: '2021-04-28T00:00:00.000',
               status: 'PAUSE',
            },
            {
               studyId: 2,
               studyTime: '2021-04-28T01:00:00.000',
               startTime: '2021-04-28T00:00:00.000',
               status: 'START',
            },
            {
               studyId: 3,
               studyTime: '2021-04-28T04:00:00.000',
               startTime: '2021-04-28T00:00:00.000',
               status: 'END',
            },
         ],
      };
   },
   created() {
      this.initCal();
   },
   methods: {
      // 달력 초기 셋팅
      initCal: function() {
         const date = new Date();

         this.currentYear = date.getFullYear();
         this.currentMonth = date.getMonth() + 1;
         this.today = date.getDate(); // 오늘 날짜

         // 현재 날짜를 기준으로, 조회용 날짜를 셋팅함
         this.year = this.currentYear;
         this.month = this.currentMonth;

         this.calendarData();
      },

      // 달력 제어
      calendarData(arg) {
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
      getFirstDayLastDate(year, month) {
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
      getMonthOfDays(monthFirstDay, monthLastDate, prevMonthLastDate) {
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
   },
};
</script>
<style scoped lang="scss">
* {
   border: 1px dashed red;
}

.study-calendar {
   width: 300px;
   height: 500px;
   border: 1px solid red;

   table.calendar {
      width: 300px;
      border: 1px solid blue;

      border-spacing: 0px; // cell 사이 여백을 없앰

      /* 월화수목금토일 */
      thead.head-section {
         color: gray;
         font-size: 10pt;

         /* 일요일 */
         th:nth-child(1) {
            color: rgb(247, 53, 53);
         }
         /* 토요 */
         th:nth-child(7) {
            color: rgb(49, 17, 255);
         }
      }
   }
}

/* 달력 부분의 디자인 */
/* 가독성을 위해 분리 */
tbody.day-section {
   /* background-color: rgb(203, 203, 203); */

   .day {
      height: 35px;

      $f_size: 10pt;

      text-align: center;
      font-size: $f_size;
      line-height: $f_size;

      color: rgb(157, 157, 157);

      &--today {
         color: rgb(255, 77, 0);
      }
      /* 이전달, 다음달 */
      &--pre,
      &--next {
         color: rgb(206, 206, 206);
         background-color: rgb(228, 228, 228);
         pointer-events: none;
      }
   }
}
</style>
