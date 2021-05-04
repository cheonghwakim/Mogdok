const state = () => ({
  userInfo: {
    // 테스트데이터
    userId: 1,
    userName: '테스트',
  },
  videoSourceList: [],
  videoSourceIdx: 0,
});

const getters = {};

const actions = {};

const mutations = {
  SET_VIDEO_SOURCE_LIST(state, payload) {
    state.videoSourceList = payload;
    console.log('%cuser.js line:18 payload', 'color: #007acc;', payload);
  },
  SET_VIDEO_SOURCE_IDX(state, payload) {
    state.videoSourceIdx = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
