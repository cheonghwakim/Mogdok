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
                     `level-${convertLevel(day)}`,
                  ]"
               >
                  {{ day }}
               </td>
            </tr>
         </tbody>
      </table>
      <button @click="getData">데이터 얻기</button>
   </div>
</template>
<script>
import { mapState } from 'vuex';

export default {
   data() {
      return {
         isLoaded: false,

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
         handler() {
            console.log('watch');
            // this.apiGetData('ssafy', 2021, 5);
            // const param = {
            //    userName: 'ssafy',
            //    month: value,
            //    year: this.year,
            // };

            // this.$store.dispatch('GET_CALENDAR', param);
         },
      },
   },
   // beforeCreate() {
   //    console.log('beforeCreate');
   //    console.log(this.days);
   // },
   created() {
      this.initCal();
   },
   methods: {
      // 달력 초기 셋팅
      initCal: function() {
         console.log('initCal');
         const date = new Date();

         this.currentYear = date.getFullYear();
         this.currentMonth = date.getMonth() + 1;
         this.today = date.getDate(); // 오늘 날짜

         // 현재 날짜를 기준으로, 조회용 날짜를 셋팅함
         this.year = this.currentYear;
         this.month = this.currentMonth;

         this.calendarData();
         // await this.apiGetData('ssafy', this.year, this.month);
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

      apiGetData: async function(name, year, month) {
         console.log('apiGetData 시작');
         const param = {
            userName: name,
            month: month,
            year: year,
         };

         await this.$store.dispatch('GET_CALENDAR', param);
         console.log('apiGetData 끝!!!');
         return Promise.resolve();
      },

      // ===================================================
      // 현재 날짜의 러닝타임을 5단계 스탭으로 변환하여 표시
      convertLevel: function(day) {
         console.log(day);
         // if (this.runningTimeCalendar[day].runningTime) {
         // console.log(this.runningTimeCalendar[day].runningTime);
         // }
         return 1;
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
   border: 1px dashed red;
}

/* 테이블 day의 높이(이걸 기준으로 전체 캘린더 높이가 결정됨) */
$cell_h: 25px;

.study-calendar {
   width: 200px;
   height: 500px;

   background-color: white;
   border-radius: 20px;
   box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.2);

   border: 1px solid red;

   table.calendar {
      width: inherit;
      border: 1px solid blue;

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

/* 달력 부분의 디자인 */
/* 가독성을 위해 분리 */
tbody.day-section {
   /* background-color: rgb(203, 203, 203); */

   .day {
      text-align: center;
      font-size: 8pt;

      height: $cell_h;
      line-height: $cell_h;

      color: rgb(157, 157, 157);

      &--today {
         color: rgb(255, 77, 0);
      }

      /* 이전달, 다음달 */
      &--pre,
      &--next {
         color: rgb(206, 206, 206);
         background-color: rgb(238, 238, 238);
         pointer-events: none;
      }
   }
}
</style>
