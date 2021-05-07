import { getDeskInfo, saveMemoList, deleteMemoList } from '../../api/desk';

const MEMO_MAX_SIZE = 10;

const movedisableState = {
  draggable: false,
  scalable: false,
  rotatable: false,
  resizable: false,
  pinchable: false,
  throttleDrag: 0,
  keepRatio: true,
  throttleScale: 0,
  throttleRotate: 0,
  origin: false,
  zoom: 1,
  className: 'movedisable',
  snappable: true,
  bounds: { left: 0, top: 0, right: 1000, bottom: 600 },
};

const moveableState = {
  draggable: true,
  scalable: true,
  rotatable: true,
  resizable: false,
  pinchable: true,
  throttleDrag: 0,
  keepRatio: true,
  throttleScale: 0,
  throttleRotate: 0,
  origin: false,
  zoom: 1,
  className: 'moveable',
  snappable: true,
  bounds: { left: 0, top: 0, right: 1000, bottom: 600 },
};

const state = () => ({
  deskId: undefined,
  promise: '',
  memoList: [],
  ddayList: [],
  boardList: [],
  selectedMemoIdx: -1,
  editable: false,
  dialog: false,
  removedMemoList: [],
  memoEditDialog: false,
});

const getters = {
  convertedMemoIdList(state) {
    return state.memoList.map((e) => {
      if (isNaN(e.memoId)) {
        e.memoId = undefined;
        return e;
      } else {
        return e;
      }
    });
  },
  convertRemovedMemoList(state) {
    return state.removedMemoList.filter((e) => {
      return !isNaN(e);
    });
  },
};

const actions = {
  GET_DESK_INFO({ commit }, nickname) {
    getDeskInfo(
      { nickname },
      (res) => {
        commit('SET_DESK_ID', res.data.data.deskId);
        commit('SET_MEMO_LIST', res.data.data.memoList);
        commit('SET_DDAY_LIST', res.data.data.ddayList);
        commit('SET_BOARD_LIST', res.data.data.boardList);
        commit('SET_PROMISE', res.data.data.promise);
      },
      () => {}
    );
  },
  SAVE_MEMO_LIST({ commit, getters }) {
    saveMemoList(
      getters.convertedMemoIdList,
      (res) => {
        commit('SET_MEMO_LIST', res.data.data);
      },
      () => {}
    );
  },
  DELETE_MEMO_LIST({ commit, getters }) {
    deleteMemoList(
      getters.convertRemovedMemoList,
      (res) => {
        commit('SET_MEMO_LIST', res.data.data);
      },
      () => {}
    );
  },
  UPDATE_SELECTED_MEMO_UI_BY_INDEX({ state, commit }, index) {
    if (state.selectedMemoIdx >= 0 && state.selectedMemoIdx < state.memoList.length) {
      commit('SET_MEO_CLASSNAME_BY_INDEX', {
        index: state.selectedMemoIdx,
        className: state.editable ? 'moveable' : 'movedisable',
      }); // 이전에 클릭한 메모의 선택이 풀린 UI로 변경
      state.memoList[state.selectedMemoIdx].zIndex = zIndexCount++;
    }
    commit('SET_SELECTED_MEMO_IDX', index); // 클릭된 메모의 index 업데이트
    commit('SET_MEO_CLASSNAME_BY_INDEX', {
      index: state.selectedMemoIdx,
      className: state.editable ? 'clicked' : 'moveable',
    });
    state.memoList[state.selectedMemoIdx].zIndex = 3000 + zIndexCount;
  },
};

const mutations = {
  SET_DESK_ID(state, payload) {
    state.deskId = payload;
  },
  SET_MEMO_LIST(state, payload) {
    state.memoList = payload;
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
  ADD_PROPERTY_MOVEABLE_TO_MEMO_LIST(state) {
    for (let i = 0; i < state.memoList.length; i++)
      state.memoList[i].moveable = { ...movedisableState };
  },
  SET_EDIT_STATE(state, payload) {
    state.editable = payload;
  },
  SET_REMOVED_MEMO_LIST(state, payload) {
    state.removedMemoList = payload;
  },
  SET_SELECTED_MEMO_IDX(state, payload) {
    state.selectedMemoIdx = payload;
  },
  SET_MEO_CLASSNAME_BY_INDEX(state, { index, className }) {
    state.memoList[index].moveable.className = className;
  },
  SET_MEMO_MOVEABLE_STATUS_BY_INDEX(state, { index, status }) {
    state.memoList[index].moveable.draggable = status;
    state.memoList[index].moveable.scalable = status;
    state.memoList[index].moveable.rotatable = status;
  },
  REMOVE_MEMO(state) {
    state.removedMemoList.push(state.memoList[state.selectedMemoIdx].memoId);
    state.memoList.splice(state.selectedMemoIdx, 1);
  },
  CREATE_MEMO(state) {
    if (state.memoList.length >= MEMO_MAX_SIZE) return; // 메모지는 최대갯수를 넘길 수 없음
    state.memoList.push({
      deskId: state.deskId,
      memoId: 'tmp' + state.createMemoKeyIndex++,
      content: '',
      zIndex: 1,
      moveable: { ...moveableState },
      color: 0,
    });
  },
  SET_SELECTED_MEMO_CONTENT(state, payload) {
    state.memoList[state.selectedMemoIdx].content = payload;
  },
  SET_MEMO_EDIT_DIALOG(state, payload) {
    state.memoEditDialog = payload;
  },
  SET_SELECTED_MEMO_COLOR(state, payload) {
    state.memoList[state.selectedMemoIdx].color = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
