import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';

// 모듈 import
import desk from './modules/desk';
import user from './modules/user';
import room from './modules/room';
import openvidu from './modules/openvidu';
import memo from './modules/memo';

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    desk,
    user,
    room,
    openvidu,
    memo,
  },
  plugins: [
    createPersistedState({
      paths: ['user'],
    }),
  ],
});

export default store;
