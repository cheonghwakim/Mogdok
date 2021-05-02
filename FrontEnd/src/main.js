import Vue from 'vue';
import App from './App.vue';
import router from './routes';
import VueDragscroll from 'vue-dragscroll';
import store from './store';

Vue.config.productionTip = false;

Vue.use(router);
Vue.use(VueDragscroll);

new Vue({
   router,
   store,
   render: (h) => h(App),
}).$mount('#app');
