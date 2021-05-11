import { roomInstance } from './index.js';

function getAllRooms(success, fail) {
  roomInstance
    .get(`rooms`)
    .then(success)
    .catch(fail);
}

function getSeatInfo({ roomId }, success, fail) {
  roomInstance
    .get(`seats/${roomId}`)
    .then(success)
    .catch(fail);
}

export { getAllRooms, getSeatInfo };
