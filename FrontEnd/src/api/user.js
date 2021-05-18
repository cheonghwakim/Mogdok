import { loginInstance } from './index.js';

// auth-token 요청
async function getAuthToken(params, success, fail) {
  await loginInstance
    .get(`klogin`, { params })
    .then(success)
    .catch(fail);
}

// login 요청
async function login({ kakaoId }, success, fail) {
  await loginInstance
    .get(`login`, { params: { kakaoId } })
    .then(success)
    .catch(fail);
}

async function loginByAuthToken(success, fail) {
  await loginInstance
    .get(`auth`)
    .then(success)
    .catch(fail);
}

function checkUserNameDuplicated(nickname, success, fail) {
  loginInstance
    .get(`nickname`, { params: { nickname } })
    .then(success)
    .catch(fail);
}

function signup(userInfo, success, fail) {
  loginInstance
    .post(`signup`, userInfo)
    .then(success)
    .catch(fail);
}

async function getProfileByUserName({ userName }, success, fail) {
  await loginInstance
    .get(`profile`, { params: { userName } })
    .then(success)
    .catch(fail);
}

export {
  getAuthToken,
  login,
  checkUserNameDuplicated,
  signup,
  loginByAuthToken,
  getProfileByUserName,
};
