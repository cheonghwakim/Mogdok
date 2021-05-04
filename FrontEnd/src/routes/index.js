/* 1. 기본 셋팅
 - npm으로 라우터 설치(모듈 시스템 이용)시, 임포트 후 명시적으로 추가
*/
import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '../store';

Vue.use(VueRouter);

const requireAuth = () => async (to, from, next) => {
  let token = localStorage.getItem('authToken');
  if (store.state.user.authToken === undefined && token) {
    await store.dispatch('SET_AUTH_TOKEN', token);
  }
  if (store.state.user.authToken) {
    await store
      .dispatch('LOGIN')
      .then(() => {
        if (store.state.user.userInfo) next();
        else next('/join');
      })
      .catch(() => {
        next('/login');
      });
  } else next('/login');
};

// ==============================

/* 2. 컴포넌트 및 views 임포트
 - 컴포넌트 : 모든 사이트에 공통으로 들어가는 놈
 - 뷰 : 화면 바뀌는 녀석들
 */

// 공통으로 들어가는 컴포넌트
import Header from '@/components/common/RoomHeader.vue';
import Footer from '@/components/common/RoomFooter.vue';

// 바뀌는 views import
import Login from '@/views/Login.vue';
import SocialLogin from '@/views/SocialLogin.vue';
import Enterance from '@/views/Enterance.vue';
import Room from '@/views/Room.vue';
import Desk from '@/views/Desk.vue';
import Join from '@/views/Join.vue';

// ==============================
// 여러개 태울 때, routes 변수 생성
const routes = [
  {
    path: '/',
    name: 'Enterance',
    beforeEnter: requireAuth(),
    components: {
      default: Enterance,
    },
  },
  {
    path: '/login',
    name: 'Login',
    components: {
      default: Login,
    },
  },
  {
    path: '/kakaologin',
    name: 'KakaoLogin',
    components: {
      default: SocialLogin,
    },
  },
  {
    path: '/join',
    name: 'Join',
    components: {
      default: Join,
    },
  },
  {
    path: '/room',
    name: 'Room',
    beforeEnter: requireAuth(),
    components: {
      header: Header,
      default: Room,
      footer: Footer,
    },
  },
  {
    path: '/desk',
    name: 'Desk',
    beforeEnter: requireAuth(),
    components: {
      default: Desk,
      footer: Footer,
    },
  },
];

const router = new VueRouter({
  mode: 'history', //뒤에 # 을 없애줌
  // base: process.env.BASE_URL,
  routes,
});

export default router;
