import Vue from 'vue';
import App from './App.vue';
import router from './routes';
import VueDragscroll from 'vue-dragscroll';
import store from './store';
import './utils/filters';

Vue.config.productionTip = false;
Vue.use(VueDragscroll);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
