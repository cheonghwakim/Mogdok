import { getCalendarMonth } from '../../api/desk';

const state = () => ({
   // 서버로 받은 RAW DATA
   studyCalendarMonth: null, // 선택한 달의 전체 공부 시간 [{},{},{}, ...]
   studyCalendarDay: null, // 선택한 날의 전체 공부 시간

   // 공부한 시간(Running)이 담긴 달력
   runningTimeCalendar: null,
});

const actions = {
   // 데이터 API 요청 및 러닝 달력 변환 동기 처리
   async GET_CALENDAR({ dispatch, state }, payload) {
      console.log('📆 GET_CALENDAR', payload);
      console.log('Promise 1');
      await dispatch('GET_MONTH_STUDY_CALENDAR', payload);
      console.log('Promise 2');
      await dispatch('CONVERT_STUDY_2_RUNNING');

      console.log('👀 GET_CALENDAR result : ', state.runningTimeCalendar);
      return Promise.resolve();
   },

   // 년,월,닉네임을 기준으로 선택한 달의 전체 데이터를 LOAD
   async GET_MONTH_STUDY_CALENDAR({ commit }, payload) {
      commit('SET_STUDY_CALENDAR_MONTH', null);

      await getCalendarMonth(
         payload,
         (res) => {
            commit('SET_STUDY_CALENDAR_MONTH', res.data.data);
            return Promise.resolve();
         },
         (error) => {
            alert(error);
            return Promise.reject(error);
         }
      );
   },

   // RAW DATA -> RUNNING 달력 배열로 변환
   CONVERT_STUDY_2_RUNNING({ state, commit }) {
      // array 순회하며 공부가 기록된 날 찾기
      // 기록된날에 작성된 것들을 그 날에 배열 형태로 넣기
      // console.log('Promise 2-1');
      commit('SET_RUNNING_TIME_CALENDAR', null);

      const rawData = state.studyCalendarMonth;

      if (typeof rawData === 'string') {
         console.log(rawData);
         return; // 비어있음 끝내기
      }

      const calendar = new Array(32); // 1달 최대 31일
      for (let i = 1; i < calendar.length; i++) {
         // 캘린더 초기화
         calendar[i] = [];
      }

      for (let i = 0; i < rawData.length; i++) {
         // 해당 날짜에 일치하는 정보 삽입
         const studyDetail = { ...rawData[i] };

         // UTC 기준 시간을 로컬 시간으로 변환하여 반환
         const day = convertLocalTime(studyDetail.startTime).getDate();
         calendar[day].push(studyDetail);
      }

      const result = [];

      for (let i = 0; i < calendar.length; i++) {
         const log = calendar[i];

         if (!log) {
            result.push(undefined);
         } else {
            result.push({
               runningTime: calculateTotalRunningTime(convertDay2RunningTime(log)),
               log,
            });
         }
      }

      commit('SET_RUNNING_TIME_CALENDAR', result);
   },
};
const mutations = {
   SET_STUDY_CALENDAR_MONTH: function(state, payload) {
      state.studyCalendarMonth = payload;
   },
   SET_STUDY_CALENDAR_DAY: function(state, payload) {
      state.studyCalendarDay = payload;
   },
   SET_RUNNING_TIME_CALENDAR: function(state, payload) {
      state.runningTimeCalendar = payload;
   },
};

export default {
   state,
   actions,
   mutations,
};

// =========================================================================
// 선택한 날 하루의 공부 시간(러닝 타임)을 계산해서 리스트로 반환
// [ runningTime : 18000, runningTime : 120000 ]
function convertDay2RunningTime(param) {
   const size = param.length;
   const studyRunningTimeList = []; // 하루에 공부를 끊었다가, 다시 들어와서 할 수 있으므로 []로

   let flag = false;
   let curStartTime = null;
   let runningTime = 0;

   for (let i = 0; i < size; i++) {
      const { studyTime, status, startTime } = param[i];
      if (status === 'START') {
         // 공부 시작
         flag = true;
         curStartTime = studyTime;
      } else {
         if (flag) {
            runningTime += convertLocalTime(studyTime) - convertLocalTime(curStartTime);
            flag = false;
         }
         if (status === 'END') {
            studyRunningTimeList.push({
               startTime: convertLocalTime(startTime), // 로컬 시간으로 변환해서 기록
               runningTime,
            });
            runningTime = 0; // 공부가 끝났으므로 다음 공부시간 기록을 위해 초기화
         }
      }
   }

   return studyRunningTimeList;
}

// 선택한 날 하루의 공부 시간을 전부 합쳣허 토탈 시간을 구하기
// 하루에 공부 끊었다가, 다시 했다가 한 시간 전부 합
function calculateTotalRunningTime(param) {
   let sum = 0;
   for (let i = 0; i < param.length; i++) {
      sum += param[i].runningTime;
   }
   return sum;
}

// 입력받은 데이터를 로컬 기준으로 변환
function convertLocalTime(input) {
   // console.log('INPUT STRING : ', input);
   // console.log('CHANGE DATE() : ', new Date(input));

   return new Date(input);
}
