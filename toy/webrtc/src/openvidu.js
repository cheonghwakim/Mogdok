import { OpenVidu } from 'openvidu-browser';
var OV;
var session;

export function joinSession(publisherElement) {
  return new Promise((resolve, reject) => {
    var mySessionId = document.getElementById('sessionId').value;

    OV = new OpenVidu();
    session = OV.initSession();

    session.on('streamCreated', function(event) {
      session.subscribe(event.stream, 'subscriber');
    });

    getToken(mySessionId).then((token) => {
      session
        .connect(token)
        .then(() => {
          document.getElementById('session-header').innerText = mySessionId;
          document.getElementById('join').style.display = 'none';
          document.getElementById('session').style.display = 'block';
          var publisher = OV.initPublisher(publisherElement.innerText, {
            audioSource: false, // The source of audio. If undefined default audio input
            videoSource: undefined, // The source of video. If undefined default video input
            publishAudio: false, // Whether to start publishing with your audio unmuted or not
            publishVideo: true, // Whether to start publishing with your video enabled or not
            resolution: '640x480', // The resolution of your video
            frameRate: 30, // The frame rate of your video
            insertMode: 'APPEND', // How the video is inserted in target element 'video-container' (https://docs.openvidu.io/en/2.17.0/api/openvidu-browser/enums/videoinsertmode.html)
            mirror: true, // Whether to mirror your local video or not
          });
          session.publish(publisher);
          resolve(session);
        })
        .catch((error) => {
          reject(error);
        });
    });
  });
}

export function leaveSession() {
  session.disconnect();
  document.getElementById('join').style.display = 'block';
  document.getElementById('session').style.display = 'none';
}

window.onbeforeunload = function() {
  if (session) session.disconnect();
};

/**
 * --------------------------
 * SERVER-SIDE RESPONSIBILITY
 * --------------------------
 * These methods retrieve the mandatory user token from OpenVidu Server.
 * This behavior MUST BE IN YOUR SERVER-SIDE IN PRODUCTION (by using
 * the API REST, openvidu-java-client or openvidu-node-client):
 *   1) Initialize a session in OpenVidu Server	(POST /api/sessions)
 *   2) Generate a token in OpenVidu Server		(POST /api/tokens)
 *   3) The token must be consumed in Sessi1on.connect() method
 */

// var OPENVIDU_SERVER_URL = 'https://' + location.hostname + ':8080';
// var OPENVIDU_SERVER_URL = 'https://' + location.hostname + ':8080';
// var OPENVIDU_SERVER_SECRET = 'MY_SECRET';
const OPENVIDU_SERVER_URL = 'http://k4a101.p.ssafy.io:4443/api/sessions';
const OPENVIDU_SERVER_SECRET = '123456789';

async function getToken(mySessionId) {
  const session = await createSession(mySessionId);
  const token = await createToken(session);
  console.log('token', token);
  return token.token;
}

async function createSession(sessionId) {
  // See https://openvidu.io/docs/reference-docs/REST-API/#post-apisessions
  //   try {
  return fetch(OPENVIDU_SERVER_URL + '/api/sessions', {
    method: 'POST',
    headers: {
      Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET,POST',
    },
    body: JSON.stringify({ customSessionId: sessionId }),
  })
    .then(async (response) => {
      if (response.status === 200) {
        const session = await response.json();
        return session.id;
      } else if (response.status === 409) {
        return sessionId;
      } else {
        confirm();
      }
    })
    .catch((error) => {
      throw error;
    });
  //   } catch (error) {
  //     throw error;
  //   }
}

function confirm() {
  console.warn(
    'No connection to OpenVidu Server. This may be a certificate error at ' + OPENVIDU_SERVER_URL
  );
  if (
    window.confirm(
      'No connection to OpenVidu Server. This may be a certificate error at "' +
        OPENVIDU_SERVER_URL +
        '"\n\nClick OK to navigate and accept it. ' +
        'If no certificate warning is shown, then check that your OpenVidu Server is up and running at "' +
        OPENVIDU_SERVER_URL +
        '"'
    )
  ) {
    location.assign(OPENVIDU_SERVER_URL + '/accept-certificate');
  }
}

async function createToken(sessionId) {
  // See https://openvidu.io/docs/reference-docs/REST-API/#post-apitokens
  try {
    return fetch(OPENVIDU_SERVER_URL + '/api/tokens', {
      method: 'POST',
      headers: {
        Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ session: sessionId }),
    })
      .then((response) => {
        return response.json();
      })
      .catch((error) => {
        throw error;
      });
  } catch (error) {
    return error;
  }
}
