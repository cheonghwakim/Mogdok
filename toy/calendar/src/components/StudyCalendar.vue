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
          <th v-for="day in days" :key="day" class="day-box">{{ day }}</th>
        </thead>
        <tbody>
          <tr v-for="(date, idx) in dates" :key="idx" class="day-container">
            <td
              v-for="(day, secondIdx) in date"
              :key="secondIdx"
              :class="{
                'calendar-today': day === today && month === currentMonth && year === currentYear,
                'calendar-pre-month': idx === 0 && day >= lastMonthStart,
                'calendar-next-month': dates.length - 1 === idx && nextMonthStart > day,
              }"
              class="day-box"
            >
              {{ day }}
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
      lastMonthStart: 0,
      nextMonthStart: 0,
      today: 0,
    };
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
  mounted() {
    this.parseStudyTimeOfDay();
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
      const [monthFirstDay, monthLastDate, lastMonthLastDate] = this.getFirstDayLastDate(
        this.year,
        this.month
      );
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
    },
    parseStudyTimeOfDay() {
      // 테스트데이터
      const studyTimestamp = [
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
          studyTime: '2021-04-28T03:00:00.000',
          startTime: '2021-04-28T00:00:00.000',
          status: 'PAUSE',
        },
        {
          studyId: 2,
          studyTime: '2021-04-28T04:00:00.000',
          startTime: '2021-04-28T00:00:00.000',
          status: 'START',
        },
        {
          studyId: 3,
          studyTime: '2021-04-28T05:00:00.000',
          startTime: '2021-04-28T00:00:00.000',
          status: 'PAUSE',
        },
        {
          studyId: 4,
          studyTime: '2021-04-29T04:00:00.000',
          startTime: '2021-04-28T00:00:00.000',
          status: 'END',
        },
        {
          studyId: 1,
          studyTime: '2021-04-29T00:00:00.000',
          startTime: '2021-04-29T00:00:00.000',
          status: 'PAUSE',
        },
        {
          studyId: 2,
          studyTime: '2021-04-28T04:00:00.000',
          startTime: '2021-04-28T00:00:00.000',
          status: 'START',
        },
        {
          studyId: 4,
          studyTime: '2021-04-29T04:00:00.000',
          startTime: '2021-04-28T00:00:00.000',
          status: 'END',
        },
      ];
      const size = studyTimestamp.length;
      const studyRunningTimeList = [];

      // let hourToMs = 1000 * 60 * 60;
      let flag = false;
      let curStartTime = null;
      let runningTime = 0;
      for (let i = 0; i < size; i++) {
        const { studyTime, status, startTime } = studyTimestamp[i];
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
            console.log(
              '%cStudyCalendar.vue line:210 runningTime',
              'color: #007acc;',
              runningTime / (1000 * 60 * 60)
            );
            runningTime = 0; // 공부가 끝났으므로 다음 공부시간 기록을 위해 초기화
          }
        }
      }
      console.log('%cStudyCalendar.vue line:190 study', 'color: #007acc;', studyRunningTimeList);
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
.calendar-pre-month {
  color: gray !important;
  background-color: lightgray;
}
.calendar-next-month {
  color: gray !important;
  background-color: lightgray;
}
.calendar-today {
  color: orange;
}
</style>
