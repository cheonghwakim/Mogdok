import { getAuthToken, login, loginByAuthToken } from '../../api/user';

const state = () => ({
  userInfo: {},
  videoSourceList: [],
  videoSource: undefined,
});

const getters = {};

const actions = {
  async GET_AUTH_TOKEN({ commit }, code) {
    return await getAuthToken(
      { authorizeCode: code },
      (res) => {
        commit('SET_KAKAO_ID', res.data);
      },
      (error) => {
        alert('카카오아이디를 받아오는데 실패했습니다. ' + error);
      }
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
          } else if (res.data === undefined) {
            resolve('join');
          } else {
            reject('로그인에 실패했어요');
          }
        },
        (error) => {
          reject(error);
        }
      );
    });
  },
  async LOGIN_BY_AUTH_TOKEN({ commit }, token) {
    commit('SET_AUTH_TOKEN', token);
    await loginByAuthToken(
      (res) => {
        commit('SET_USER_INFO', res.data);
      },
      (error) => {
        alert(error);
      }
    );
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
  SET_AUTH_TOKEN(state, payload) {
    state.userInfo.authToken = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
