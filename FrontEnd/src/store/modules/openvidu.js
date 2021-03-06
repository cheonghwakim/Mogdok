import { OpenVidu } from 'openvidu-browser';
import { createSession, createToken } from '../../api/openvidu';

async function getDeviceList(device) {
  //console.log('π getDeviceList');
  const videoSoruces = [];
  videoSoruces.push({ kind: 'videoinput', label: 'default', deviceId: undefined });
  device.forEach((e) => {
    if (e.kind === 'videoinput' && e.label) {
      videoSoruces.push(e);
    }
  });

  //console.log('π getDeviceList FIN');
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
    //console.log('π SET_VIDEO_SOURCE_LIST');
    await state.OV.getDevices()
      .then(async (res) => {
        const videoSoruces = await getDeviceList(res);
        //console.log('π > ', videoSoruces);
        commit('SET_VIDEO_SOURCE_LIST', videoSoruces, { root: true });
        //console.log('π SET_VIDEO_SOURCE_LIST FIN.');
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
    // λ©μμ§ μκ·Έλ
    // state.session.on('signal:message', (signalEvent) => {
    //   this.receivedMessages.push(signalEvent);
    // });
  },
  CONNECT_USER_TO_SESSION({ state, rootState, dispatch }, { userId, userName }) {
    dispatch('GET_TOKEN', rootState.user.roomInfo.roomId).then((token) => {
      state.session
        .connect(token, `${userId}##${userName}`) // Todo: νμ¬ λ‘κ·ΈμΈ μ€μΈ μ μ μ΄λ¦ (userId##userName)
        .then(() => {})
        .catch((error) => {
          alert('μΈμ μ°κ²°μ μ€ν¨νμ΅λλ€.' + error.code + ':' + error.message);
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
          resolve(data.id);
        },
        (error) => {
          if (error.response && error.response.status === 409) {
            resolve(rootState.user.roomInfo.roomId);
          } else {
            // μλ¬μ²λ¦¬
            alert('π­ μΈμ μμ±μ μ€ν¨νμ΄μ. ' + error);
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
    // νμ¬ μ μμ€μΈ μΈμμ μμμ publish ν¨
    // publishμ μ±κ³΅νλ©΄, λ΄ μΊ νλ©΄μ λ΄κ° μ νν elementμμ λ³΄μ΄λλ‘ ν¨
    await state.session
      .publish(state.publisher)
      .then(() => {
        commit('ADD_SUBSCRIBER', state.publisher);
        commit('SET_PUBLISHED', true);
        return Promise.resolve();
      })
      .catch(() => {
        commit('SET_PUBLISHED', false);
        return Promise.reject('μμ κ³΅μ λ₯Ό μ€ν¨νμ΄μ.\nλμμ€ μκ΅¬μμ FAQλ₯Ό νμΈν΄μ£ΌμΈμ.');
      });
  },
  async CAMERA_ON({ state, rootState, commit }) {
    // [λΉλκΈ°] μΉ΄λ©λΌ μΌκΈ°
    // --- μΉ΄λ©λΌλ₯Ό ν΅ν΄ μ€νΈλ¦Όν  λ°μ΄ν°μ μμ±μ μ΄κΈ°ννλ€ ---
    await state.OV.initPublisherAsync(undefined, {
      audioSource: false, // μ€λμ€μμ€. If undefined default microphone
      videoSource: rootState.user.videoSource, // λΉλμ€μμ€. If undefined default webcam
      publishAudio: false, // Whether you want to start publishing with your audio unmuted or not
      publishVideo: true, // Whether you want to start publishing with your video enabled or not
      resolution: '320x240', // The resolution of your video
      frameRate: 10, // νλ μ. The frame rate of your video
      insertMode: 'APPEND', // How the video is inserted in the target element 'video-container'
      mirror: false, // Whether to mirror your local video or not
    })
      .then((publisher) => {
        commit('SET_PUBLISHER', publisher);
        return Promise.resolve();
      })
      .catch(() => {
        commit('SET_VIDEO_SOURCE', rootState.user.videoSourceList[0], { root: true });
        return Promise.reject('λ€μ μλν΄μ£ΌμΈμ.');
      });
  },
  async CAMERA_OFF({ state, commit }) {
    // μΉ΄λ©λΌ λκΈ°
    // if (this.session && this.isPublished) {
    if (state.session && state.isPublished) {
      // μΈμμ λ€μ΄κ°μκ³  "publish μ€μΈ μν"μμλ unpublish ν΄μΌ ν¨
      // unpublishλ μΉ΄λ©λΌ μμ ν΄μ κΉμ§ ν΄μ£Όλ λ©μλ μ‘΄μ¬
      await state.session.unpublish(state.publisher);
      if (!state.subscribers) return;
      const index = state.subscribers.indexOf(state.publisher, 0);
      if (index >= 0) commit('REMOVE_SUBSCRIBER', index);
    } else {
      // μΈμμ ν΅ν΄ unpublishνμ§ μμ κ²½μ° μΉ΄λ©λΌλ₯Ό OFFλ§ νλ©΄,
      // μΉ΄λ©λΌ μμμ΄ μ¬μ ν μ€ν μ€μ΄κ² λλ―λ‘ μΉ΄λ©λΌ μμμ ν΄μ νλ μμμ ν΄μ£Όμ΄μΌ ν¨
      if (state.publisher) await state.publisher.stream.disposeMediaStream();
    }
    commit('SET_PUBLISHED', false);
    commit('SET_PUBLISHER', undefined);
  },
  async LEAVE_SESSION({ state, commit, dispatch }) {
    await dispatch('CAMERA_OFF');
    if (state.session) state.session.disconnect();
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
    if (state.subscribers) state.subscribers.push(payload);
  },
  //   SET_SUBSCRIBERS(state, payload) {
  //     state.subscribers = [];
  //   },
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
    state.subscribers = [];
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
