<template>
   <section class="section">
      <div class="container">
         <h2 class="subtitle text-center">
            <button class="button" @click="calendarData(-1)">
               &lt;
            </button>
            {{ year }}년 {{ month }}월
            <button class="button" @click="calendarData(1)">
               &gt;
            </button>
         </h2>
         <table class="table text-center">
            <thead class="day-container">
               <!-- 월화수목금토일 -->
               <th v-for="day in days" :key="day" class="day-box">{{ day }}</th>
            </thead>
            <tbody>
               <tr v-for="(date, idx) in dates" :key="idx" class="day-container">
                  <td
                     v-for="(day, secondIdx) in date"
                     :key="secondIdx"
                     :class="[
                        {
                           'calendar-today': day === today && month === currentMonth && year === currentYear,
                           'calendar-pre-month': idx === 0 && day >= lastMonthStart,
                           'calendar-next-month': dates.length - 1 === idx && nextMonthStart > day,
                        },
                        `orange-${convertRunningTimeToColorLevel(convertMsToHour(studyTimeCalendar[day].runningTime))}`,
                     ]"
                     class="day-box tooltip"
                     @mouseover="getDetailStudyTimeOfSelectedDate(day)"
                  >
                     {{ day }}
                     <div class="tooltip-box">
                        {{ selectedDate }}
                        <div class="day-container" :key="selectedDate">
                           <div v-for="(hour, index) in selectedDateStudyHourList" :key="'detail' + index" class="day-detail-box" :class="{ 'has-study-hour': hour }">
                              {{ index }}
                           </div>
                        </div>
                     </div>
                  </td>
               </tr>
            </tbody>
         </table>
      </div>
   </section>
</template>

<script>
export default {
   data() {
      return {
         days: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
         dates: [],
         currentYear: 0,
         currentMonth: 0,
         year: 0,
         month: 0,
         today: 0,

         lastMonthStart: 0,
         nextMonthStart: 0,

         showDetailStudyTime: false,
         selectedDate: '',
         selectedDateStudyHourList: new Array(24),
         studyTimeCalendar: undefined,
         // 테스트데이터
         sampleData: [
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
   watch: {
      month: {
         immediate: true,
         handler(value) {
            // 변경된 달로 axios 요청을 보내고 수신한 결과를 달력에 뿌려준다
            // 서버로부터 받아온 그 달의 공부시간 기록 데이터를 이용해 달력과 맞춘다
            this.studyTimeCalendar = this.convertStudyTimeMonthToCalendar(this.getStudyTimeMonth(this.year, value));
            // console.log('%cStudyCalendar.vue line:104 this.studyTimeCalendar', 'color: #007acc;', this.studyTimeCalendar);
         },
      },
   },
   created() {
      // 데이터에 접근이 가능한 첫 번째 라이프 사이클
      const date = new Date();
      this.currentYear = date.getFullYear(); // 이하 현재 년, 월 가지고 있기
      this.currentMonth = date.getMonth() + 1;
      this.year = this.currentYear;
      this.month = this.currentMonth;
      this.today = date.getDate(); // 오늘 날짜
      this.calendarData();
   },
   methods: {
      calendarData(arg) {
         // 인자를 추가
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
         this.dates = this.getMonthOfDays(monthFirstDay, monthLastDate, lastMonthLastDate);
      },
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
      }, //==================================================================================================================================
      parseStudyTimeOfDay(sampleData) {
         const size = sampleData.length;
         const studyRunningTimeList = [];
         let flag = false;
         let curStartTime = null;
         let runningTime = 0;
         for (let i = 0; i < size; i++) {
            const { studyTime, status, startTime } = sampleData[i];
            if (status === 'START') {
               // 공부 시작
               flag = true;
               curStartTime = studyTime;
            } else {
               if (flag) {
                  runningTime += new Date(studyTime) - new Date(curStartTime);
                  flag = false;
               }
               if (status === 'END') {
                  studyRunningTimeList.push({
                     startTime: new Date(startTime + 'Z'), // 로컬 시간으로 변환해서 기록
                     runningTime,
                  });
                  runningTime = 0; // 공부가 끝났으므로 다음 공부시간 기록을 위해 초기화
               }
            }
         }
         console.log(studyRunningTimeList);

         return studyRunningTimeList;
      },
      getDetailStudyTimeOfSelectedDate(day) {
         this.showDetailStudyTime = true;
         this.selectedDate = `${this.year} ${this.month} ${day}`;
         // 해당 날짜를 이용해 axios 요청하여 그 날의 공부 시간 기록을 가져옴
         // 가져온 데이터를 parseStudyTimeOfDay를 통해 파싱
         // 파싱된 결과에 담긴 {startTime, runningTime}을 이용한다
         const studyRunningTimeList = this.parseStudyTimeOfDay(this.studyTimeCalendar[day].log);
         // 시작시간에서부터 공부시간을 더한 시간까지 색을 칠한다
         this.selectedDateStudyHourList = new Array(24);
         for (let i = 0; i < studyRunningTimeList.length; i++) {
            const { startTime, runningTime } = studyRunningTimeList[i];
            const _startTime = new Date(startTime + 'Z');
            const startHour = _startTime.getHours();
            const runningHour = runningTime / (1000 * 60 * 60);
            // array splice 기능 이용해서 해당 시간에 true 삽입
            for (let j = 0; j < runningHour; j++) {
               this.selectedDateStudyHourList.splice(startHour + j, 1, true); // 시작시간부터 공부시간만큼 제거
            }
         }
      },
      getStudyTimeMonth(year, month) {
         // Todo : 해당 년,월의 공부기록 axios 요청
         console.log('API', year + ',' + month);
         return this.sampleData;
      },
      convertStudyTimeMonthToCalendar(array) {
         // Todo : 캘린더와 인덱스를 맞춰야 함
         // array 순회하며 공부가 기록된 날 찾기
         // 기록된날에 작성된 것들을 그 날에 배열 형태로 넣기
         const calendar = new Array(32); // 1달 최대 31일
         for (let i = 1; i < calendar.length; i++) {
            // 캘린더 초기화
            calendar[i] = [];
         }
         for (let i = 0; i < array.length; i++) {
            // 해당 날짜에 일치하는 정보 삽입
            const studyInfo = array[i];
            const day = new Date(studyInfo.startTime).getDate();
            calendar[day].push(studyInfo);
         }
         const result = [];
         for (let i = 0; i < calendar.length; i++) {
            const log = calendar[i];
            if (!log) {
               result.push(undefined);
               continue;
            }
            result.push({
               runningTime: this.getRunningTimeBysampleData(this.parseStudyTimeOfDay(log)),
               log,
            });
         }
         return result;
      },
      getRunningTimeBysampleData(timestamp) {
         let sum = 0;
         for (let i = 0; i < timestamp.length; i++) {
            sum += timestamp[i].runningTime;
         }
         return sum;
      },
      convertMsToHour(value) {
         return parseInt(value / 3600000);
      },
      convertRunningTimeToColorLevel(value) {
         if (value >= 10) return 5;
         return parseInt(value / 2);
      },
   },
};
</script>
<style scoped>
.text-center {
   text-align: center;
}
.day-container {
   display: flex;
   flex-wrap: wrap;
}
.day-box {
   display: flex;
   justify-content: center;
   align-items: center;
   height: 50px;
   width: 50px;
}
.day-detail-box {
   display: flex;
   justify-content: center;
   align-items: center;
   height: 50px;
   width: 20px;
}
.calendar-pre-month {
   color: gray !important;
   background-color: lightgray !important;
   pointer-events: none;
}
.calendar-next-month {
   color: gray !important;
   background-color: lightgray !important;
   pointer-events: none;
}
.calendar-today {
   color: orange;
}
.has-study-hour {
   background-color: orange;
}
.tooltip {
   display: inline-block;
   font-weight: bold;
}
.tooltip-box {
   display: none;
   position: absolute;
   border: 1px solid;
   border-radius: 5px;
   color: black;
   background: white;
   margin-top: 40px;
}
.tooltip:hover {
   border: 2px solid orange;
}
.tooltip:hover .tooltip-box {
   display: block;
}
.orange-1 {
   background-color: rgb(255, 140, 0, 0.2);
}
.orange-2 {
   background-color: rgb(255, 140, 0, 0.4);
}
.orange-3 {
   background-color: rgb(255, 140, 0, 0.6);
}
.orange-4 {
   background-color: rgb(255, 140, 0, 0.8);
}
.orange-5 {
   background-color: rgb(255, 140, 0, 1);
}
</style>
