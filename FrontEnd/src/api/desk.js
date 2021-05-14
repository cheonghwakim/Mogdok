import { deskInstance } from './index.js';

function getDeskInfo(params, success, fail) {
   deskInstance
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
// #JS : 오타 수정해야함 calender
async function getCalendarDay(params, success, fail) {
   await deskInstance
      .get(`calender/day`, { params })
      .then(success)
      .catch(fail);
}

async function getCalendarMonth(params, success, fail) {
   await deskInstance
      .get(`calender/month`, { params })
      .then(success)
      .catch(fail);
}

export { getDeskInfo, saveMemoList, deleteMemoList, getCalendarDay, getCalendarMonth };
