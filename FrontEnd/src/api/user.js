import { createLoginInstance } from './index.js';

const instance = createLoginInstance();

// auth-token 요청
async function getAuthToken(params, success, fail) {
  await instance
    .get(`klogin`, { params })
    .then(success)
    .catch(fail);
}

// login 요청
async function login(success, fail) {
  console.log('%cuser.js line:15 login', 'color: #007acc;');
  await instance
    .get(`login`)
    .then(success)
    .catch(fail);
}

function checkUserNameDuplicated(params, success, fail) {
  instance
    .post(`nickname`, { nickname: params })
    .then(success)
    .catch(fail);
}

export { getAuthToken, login, checkUserNameDuplicated };
