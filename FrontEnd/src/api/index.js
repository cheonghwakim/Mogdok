import axios from 'axios';

function createDeskInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL_DESK,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return instance;
}

function createRoomInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL_ROOM,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return instance;
}

function createLoginInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL_LOGIN,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return instance;
}

export { createDeskInstance, createLoginInstance, createRoomInstance };
