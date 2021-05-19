import { OpenVidu } from 'openvidu-browser';
import { createSession, createToken } from '../../api/openvidu';

async function getDeviceList(device) {
   //console.log('🏆 getDeviceList');
   const videoSoruces = [];
   videoSoruces.push({ kind: 'videoinput', label: 'default', deviceId: undefined });
   device.forEach((e) => {
      if (e.kind === 'videoinput' && e.label) {
         videoSoruces.push(e);
      }
   });

   //console.log('🏆 getDeviceList FIN');
   return Promise.resolve(videoSoruces);
}

const state = () => ({
   OV: undefined,
   session: undefined,
   publisher: undefined,
   subscribers: [],
   isPublished: false,
});

const getters = {};

const actions = {
   async SET_VIDEO_SOURCE_LIST({ state, commit }) {
      //console.log('🏆 SET_VIDEO_SOURCE_LIST');
      await state.OV.getDevices()
         .then(async (res) => {
            const videoSoruces = await getDeviceList(res);
            //console.log('🏆 > ', videoSoruces);
            commit('SET_VIDEO_SOURCE_LIST', videoSoruces, { root: true });
            //console.log('🏆 SET_VIDEO_SOURCE_LIST FIN.');
            return Promise.resolve();
         })
         .catch((error) => {
            return Promise.reject(error);
         });
   },
   async INIT_OV_SESSION_EVENT({ state, commit }) {
      state.session.on('streamCreated', ({ stream }) => {
         const subscriber = state.session.subscribe(stream);
         commit('ADD_SUBSCRIBER', subscriber);
      });
      state.session.on('streamDestroyed', ({ stream }) => {
         if (!state.subscribers) return;
         const index = state.subscribers.indexOf(stream.streamManager, 0);
         if (index >= 0) commit('REMOVE_SUBSCRIBER', index);
      });
      // 메시지 시그널
      // state.session.on('signal:message', (signalEvent) => {
      //   this.receivedMessages.push(signalEvent);
      // });
   },
   CONNECT_USER_TO_SESSION({ state, rootState, dispatch }, { userId, userName }) {
      dispatch('GET_TOKEN', rootState.user.roomInfo.roomId).then((token) => {
         state.session
            .connect(token, `${userId}##${userName}`) // Todo: 현재 로그인 중인 유저이름 (userId##userName)
            .then(() => {
               console.log('세션에 참가했습니다');
            })
            .catch((error) => {
               console.log('세션 연결에 실패했습니다.', error.code, error.message);
            });
      });
   },
   GET_TOKEN({ dispatch }) {
      return dispatch('CREATE_SESSION').then(() => dispatch('CREATE_TOKEN'));
   },
   CREATE_SESSION({ rootState }) {
      return new Promise((resolve, reject) => {
         createSession(
            { sessionId: rootState.user.roomInfo.roomId },
            (res) => res.data,
            (data) => {
               console.log('세션 생성 성공');
               resolve(data.id);
            },
            (error) => {
               if (error.response && error.response.status === 409) {
                  resolve(rootState.user.roomInfo.roomId);
               } else {
                  // 에러처리
                  alert('createSession ERROR' + error);
                  reject(error.response);
               }
            }
         );
      });
   },
   CREATE_TOKEN({ rootState }) {
      return new Promise((resolve, reject) => {
         createToken(
            { sessionId: rootState.user.roomInfo.roomId },
            (res) => res.data,
            (data) => resolve(data.token),
            (error) => reject(error.response)
         );
      });
   },
   async PUBLISH_VIDEO_TO_SESSION({ state, commit }) {
      // 현재 접속중인 세션에 영상을 publish 함
      // publish에 성공하면, 내 캠화면을 내가 선택한 element에서 보이도록 함
      await state.session
         .publish(state.publisher)
         .then(() => {
            commit('ADD_SUBSCRIBER', state.publisher);
            commit('SET_PUBLISHED', true);
            return Promise.resolve();
         })
         .catch((error) => {
            console.log('%copenvidu.js line:102 error', 'color: #007acc;', error);
            commit('SET_PUBLISHED', false);
            return Promise.reject('영상 공유를 실패했어요.');
         });
   },
   async CAMERA_ON({ state, rootState, commit }) {
      // [비동기] 카메라 켜기
      // --- 카메라를 통해 스트림할 데이터의 속성을 초기화한다 ---
      console.log(rootState.user.videoSource);
      await state.OV.initPublisherAsync(undefined, {
         audioSource: false, // 오디오소스. If undefined default microphone
         videoSource: rootState.user.videoSource, // 비디오소스. If undefined default webcam
         publishAudio: false, // Whether you want to start publishing with your audio unmuted or not
         publishVideo: true, // Whether you want to start publishing with your video enabled or not
         resolution: '320x240', // The resolution of your video
         frameRate: 30, // 프레임. The frame rate of your video
         insertMode: 'APPEND', // How the video is inserted in the target element 'video-container'
         mirror: false, // Whether to mirror your local video or not
      })
         .then((publisher) => {
            commit('SET_PUBLISHER', publisher);
            return Promise.resolve();
         })
         .catch(() => {
            commit('SET_VIDEO_SOURCE', rootState.user.videoSourceList[0], { root: true });
            return Promise.reject('다시 시도해주세요.');
         });
   },
   async CAMERA_OFF({ state, commit }) {
      // 카메라 끄기
      // if (this.session && this.isPublished) {
      if (state.session && state.isPublished) {
         // 세션에 들어가있고 "publish 중인 상태"에서는 unpublish 해야 함
         // unpublish는 카메라 자원 해제까지 해주는 메서드 존재
         await state.session.unpublish(state.publisher);
         const index = state.subscribers.indexOf(state.publisher, 0);
         if (index >= 0) commit('REMOVE_SUBSCRIBER', index);
      } else {
         // 세션을 통해 unpublish하지 않은 경우 카메라를 OFF만 하면,
         // 카메라 자원이 여전히 실행 중이게 되므로 카메라 자원을 해제하는 작업을 해주어야 함
         if (state.publisher) await state.publisher.stream.disposeMediaStream();
      }
      commit('SET_PUBLISHED', false);
      commit('SET_PUBLISHER', undefined);
   },
   async LEAVE_SESSION({ state, commit, dispatch }) {
      if (state.session) state.session.disconnect();
      await dispatch('CAMERA_OFF');
      commit('CLEAR_SESSION');
   },
   async CHANGE_CAMERA({ dispatch }) {
      await dispatch('CAMERA_OFF');
      await dispatch('CAMERA_ON');
   },
};

const mutations = {
   CREATE_OPENVIDU(state) {
      state.OV = new OpenVidu();
   },
   SET_SESSION(state, payload) {
      state.session = payload;
   },
   SET_PUBLISHER(state, payload) {
      state.publisher = payload;
   },
   ADD_SUBSCRIBER(state, payload) {
      state.subscribers.push(payload);
   },
   SET_SUBSCRIBERS(state, payload) {
      console.log('%copenvidu.js line:163 payload', 'color: #007acc;', payload);
      state.subscribers = [];
   },
   REMOVE_SUBSCRIBER(state, payload) {
      state.subscribers.splice(payload, 1);
   },
   REMOVE_AND_ADD_SUBSCRIBER(state, payload) {
      state.subscribers.splice(payload, 1, state.publisher);
   },
   CLEAR_SESSION(state) {
      state.OV = undefined;
      state.session = undefined;
      state.publisher = undefined;
      state.subscribers = undefined;
   },
   SET_PUBLISHED(state, payload) {
      state.isPublished = payload;
   },
};

export default {
   state,
   getters,
   actions,
   mutations,
};
