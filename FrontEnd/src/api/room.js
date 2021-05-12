import { roomInstance } from './index.js';

function getAllRooms(success, fail) {
  roomInstance
    .get(`rooms`)
    .then(success)
    .catch(fail);
}

function getSeatList({ roomId }, success, fail) {
  roomInstance
    .get(`seats/${roomId}`)
    .then(success)
    .catch(fail);
}

export { getAllRooms, getSeatList };
