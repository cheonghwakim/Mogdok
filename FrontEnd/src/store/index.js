import Vue from 'vue';
import Vuex from 'vuex';

// 모듈 import
import desk from './modules/desk';

Vue.use(Vuex);

const store = new Vuex.Store({
   modules: {
      desk,
   },
   plugins: [],
});

export default store;
