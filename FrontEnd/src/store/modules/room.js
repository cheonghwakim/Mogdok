import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { getSeatList, getAllRooms } from '../../api/room';

const ROOM_MESSAGE_SEAT_ALLOCATED = 'SEAT_ALLOCATED';
const ROOM_MESSAGE_SEAT_STATUS = 'SEAT_STATUS';
const ROOM_STUDY_TYPE_START = 'START';
const ROOM_STUDY_TYPE_PAUSE = 'PAUSE';
const ROOM_STUDY_TYPE_NO_ACTION = 'NO_ACTION';
export {
  ROOM_MESSAGE_SEAT_ALLOCATED,
  ROOM_MESSAGE_SEAT_STATUS,
  ROOM_STUDY_TYPE_START,
  ROOM_STUDY_TYPE_PAUSE,
  ROOM_STUDY_TYPE_NO_ACTION,
};

const state = () => ({
  socket: undefined,
  stomp: undefined,
  roomList: undefined,
  roomInfo: undefined,
  seatList: undefined,
  selectedSeatInfo: undefined,
  userRoomState: ROOM_STUDY_TYPE_NO_ACTION,
});

const getters = {};

const actions = {
  GET_ALL_ROOMS({ commit }) {
    getAllRooms(
      (res) => {
        commit('SET_ALL_ROOMS', res.data);
      },
      (error) => {
        console.log('%croom.js line:19 error', 'color: #007acc;', error);
      }
    );
  },
  CONNECT_ROOM_SERVER({ state, rootState, commit }) {
    commit('INIT_CONNECT');
    return new Promise((resolve, reject) => {
      state.stomp.connect(
        { token: rootState.user.userInfo.authToken },
        () => {
          resolve();
        },
        (error) => {
          console.log('%croom.js line:35 error', 'color: #007acc;', error);
          reject();
        }
      );
    });
  },
  GET_SEAT_INFO({ state, commit }) {
    return new Promise((resolve, reject) => {
      getSeatList(
        { roomId: state.roomInfo.roomId },
        (res) => {
          res.data.forEach((seatInfo) => {
            commit('ADD_SEAT_INFO', { index: seatInfo.seatNo - 1, seatInfo });
          });
          resolve();
        },
        (error) => {
          console.log('%croom.js line:32 error', 'color: #007acc;', error);
          reject();
        }
      );
    });
  },
  SUBSCRIBE_ROOM_SERVER({ state, commit }) {
    state.stomp.subscribe(
      `/sub/room/${state.roomInfo.roomId}`,
      (message) => {
        console.log(message);
        const res = JSON.parse(message.body);
        if (res.type === 'SEAT_ALLOCATED') {
          alert('자리에 앉았어요.');
          console.log('%croom.js line:81 res', 'color: #007acc;', res);
          // 자리앉기
          res.seatInfo.userName = res.sender;
          res.seatInfo.userId = res.userId;
          commit('ADD_SEAT_INFO', { index: res.seatInfo.seatNo - 1, seatInfo: res.seatInfo });
          commit('SET_USER_ROOM_STATE', 'PAUSE');
          commit('UPDATE_ROOM_INFO', { key: 'userCount', value: res.userCount });
        } else if (res.type === 'SEAT_ALLOCATE_FAIL') {
          alert('자리에 앉지 못했어요.');
        }
      },
      (error) => {
        console.log('%croom.js line:41 error', 'color: #007acc;', error);
      }
    );
  },
  CONNECT_ROOM_WITH_OPENVIDU({ state, rootState, commit }) {
    // userName을 이용해서 자리 매칭
    rootState.openvidu.subscribers.forEach((subscriber) => {
      for (let i = 0; i < state.seatList.length; i++) {
        if (state.seatList[i] && state.seatList[i].userName === subscriber.stream.connection.data) {
          commit('ADD_SUBSCRIBER_INTO_SEAT', { index: i, subscriber });
          break;
        }
      }
    });
  },
  SEND_SEAT_ALLOCATED({ state, rootState }, { seatNo }) {
    state.stomp.send(
      `/pub/room/message`,
      JSON.stringify({
        type: ROOM_MESSAGE_SEAT_ALLOCATED,
        roomId: state.roomInfo.roomId,
        seatInfo: {
          seatNo,
          studyType: ROOM_STUDY_TYPE_PAUSE,
        },
      }),
      { token: rootState.user.userInfo.authToken }
    );
  },
  ADD_SUBSCRIBER_INTO_SEAT_LIST({ state, commit }, subscriber) {
    console.log(
      '%croom.js line:119 subscriber.stream.connection.data',
      'color: #007acc;',
      subscriber
    );

    for (let i = 0; i < state.seatList.length; i++) {
      if (state.seatList[i])
        console.log(
          '%croom.js line:123 state.seatList[i].userName',
          'color: #007acc;',
          state.seatList[i].userName
        );
      if (state.seatList[i] && state.seatList[i].userName === subscriber.stream.connection.data) {
        console.log('%croom.js line:121 매칭!!', 'color: #007acc;');
        commit('ADD_SUBSCRIBER_INTO_SEAT', { index: i, subscriber });
        break;
      }
    }
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
  SET_ROOM_INFO(state, payload) {
    state.roomInfo = payload;
    state.seatList = new Array(payload.limitUserCount);
  },
  UPDATE_ROOM_INFO(state, { key, value }) {
    state.roomInfo[key] = value;
  },
  ADD_SEAT_INFO(state, { index, seatInfo }) {
    state.seatList.splice(index, 1, seatInfo);
  },
  ADD_SUBSCRIBER_INTO_SEAT(state, { index, subscriber }) {
    console.log('%croom.js line:144 add!!', 'color: #007acc;');
    state.seatList[index].subscriber = subscriber;
    console.log('%croom.js line:146 ', 'color: #007acc;', state.seatList[index]);
  },
  SET_SELECTED_SEAT_INFO(state, payload) {
    state.selectedSeatInfo = payload;
  },
  SET_USER_ROOM_STATE(state, payload) {
    state.userRoomState = payload;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
