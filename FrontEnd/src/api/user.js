import { createLoginInstance } from "./index.js";

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
  await instance
    .get(`login`)
    .then(success)
    .catch(fail);
}

function checkUserNameDuplicated(nickname, success, fail) {
  instance
    .get(`nickname`, { params: {nickname} })
    .then(success)
    .catch(fail);
}

function signup(userInfo, success, fail) {
  instance
    .post(`signup`, userInfo )
    .then(success)
    .catch(fail);
}

export { getAuthToken, login, checkUserNameDuplicated ,signup };
