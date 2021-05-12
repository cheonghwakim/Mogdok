import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { getSeatList, getAllRooms } from '../../api/room';

const ROOM_MESSAGE_SEAT_ALLOCATED = 'SEAT_ALLOCATED';
const ROOM_MESSAGE_SEAT_STATUS = 'SEAT_STATUS';
const ROOM_MESSAGE_SEAT_END = 'END';
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
  SUBSCRIBE_ROOM_SERVER({ state, rootState, commit }) {
    state.stomp.subscribe(
      `/sub/room/${state.roomInfo.roomId}`,
      (message) => {
        console.log(message);
        const res = JSON.parse(message.body);
        // TODO: sender가 '나'일때 처리 userRoomState 변경
        // TODO: 각 사용자의 변경된 상태를 적용해야함
        if (res.type === 'SEAT_ALLOCATED') {
          console.log('%croom.js line:81 res', 'color: #007acc;', res);
          // 자리앉기
          res.seatInfo.userName = res.sender;
          res.seatInfo.userId = res.userId;
          commit('ADD_SEAT_INFO', { index: res.seatInfo.seatNo - 1, seatInfo: res.seatInfo });
          commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_PAUSE);
          commit('UPDATE_ROOM_INFO', { key: 'userCount', value: res.userCount });
        } else if (res.type === 'SEAT_ALLOCATE_FAIL') {
          if (res.sender === rootState.user.userInfo.userName) alert('자리에 앉을 수 없어요!');
          console.log('%croom.js line:88 SEAT_ALLOCATE_FAIL', 'color: #007acc;');
        } else if (res.type === 'END') {
          // 자리 떠나기
          commit('REMOVE_SEAT_INFO', { index: res.seatInfo.seatNo - 1 });
          commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_NO_ACTION);
        } else if (res.type === 'QUIT') {
          // 소켓마저 끊김
          commit('REMOVE_SEAT_INFO', { index: res.seatInfo.seatNo - 1 });
          commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_NO_ACTION);
        }
      },
      (error) => {
        console.log('%croom.js line:41 error', 'color: #007acc;', error);
      }
    );
  },
  CONNECT_ROOM_WITH_OPENVIDU({ state, rootState, commit }) {
    // userName을 이용해서 자리 매칭
    loop: for (let i = 0; i < state.seatList.length; i++) {
      if (!state.seatList[i]) continue;
      for (let j = 0; j < rootState.openvidu.subscribers.length; j++) {
        const subscriber = rootState.openvidu.subscribers[j];
        if (state.seatList[i].userName === subscriber.stream.connection.data) {
          commit('ADD_SUBSCRIBER_INTO_SEAT', { index: i, subscriber });
          continue loop;
        }
      }
      commit('ADD_SUBSCRIBER_INTO_SEAT', { index: i, undefined });
    }
  },
  async SEND_SEAT_ALLOCATED({ state, rootState, dispatch }, { seatNo }) {
    const hasAlreadySeat = await dispatch('HAS_ALREADY_SEAT');
    if (hasAlreadySeat) {
      return Promise.reject('이미 앉아있는 좌석이 있습니다. 자리에서 일어나서 시도해주세요.');
    } else {
      try {
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
      } catch (error) {
        return Promise.reject(error);
      }
    }
  },
  async SEND_SEAT_END({ state, rootState, dispatch }) {
    // 카메라 ON/OFF 끄는 것 비동기로 되도록?
    await dispatch('CAMERA_OFF', { root: true });
    try {
      state.stomp.send(
        `/pub/room/message`,
        JSON.stringify({
          type: ROOM_MESSAGE_SEAT_END,
          roomId: state.roomInfo.roomId,
        }),
        { token: rootState.user.userInfo.authToken }
      );
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  },
  ADD_SUBSCRIBER_INTO_SEAT_LIST({ state, commit }, subscriber) {
    console.log(
      '%croom.js line:119 subscriber.stream.connection.data',
      'color: #007acc;',
      subscriber
    );

    for (let i = 0; i < state.seatList.length; i++) {
      if (state.seatList[i] && state.seatList[i].userName === subscriber.stream.connection.data) {
        console.log('%croom.js line:121 매칭!!', 'color: #007acc;');
        commit('ADD_SUBSCRIBER_INTO_SEAT', { index: i, subscriber });
        break;
      }
    }
  },
  HAS_ALREADY_SEAT({ state, rootState }) {
    for (let i = 0; i < state.seatList.length; i++) {
      if (state.seatList[i] && state.seatList[i].userName === rootState.user.userInfo.userName) {
        return true;
      }
    }
    return false;
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
  CLEAR_CONNECT(state) {
    // TODO : 백엔드와 연동 필요
    state.stomp.disconnect();
    state.socket.close();
    state.stomp = undefined;
    state.socket = undefined;
  },
  SET_ROOM_INFO(state, payload) {
    state.roomInfo = payload;
    state.seatList = new Array(payload.limitUserCount);
  },
  UPDATE_ROOM_INFO(state, { key, value }) {
    state.roomInfo[key] = value;
  },
  ADD_SEAT_INFO(state, { index, seatInfo }) {
    const tmp = [...state.seatList];
    tmp.splice(index, 1, seatInfo);
    state.seatList = tmp;
  },
  ADD_SUBSCRIBER_INTO_SEAT(state, { index, subscriber }) {
    // TODO : 매번 깊은복사해서 바꿔주는 과정 별로 안좋아보임
    console.log('%croom.js line:144 add!!', 'color: #007acc;');
    const tmp = [...state.seatList];
    tmp[index].subscriber = subscriber;
    state.seatList = tmp;
    // state.seatList[index].subscriber = subscriber;
    console.log('%croom.js line:146 ', 'color: #007acc;', state.seatList[index]);
  },
  SET_SELECTED_SEAT_INFO(state, payload) {
    state.selectedSeatInfo = payload;
  },
  SET_USER_ROOM_STATE(state, payload) {
    state.userRoomState = payload;
  },
  REMOVE_SEAT_INFO(state, { index }) {
    const tmp = [...state.seatList];
    tmp.splice(index, 1, undefined);
    state.seatList = tmp;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
