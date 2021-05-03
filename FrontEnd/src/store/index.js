import Vue from 'vue';
import Vuex from 'vuex';

// 모듈 import
import desk from './modules/desk';
import user from './modules/user';

Vue.use(Vuex);

const store = new Vuex.Store({
   modules: {
      desk,
      user,
   },
   plugins: [],
});

export default store;
