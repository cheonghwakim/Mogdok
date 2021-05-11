import { getDeskInfo } from '../../api/desk';
import { moveDisableState, moveableState } from './deskedit';

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
   // DESK 내의 모든 메모 정보를 LOAD (Desk.vue에서 최초로드)
   GET_DESK_ALL_MEMO({ commit }) {
      console.log('---------- GET_DESK_ALL_MEMO ----------');
      // const elem = document.getElementsByClassName('desk-draw-area')[0];
      // console.log('할당 전 elem은? \n', elem);

      getDeskInfo(
         // { nickname },
         { nickname: 'ssafy' }, // 테스트용
         (res) => {
            commit('SET_DESK_ID', res.data.data.deskId);
            commit(
               'SET_MEMO_LIST',
               res.data.data.memoList.map((e) => {
                  // 최초엔 움직이지 못하는 상태
                  e.moveable = { ...moveDisableState };

                  // 컨테이너를 body가 아닌, desk-draw-area 하단으로 설정 Desk.vue
                  const elem = document.getElementsByClassName('desk-draw-area')[0];
                  e.moveable.container = elem;

                  // console.log('>> ', e.moveable.container);

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

   // DESK 내의 모든 메모 정보를 LOAD (Desk.vue에서 최초로드)
   GET_DESK_ALL_MEMO_4_EDIT({ commit }) {
      console.log('---------- GET_DESK_ALL_MEMO_4_EDIT ----------');
      // const elem = document.getElementsByClassName('desk-draw-area')[0];
      // console.log('할당 전 elem은? \n', elem);

      getDeskInfo(
         // { nickname },
         { nickname: 'ssafy' }, // 테스트용
         (res) => {
            commit('SET_DESK_ID', res.data.data.deskId);
            commit(
               'SET_MEMO_LIST',
               res.data.data.memoList.map((e) => {
                  // 최초엔 움직이지 못하는 상태
                  e.moveable = { ...moveableState };

                  // 컨테이너를 body가 아닌, desk-draw-area 하단으로 설정 Desk.vue
                  const elem = document.getElementsByClassName('desk-draw-area')[0];
                  e.moveable.container = elem;

                  // console.log('>> ', e.moveable.container);

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
