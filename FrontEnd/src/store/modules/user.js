import { getAuthToken, login } from '../../api/user';

const state = () => ({
  userInfo: {},
  videoSourceList: [],
  videoSource: undefined,
});

const getters = {};

const actions = {
  GET_AUTH_TOKEN({ commit }, code) {
    return getAuthToken(
      { authorizeCode: code },
      (res) => {
        commit('SET_KAKAO_ID', res.data);
      },
      () => {}
    );
  },
  LOGIN({ state, commit }) {
    return new Promise((resolve, reject) => {
      login(
        { kakaoId: state.userInfo.kakaoId },
        (res) => {
          if (res.data) {
            commit('SET_USER_INFO', res.data);
            localStorage.setItem('authToken', state.userInfo.authToken);
            resolve('ok');
          } else {
            resolve('join');
          }
        },
        () => {
          reject();
        }
      );
    });
  },
};

const mutations = {
  SET_KAKAO_ID(state, payload) {
    state.userInfo.kakaoId = payload;
  },
  SET_USERINFO_PROPERTY(state, { key, value }) {
    state.userInfo[key] = value;
  },
  SET_USER_INFO(state, payload) {
    state.userInfo = payload;
  },
  SET_VIDEO_SOURCE_LIST(state, payload) {
    state.videoSourceList = payload;
  },
  SET_VIDEO_SOURCE(state, payload) {
    state.videoSource = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
