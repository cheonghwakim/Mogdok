import Vue from 'vue';
import App from './App.vue';
import router from './routes';
import VueDragscroll from 'vue-dragscroll';
import VueLottiePlayer from 'vue-lottie-player';
import store from './store';
import './utils/filters';
import VWave from 'v-wave';

Vue.config.productionTip = false;
Vue.use(VueDragscroll);
Vue.use(VueLottiePlayer);
Vue.use(VWave);

new Vue({
   router,
   store,
   render: (h) => h(App),
}).$mount('#app');
