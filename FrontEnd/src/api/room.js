import { roomInstance } from './index.js';

async function getAllRooms(success, fail) {
  await roomInstance
    .get(`rooms`)
    .then(success)
    .catch(fail);
}

async function getSeatList({ roomId }, success, fail) {
  await roomInstance
    .get(`seats/${roomId}`)
    .then(success)
    .catch(fail);
}

export { getAllRooms, getSeatList };
