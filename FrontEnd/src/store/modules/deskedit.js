import { saveMemoList, deleteMemoList } from '../../api/desk';

const MEMO_MAX_SIZE = 10;

const state = () => ({
   deskId: undefined, // TODO: deskId는 desk에서 받아온다
   promise: '',
   memoList: [],
   ddayList: [],
   boardList: [],
   selectedMemoIdx: -1,
   editable: false,
   removedMemoList: [],
   memoEditDialog: false,
   createMemoKeyIndex: 0,
   zIndexCount: 0,
});

const getters = {};

const actions = {
   CREATE_MEMO({ state, rootState, commit }, object) {
      if (state.memoList.length > MEMO_MAX_SIZE) return; // 메모지는 최대갯수를 넘길 수 없음
      const memo = {
         deskId: rootState.desk.deskId,
         memoId: 'tmp' + state.createMemoKeyIndex++,
         content: '',
         zIndex: 1,
         moveable: { ...object },
         color: 0,
      };
      commit('ADD_MEMO', memo);
   },
   SAVE_MEMO_LIST({ state, commit }) {
      const tmp = [];
      state.memoList.forEach((e) => {
         tmp.push({
            color: e.color,
            content: e.content,
            deskId: e.deskId,
            memoId: isNaN(e.memoId) ? undefined : e.memoId,
            transform: e.transform,
         });
      });
      saveMemoList(
         tmp,
         (res) => {
            commit('SET_MEMO_LIST', res.data.data);
         },
         () => {}
      );
   },
   DELETE_MEMO_LIST({ state, commit }) {
      commit(
         'SET_REMOVED_MEMO_LIST',
         state.removedMemoList.filter((e) => {
            return !isNaN(e);
         })
      );
      deleteMemoList(
         state.removedMemoList,
         (res) => {
            commit('SET_MEMO_LIST', res.data.data);
         },
         () => {}
      );
   },
   // 선택한 메모의 selectedMemoIdx 를 변경함
   UPDATE_SELECTED_MEMO_UI_BY_INDEX({ state, commit }, index) {
      if (state.selectedMemoIdx >= 0 && state.selectedMemoIdx < state.memoList.length) {
         // 범위 안
         commit('SET_MEMO_CLASSNAME_BY_INDEX', {
            index: state.selectedMemoIdx,
            className: state.editable ? 'moveable' : 'moveDisable',
         }); // 이전에 클릭한 메모의 선택이 풀린 UI로 변경
         state.memoList[state.selectedMemoIdx].zIndex = state.zIndexCount;
         commit('ADD_ZINDEX_COUNT');
      }

      commit('SET_SELECTED_MEMO_IDX', index); // 클릭된 메모의 index 업데이트
      commit('SET_MEMO_CLASSNAME_BY_INDEX', {
         index: state.selectedMemoIdx,
         className: state.editable ? 'clicked' : 'moveable',
      });

      state.memoList[state.selectedMemoIdx].zIndex = state.zIndexCount;
   },
};

const mutations = {
   SET_MEMO_LIST(state, payload) {
      state.memoList = payload;
   },
   ADD_MEMO(state, payload) {
      state.memoList.push(payload);
   },
   SET_DDAY_LIST(state, payload) {
      state.ddayList = payload;
   },
   SET_BOARD_LIST(state, payload) {
      state.boardList = payload;
   },
   SET_PROMISE(state, payload) {
      state.promise = payload;
   },
   SET_EDIT_STATE(state, payload) {
      state.editable = payload;
      state.createMemoKeyIndex = 0;
      state.zIndexCount = 0;
   },
   SET_REMOVED_MEMO_LIST(state, payload) {
      state.removedMemoList = payload;
   },
   SET_SELECTED_MEMO_IDX(state, payload) {
      state.selectedMemoIdx = payload;
   },
   SET_MEMO_CLASSNAME_BY_INDEX(state, { index, className }) {
      state.memoList[index].moveable.className = className;
   },
   SET_MEMO_TRANSFORM_BY_INDEX(state, { index, transform }) {
      state.memoList[index].transform = transform;
   },
   SET_MEMO_MOVEABLE_STATUS_BY_INDEX(state, { index, status }) {
      state.memoList[index].moveable.draggable = status;
      state.memoList[index].moveable.scalable = status;
      state.memoList[index].moveable.rotatable = status;
   },
   REMOVE_MEMO(state) {
      if (state.selectedMemoIdx < 0) return;
      state.removedMemoList.push(state.memoList[state.selectedMemoIdx].memoId);
      state.memoList.splice(state.selectedMemoIdx, 1);
      state.selectedMemoIdx = -1;
   },
   SET_SELECTED_MEMO_CONTENT(state, payload) {
      if (state.selectedMemoIdx < 0) return;
      state.memoList[state.selectedMemoIdx].content = payload;
   },
   SET_MEMO_EDIT_DIALOG(state, payload) {
      state.memoEditDialog = payload;
   },
   SET_SELECTED_MEMO_COLOR(state, payload) {
      if (state.selectedMemoIdx < 0) return;
      state.memoList[state.selectedMemoIdx].color = payload;
   },
   ADD_ZINDEX_COUNT(state) {
      state.zIndexCount++;
   },

   //  DESK-DRAW-AREA 에서 EDIT-AREA로 변환
   CHANG_DRAW_2_EDIT(state, { index, elem }) {
      state.memoList[index].moveable.container = elem;
   },
};

export default {
   state,
   getters,
   actions,
   mutations,
};
