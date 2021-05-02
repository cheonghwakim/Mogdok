import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const store = new Vuex.Store({
   // 컴포넌트 간에 공유할 data
   state: {
      desk: null,
      isOpenProfile: false,
   },

   // state의 변화를 일으키는 곳, state 조작 수행 | commit으로 호출
   // mutation의 함수는 대문자 , Snake_Case 로 표현하는게 컨벤션
   mutations: {
      SET_DESK: function(state, payload) {
         state.desk = payload;
         state.isOpenProfile = true;
      },
      CLEAR_DESK: function(state) {
         state.desk = null;
         state.isOpenProfile = false;
      },
   },

   // 사용자의 입력에 따라 데이터를 변경하는 methods | 비동기 로직 처리용
   actions: {},
});

export default store;
