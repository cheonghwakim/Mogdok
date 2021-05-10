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
export const deskInstance = createDeskInstance();

function createRoomInstance() {
   const instance = axios.create({
      baseURL: process.env.VUE_APP_BASE_URL_ROOM,
      headers: {
         'Content-Type': 'application/json',
      },
   });
   return setInterceptors(instance);
}
export const roomInstance = createRoomInstance();

function createLoginInstance() {
   const instance = axios.create({
      baseURL: process.env.VUE_APP_BASE_URL_LOGIN,
      headers: {
         'Content-Type': 'application/json',
      },
   });
   return instance;
}
export const loginInstance = createLoginInstance();

function createOpenViduInstance() {
   const instance = axios.create({
      baseURL: process.env.VUE_APP_OPENVIDU_SERVER_URL,
      headers: {
         'Content-Type': 'application/json',
      },
   });
   return setInterceptors(instance);
}
export const openViduInstance = createOpenViduInstance();
