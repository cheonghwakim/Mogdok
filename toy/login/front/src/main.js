import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueCookies from 'vue-cookies';
import VueCompositionApi from '@vue/composition-api';

Vue.config.productionTip = false;
Vue.use(VueCompositionApi);
Vue.use(VueCookies);
// window.Kakao.init('7242231a7200a6234158327cb280f84d');

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
