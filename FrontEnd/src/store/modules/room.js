import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { getSeatInfo, getAllRooms } from '../../api/room';

const state = () => ({
  socket: undefined,
  stomp: undefined,
  roomList: undefined,
  roomInfo: undefined,
  seatInfo: undefined,
});

const getters = {};

const actions = {
  GET_ALL_ROOMS({ commit }) {
    getAllRooms(
      (res) => {
        commit('SET_ALL_ROOMS', res.data);
        console.log('%croom.js line:21 allRooms', 'color: #007acc;', res.data);
      },
      (error) => {
        console.log('%croom.js line:19 error', 'color: #007acc;', error);
      }
    );
  },
  CONNECT_ROOM_SERVER({ state, rootState, commit }) {
    commit('INIT_CONNECT');
    state.stomp.connect(
      { token: rootState.user.authToken },
      (frame) => {
        console.log('소켓 연결 성공', frame);
        console.log('token', rootState.user.authToken);
        // 좌석 정보 받기
        // 서버 구독하기
      },
      (error) => {
        console.log('%croom.js line:35 error', 'color: #007acc;', error);
      }
    );
  },
  GET_SEAT_INFO({ state, commit }) {
    getSeatInfo(
      { roomId: state.roomInfo.roomId },
      (res) => {
        commit('SET_SEAT_INFO', res.data);
      },
      (error) => {
        console.log('%croom.js line:32 error', 'color: #007acc;', error);
      }
    );
  },
  SUBSCRIBE_ROOM_SERVER({ state }) {
    state.stomp.subscribe(
      `/sub/room/${state.roomInfo.roomId}`,
      (message) => {
        console.log(message);
      },
      (error) => {
        console.log('%croom.js line:41 error', 'color: #007acc;', error);
      }
    );
  },
};

const mutations = {
  SET_ALL_ROOMS(state, payload) {
    state.roomList = payload;
  },
  INIT_CONNECT(state) {
    state.socket = new SockJS(process.env.VUE_APP_BASE_URL_ROOM_CONNECT);
    state.stomp = Stomp.over(state.socket);
  },
  SET_ROOMINFO(state, payload) {
    state.roomInfo = payload;
  },
  SET_SEAT_INFO(state, payload) {
    state.seatInfo = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
