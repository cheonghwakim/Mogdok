const state = () => ({
  roomInfo: undefined,
});

const getters = {};

const actions = {};

const mutations = {
  SET_ROOMINFO(state, payload) {
    state.roomInfo = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
