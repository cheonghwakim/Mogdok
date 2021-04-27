<template>
  <div id="main-container" class="container">
    <div id="join" v-if="!session">
      <div id="img-div"><img src="resources/images/openvidu_grey_bg_transp_cropped.png" /></div>
      <div id="join-dialog" class="jumbotron vertical-center">
        <h1>Join a video session</h1>
        <div class="form-group">
          <p>
            <label>Participant</label>
            <input v-model="myUserName" class="form-control" type="text" required />
          </p>
          <p>
            <label>Session</label>
            <input v-model="mySessionId" class="form-control" type="text" required />
          </p>
          <div id="main-video" class="col-md-6">
            <h3>입장 전에 내 화면을 확인하세요</h3>
            <div id="beforeJoinCam" ref="beforeJoinCam"></div>
            <user-video :stream-manager="beforeJoin" />
          </div>
          <p class="text-center">
            <button class="btn btn-lg btn-success" @click="joinSession()">Join!</button>
          </p>
        </div>
      </div>
    </div>
    <div id="session" v-if="session">
      <input
        class="btn btn-large btn-danger"
        type="button"
        id="buttonLeaveSession"
        @click="leaveSession"
        value="세션에서 나가기"
        style="backgroundColor:red;color:white"
      />
      <h4>
        세션에 접속하려면 Camera ON 버튼을 클릭해서 내 모습을 확인한 뒤 "세션에 접속하기"버튼을
        클릭해주세요
      </h4>
      <div>
        <div v-if="publisher">
          <ov-video :stream-manager="publisher" />
        </div>
        <button class="btn btn-lg btn-success" @click="cameraState ? cameraOff() : cameraOn()">
          Camera
          {{ cameraState ? 'OFF' : 'ON' }}
        </button>
        <br />
        <button v-if="!isPublished" :disabled="!cameraState" @click="publishSession()">
          세션에 접속하기
        </button>
      </div>
      <div id="session-header">
        <h1 id="session-title">{{ mySessionId }}</h1>
      </div>
      <div id="video-container" class="col-md-6">
        <user-video
          v-if="isPublished"
          :stream-manager="publisher"
          @click.native="updateMainVideoStreamManager(publisher)"
        />
        <user-video
          v-for="sub in subscribers"
          :key="sub.stream.connection.connectionId"
          :stream-manager="sub"
          @click.native="updateMainVideoStreamManager(sub)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import UserVideo from '../components/UserVideo';
import OvVideo from '../components/OvVideo';
axios.defaults.headers.post['Content-Type'] = 'application/json';
const OPENVIDU_SERVER_URL = 'https://k4a401.p.ssafy.io';
const OPENVIDU_SERVER_SECRET = 'ssafy';
export default {
  name: 'App',
  components: {
    UserVideo,
    OvVideo,
  },
  data() {
    return {
      cameraState: false,
      isPublished: false,
      OV: undefined,
      session: undefined,
      beforeJoin: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      mySessionId: 'SessionA',
      myUserName: 'Participant' + Math.floor(Math.random() * 100),
    };
  },
  methods: {
    joinSession() {
      // --- Get an OpenVidu object ---
      if (!this.OV) this.OV = new OpenVidu();
      // --- Init a session ---
      this.session = this.OV.initSession();
      // --- Specify the actions when events take place in the session ---
      // On every new Stream received...
      this.session.on('streamCreated', ({ stream }) => {
        console.log('%cOpenVidu.vue line:97 stream', 'color: #007acc;', stream);
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });
      // On every Stream destroyed...
      this.session.on('streamDestroyed', ({ stream }) => {
        console.log('%cOpenVidu.vue line:102 ', 'color: #007acc;', stream);
        console.log('%cOpenVidu.vue line:104 ', 'color: #007acc;', stream.connection.data);
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });
      // --- Connect to the session with a valid user token ---
      // 'getToken' method is simulating what your server-side should do.
      // 'token' parameter should be retrieved and returned by your own backend
      this.getToken(this.mySessionId).then((token) => {
        this.session
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            // 세션에 성공적으로 입장
            console('세션에 참가했습니다');
          })
          .catch((error) => {
            console.log('There was an error connecting to the session:', error.code, error.message);
          });
      });
      window.addEventListener('beforeunload', this.leaveSession);
    },
    leaveSession() {
      // --- Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();
      this.cameraOff();
      this.session = undefined;
      this.mainStreamManager = undefined;
      this.subscribers = [];
      this.OV = undefined;
      window.removeEventListener('beforeunload', this.leaveSession);
    },
    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },
    getToken(mySessionId) {
      return this.createSession(mySessionId).then((sessionId) => this.createToken(sessionId));
    },
    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessions
    createSession(sessionId) {
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions`,
            JSON.stringify({
              customSessionId: sessionId,
            }),
            {
              auth: {
                username: 'OPENVIDUAPP',
                password: OPENVIDU_SERVER_SECRET,
              },
            }
          )
          .then((response) => response.data)
          .then((data) => resolve(data.id))
          .catch((error) => {
            if (error.response.status === 409) {
              resolve(sessionId);
            } else {
              console.warn(
                `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}`
              );
              if (
                window.confirm(
                  `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`
                )
              ) {
                location.assign(`${OPENVIDU_SERVER_URL}/accept-certificate`);
              }
              reject(error.response);
            }
          });
      });
    },
    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessionsltsession_idgtconnection
    createToken(sessionId) {
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
            {},
            {
              auth: {
                username: 'OPENVIDUAPP',
                password: OPENVIDU_SERVER_SECRET,
              },
            }
          )
          .then((response) => response.data)
          .then((data) => resolve(data.token))
          .catch((error) => reject(error.response));
      });
    },
    cameraOn() {
      if (!this.OV) this.OV = new OpenVidu();
      // --- Get your own camera stream with the desired properties ---
      let publisher = this.OV.initPublisher(undefined, {
        audioSource: false, // The source of audio. If undefined default microphone
        videoSource: undefined, // The source of video. If undefined default webcam
        publishAudio: false, // Whether you want to start publishing with your audio unmuted or not
        publishVideo: true, // Whether you want to start publishing with your video enabled or not
        resolution: '320x240', // The resolution of your video
        frameRate: 30, // The frame rate of your video
        insertMode: 'APPEND', // How the video is inserted in the target element 'video-container'
        mirror: false, // Whether to mirror your local video or not
      });
      this.publisher = publisher;
      this.cameraState = true;
    },
    cameraOff() {
      if (this.session && this.isPublished) {
        // 세션에 들어가있고 "publish 중인 상태"에서는 unpublish 해야 함
        this.session.unpublish(this.publisher);
      } else {
        // 세션을 통해 unpublish하지 않은 경우에 카메라를 OFF할 경우,
        // 카메라 자원이 여전히 실행 중이므로 카메라 자원을 해제하는 작업을 해주어야 함
        if (this.publisher) this.publisher.stream.disposeMediaStream();
      }
      this.publisher = undefined;
      this.cameraState = false;
      this.isPublished = false;
    },
    publishSession() {
      this.session
        .publish(this.publisher)
        .then(() => {
          this.isPublished = true;
        })
        .catch(() => {
          this.isPublished = false;
        });
    },
  },
};
</script>
