import Vue from 'vue';
import App from './App.vue';
import router from './routes';
import VueDragscroll from 'vue-dragscroll';

Vue.config.productionTip = false;

Vue.use(router);
Vue.use(VueDragscroll);

new Vue({
   router,
   render: (h) => h(App),
}).$mount('#app');
