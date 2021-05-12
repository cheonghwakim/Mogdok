import { getDeskInfo } from '../../api/desk';
// import { moveableState } from './deskedit';

// 컴포넌트 간에 공유할 data
const state = () => ({
   desk: null,
   deskId: undefined,
   isOpenProfile: false,
});

// 목적은 단순히 조회! 이곳에서 보고 싶은 정보만 보여주는 로직 처리!
const getters = {};

// 사용자의 입력에 따라 데이터를 변경하는 methods | 비동기 로직 처리용
const actions = {
   // DESK 내의 모든 메모 정보를 LOAD [Desk.vue]
   GET_DESK_ALL_MEMO({ commit }, object) {
      // 기존 메모리스트 초기화
      commit('SET_MEMO_LIST', []);

      getDeskInfo(
         // { nickname },
         { nickname: 'ssafy' }, // 테스트용
         (res) => {
            commit('SET_DESK_ID', res.data.data.deskId);
            commit(
               'SET_MEMO_LIST',
               res.data.data.memoList.map((e) => {
                  // moveFixedState 객체를 무버블에 할당 (= 움직이지 못하도록)
                  e.moveable = { ...object }; // 깊은 복사!!!
                  return e;
               }),
               { root: true }
            );
            commit('SET_DDAY_LIST', res.data.data.ddayList, { root: true });
            commit('SET_BOARD_LIST', res.data.data.boardList, { root: true });
            commit('SET_PROMISE', res.data.data.promise, { root: true });
         },
         (error) => {
            alert(error);
         }
      );
   },

   // DESK EDIT 모드용으로 모든 메모 정보를 다시 LOAD [DeskEdit.vue]
   GET_DESK_ALL_MEMO_4_EDIT({ commit }, object) {
      // 기존 메모리스트 초기화
      commit('SET_MEMO_LIST', []);

      getDeskInfo(
         // { nickname },
         { nickname: 'ssafy' }, // 테스트용
         (res) => {
            commit('SET_DESK_ID', res.data.data.deskId);
            commit(
               'SET_MEMO_LIST',
               res.data.data.memoList.map((e) => {
                  // moveableState 객체를 무버블에 할당 (= 편집 가능한 요소)
                  e.moveable = { ...object }; // 깊은 복사!!!
                  return e;
               }),
               { root: true }
            );
            commit('SET_DDAY_LIST', res.data.data.ddayList, { root: true });
            commit('SET_BOARD_LIST', res.data.data.boardList, { root: true });
            commit('SET_PROMISE', res.data.data.promise, { root: true });
         },
         (error) => {
            alert(error);
         }
      );
   },
};

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
   SET_DESK_ID(state, payload) {
      state.deskId = payload;
   },
};

export default {
   state,
   getters,
   actions,
   mutations,
};
