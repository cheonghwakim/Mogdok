import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { getSeatList, getAllRooms, hasAlreadyEntered } from '../../api/room';
import { getProfileByUserName } from '../../api/user';

const ROOM_MESSAGE_SEAT_ALLOCATED = 'SEAT_ALLOCATED';
const ROOM_MESSAGE_SEAT_ALLOCATE_FAIL = 'SEAT_ALLOCATE_FAIL';
const ROOM_MESSAGE_SEAT_STATUS = 'SEAT_STATUS';
const ROOM_MESSAGE_SEAT_ENTER = 'ENTER';
const ROOM_MESSAGE_SEAT_END = 'END';
const ROOM_MESSAGE_SEAT_QUIT = 'QUIT';
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

function initSeatInfo(seatInfo) {
  seatInfo.userName = seatInfo.sender;
  seatInfo.isRunning = false;
  seatInfo.time = '00:00:00';
  seatInfo.timer = null;
  seatInfo.timeBegan = null;
  seatInfo.timeStopped = null;
  seatInfo.stoppedDuration = 0;
  return seatInfo;
}

function zeroPrefix(num, digit) {
  var zero = '';
  for (var i = 0; i < digit; i++) {
    zero += '0';
  }
  return (zero + num).slice(-digit);
}

function getStoppedDuration(seatInfo) {
  let stoppedDuration = 0;
  // 현재시간(UTC)
  var currentTime = new Date();
  const currentUTCTime = new Date(
    currentTime.getTime() + currentTime.getTimezoneOffset() * 60 * 1000
  ).getTime();

  for (var i = seatInfo.timestampList.length - 1; i >= 0; --i) {
    seatInfo.timestampList[i].time = new Date(seatInfo.timestampList[i].time).getTime();
    if (seatInfo.timestampList[i].type === 'PAUSE') {
      // 휴식시간인 경우
      // 현재 i가 마지막 인덱스인 경우 ------> 현재시간에서 차감
      if (i === seatInfo.timestampList.length - 1) {
        stoppedDuration += currentUTCTime - seatInfo.timestampList[i].time;
      } else {
        // 중간에 있는 인덱스인 경우 ------> 이전 시간에서 차감
        stoppedDuration += seatInfo.timestampList[i + 1].time - seatInfo.timestampList[i].time;
      }
    }
  }
  return stoppedDuration;
}

function getCurTime(seatInfo) {
  var currentTime = new Date();
  var currentUTCTime = new Date(
    currentTime.getTime() + currentTime.getTimezoneOffset() * 60 * 1000
  ).getTime();
  var timeElapsed = new Date(currentUTCTime - seatInfo.timeBegan - seatInfo.stoppedDuration);
  var hour = timeElapsed.getUTCHours();
  var min = timeElapsed.getUTCMinutes();
  var sec = timeElapsed.getUTCSeconds();
  return zeroPrefix(hour, 2) + ':' + zeroPrefix(min, 2) + ':' + zeroPrefix(sec, 2);
}

const state = () => ({
  socket: undefined,
  stomp: undefined,
  roomList: undefined,
  seatList: [],
  timeList: undefined,
  selectedSeatInfo: undefined,
  userRoomState: ROOM_STUDY_TYPE_NO_ACTION,
  userSeatIndex: -1,
  roomUserCount: 0,
});

const getters = {};

const actions = {
  async HAS_ALREADY_ENTERED({ rootState }) {
    await hasAlreadyEntered(
      { token: rootState.user.userInfo.authToken },
      () => {
        return Promise.resolve();
      },
      (error) => {
        if (error.response.status === 409)
          return Promise.reject(
            '😱 이미 접속된 계정이 있어요. 접속된 계정을 종료하고 다시 시도해주세요.'
          );
        else return Promise.reject(error);
      }
    );
  },
  async GET_ALL_ROOMS({ commit }) {
    await getAllRooms(
      (res) => {
        commit('SET_ALL_ROOMS', res.data);
        return Promise.resolve();
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  },
  ENTER_ROOM({ commit }, room) {
    commit('SET_ROOM_INFO', room, { root: true });
    commit('INIT_ROOM', room);
  },
  CONNECT_ROOM_SERVER({ state, rootState, commit }) {
    commit('INIT_CONNECT');
    return new Promise((resolve, reject) => {
      state.stomp.connect(
        { token: rootState.user.userInfo.authToken },
        () => {
          resolve('');
        },
        (error) => {
          reject(
            '⛔️ 연결을 실패했어요. 이미 접속된 계정이 있을 수 있습니다. 기존 계정을 종료하고 다시 시도하거나 다음 내용을 참고하세요.\n[Error]' +
              error
          );
        }
      );
    });
  },
  async GET_SEAT_INFO({ state, rootState, commit, dispatch }) {
    await getSeatList(
      { roomId: rootState.user.roomInfo.roomId },
      async (res) => {
        await res.data.forEach(async (seatInfo) => {
          seatInfo.isRunning = false;
          seatInfo.timer = null;
          seatInfo.timeBegan = new Date(seatInfo.timestampList[0].time).getTime(); // 시작시간 세팅;
          seatInfo.timeStopped = null;
          seatInfo.stoppedDuration = getStoppedDuration(seatInfo);
          state.timeList[seatInfo.seatNo - 1] = getCurTime(seatInfo);
          const index = seatInfo.seatNo - 1;
          commit('ADD_SEAT_INFO', { index, seatInfo });
          await dispatch('INIT_SEAT_INFO_BY_STATUS', seatInfo);
        });
        return Promise.resolve();
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  },
  HAS_ALREADY_SEAT({ state, rootState }) {
    for (let i = 0; i < state.seatList.length; i++) {
      if (state.seatList[i] && state.seatList[i].userName === rootState.user.userInfo.userName) {
        return true;
      }
    }
    return false;
  },

  SUBSCRIBE_ROOM_SERVER({ state, rootState, commit, dispatch }) {
    state.stomp.subscribe(
      `/sub/room/${rootState.user.roomInfo.roomId}`,
      (message) => {
        let res = JSON.parse(message.body);
        // TODO: 각 사용자의 변경된 상태를 적용해야함
        if (res.type === ROOM_MESSAGE_SEAT_ENTER) {
          if (res.sender === rootState.user.userInfo.userName) {
            commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_NO_ACTION);
          }
        } else if (res.type === ROOM_MESSAGE_SEAT_ALLOCATED) {
          // 자리앉기
          res = initSeatInfo(res);
          const index = res.seatInfo.seatNo - 1;
          commit('ADD_SEAT_INFO', { index, seatInfo: res });
          commit('ADD_TIMER_BY_INDEX', { index });
          commit('UPDATE_ROOM_USER_COUNT', res.userCount);
          if (res.sender === rootState.user.userInfo.userName) {
            // 내가 앉았을 경우
            commit('SET_USER_ROOM_STATE', res.seatInfo.studyType); // 유저 상태를 변경함
            commit('SET_USER_SEAT_INDEX', { index });
          }
        } else if (res.type === ROOM_MESSAGE_SEAT_STATUS) {
          res = initSeatInfo(res);
          if (res.sender === rootState.user.userInfo.userName) {
            commit('SET_USER_ROOM_STATE', res.seatInfo.studyType);
          }
          dispatch('UPDATE_SEAT_INFO_BY_STATUS', res);
        } else if (res.type === ROOM_MESSAGE_SEAT_ALLOCATE_FAIL) {
          if (res.sender === rootState.user.userInfo.userName) {
            alert('자리에 앉을 수 없어요!');
            commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_NO_ACTION);
            commit('SET_USER_SEAT_INDEX', { index: -1 });
          }
        } else if (res.type === ROOM_MESSAGE_SEAT_END) {
          // 자리 떠나기
          const index = res.seatInfo.seatNo - 1;
          dispatch('PAUSE_TIMER_BY_INDEX', index);
          commit('UPDATE_ROOM_USER_COUNT', res.userCount);
          commit('STOP_SEAT_INFO_TIMER', { index });
          commit('REMOVE_SEAT_INFO', { index });
          commit('REMOVE_TIMER_BY_INDEX', { index });
          if (res.sender === rootState.user.userInfo.userName) {
            commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_NO_ACTION);
            commit('SET_USER_SEAT_INDEX', { index: -1 });
          }
        } else if (res.type === ROOM_MESSAGE_SEAT_QUIT) {
          // 소켓마저 끊김
          const index = res.seatInfo.seatNo - 1;
          dispatch('PAUSE_TIMER_BY_INDEX', index);
          commit('UPDATE_ROOM_USER_COUNT', res.userCount);
          commit('STOP_SEAT_INFO_TIMER', { index });
          commit('REMOVE_SEAT_INFO', { index });
          commit('REMOVE_TIMER_BY_INDEX', { index });
          if (res.sender === rootState.user.userInfo.userName) {
            commit('SET_USER_ROOM_STATE', ROOM_STUDY_TYPE_NO_ACTION);
            commit('SET_USER_SEAT_INDEX', { index: -1 });
          }
        }
      },
      (error) => {
        alert('열람실 좌석 정보를 가져오는데 실패했어요. ' + error);
      }
    );
  },
  CONNECT_ROOM_WITH_OPENVIDU({ state, rootState, commit }) {
    // userName을 이용해서 자리 매칭
    loop: for (let i = 0; i < state.seatList.length; i++) {
      if (!state.seatList[i] || !rootState.openvidu.subscribers) continue;
      for (let j = 0; j < rootState.openvidu.subscribers.length; j++) {
        const subscriber = rootState.openvidu.subscribers[j];
        if (state.seatList[i].userName === subscriber.stream.connection.data) {
          commit('ADD_SUBSCRIBER_INTO_SEAT', { index: i, subscriber });
          continue loop;
        }
      }
      commit('ADD_SUBSCRIBER_INTO_SEAT', { index: i, undefined });
    }
    // 화면 갱신
    commit('REFRESH_SEAT_LIST');
  },
  async SEND_SEAT_ALLOCATED({ state, rootState, dispatch }, { seatNo }) {
    const hasAlreadySeat = await dispatch('HAS_ALREADY_SEAT');
    if (hasAlreadySeat) {
      return Promise.reject('✋ 이미 앉아있는 좌석이 있어요.\n자리에서 일어나서 시도해주세요.');
    } else {
      try {
        state.stomp.send(
          `/pub/room/message`,
          JSON.stringify({
            type: ROOM_MESSAGE_SEAT_ALLOCATED,
            roomId: rootState.user.roomInfo.roomId,
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
    // 카메라 ON/OFF 끄는 것 비동기
    await dispatch('CAMERA_OFF', { root: true });
    try {
      state.stomp.send(
        `/pub/room/message`,
        JSON.stringify({
          type: ROOM_MESSAGE_SEAT_END,
          roomId: rootState.user.roomInfo.roomId,
        }),
        { token: rootState.user.userInfo.authToken }
      );
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  },
  async GET_SELECTED_SEAT_USER_INFO({ commit }, seat) {
    await getProfileByUserName(
      { userName: seat.userName },
      (res) => {
        seat.promise = res.data.promise;
        seat.category = res.data.category;
        seat.deskId = res.data.deskId;
        commit('SET_SELECTED_SEAT_INFO', seat);
        return Promise.resolve();
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  },
  INIT_SEAT_INFO_BY_STATUS({ commit, dispatch }, seat) {
    const index = seat.seatNo - 1;
    const value = seat.studyType;
    commit('UPDATE_SEAT_INFO', {
      index,
      key: 'studyType',
      value,
    });
    if (value === ROOM_STUDY_TYPE_START) {
      dispatch('START_TIMER_BY_INDEX', index);
    } else if (value === ROOM_STUDY_TYPE_PAUSE) {
      dispatch('PAUSE_TIMER_BY_INDEX', index);
    }
  },
  UPDATE_SEAT_INFO_BY_STATUS({ commit, dispatch }, seat) {
    const index = seat.seatInfo.seatNo - 1;
    const value = seat.seatInfo.studyType;
    commit('UPDATE_SEAT_INFO', {
      index,
      key: 'studyType',
      value,
    });
    if (value === ROOM_STUDY_TYPE_START) {
      dispatch('START_TIMER_BY_INDEX', index);
    } else if (value === ROOM_STUDY_TYPE_PAUSE) {
      dispatch('PAUSE_TIMER_BY_INDEX', index);
    }
  },
  START_TIMER_BY_INDEX({ state, commit }, index) {
    if (state.seatList[index].isRunning) {
      return;
    }
    const currentTime = new Date();
    if (state.seatList[index].timeBegan === null) {
      commit('UPDATE_SEAT_INFO', {
        index,
        key: 'timeBegan',
        value: new Date(
          currentTime.getTime() + currentTime.getTimezoneOffset() * 60 * 1000
        ).getTime(),
      });
    }
    if (state.seatList[index].timeStopped !== null) {
      commit('UPDATE_SEAT_INFO', {
        index,
        key: 'stoppedDuration',
        value:
          state.seatList[index].stoppedDuration +
          new Date(currentTime.getTime() + currentTime.getTimezoneOffset() * 60 * 1000).getTime() -
          state.seatList[index].timeStopped,
      });
    }

    commit('SET_SEAT_INFO_TIMER', {
      index,
      timer: setInterval(() => {
        const currentTime = new Date();
        const currentUTCTime = new Date(
          currentTime.getTime() + currentTime.getTimezoneOffset() * 60 * 1000
        ).getTime();
        const timeElapsed = new Date(
          currentUTCTime - state.seatList[index].timeBegan - state.seatList[index].stoppedDuration
        );
        const hour = timeElapsed.getUTCHours();
        const min = timeElapsed.getUTCMinutes();
        const sec = timeElapsed.getUTCSeconds();
        commit('RUN_SEAT_INFO_TIMER', {
          index,
          time: zeroPrefix(hour, 2) + ':' + zeroPrefix(min, 2) + ':' + zeroPrefix(sec, 2),
        });
      }, 1000),
    });

    state.seatList[index].isRunning = true;
  },
  PAUSE_TIMER_BY_INDEX({ state, commit }, index) {
    commit('UPDATE_SEAT_INFO', {
      index,
      key: 'isRunning',
      value: false,
    });
    const currentTime = new Date();
    const currentUTCTime = new Date(
      currentTime.getTime() + currentTime.getTimezoneOffset() * 60 * 1000
    ).getTime();
    commit('UPDATE_SEAT_INFO', {
      index,
      key: 'timeStopped',
      value: currentUTCTime,
    });
    if (state.seatList[index] && state.seatList[index].timer != null) {
      clearInterval(state.seatList[index].timer);
      commit('STOP_SEAT_INFO_TIMER', { index });
    }
  },
  async SEND_STUDY_START({ state, rootState }) {
    try {
      state.stomp.send(
        `/pub/room/message`,
        JSON.stringify({
          type: ROOM_MESSAGE_SEAT_STATUS,
          roomId: rootState.user.roomInfo.roomId,
          seatInfo: {
            studyType: ROOM_STUDY_TYPE_START,
          },
        }),
        { token: rootState.user.userInfo.authToken }
      );
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  },
  async SEND_STUDY_PAUSE({ state, rootState }) {
    try {
      state.stomp.send(
        `/pub/room/message`,
        JSON.stringify({
          type: ROOM_MESSAGE_SEAT_STATUS,
          roomId: rootState.user.roomInfo.roomId,
          seatInfo: {
            studyType: ROOM_STUDY_TYPE_PAUSE,
          },
        }),
        { token: rootState.user.userInfo.authToken }
      );
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  },
};

const mutations = {
  SET_ALL_ROOMS(state, payload) {
    state.roomList = payload;
  },
  INIT_ROOM(state, payload) {
    state.seatList = new Array(payload.limitUserCount);
    state.timeList = Array.from({ length: payload.limitUserCount }, () => '');
    state.roomUserCount = payload.userCount;
  },
  INIT_CONNECT(state) {
    state.socket = new SockJS(process.env.VUE_APP_BASE_URL_ROOM_CONNECT);
    state.stomp = Stomp.over(state.socket);
  },
  CLEAR_CONNECT(state) {
    for (let i = 0; i < state.seatList.length; i++) {
      if (state.seatList[i]) {
        clearInterval(state.seatList[i].timer);
        state.seatList[i].timer = undefined;
      }
    }
    state.stomp.disconnect();
    state.socket.close();
    state.stomp = undefined;
    state.socket = undefined;
  },
  UPDATE_ROOM_USER_COUNT(state, payload) {
    state.roomUserCount = payload;
  },
  ADD_SEAT_INFO(state, { index, seatInfo }) {
    const tmp = [...state.seatList];
    tmp.splice(index, 1, seatInfo);
    state.seatList = tmp;
  },
  ADD_SUBSCRIBER_INTO_SEAT(state, { index, subscriber }) {
    state.seatList[index].subscriber = subscriber;
  },
  REFRESH_SEAT_LIST(state) {
    const tmp = [...state.seatList];
    state.seatList = tmp;
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
  UPDATE_SEAT_INFO(state, { index, key, value }) {
    if (state.seatList[index]) state.seatList[index][key] = value;
  },
  SET_SEAT_INFO_TIMER(state, { index, timer }) {
    // 객체변경말고 배열자체 변경되면 반영되는지 한번 확인해보자
    state.seatList[index].timer = timer;
  },
  RUN_SEAT_INFO_TIMER(state, { index, time }) {
    const tmp = { ...state.timeList };
    tmp[index] = time;
    state.timeList = tmp;
  },
  STOP_SEAT_INFO_TIMER(state, { index }) {
    if (!state.seatList[index]) return;
    clearInterval(state.seatList[index].timer);
    state.seatList[index].timer = undefined;
  },
  CLEAR_ALL_TIMER(state) {
    state.seatList.forEach((e) => {
      if (e) e.timer = undefined;
    });
  },
  ADD_TIMER_BY_INDEX(state, { index }) {
    state.timeList[index] = '00:00:00';
  },
  REMOVE_TIMER_BY_INDEX(state, { index }) {
    state.timeList[index] = '';
  },
  SET_USER_SEAT_INDEX(state, { index }) {
    state.userSeatIndex = index;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};
