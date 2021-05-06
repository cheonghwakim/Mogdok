import { getAuthToken, login } from '../../api/user';

const state = () => ({
  authToken: undefined,
  userInfo: undefined,
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
  SET_AUTH_TOKEN(state, payload) {
    state.authToken = payload;
  },
  SET_USER_INFO(state, payload) {
    state.userInfo = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
