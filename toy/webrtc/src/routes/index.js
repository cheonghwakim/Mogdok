import Vue from 'vue';
import Router from 'vue-router';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  {
    path: '/openvidu',
    name: 'OpenVidu',
    component: () => import('@/views/OpenVidu'),
  },
];

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
