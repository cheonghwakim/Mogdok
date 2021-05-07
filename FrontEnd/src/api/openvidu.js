import { createOpenViduInstance } from './index.js';

const instance = createOpenViduInstance();
const auth = {
  username: 'OPENVIDUAPP',
  password: process.env.VUE_APP_OPENVIDU_SERVER_SECRET,
};

// 세션 생성
function createSession({ sessionId }, success, resolve, fail) {
  instance
    .post(
      `openvidu/api/sessions`,
      JSON.stringify({
        customSessionId: sessionId,
      }),
      { auth }
    )
    .then(success)
    .then(resolve)
    .catch(fail);
}

// 토큰 생성
function createToken({ sessionId }, success, resolve, fail) {
  instance
    .post(
      `openvidu/api/sessions/${sessionId}/connection`,
      {},
      {
        auth: {
          username: 'OPENVIDUAPP',
          password: process.env.VUE_APP_OPENVIDU_SERVER_SECRET,
        },
      }
    )
    .then(success)
    .then(resolve)
    .catch(fail);
}

export { createToken, createSession };
