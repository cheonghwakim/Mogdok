import { getAuthToken, login } from '../../api/user';

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

const actions = {
  async GET_AUTH_TOKEN({ dispatch }, code) {
    await getAuthToken(
      { authorizeCode: code },
      async (res) => {
        await dispatch('SET_AUTH_TOKEN', res.data);
        localStorage.setItem('authToken', res.data);
      },
      () => {}
    );
  },
  async SET_AUTH_TOKEN({ commit }, authToken) {
    await commit('SET_AUTH_TOKEN', authToken);
  },
  async LOGIN({ commit }) {
    let ret = 'error';
    await login(
      async (res) => {
        if (res.data) {
          // 기존회원
          await commit('SET_USER_INFO', res.data);
          ret = 'ok';
        } else {
          ret = 'join';
        }
      },
      () => {}
    );
    return ret;
  },
};

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
