import { roomInstance } from './index.js';

async function getAllRooms(success, fail) {
  await roomInstance
    .get(`rooms`)
    .then(success)
    .catch(fail);
}

async function hasAlreadyEntered({ token }, success, fail) {
  await roomInstance
    .post(`rooms/users`, { token })
    .then(success)
    .catch(fail);
}

async function getSeatList({ roomId }, success, fail) {
  await roomInstance
    .get(`seats/${roomId}`)
    .then(success)
    .catch(fail);
}

export { getAllRooms, hasAlreadyEntered, getSeatList };
