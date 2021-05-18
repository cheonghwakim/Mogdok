import { deskInstance } from './index.js';

async function getDeskInfo(params, success, fail) {
   await deskInstance
      .get(`desk/all`, { params })
      .then(success)
      .catch(fail);
}

function saveMemoList(params, success, fail) {
   deskInstance
      .post(`desk/memo/update`, params)
      .then(success)
      .catch(fail);
}

function deleteMemoList(params, success, fail) {
   deskInstance
      .post(`desk/memo/delete`, params)
      .then(success)
      .catch(fail);
}

// Study Calendar API
async function getCalendarDay(params, success, fail) {
   await deskInstance
      .get(`calendar/day`, { params })
      .then(success)
      .catch(fail);
}

async function getCalendarMonth(params, success, fail) {
   await deskInstance
      .get(`calendar/month`, { params })
      .then(success)
      .catch(fail);
}

export { getDeskInfo, saveMemoList, deleteMemoList, getCalendarDay, getCalendarMonth };
