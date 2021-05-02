import Vue from 'vue';
import App from './App.vue';
import router from './routes';
import store from './store';
// import './utils/filters'; // 전역 필터 기능을 사용한다면 주석 제거

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
