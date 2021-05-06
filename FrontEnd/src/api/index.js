import axios from 'axios';
import setInterceptors from './common/interceptors';

function createDeskInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL_DESK,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return setInterceptors(instance);
}

function createRoomInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL_ROOM,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return setInterceptors(instance);
}

function createLoginInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL_LOGIN,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return setInterceptors(instance);
}

function createOpenViduInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_OPENVIDU_SERVER_URL,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return instance;
}

export { createDeskInstance, createLoginInstance, createRoomInstance, createOpenViduInstance };
