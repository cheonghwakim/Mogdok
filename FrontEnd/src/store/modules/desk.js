// 컴포넌트 간에 공유할 data
const state = () => ({
   desk: null,
   isOpenProfile: false,
});

// 목적은 단순히 조회! 이곳에서 보고 싶은 정보만 보여주는 로직 처리!
const getters = {};

// state의 변화를 일으키는 곳, state 조작 수행 | commit으로 호출
// mutation의 함수는 대문자 , Snake_Case 로 표현하는게 컨벤션
const mutations = {
   SET_DESK: function(state, payload) {
      state.desk = payload;
      state.isOpenProfile = true;
   },
   CLEAR_DESK: function(state) {
      state.desk = null;
      state.isOpenProfile = false;
   },
};

// 사용자의 입력에 따라 데이터를 변경하는 methods | 비동기 로직 처리용
const actions = {};

export default {
   state,
   getters,
   actions,
   mutations,
};
